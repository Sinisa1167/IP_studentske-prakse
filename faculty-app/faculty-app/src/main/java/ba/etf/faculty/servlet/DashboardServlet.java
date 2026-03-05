package ba.etf.faculty.servlet;

import ba.etf.faculty.util.ApiClient;
import ba.etf.faculty.util.SessionUtil;
import com.fasterxml.jackson.databind.JsonNode;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req) || !SessionUtil.isFaculty(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String token = SessionUtil.getToken(req);

        try {
            String studentsResp = ApiClient.get("/students?page=0&size=1", token);
            JsonNode studentsJson = ApiClient.getMapper().readTree(studentsResp);
            req.setAttribute("studentCount", studentsJson.get("totalElements").asLong());

            String companiesResp = ApiClient.get("/companies?page=0&size=1", token);
            JsonNode companiesJson = ApiClient.getMapper().readTree(companiesResp);
            req.setAttribute("companyCount", companiesJson.get("totalElements").asLong());

            String internshipsResp = ApiClient.get("/internships?page=0&size=1", token);
            JsonNode internshipsJson = ApiClient.getMapper().readTree(internshipsResp);
            req.setAttribute("internshipCount", internshipsJson.get("totalElements").asLong());

        } catch (Exception e) {
            req.setAttribute("studentCount", 0);
            req.setAttribute("companyCount", 0);
            req.setAttribute("internshipCount", 0);
        }

        req.setAttribute("username", req.getSession().getAttribute("username"));
        req.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(req, resp);
    }
}