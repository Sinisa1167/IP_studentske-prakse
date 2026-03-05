package ba.etf.faculty.servlet;

import ba.etf.faculty.model.Company;
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
import java.util.Map;

@WebServlet("/companies/*")
public class CompanyServlet extends HttpServlet {

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
            // Lista kompanija
            String search = req.getParameter("search");
            String pageParam = req.getParameter("page");
            int page = pageParam != null ? Integer.parseInt(pageParam) : 0;

            String endpoint = "/companies?page=" + page + "&size=10";
            if (search != null && !search.isBlank()) {
                endpoint += "&search=" + search;
            }

            String response = ApiClient.get(endpoint, token);
            PageResponse<Company> pageResponse = mapper.readValue(response,
                    new TypeReference<PageResponse<Company>>() {});

            req.setAttribute("companies", pageResponse.getContent());
            req.setAttribute("totalPages", pageResponse.getTotalPages());
            req.setAttribute("currentPage", page);
            req.setAttribute("search", search);
            req.getRequestDispatcher("/WEB-INF/jsp/companies/list.jsp").forward(req, resp);

        } else if (pathInfo.equals("/new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/companies/form.jsp").forward(req, resp);

        } else if (pathInfo.matches("/\\d+/edit")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            String response = ApiClient.get("/companies/" + id, token);
            Company company = mapper.readValue(response, Company.class);
            req.setAttribute("company", company);
            req.getRequestDispatcher("/WEB-INF/jsp/companies/form.jsp").forward(req, resp);
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
            // Kreiraj novu kompaniju
            String name = req.getParameter("name");
            String address = req.getParameter("address");
            String contactEmail = req.getParameter("contactEmail");
            String description = req.getParameter("description");
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            Company company = new Company();
            company.setName(name);
            company.setAddress(address);
            company.setContactEmail(contactEmail);
            company.setDescription(description);

            String endpoint = "/companies?username=" + username + "&password=" + password;
            ApiClient.post(endpoint, company, token);
            resp.sendRedirect(req.getContextPath() + "/companies");

        } else if (pathInfo.matches("/\\d+/edit")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            String name = req.getParameter("name");
            String address = req.getParameter("address");
            String contactEmail = req.getParameter("contactEmail");
            String description = req.getParameter("description");

            Company company = new Company();
            company.setName(name);
            company.setAddress(address);
            company.setContactEmail(contactEmail);
            company.setDescription(description);

            ApiClient.put("/companies/" + id, company, token);
            resp.sendRedirect(req.getContextPath() + "/companies");

        } else if (pathInfo.matches("/\\d+/activate")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            ApiClient.patch("/companies/" + id + "/activate", token);
            resp.sendRedirect(req.getContextPath() + "/companies");

        } else if (pathInfo.matches("/\\d+/deactivate")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            ApiClient.patch("/companies/" + id + "/deactivate", token);
            resp.sendRedirect(req.getContextPath() + "/companies");
        }
    }
}