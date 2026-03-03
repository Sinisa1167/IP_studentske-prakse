package ba.etf.prakse.controller;

import ba.etf.prakse.model.AiRecommendation;
import ba.etf.prakse.service.AiRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AiRecommendation>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(aiRecommendationService.getByStudentId(studentId));
    }



    @PostMapping("/student/{studentId}/generate")
    public ResponseEntity<?> generate(@PathVariable Long studentId) {
        try {
            List<AiRecommendation> recommendations = aiRecommendationService.generateRecommendations(studentId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Greška pri generisanju preporuka: " + e.getMessage());
        }
    }
}
