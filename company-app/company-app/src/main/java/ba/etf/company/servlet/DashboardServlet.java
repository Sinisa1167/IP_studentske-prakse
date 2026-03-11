package ba.etf.company.servlet;

import ba.etf.company.util.ApiClient;
import ba.etf.company.util.SessionUtil;
import com.fasterxml.jackson.databind.JsonNode;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req) || !SessionUtil.isCompany(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String token = SessionUtil.getToken(req);
        Long companyId = SessionUtil.getCompanyId(req);

        try {
            String companyResp = ApiClient.get("/companies/" + companyId, token);
            JsonNode company = ApiClient.getMapper().readTree(companyResp);
            req.setAttribute("companyName", company.get("name").asText());

            String internshipsResp = ApiClient.get(
                    "/internships/company/" + companyId + "?page=0&size=1", token);
            JsonNode internships = ApiClient.getMapper().readTree(internshipsResp);
            req.setAttribute("internshipCount", internships.get("totalElements").asLong());

        } catch (Exception e) {
            req.setAttribute("companyName", "Kompanija");
            req.setAttribute("internshipCount", 0);
        }

        req.setAttribute("username", req.getSession().getAttribute("username"));
        req.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(req, resp);
    }
}