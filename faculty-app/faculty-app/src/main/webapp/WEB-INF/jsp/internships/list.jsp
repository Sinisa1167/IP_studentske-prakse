<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="ba.etf.faculty.model.Internship" %>
<%@ page import="ba.etf.faculty.model.Technology" %>
<jsp:include page="../layout/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">work</span>
        Prakse
    </h2>
</div>

<div class="search-bar">
    <form method="get" action="<%= request.getContextPath() %>/internships">
        <div class="input-group">
            <span class="input-group-text">
                <span class="material-icons" style="font-size:1.2rem">search</span>
            </span>
            <input type="text" name="search" class="form-control"
                   placeholder="Pretraži po nazivu..."
                   value="<%= request.getAttribute("search") != null ? request.getAttribute("search") : "" %>">
            <button type="submit" class="btn btn-primary">Pretraži</button>
            <a href="<%= request.getContextPath() %>/internships" class="btn btn-outline-secondary">Reset</a>
        </div>
    </form>
</div>

<div class="card">
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>Naziv</th>
                <th class="d-none d-md-table-cell">Kompanija</th>
                <th class="d-none d-lg-table-cell">Period</th>
                <th class="d-none d-lg-table-cell">Tehnologije</th>
                <th>Status</th>
                <th>Akcije</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Internship> internships = (List<Internship>) request.getAttribute("internships");
                if (internships != null) {
                    for (Internship internship : internships) {
            %>
            <tr>
                <td><strong><%= internship.getTitle() %></strong></td>
                <td class="d-none d-md-table-cell">
                    <%= internship.getCompany() != null ? internship.getCompany().getName() : "-" %>
                </td>
                <td class="d-none d-lg-table-cell">
                    <%= internship.getStartDate() != null ? internship.getStartDate() : "-" %>
                    <% if (internship.getEndDate() != null) { %>
                    — <%= internship.getEndDate() %>
                    <% } %>
                </td>
                <td class="d-none d-lg-table-cell">
                    <% if (internship.getTechnologies() != null) {
                        for (Technology tech : internship.getTechnologies()) { %>
                    <span class="badge bg-secondary me-1"><%= tech.getName() %></span>
                    <%  } } %>
                </td>
                <td>
                    <% if (Boolean.TRUE.equals(internship.getActive())) { %>
                    <span class="badge bg-success">Aktivna</span>
                    <% } else { %>
                    <span class="badge bg-danger">Neaktivna</span>
                    <% } %>
                </td>
                <td>
                    <a href="<%= request.getContextPath() %>/internships/<%= internship.getId() %>"
                       class="btn btn-sm btn-outline-primary btn-action">
                        <span class="material-icons" style="font-size:1rem">visibility</span>
                    </a>
                </td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </div>
</div>

<%
    Integer totalPages = (Integer) request.getAttribute("totalPages");
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    if (totalPages != null && totalPages > 1) {
%>
<nav class="mt-3">
    <ul class="pagination justify-content-center">
        <% for (int i = 0; i < totalPages; i++) { %>
        <li class="page-item <%= i == currentPage ? "active" : "" %>">
            <a class="page-link"
               href="<%= request.getContextPath() %>/internships?page=<%= i %><%= request.getAttribute("search") != null ? "&search=" + request.getAttribute("search") : "" %>">
                <%= i + 1 %>
            </a>
        </li>
        <% } %>
    </ul>
</nav>
<% } %>

<jsp:include page="../layout/footer.jsp"/>