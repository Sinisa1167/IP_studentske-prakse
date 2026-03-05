<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ba.etf.faculty.model.Student" %>
<jsp:include page="../layout/header.jsp"/>

<%
    Student student = (Student) request.getAttribute("student");
    boolean isEdit = student != null;
%>

<div class="d-flex align-items-center mb-3">
    <a href="<%= request.getContextPath() %>/students" class="btn btn-outline-secondary me-3">
        <span class="material-icons align-middle">arrow_back</span>
    </a>
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">people</span>
        <%= isEdit ? "Uredi studenta" : "Novi student" %>
    </h2>
</div>

<div class="card">
    <div class="card-body p-4">
        <form method="post"
              action="<%= request.getContextPath() %>/students<%= isEdit ? "/" + student.getId() + "/edit" : "" %>">
            <div class="row g-3">
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Ime *</label>
                    <input type="text" name="firstName" class="form-control" required
                           value="<%= isEdit ? student.getFirstName() : "" %>">
                </div>
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Prezime *</label>
                    <input type="text" name="lastName" class="form-control" required
                           value="<%= isEdit ? student.getLastName() : "" %>">
                </div>
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Email *</label>
                    <input type="email" name="email" class="form-control" required
                           value="<%= isEdit && student.getEmail() != null ? student.getEmail() : "" %>">
                </div>
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Telefon</label>
                    <input type="text" name="phone" class="form-control"
                           value="<%= isEdit && student.getPhone() != null ? student.getPhone() : "" %>">
                </div>
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Datum rođenja</label>
                    <input type="date" name="dateOfBirth" class="form-control"
                           value="<%= isEdit && student.getDateOfBirth() != null ? student.getDateOfBirth() : "" %>">
                </div>
                <div class="col-12 col-md-6">
                    <label class="form-label fw-semibold">Adresa</label>
                    <input type="text" name="address" class="form-control"
                           value="<%= isEdit && student.getAddress() != null ? student.getAddress() : "" %>">
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
                        <%= isEdit ? "Sačuvaj izmjene" : "Kreiraj studenta" %>
                    </button>
                    <a href="<%= request.getContextPath() %>/students" class="btn btn-outline-secondary ms-2">
                        Otkaži
                    </a>
                </div>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>