package ba.etf.faculty.servlet;

import ba.etf.faculty.model.Student;
import ba.etf.faculty.model.PageResponse;
import ba.etf.faculty.util.ApiClient;
import ba.etf.faculty.util.SessionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@WebServlet("/students/*")
@MultipartConfig
public class StudentServlet extends HttpServlet {

    private final ObjectMapper mapper = ApiClient.getMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req) || !SessionUtil.isFaculty(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String token = SessionUtil.getToken(req);
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            String search = req.getParameter("search");
            String pageParam = req.getParameter("page");
            int page = pageParam != null ? Integer.parseInt(pageParam) : 0;

            String endpoint = "/students?page=" + page + "&size=10";
            if (search != null && !search.isBlank()) {
                endpoint += "&search=" + search;
            }

            String response = ApiClient.get(endpoint, token);
            PageResponse<Student> pageResponse = mapper.readValue(response,
                    new TypeReference<PageResponse<Student>>() {});

            req.setAttribute("students", pageResponse.getContent());
            req.setAttribute("totalPages", pageResponse.getTotalPages());
            req.setAttribute("currentPage", page);
            req.setAttribute("search", search);
            req.getRequestDispatcher("/WEB-INF/jsp/students/list.jsp").forward(req, resp);

        } else if (pathInfo.equals("/new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/students/form.jsp").forward(req, resp);

        } else if (pathInfo.matches("/\\d+/edit")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            String response = ApiClient.get("/students/" + id, token);
            Student student = mapper.readValue(response, Student.class);
            req.setAttribute("student", student);
            req.getRequestDispatcher("/WEB-INF/jsp/students/form.jsp").forward(req, resp);

        } else if (pathInfo.matches("/\\d+/progress")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            String response = ApiClient.get("/students/" + id, token);
            Student student = mapper.readValue(response, Student.class);
            req.setAttribute("from", req.getParameter("from"));
            req.setAttribute("token", token);
            req.setAttribute("student", student);

            String appsResponse = ApiClient.get("/applications/student/" + id, token);
            req.setAttribute("applicationsJson", appsResponse);
            req.getRequestDispatcher("/WEB-INF/jsp/students/progress.jsp").forward(req, resp);

        } else if (pathInfo.equals("/progress")) {
            String response = ApiClient.get("/students?page=0&size=100", token);
            PageResponse<Student> pageResponse = mapper.readValue(response,
                    new TypeReference<PageResponse<Student>>() {});
            req.setAttribute("students", pageResponse.getContent());
            req.getRequestDispatcher("/WEB-INF/jsp/students/progress-list.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req) || !SessionUtil.isFaculty(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String token = SessionUtil.getToken(req);
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            String contentType = req.getContentType();

            if (contentType != null && contentType.contains("multipart")) {
                // CSV import
                Part filePart = req.getPart("csvFile");
                if (filePart != null && filePart.getSize() > 0) {
                    try (CSVReader csvReader = new CSVReader(
                            new InputStreamReader(filePart.getInputStream()))) {
                        String[] line;
                        boolean firstLine = true;
                        int imported = 0;
                        int errors = 0;

                        try{

                        while ((line = csvReader.readNext()) != null) {
                            if (firstLine) { firstLine = false; continue; }
                            if (line.length < 6) continue;

                            try {
                                Student student = new Student();
                                student.setFirstName(line[0].trim());
                                student.setLastName(line[1].trim());
                                student.setEmail(line[2].trim());
                                student.setPhone(line[3].trim());
                                student.setDateOfBirth(line[4].trim());
                                student.setAddress(line[5].trim());

                                String username = line.length > 6 ? line[6].trim() :
                                        line[0].toLowerCase() + line[1].toLowerCase();
                                String password = line.length > 7 ? line[7].trim() : "password123";

                                String endpoint = "/students?username=" + username +
                                        "&password=" + password;
                                ApiClient.post(endpoint, student, token);
                                imported++;
                            } catch (Exception e) {
                                errors++;
                            }
                        }
                        } catch (CsvValidationException e) {
                            errors++;
                        }
                        req.getSession().setAttribute("importMsg",
                                "Uvezeno: " + imported + ", Greške: " + errors);
                    }
                }
                resp.sendRedirect(req.getContextPath() + "/students");

            } else {
                // Kreiraj studenta
                Student student = new Student();
                student.setFirstName(req.getParameter("firstName"));
                student.setLastName(req.getParameter("lastName"));
                student.setEmail(req.getParameter("email"));
                student.setPhone(req.getParameter("phone"));
                student.setDateOfBirth(req.getParameter("dateOfBirth"));
                student.setAddress(req.getParameter("address"));

                String username = req.getParameter("username");
                String password = req.getParameter("password");

                String endpoint = "/students?username=" + username + "&password=" + password;
                ApiClient.post(endpoint, student, token);
                resp.sendRedirect(req.getContextPath() + "/students");
            }

        } else if (pathInfo.matches("/\\d+/edit")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);

            Student student = new Student();
            student.setFirstName(req.getParameter("firstName"));
            student.setLastName(req.getParameter("lastName"));
            student.setEmail(req.getParameter("email"));
            student.setPhone(req.getParameter("phone"));
            student.setDateOfBirth(req.getParameter("dateOfBirth"));
            student.setAddress(req.getParameter("address"));

            ApiClient.put("/students/" + id, student, token);
            resp.sendRedirect(req.getContextPath() + "/students");

        } else if (pathInfo.matches("/\\d+/delete")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            ApiClient.delete("/students/" + id, token);
            resp.sendRedirect(req.getContextPath() + "/students");
        }
    }
}