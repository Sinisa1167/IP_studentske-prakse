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
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (SessionUtil.isLoggedIn(req) && SessionUtil.isCompany(req)) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            String response = ApiClient.post("/auth/login",
                    Map.of("username", username, "password", password), null);

            JsonNode json = ApiClient.getMapper().readTree(response);

            if (json.has("token")) {
                String token = json.get("token").asText();
                String role = json.get("role").asText();
                Long userId = json.get("userId").asLong();
                String uname = json.get("username").asText();

                if (!"COMPANY".equals(role)) {
                    req.setAttribute("error", "Pristup dozvoljen samo za kompanije.");
                    req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
                    return;
                }

                // Dohvati companyId
                String companyResp = ApiClient.get("/companies/user/" + userId, token);
                JsonNode companyJson = ApiClient.getMapper().readTree(companyResp);
                Long companyId = companyJson.get("id").asLong();

                // Provjeri da li je kompanija aktivna
                if (!companyJson.get("active").asBoolean()) {
                    req.setAttribute("error", "Vaš nalog je deaktiviran. Kontaktirajte fakultet.");
                    req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
                    return;
                }

                SessionUtil.setSession(req, token, role, userId, uname, companyId);
                resp.sendRedirect(req.getContextPath() + "/dashboard");
            } else {
                req.setAttribute("error", "Pogrešan username ili password.");
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Greška pri povezivanju sa serverom.");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }
}