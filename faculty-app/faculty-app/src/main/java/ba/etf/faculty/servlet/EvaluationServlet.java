package ba.etf.faculty.servlet;

import ba.etf.faculty.util.ApiClient;
import ba.etf.faculty.util.SessionUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/evaluations/*")
public class EvaluationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req) || !SessionUtil.isFaculty(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String token = SessionUtil.getToken(req);
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long appId = Long.parseLong(pathInfo.substring(1));

            Map<String, Object> evaluation = new HashMap<>();
            evaluation.put("grade", Integer.parseInt(req.getParameter("grade")));
            evaluation.put("comment", req.getParameter("comment"));
            evaluation.put("evaluatorRole", req.getParameter("evaluatorRole"));

            try {
                ApiClient.post("/evaluations/application/" + appId, evaluation, token);
            } catch (Exception e) {
                // ocjena već postoji
            }
        }

        String referer = req.getHeader("Referer");
        resp.sendRedirect(referer != null ? referer : req.getContextPath() + "/students/progress");
    }
}