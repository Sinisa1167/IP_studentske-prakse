package ba.etf.company.servlet;

import ba.etf.company.model.CvEntry;
import ba.etf.company.model.Student;
import ba.etf.company.util.ApiClient;
import ba.etf.company.util.SessionUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.BaseFont;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/students/*")
public class StudentServlet extends HttpServlet {

    private final ObjectMapper mapper = ApiClient.getMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req) || !SessionUtil.isCompany(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String token = SessionUtil.getToken(req);
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long id = Long.parseLong(pathInfo.substring(1));
            String response = ApiClient.get("/students/" + id, token);
            Student student = mapper.readValue(response, Student.class);

            String cvResp = ApiClient.get("/cv/student/" + id, token);
            List<CvEntry> cvEntries = mapper.readValue(cvResp,
                    mapper.getTypeFactory().constructCollectionType(List.class, CvEntry.class));

            req.setAttribute("student", student);
            req.setAttribute("cvEntries", cvEntries);
            req.getRequestDispatcher("/WEB-INF/jsp/students/cv.jsp").forward(req, resp);

        } else if (pathInfo != null && pathInfo.matches("/\\d+/pdf")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            String response = ApiClient.get("/students/" + id, token);
            Student student = mapper.readValue(response, Student.class);

            String cvResp = ApiClient.get("/cv/student/" + id, token);
            List<CvEntry> cvEntries = mapper.readValue(cvResp,
                    mapper.getTypeFactory().constructCollectionType(List.class, CvEntry.class));

            generateCvPdf(resp, student, cvEntries);

        } else if (pathInfo != null && pathInfo.contains("/progress")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            Long companyId = SessionUtil.getCompanyId(req);

            String response = ApiClient.get("/students/" + id, token);
            Student student = mapper.readValue(response, Student.class);

            String appsResp = ApiClient.get("/applications/student/" + id, token);

            // Filtriraj samo prijave za prakse ove kompanije
            JsonNode allApps = mapper.readTree(appsResp);
            java.util.List<JsonNode> filtered = new java.util.ArrayList<>();
            for (JsonNode app : allApps) {
                Long appCompanyId = app.path("internship").path("company").path("id").asLong();
                if (appCompanyId.equals(companyId)) {
                    filtered.add(app);
                }
            }

            req.setAttribute("student", student);
            req.setAttribute("applicationsJson", mapper.writeValueAsString(filtered));
            req.setAttribute("token", token);
            req.getRequestDispatcher("/WEB-INF/jsp/students/progress.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!SessionUtil.isLoggedIn(req) || !SessionUtil.isCompany(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String token = SessionUtil.getToken(req);
        String pathInfo = req.getPathInfo();

        System.out.println("Matches: " + (pathInfo != null && pathInfo.contains("/progress/evaluate")));

        if (pathInfo != null && pathInfo.contains("/progress/evaluate")) {
            String[] parts = pathInfo.split("/");
            Long studentId = Long.parseLong(parts[1]);
            Long appId = Long.parseLong(req.getParameter("applicationId"));

            java.util.Map<String, Object> evaluation = new java.util.HashMap<>();
            evaluation.put("grade", Integer.parseInt(req.getParameter("grade")));
            evaluation.put("comment", req.getParameter("comment"));
            evaluation.put("evaluatorRole", "COMPANY");

            try {
                ApiClient.postChecked("/evaluations/application/" + appId, evaluation, token);
                System.out.println("Ocjena uspješno sačuvana za appId: " + appId);
            } catch (Exception e) {
                System.out.println("Greška pri čuvanju ocjene: " + e.getMessage());
            }

            resp.sendRedirect(req.getContextPath() + "/students/" + studentId + "/progress");
        }
    }

    private void generateCvPdf(HttpServletResponse resp, Student student,
                               List<CvEntry> cvEntries) throws IOException {
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"CV_" + java.net.URLEncoder.encode(student.getLastName(), "UTF-8") + ".pdf\"");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, resp.getOutputStream());
            document.open();

            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font sectionFont = new Font(baseFont, 13, Font.BOLD);
            Font normalFont = new Font(baseFont, 11);
            Font smallFont = new Font(baseFont, 10, Font.ITALIC);

            Paragraph title = new Paragraph(
                    student.getFirstName() + " " + student.getLastName(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            if (student.getEmail() != null) {
                Paragraph contact = new Paragraph(student.getEmail(), smallFont);
                contact.setAlignment(Element.ALIGN_CENTER);
                document.add(contact);
            }

            document.add(Chunk.NEWLINE);

            String[] types = {"EDUCATION", "EXPERIENCE", "INTERNSHIP", "SKILL", "INTEREST"};
            String[] labels = {"Obrazovanje", "Radno iskustvo", "Prakse", "Vještine", "Interesovanja"};

            for (int i = 0; i < types.length; i++) {
                final String type = types[i];
                List<CvEntry> filteredEntries = cvEntries.stream()
                        .filter(e -> type.equals(e.getType()))
                        .collect(java.util.stream.Collectors.toList());

                if (!filteredEntries.isEmpty()) {
                    document.add(new Paragraph(labels[i], sectionFont));
                    document.add(new LineSeparator());
                    document.add(Chunk.NEWLINE);

                    for (CvEntry entry : filteredEntries) {
                        document.add(new Paragraph(entry.getTitle(), normalFont));

                        if (entry.getDescription() != null && !entry.getDescription().isEmpty()) {
                            document.add(new Paragraph(entry.getDescription(), smallFont));
                        }
                        if (entry.getExtra() != null && !entry.getExtra().isEmpty()) {
                            document.add(new Paragraph("Nivo: " + entry.getExtra(), smallFont));
                        }
                        if (entry.getStartDate() != null) {
                            String dates = entry.getStartDate();
                            if (entry.getEndDate() != null) dates += " - " + entry.getEndDate();
                            document.add(new Paragraph(dates, smallFont));
                        }
                        document.add(Chunk.NEWLINE);
                    }
                }
            }

            document.close();
        } catch (DocumentException e) {
            throw new IOException("Greška pri generisanju PDF-a", e);
        }
    }
}