package ba.etf.prakse.service;

import ba.etf.prakse.model.AiRecommendation;
import ba.etf.prakse.model.CvEntry;
import ba.etf.prakse.model.Internship;
import ba.etf.prakse.model.Student;
import ba.etf.prakse.repository.AiRecommendationRepository;
import ba.etf.prakse.repository.CvEntryRepository;
import ba.etf.prakse.repository.InternshipRepository;
import ba.etf.prakse.repository.StudentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private final AiRecommendationRepository aiRecommendationRepository;
    private final StudentRepository studentRepository;
    private final InternshipRepository internshipRepository;
    private final CvEntryRepository cvEntryRepository;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public List<AiRecommendation> getByStudentId(Long studentId) {
        return aiRecommendationRepository.findByStudentIdOrderByScoreDesc(studentId);
    }

    @Transactional
    public List<AiRecommendation> generateRecommendations(Long studentId) throws Exception {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student nije pronađen"));

        List<Internship> internships = internshipRepository.findAll()
                .stream().filter(Internship::getActive).toList();

        if (internships.isEmpty()) {
            throw new RuntimeException("Nema dostupnih praksi");
        }

        // Briši stare preporuke
        aiRecommendationRepository.deleteByStudentId(studentId);

        // Kreiraj CV opis
        String cvDescription = buildCvDescription(studentId, student);

        // Kreiraj opis svih praksi
        String internshipsDescription = buildInternshipsDescription(internships);

        // Pozovi Gemini API
        String prompt = buildPrompt(cvDescription, internshipsDescription, internships);
        String aiResponse = callGeminiApi(prompt);

        // Parsiraj odgovor i kreiraj preporuke
        List<AiRecommendation> recommendations = parseAndSave(aiResponse, student, internships);

        // Vrati top 5 sortirano po score-u
        return recommendations.stream()
                .sorted(Comparator.comparing(AiRecommendation::getScore).reversed())
                .limit(5)
                .toList();
    }

    private String buildCvDescription(Long studentId, Student student) {
        List<CvEntry> entries = cvEntryRepository.findByStudentId(studentId);

        StringBuilder sb = new StringBuilder();
        sb.append("Student: ").append(student.getFirstName()).append(" ").append(student.getLastName()).append("\n");

        List<CvEntry> skills = entries.stream().filter(e -> "SKILL".equals(e.getType())).toList();
        if (!skills.isEmpty()) {
            sb.append("Vještine: ");
            skills.forEach(s -> sb.append(s.getTitle()).append(" (").append(s.getExtra()).append("), "));
            sb.append("\n");
        }

        List<CvEntry> interests = entries.stream().filter(e -> "INTEREST".equals(e.getType())).toList();
        if (!interests.isEmpty()) {
            sb.append("Interesovanja: ");
            interests.forEach(i -> sb.append(i.getTitle()).append(", "));
            sb.append("\n");
        }

        List<CvEntry> education = entries.stream().filter(e -> "EDUCATION".equals(e.getType())).toList();
        if (!education.isEmpty()) {
            sb.append("Obrazovanje: ");
            education.forEach(e -> sb.append(e.getTitle()).append(", "));
            sb.append("\n");
        }

        List<CvEntry> experience = entries.stream().filter(e -> "EXPERIENCE".equals(e.getType())).toList();
        if (!experience.isEmpty()) {
            sb.append("Iskustvo: ");
            experience.forEach(e -> sb.append(e.getTitle()).append(" - ").append(e.getDescription()).append(", "));
        }

        return sb.toString();
    }

    private String buildInternshipsDescription(List<Internship> internships) {
        StringBuilder sb = new StringBuilder();
        for (Internship i : internships) {
            sb.append("ID:").append(i.getId())
              .append(" | Naziv: ").append(i.getTitle())
              .append(" | Kompanija: ").append(i.getCompany().getName())
              .append(" | Opis: ").append(i.getDescription())
              .append(" | Tehnologije: ");
            i.getTechnologies().forEach(t -> sb.append(t.getName()).append(", "));
            sb.append(" | Uslovi: ").append(i.getConditions()).append("\n");
        }
        return sb.toString();
    }

    private String buildPrompt(String cv, String internships, List<Internship> internshipList) {
        StringBuilder ids = new StringBuilder();
        internshipList.forEach(i -> ids.append(i.getId()).append(","));

        return "Analiziraj CV studenta i dostupne prakse. Za svaku praksu vrati kompatibilnost od 0 do 1 i kratko obrazloženje.\n\n" +
               "CV studenta:\n" + cv + "\n\n" +
               "Dostupne prakse:\n" + internships + "\n\n" +
               "Vrati SAMO JSON u ovom formatu, bez ikakvog drugog teksta:\n" +
               "{\n" +
               "  \"recommendations\": [\n" +
               "    {\"internshipId\": 1, \"score\": 0.95, \"explanation\": \"Kratko obrazloženje\"},\n" +
               "    {\"internshipId\": 2, \"score\": 0.70, \"explanation\": \"Kratko obrazloženje\"}\n" +
               "  ]\n" +
               "}\n\n" +
               "IDs praksi koje trebaš ocjeniti: " + ids;
    }


    private String callGeminiApi(String prompt) throws Exception {
        String requestBody = "{\n" +
                "  \\\"model\\\": \\\"google/gemini-2.0-flash-001\\\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"user\", \"content\": " + objectMapper.writeValueAsString(prompt) + "}\n" +
                "  ]\n" +
                "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("AI API greška: " + response.body());
        }

        JsonNode root = objectMapper.readTree(response.body());
        return root.path("choices").get(0)
                .path("message").path("content").asText();
    }

    private List<AiRecommendation> parseAndSave(String aiResponse, Student student,
                                                  List<Internship> internships) throws Exception {
        // Očisti odgovor od markdown backtick-ova ako ih Gemini doda
        String cleaned = aiResponse.replaceAll("```json", "").replaceAll("```", "").trim();

        JsonNode root = objectMapper.readTree(cleaned);
        JsonNode recommendations = root.path("recommendations");

        List<AiRecommendation> saved = new ArrayList<>();

        for (JsonNode rec : recommendations) {
            Long internshipId = rec.path("internshipId").asLong();
            double score = rec.path("score").asDouble();
            String explanation = rec.path("explanation").asText();

            internships.stream()
                    .filter(i -> i.getId().equals(internshipId))
                    .findFirst()
                    .ifPresent(internship -> {
                        AiRecommendation recommendation = new AiRecommendation();
                        recommendation.setStudent(student);
                        recommendation.setInternship(internship);
                        recommendation.setScore(BigDecimal.valueOf(score));
                        recommendation.setExplanation(explanation);
                        saved.add(aiRecommendationRepository.save(recommendation));
                    });
        }

        return saved;
    }
}
