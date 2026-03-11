<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="ba.etf.company.model.Internship" %>
<%@ page import="ba.etf.company.model.Technology" %>
<jsp:include page="../layout/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">work</span>
        Moje prakse
    </h2>
    <a href="<%= request.getContextPath() %>/internships/new" class="btn btn-success">
        <span class="material-icons align-middle">add</span>
        Nova praksa
    </a>
</div>

<div class="card">
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>Naziv</th>
                <th class="d-none d-md-table-cell">Period</th>
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
                    <%= internship.getStartDate() != null ? internship.getStartDate() : "-" %>
                    <% if (internship.getEndDate() != null) { %> — <%= internship.getEndDate() %><% } %>
                </td>
                <td class="d-none d-lg-table-cell">
                    <% if (internship.getTechnologies() != null) {
                        for (Technology tech : internship.getTechnologies()) { %>
                    <span class="badge bg-secondary me-1"><%= tech.getName() %></span>
                    <% } } %>
                </td>
                <td>
                    <% if (Boolean.TRUE.equals(internship.getActive())) { %>
                    <span class="badge bg-success">Aktivna</span>
                    <% } else { %>
                    <span class="badge bg-danger">Neaktivna</span>
                    <% } %>
                </td>
                <td>
                    <div class="d-flex gap-1 flex-wrap">
                        <a href="<%= request.getContextPath() %>/internships/<%= internship.getId() %>/applications"
                           class="btn btn-sm btn-outline-primary btn-action">
                            <span class="material-icons" style="font-size:1rem">people</span>
                        </a>
                        <a href="<%= request.getContextPath() %>/internships/<%= internship.getId() %>/edit"
                           class="btn btn-sm btn-outline-secondary btn-action">
                            <span class="material-icons" style="font-size:1rem">edit</span>
                        </a>
                        <% if (Boolean.TRUE.equals(internship.getActive())) { %>
                        <form method="post"
                              action="<%= request.getContextPath() %>/internships/<%= internship.getId() %>/deactivate"
                              style="display:inline">
                            <button type="submit" class="btn btn-sm btn-outline-warning btn-action"
                                    onclick="return confirm('Deaktivirati praksu?')">
                                <span class="material-icons" style="font-size:1rem">block</span>
                            </button>
                        </form>
                        <% } %>
                    </div>
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
               href="<%= request.getContextPath() %>/internships?page=<%= i %>">
                <%= i + 1 %>
            </a>
        </li>
        <% } %>
    </ul>
</nav>
<% } %>

<jsp:include page="../layout/footer.jsp"/>