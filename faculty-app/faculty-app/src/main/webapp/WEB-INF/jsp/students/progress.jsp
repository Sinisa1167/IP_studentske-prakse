<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ba.etf.faculty.model.Student" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<jsp:include page="../layout/header.jsp"/>

<%
    Student student = (Student) request.getAttribute("student");
    String appsJson = (String) request.getAttribute("applicationsJson");
    ObjectMapper mapper = new ObjectMapper();
    JsonNode applications = mapper.readTree(appsJson);
%>

<%
    String back = request.getParameter("back");
    String from = (String) request.getAttribute("from");
    String backUrl;
    if (back != null && !back.isEmpty()) {
        backUrl = back;
    } else if ("list".equals(from)) {
        backUrl = request.getContextPath() + "/students";
    } else {
        backUrl = request.getContextPath() + "/students/progress";
    }
%>

<div class="d-flex align-items-center mb-3">
    <a href="<%= backUrl %>" class="btn btn-outline-secondary me-3">
        <span class="material-icons align-middle">arrow_back</span>
    </a>
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">assessment</span>
        <%= student.getFirstName() %> <%= student.getLastName() %>
    </h2>
</div>

<% for (JsonNode app : applications) {
    String status = app.path("status").asText();
    String internshipTitle = app.path("internship").path("title").asText();
    String companyName = app.path("internship").path("company").path("name").asText();
    Long appId = app.path("id").asLong();
%>
<div class="card mb-3">
    <div class="card-header d-flex justify-content-between align-items-center">
        <div>
            <strong><%= internshipTitle %></strong>
            <span class="text-muted ms-2"><%= companyName %></span>
        </div>
        <span class="badge <%= "ACCEPTED".equals(status) ? "bg-success" : "REJECTED".equals(status) ? "bg-danger" : "bg-warning" %>">
            <%= "ACCEPTED".equals(status) ? "Prihvaćena" : "REJECTED".equals(status) ? "Odbijena" : "Na čekanju" %>
        </span>
    </div>
    <div class="card-body">
        <div class="row g-3">
            <!-- Dnevnik rada -->
            <div class="col-12 col-md-6">
                <h6><span class="material-icons align-middle" style="font-size:1rem">book</span> Dnevnik rada</h6>
                <%
                    String diaryJson = ba.etf.faculty.util.ApiClient.get(
                            "/diary/student/" + student.getId() + "/internship/" +
                                    app.path("internship").path("id").asLong(),
                            (String) request.getAttribute("token"));
                    JsonNode diaryEntries = mapper.readTree(diaryJson);
                %>
                <% if (diaryEntries.size() == 0) { %>
                <p class="text-muted small">Nema unosa</p>
                <% } else { %>
                <div class="list-group list-group-flush">
                    <% for (JsonNode entry : diaryEntries) { %>
                    <div class="list-group-item px-0">
                        <small class="fw-semibold">Sedmica <%= entry.path("weekNumber").asInt() %></small>
                        <p class="mb-0 small text-muted"><%= entry.path("activities").asText() %></p>
                    </div>
                    <% } %>
                </div>
                <% } %>
            </div>

            <!-- Ocjene -->
            <div class="col-12 col-md-6">
                <h6><span class="material-icons align-middle" style="font-size:1rem">grade</span> Ocjene</h6>
                <%
                    String evalsJson = ba.etf.faculty.util.ApiClient.get(
                            "/evaluations/application/" + appId,
                            (String) request.getAttribute("token"));
                    JsonNode evals = mapper.readTree(evalsJson);
                %>
                <% if (evals.size() == 0) { %>
                <p class="text-muted small">Nema ocjena</p>
                <% } else { %>
                <% for (JsonNode eval : evals) { %>
                <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="badge bg-secondary">
                                <%= "FACULTY".equals(eval.path("evaluatorRole").asText()) ? "Fakultet" : "Kompanija" %>
                            </span>
                    <span class="fw-bold text-primary"><%= eval.path("grade").asInt() %>/10</span>
                </div>
                <p class="small text-muted"><%= eval.path("comment").asText() %></p>
                <% } %>
                <% } %>

                <!-- Forma za ocjenu -->
                <% if ("ACCEPTED".equals(status)) { %>
                <hr>
                <h6 class="small">Dodaj ocjenu</h6>
                <form method="post" action="<%= request.getContextPath() %>/evaluations/<%= appId %>">
                    <div class="mb-2">
                        <input type="number" name="grade" class="form-control form-control-sm"
                               placeholder="Ocjena (1-10)" min="1" max="10" required>
                    </div>
                    <div class="mb-2">
                        <textarea name="comment" class="form-control form-control-sm"
                                  placeholder="Komentar" rows="2"></textarea>
                    </div>
                    <input type="hidden" name="evaluatorRole" value="FACULTY">
                    <button type="submit" class="btn btn-sm btn-primary w-100">
                        <span class="material-icons align-middle" style="font-size:1rem">save</span>
                        Sačuvaj ocjenu
                    </button>
                </form>
                <% } %>
            </div>
        </div>
    </div>
</div>
<% } %>

<% if (applications.size() == 0) { %>
<div class="card">
    <div class="card-body text-center py-4 text-muted">
        <span class="material-icons" style="font-size:3rem">assignment</span>
        <p>Student nema prijava na prakse.</p>
    </div>
</div>
<% } %>

<jsp:include page="../layout/footer.jsp"/>