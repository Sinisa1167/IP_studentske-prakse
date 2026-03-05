<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ba.etf.faculty.model.Internship" %>
<%@ page import="ba.etf.faculty.model.Technology" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<jsp:include page="../layout/header.jsp"/>

<%
    Internship internship = (Internship) request.getAttribute("internship");
    String appsJson = (String) request.getAttribute("applicationsJson");
    ObjectMapper mapper = new ObjectMapper();
    JsonNode appsNode = mapper.readTree(appsJson);
    JsonNode applications = appsNode.path("content");
%>

<div class="d-flex align-items-center mb-3">
    <a href="<%= request.getContextPath() %>/internships" class="btn btn-outline-secondary me-3">
        <span class="material-icons align-middle">arrow_back</span>
    </a>
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">work</span>
        <%= internship.getTitle() %>
    </h2>
</div>

<div class="row g-3 mb-4">
    <div class="col-12 col-md-6">
        <div class="card h-100">
            <div class="card-body">
                <h6 class="text-muted mb-3">Detalji prakse</h6>
                <p><strong>Kompanija:</strong>
                    <%= internship.getCompany() != null ? internship.getCompany().getName() : "-" %>
                </p>
                <p><strong>Period:</strong>
                    <%= internship.getStartDate() != null ? internship.getStartDate() : "-" %>
                    <% if (internship.getEndDate() != null) { %> — <%= internship.getEndDate() %><% } %>
                </p>
                <p><strong>Opis:</strong> <%= internship.getDescription() != null ? internship.getDescription() : "-" %></p>
                <p><strong>Uslovi:</strong> <%= internship.getConditions() != null ? internship.getConditions() : "-" %></p>
                <div>
                    <strong>Tehnologije:</strong>
                    <% if (internship.getTechnologies() != null) {
                        for (Technology tech : internship.getTechnologies()) { %>
                    <span class="badge bg-secondary me-1"><%= tech.getName() %></span>
                    <% } } %>
                </div>
            </div>
        </div>
    </div>
    <div class="col-12 col-md-6">
        <div class="card h-100">
            <div class="card-body">
                <h6 class="text-muted mb-3">Statistika prijava</h6>
                <%
                    int total = 0, accepted = 0, rejected = 0, pending = 0;
                    for (JsonNode app : applications) {
                        total++;
                        String s = app.path("status").asText();
                        if ("ACCEPTED".equals(s)) accepted++;
                        else if ("REJECTED".equals(s)) rejected++;
                        else pending++;
                    }
                %>
                <div class="row text-center g-2">
                    <div class="col-6">
                        <div class="p-3 bg-light rounded">
                            <div class="fs-4 fw-bold text-primary"><%= total %></div>
                            <div class="small text-muted">Ukupno</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="p-3 bg-light rounded">
                            <div class="fs-4 fw-bold text-success"><%= accepted %></div>
                            <div class="small text-muted">Prihvaćeno</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="p-3 bg-light rounded">
                            <div class="fs-4 fw-bold text-warning"><%= pending %></div>
                            <div class="small text-muted">Na čekanju</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="p-3 bg-light rounded">
                            <div class="fs-4 fw-bold text-danger"><%= rejected %></div>
                            <div class="small text-muted">Odbijeno</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<h5 class="mb-3">
    <span class="material-icons align-middle">people</span>
    Prijavljeni studenti
</h5>

<div class="card">
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>Student</th>
                <th class="d-none d-md-table-cell">Email</th>
                <th>Status</th>
                <th class="d-none d-md-table-cell">Datum prijave</th>
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
                    <a href="<%= request.getContextPath() %>/students/<%= studentId %>/progress?back=<%= java.net.URLEncoder.encode("/internships/" + internship.getId(), "UTF-8") %>"
                       class="btn btn-sm btn-outline-primary btn-action">
                        <span class="material-icons" style="font-size:1rem">assessment</span>
                    </a>
                </td>
            </tr>
            <% } %>
            <% if (!applications.iterator().hasNext()) { %>
            <tr>
                <td colspan="5" class="text-center text-muted py-4">Nema prijavljenih studenata</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>