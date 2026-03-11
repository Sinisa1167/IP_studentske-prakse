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

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req) || !SessionUtil.isCompany(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/change-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req) || !SessionUtil.isCompany(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error", "Lozinke se ne poklapaju.");
            req.getRequestDispatcher("/WEB-INF/jsp/change-password.jsp").forward(req, resp);
            return;
        }

        if (newPassword.length() < 6) {
            req.setAttribute("error", "Lozinka mora imati najmanje 6 karaktera.");
            req.getRequestDispatcher("/WEB-INF/jsp/change-password.jsp").forward(req, resp);
            return;
        }

        try {
            String token = SessionUtil.getToken(req);
            Long userId = SessionUtil.getUserId(req);

            // Ponovo se loguj sa novom lozinkom kroz update usera
            // Koristimo login endpoint da dobijemo username
            String username = (String) req.getSession().getAttribute("username");

            // Pozovi API za promjenu lozinke
            String response = ApiClient.put("/users/" + userId + "/password",
                    Map.of("password", newPassword), token);

            req.setAttribute("success", "Lozinka uspješno promijenjena.");
            req.getRequestDispatcher("/WEB-INF/jsp/change-password.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Greška pri promjeni lozinke.");
            req.getRequestDispatcher("/WEB-INF/jsp/change-password.jsp").forward(req, resp);
        }
    }
}