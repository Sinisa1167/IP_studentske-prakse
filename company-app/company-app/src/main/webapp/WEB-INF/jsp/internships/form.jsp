<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ba.etf.company.model.Internship" %>
<%@ page import="ba.etf.company.model.Technology" %>
<%@ page import="java.util.List" %>
<jsp:include page="../layout/header.jsp"/>

<%
    Internship internship = (Internship) request.getAttribute("internship");
    boolean isEdit = internship != null;
    List<Technology> technologies = (List<Technology>) request.getAttribute("technologies");
%>

<div class="d-flex align-items-center mb-3">
    <a href="<%= request.getContextPath() %>/internships" class="btn btn-outline-secondary me-3">
        <span class="material-icons align-middle">arrow_back</span>
    </a>
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">work</span>
        <%= isEdit ? "Uredi praksu" : "Nova praksa" %>
    </h2>
</div>

<div class="card">
    <div class="card-body p-4">
        <form method="post"
              action="<%= request.getContextPath() %>/internships<%= isEdit ? "/" + internship.getId() + "/edit" : "/new" %>">
            <div class="row g-3">
                <div class="col-12">
                    <label class="form-label fw-semibold">Naziv *</label>
                    <input type="text" name="title" class="form-control" required
                           value="<%= isEdit ? internship.getTitle() : "" %>">
                </div>
                <div class="col-12">
                    <label class="form-label fw-semibold">Opis</label>
                    <textarea name="description" class="form-control" rows="3"><%= isEdit && internship.getDescription() != null ? internship.getDescription() : "" %></textarea>
                </div>
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Datum početka</label>
                    <input type="date" name="startDate" class="form-control"
                           value="<%= isEdit && internship.getStartDate() != null ? internship.getStartDate() : "" %>">
                </div>
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Datum završetka</label>
                    <input type="date" name="endDate" class="form-control"
                           value="<%= isEdit && internship.getEndDate() != null ? internship.getEndDate() : "" %>">
                </div>
                <div class="col-12">
                    <label class="form-label fw-semibold">Uslovi i ograničenja</label>
                    <textarea name="conditions" class="form-control" rows="2"><%= isEdit && internship.getConditions() != null ? internship.getConditions() : "" %></textarea>
                </div>
                <div class="col-12">
                    <label class="form-label fw-semibold">Tehnologije</label>
                    <div class="row g-2">
                        <% if (technologies != null) {
                            for (Technology tech : technologies) {
                                boolean selected = isEdit && internship.getTechnologies() != null &&
                                        internship.getTechnologies().stream()
                                                .anyMatch(t -> t.getId().equals(tech.getId()));
                        %>
                        <div class="col-6 col-md-4 col-lg-3">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox"
                                       name="technologyIds" value="<%= tech.getId() %>"
                                       id="tech_<%= tech.getId() %>"
                                    <%= selected ? "checked" : "" %>>
                                <label class="form-check-label" for="tech_<%= tech.getId() %>">
                                    <%= tech.getName() %>
                                </label>
                            </div>
                        </div>
                        <% } } %>
                    </div>
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-success">
                        <span class="material-icons align-middle">save</span>
                        <%= isEdit ? "Sačuvaj izmjene" : "Kreiraj praksu" %>
                    </button>
                    <a href="<%= request.getContextPath() %>/internships" class="btn btn-outline-secondary ms-2">
                        Otkaži
                    </a>
                </div>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>