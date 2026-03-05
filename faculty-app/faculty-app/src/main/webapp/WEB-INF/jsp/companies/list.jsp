<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="ba.etf.faculty.model.Company" %>
<jsp:include page="../layout/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">business</span>
        Kompanije
    </h2>
    <a href="<%= request.getContextPath() %>/companies/new" class="btn btn-primary">
        <span class="material-icons align-middle">add</span>
        Nova kompanija
    </a>
</div>

<div class="search-bar">
    <form method="get" action="<%= request.getContextPath() %>/companies">
        <div class="input-group">
            <span class="input-group-text">
                <span class="material-icons" style="font-size:1.2rem">search</span>
            </span>
            <input type="text" name="search" class="form-control"
                   placeholder="Pretraži po nazivu..."
                   value="<%= request.getAttribute("search") != null ? request.getAttribute("search") : "" %>">
            <button type="submit" class="btn btn-primary">Pretraži</button>
            <a href="<%= request.getContextPath() %>/companies" class="btn btn-outline-secondary">Reset</a>
        </div>
    </form>
</div>

<div class="card">
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>Naziv</th>
                <th class="d-none d-md-table-cell">Email</th>
                <th class="d-none d-md-table-cell">Adresa</th>
                <th>Status</th>
                <th>Akcije</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Company> companies = (List<Company>) request.getAttribute("companies");
                if (companies != null) {
                    for (Company company : companies) {
            %>
            <tr>
                <td><strong><%= company.getName() %></strong></td>
                <td class="d-none d-md-table-cell"><%= company.getContactEmail() != null ? company.getContactEmail() : "-" %></td>
                <td class="d-none d-md-table-cell"><%= company.getAddress() != null ? company.getAddress() : "-" %></td>
                <td>
                    <% if (Boolean.TRUE.equals(company.getActive())) { %>
                    <span class="badge bg-success">Aktivna</span>
                    <% } else { %>
                    <span class="badge bg-danger">Neaktivna</span>
                    <% } %>
                </td>
                <td>
                    <div class="d-flex gap-1 flex-wrap">
                        <a href="<%= request.getContextPath() %>/companies/<%= company.getId() %>/edit"
                           class="btn btn-sm btn-outline-primary btn-action">
                            <span class="material-icons" style="font-size:1rem">edit</span>
                        </a>
                        <% if (Boolean.TRUE.equals(company.getActive())) { %>
                        <form method="post" action="<%= request.getContextPath() %>/companies/<%= company.getId() %>/deactivate" style="display:inline">
                            <button type="submit" class="btn btn-sm btn-outline-warning btn-action"
                                    onclick="return confirm('Deaktivirati kompaniju?')">
                                <span class="material-icons" style="font-size:1rem">block</span>
                            </button>
                        </form>
                        <% } else { %>
                        <form method="post" action="<%= request.getContextPath() %>/companies/<%= company.getId() %>/activate" style="display:inline">
                            <button type="submit" class="btn btn-sm btn-outline-success btn-action">
                                <span class="material-icons" style="font-size:1rem">check_circle</span>
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
            <a class="page-link" href="<%= request.getContextPath() %>/companies?page=<%= i %>
                    <%= request.getAttribute("search") != null ? "&search=" + request.getAttribute("search") : "" %>">
                <%= i + 1 %>
            </a>
        </li>
        <% } %>
    </ul>
</nav>
<% } %>

<jsp:include page="../layout/footer.jsp"/>