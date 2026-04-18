<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ba.etf.company.model.Student" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<jsp:include page="../layout/header.jsp"/>

<%
    Student student = (Student) request.getAttribute("student");
    String appsJson = (String) request.getAttribute("applicationsJson");
    String token = (String) request.getAttribute("token");
    ObjectMapper mapper = new ObjectMapper();
    JsonNode applications = mapper.readTree(appsJson != null ? appsJson : "[]");
    String back = request.getParameter("back");
    String backUrl = back != null && !back.isEmpty() ? back : request.getContextPath() + "/internships";
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

<% for (JsonNode appNode : applications) {
    String status = appNode.path("status").asText();
    String internshipTitle = appNode.path("internship").path("title").asText();
    Long appId = appNode.path("id").asLong();
    Long internshipId = appNode.path("internship").path("id").asLong();

    String diaryJson = ba.etf.company.util.ApiClient.get(
            "/diary/student/" + student.getId() + "/internship/" + internshipId, token);
    JsonNode diaryEntries = mapper.readTree(diaryJson);

    String evalsJson = ba.etf.company.util.ApiClient.get(
            "/evaluations/application/" + appId, token);
    JsonNode evals = mapper.readTree(evalsJson);

    boolean hasCompanyEval = false;
    for (JsonNode eval : evals) {
        if ("COMPANY".equals(eval.path("evaluatorRole").asText())) {
            hasCompanyEval = true;
        }
    }
%>
<div class="card mb-3">
    <div class="card-header d-flex justify-content-between align-items-center">
        <strong><%= internshipTitle %></strong>
        <span class="badge <%= "ACCEPTED".equals(status) ? "bg-success" : "REJECTED".equals(status) ? "bg-danger" : "bg-warning" %>">
            <%= "ACCEPTED".equals(status) ? "Prihvaćena" : "REJECTED".equals(status) ? "Odbijena" : "Na čekanju" %>
        </span>
    </div>
    <div class="card-body">
        <div class="row g-3">

            <div class="col-12 col-md-6">
                <h6><span class="material-icons align-middle" style="font-size:1rem">book</span> Dnevnik rada</h6>
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

            <div class="col-12 col-md-6">
                <h6><span class="material-icons align-middle" style="font-size:1rem">grade</span> Ocjene</h6>

                <% for (JsonNode eval : evals) {
                    if ("COMPANY".equals(eval.path("evaluatorRole").asText())) { %>
                <div class="d-flex justify-content-between mb-1">
                    <span class="badge bg-secondary">Kompanija</span>
                    <span class="fw-bold text-success"><%= eval.path("grade").asInt() %>/10</span>
                </div>
                <p class="small text-muted"><%= eval.path("comment").asText() %></p>
                <%  }
                } %>

                <% if ("ACCEPTED".equals(status) && !hasCompanyEval) { %>
                <hr>
                <h6 class="small">Dodaj ocjenu</h6>
                <form method="post"
                      action="<%= request.getContextPath() %>/students/<%= student.getId() %>/progress/evaluate">
                    <input type="hidden" name="applicationId" value="<%= appId %>">
                    <div class="mb-2">
                        <input type="number" name="grade" class="form-control form-control-sm"
                               placeholder="Ocjena (1-10)" min="1" max="10" required>
                    </div>
                    <div class="mb-2">
                        <textarea name="comment" class="form-control form-control-sm"
                                  placeholder="Komentar" rows="2"></textarea>
                    </div>
                    <button type="submit" class="btn btn-sm btn-success w-100">
                        <span class="material-icons align-middle" style="font-size:1rem">save</span>
                        Sačuvaj ocjenu
                    </button>
                </form>
                <% } else if (hasCompanyEval) { %>
                <p class="text-muted small mt-2">
                    <span class="material-icons align-middle" style="font-size:1rem">check_circle</span>
                    Ocjena već unesena.
                </p>
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
        <p>Student nema prijava na prakse ove kompanije.</p>
    </div>
</div>
<% } %>

<jsp:include page="../layout/footer.jsp"/>