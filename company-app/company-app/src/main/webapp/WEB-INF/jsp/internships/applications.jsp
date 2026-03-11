<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ba.etf.company.model.Internship" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<jsp:include page="../layout/header.jsp"/>

<%
    Internship internship = (Internship) request.getAttribute("internship");
    String appsJson = (String) request.getAttribute("applicationsJson");
    ObjectMapper mapper = new ObjectMapper();
    JsonNode applications = mapper.readTree(appsJson != null ? appsJson : "[]");
    String statusFilter = (String) request.getAttribute("statusFilter");
%>

<div class="d-flex align-items-center mb-3">
    <a href="<%= request.getContextPath() %>/internships" class="btn btn-outline-secondary me-3">
        <span class="material-icons align-middle">arrow_back</span>
    </a>
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">people</span>
        Prijave — <%= internship.getTitle() %>
    </h2>
</div>

<div class="search-bar">
    <form method="get" action="<%= request.getContextPath() %>/internships/<%= internship.getId() %>/applications">
        <div class="d-flex gap-2 flex-wrap">
            <select name="status" class="form-select" style="max-width:200px">
                <option value="">Svi statusi</option>
                <option value="PENDING" <%= "PENDING".equals(statusFilter) ? "selected" : "" %>>Na čekanju</option>
                <option value="ACCEPTED" <%= "ACCEPTED".equals(statusFilter) ? "selected" : "" %>>Prihvaćene</option>
                <option value="REJECTED" <%= "REJECTED".equals(statusFilter) ? "selected" : "" %>>Odbijene</option>
            </select>
            <button type="submit" class="btn btn-success">Filtriraj</button>
            <a href="<%= request.getContextPath() %>/internships/<%= internship.getId() %>/applications"
               class="btn btn-outline-secondary">Reset</a>
        </div>
    </form>
</div>

<div class="card">
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>Student</th>
                <th class="d-none d-md-table-cell">Email</th>
                <th>Status</th>
                <th class="d-none d-md-table-cell">Datum</th>
                <th>Akcije</th>
            </tr>
            </thead>
            <tbody>
            <% for (JsonNode app : applications) {
                String status = app.path("status").asText();
                String firstName = app.path("student").path("firstName").asText();
                String lastName = app.path("student").path("lastName").asText();
                String email = app.path("student").path("email").asText();
                Long studentId = app.path("student").path("id").asLong();
                Long appId = app.path("id").asLong();
                String appliedAt = app.path("appliedAt").asText();
                if (appliedAt.length() > 10) appliedAt = appliedAt.substring(0, 10);
            %>
            <tr>
                <td><strong><%= firstName %> <%= lastName %></strong></td>
                <td class="d-none d-md-table-cell"><%= email %></td>
                <td>
                        <span class="badge <%= "ACCEPTED".equals(status) ? "bg-success" : "REJECTED".equals(status) ? "bg-danger" : "bg-warning" %>">
                            <%= "ACCEPTED".equals(status) ? "Prihvaćena" : "REJECTED".equals(status) ? "Odbijena" : "Na čekanju" %>
                        </span>
                </td>
                <td class="d-none d-md-table-cell"><%= appliedAt %></td>
                <td>
                    <div class="d-flex gap-1 flex-wrap">
                        <a href="<%= request.getContextPath() %>/students/<%= studentId %>?back=<%= java.net.URLEncoder.encode("/internships/" + internship.getId() + "/applications", "UTF-8") %>"
                           class="btn btn-sm btn-outline-primary btn-action" title="Pogledaj CV">
                            <span class="material-icons" style="font-size:1rem">description</span>
                        </a>
                        <a href="<%= request.getContextPath() %>/students/<%= studentId %>/pdf"
                           class="btn btn-sm btn-outline-secondary btn-action" title="Preuzmi PDF">
                            <span class="material-icons" style="font-size:1rem">picture_as_pdf</span>
                        </a>
                        <a href="<%= request.getContextPath() %>/students/<%= studentId %>/progress?back=<%= java.net.URLEncoder.encode("/internships/" + internship.getId() + "/applications", "UTF-8") %>"
                           class="btn btn-sm btn-outline-info btn-action" title="Ocjenjivanje">
                            <span class="material-icons" style="font-size:1rem">grade</span>
                        </a>
                        <% if ("PENDING".equals(status)) { %>
                        <form method="post"
                              action="<%= request.getContextPath() %>/internships/<%= internship.getId() %>/applications/<%= appId %>/accept"
                              style="display:inline">
                            <button type="submit" class="btn btn-sm btn-success btn-action" title="Prihvati">
                                <span class="material-icons" style="font-size:1rem">check</span>
                            </button>
                        </form>
                        <form method="post"
                              action="<%= request.getContextPath() %>/internships/<%= internship.getId() %>/applications/<%= appId %>/reject"
                              style="display:inline">
                            <button type="submit" class="btn btn-sm btn-danger btn-action" title="Odbij">
                                <span class="material-icons" style="font-size:1rem">close</span>
                            </button>
                        </form>
                        <% } %>
                    </div>
                </td>
            </tr>
            <% } %>
            <% if (!applications.iterator().hasNext()) { %>
            <tr>
                <td colspan="5" class="text-center text-muted py-4">Nema prijava</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>