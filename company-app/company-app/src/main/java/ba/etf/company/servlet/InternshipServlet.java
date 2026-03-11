package ba.etf.company.servlet;

import ba.etf.company.model.Internship;
import ba.etf.company.model.PageResponse;
import ba.etf.company.model.Technology;
import ba.etf.company.util.ApiClient;
import ba.etf.company.util.SessionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/internships/*")
public class InternshipServlet extends HttpServlet {

    private final ObjectMapper mapper = ApiClient.getMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req) || !SessionUtil.isCompany(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String token = SessionUtil.getToken(req);
        Long companyId = SessionUtil.getCompanyId(req);
        String pathInfo = req.getPathInfo();

        // Dohvati tehnologije za formu
        String techResp = ApiClient.get("/technologies", token);
        List<Technology> technologies = mapper.readValue(techResp,
                new TypeReference<List<Technology>>() {});
        req.setAttribute("technologies", technologies);

        if (pathInfo == null || pathInfo.equals("/")) {
            String pageParam = req.getParameter("page");
            int page = pageParam != null ? Integer.parseInt(pageParam) : 0;

            String response = ApiClient.get(
                    "/internships/company/" + companyId + "?page=" + page + "&size=10", token);
            PageResponse<Internship> pageResponse = mapper.readValue(response,
                    new TypeReference<PageResponse<Internship>>() {});

            req.setAttribute("internships", pageResponse.getContent());
            req.setAttribute("totalPages", pageResponse.getTotalPages());
            req.setAttribute("currentPage", page);
            req.getRequestDispatcher("/WEB-INF/jsp/internships/list.jsp").forward(req, resp);

        } else if (pathInfo.equals("/new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/internships/form.jsp").forward(req, resp);

        } else if (pathInfo.matches("/\\d+/edit")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            String response = ApiClient.get("/internships/" + id, token);
            Internship internship = mapper.readValue(response, Internship.class);
            req.setAttribute("internship", internship);
            req.getRequestDispatcher("/WEB-INF/jsp/internships/form.jsp").forward(req, resp);

        } else if (pathInfo.matches("/\\d+/applications")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            String internshipResp = ApiClient.get("/internships/" + id, token);
            Internship internship = mapper.readValue(internshipResp, Internship.class);
            req.setAttribute("internship", internship);

            String status = req.getParameter("status");
            String appsEndpoint = "/applications/internship/" + id + "?page=0&size=50";
            if (status != null && !status.isBlank()) {
                appsEndpoint += "&status=" + status;
            }
            String appsResp = ApiClient.get(appsEndpoint, token);
            JsonNode appsNode = mapper.readTree(appsResp);
            req.setAttribute("applications", appsNode.path("content"));
            req.setAttribute("applicationsJson", appsNode.path("content").toString());
            req.setAttribute("statusFilter", status);
            req.getRequestDispatcher("/WEB-INF/jsp/internships/applications.jsp").forward(req, resp);
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
        Long companyId = SessionUtil.getCompanyId(req);
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/new")) {
            Map<String, Object> internship = new HashMap<>();
            internship.put("title", req.getParameter("title"));
            internship.put("description", req.getParameter("description"));
            internship.put("startDate", req.getParameter("startDate"));
            internship.put("endDate", req.getParameter("endDate"));
            internship.put("conditions", req.getParameter("conditions"));
            internship.put("active", true);

            String[] techIds = req.getParameterValues("technologyIds");
            List<Long> technologyIds = new ArrayList<>();
            if (techIds != null) {
                for (String id : techIds) technologyIds.add(Long.parseLong(id));
            }
            internship.put("technologyIds", technologyIds);

            // Kreiraj kroz company endpoint
            String json = mapper.writeValueAsString(internship);
            ApiClient.postForm("/internships/company/" + companyId, json, token);
            resp.sendRedirect(req.getContextPath() + "/internships");

        } else if (pathInfo.matches("/\\d+/edit")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);

            Map<String, Object> internship = new HashMap<>();
            internship.put("title", req.getParameter("title"));
            internship.put("description", req.getParameter("description"));
            internship.put("startDate", req.getParameter("startDate"));
            internship.put("endDate", req.getParameter("endDate"));
            internship.put("conditions", req.getParameter("conditions"));

            String[] techIds = req.getParameterValues("technologyIds");
            List<Long> technologyIds = new ArrayList<>();
            if (techIds != null) {
                for (String techId : techIds) technologyIds.add(Long.parseLong(techId));
            }

            String technologyParam = technologyIds.isEmpty() ? "" :
                    "?technologyIds=" + String.join("&technologyIds=",
                            technologyIds.stream().map(String::valueOf).toArray(String[]::new));

            ApiClient.put("/internships/" + id + technologyParam, internship, token);
            resp.sendRedirect(req.getContextPath() + "/internships");

        } else if (pathInfo.matches("/\\d+/deactivate")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            ApiClient.patch("/internships/" + id + "/deactivate", token);
            resp.sendRedirect(req.getContextPath() + "/internships");

        } else if (pathInfo.matches("/\\d+/applications/\\d+/accept")) {
            String[] parts = pathInfo.split("/");
            Long appId = Long.parseLong(parts[3]);
            Long internshipId = Long.parseLong(parts[1]);
            ApiClient.patch("/applications/" + appId + "/accept", token);
            resp.sendRedirect(req.getContextPath() + "/internships/" + internshipId + "/applications");

        } else if (pathInfo.matches("/\\d+/applications/\\d+/reject")) {
            String[] parts = pathInfo.split("/");
            Long appId = Long.parseLong(parts[3]);
            Long internshipId = Long.parseLong(parts[1]);
            ApiClient.patch("/applications/" + appId + "/reject", token);
            resp.sendRedirect(req.getContextPath() + "/internships/" + internshipId + "/applications");
        }
    }
}