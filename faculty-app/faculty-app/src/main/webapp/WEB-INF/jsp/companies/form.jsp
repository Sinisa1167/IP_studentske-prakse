<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ba.etf.faculty.model.Company" %>
<jsp:include page="../layout/header.jsp"/>

<%
    Company company = (Company) request.getAttribute("company");
    boolean isEdit = company != null;
%>

<div class="d-flex align-items-center mb-3">
    <a href="<%= request.getContextPath() %>/companies" class="btn btn-outline-secondary me-3">
        <span class="material-icons align-middle">arrow_back</span>
    </a>
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">business</span>
        <%= isEdit ? "Uredi kompaniju" : "Nova kompanija" %>
    </h2>
</div>

<div class="card">
    <div class="card-body p-4">
        <form method="post" action="<%= request.getContextPath() %>/companies<%= isEdit ? "/" + company.getId() + "/edit" : "" %>">
            <div class="row g-3">
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Naziv kompanije *</label>
                    <input type="text" name="name" class="form-control" required
                           value="<%= isEdit ? company.getName() : "" %>">
                </div>
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Email</label>
                    <input type="email" name="contactEmail" class="form-control"
                           value="<%= isEdit && company.getContactEmail() != null ? company.getContactEmail() : "" %>">
                </div>
                <div class="col-12">
                    <label class="form-label fw-semibold">Adresa</label>
                    <input type="text" name="address" class="form-control"
                           value="<%= isEdit && company.getAddress() != null ? company.getAddress() : "" %>">
                </div>
                <div class="col-12">
                    <label class="form-label fw-semibold">Opis</label>
                    <textarea name="description" class="form-control" rows="3"><%= isEdit && company.getDescription() != null ? company.getDescription() : "" %></textarea>
                </div>

                <% if (!isEdit) { %>
                <div class="col-12"><hr><h6>Podaci za prijavu</h6></div>
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Korisničko ime *</label>
                    <input type="text" name="username" class="form-control" required>
                </div>
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Lozinka *</label>
                    <input type="password" name="password" class="form-control" required>
                </div>
                <% } %>

                <div class="col-12">
                    <button type="submit" class="btn btn-primary">
                        <span class="material-icons align-middle">save</span>
                        <%= isEdit ? "Sačuvaj izmjene" : "Kreiraj kompaniju" %>
                    </button>
                    <a href="<%= request.getContextPath() %>/companies" class="btn btn-outline-secondary ms-2">
                        Otkaži
                    </a>
                </div>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>