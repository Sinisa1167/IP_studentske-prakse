package ba.etf.faculty.servlet;

import ba.etf.faculty.model.Internship;
import ba.etf.faculty.model.PageResponse;
import ba.etf.faculty.util.ApiClient;
import ba.etf.faculty.util.SessionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/internships/*")
public class InternshipServlet extends HttpServlet {

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

            String endpoint = "/internships?page=" + page + "&size=10";
            if (search != null && !search.isBlank()) {
                endpoint += "&search=" + search;
            }

            String response = ApiClient.get(endpoint, token);
            PageResponse<Internship> pageResponse = mapper.readValue(response,
                    new TypeReference<PageResponse<Internship>>() {});

            req.setAttribute("internships", pageResponse.getContent());
            req.setAttribute("totalPages", pageResponse.getTotalPages());
            req.setAttribute("currentPage", page);
            req.setAttribute("search", search);
            req.getRequestDispatcher("/WEB-INF/jsp/internships/list.jsp").forward(req, resp);

        } else if (pathInfo.matches("/\\d+")) {
            Long id = Long.parseLong(pathInfo.substring(1));
            String response = ApiClient.get("/internships/" + id, token);
            Internship internship = mapper.readValue(response, Internship.class);
            req.setAttribute("internship", internship);

            String appsResponse = ApiClient.get(
                    "/applications/internship/" + id + "?page=0&size=50", token);
            req.setAttribute("applicationsJson", appsResponse);
            req.getRequestDispatcher("/WEB-INF/jsp/internships/detail.jsp").forward(req, resp);
        }
    }
}