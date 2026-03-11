<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="layout/header.jsp"/>

<div class="d-flex align-items-center mb-3">
    <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-outline-secondary me-3">
        <span class="material-icons align-middle">arrow_back</span>
    </a>
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">lock</span>
        Promjena lozinke
    </h2>
</div>

<div class="card" style="max-width: 500px;">
    <div class="card-body p-4">
        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger">
            <span class="material-icons align-middle">error</span>
            <%= request.getAttribute("error") %>
        </div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
        <div class="alert alert-success">
            <span class="material-icons align-middle">check_circle</span>
            <%= request.getAttribute("success") %>
        </div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/change-password">
            <div class="mb-3">
                <label class="form-label fw-semibold">Nova lozinka</label>
                <input type="password" name="newPassword" class="form-control"
                       placeholder="Unesite novu lozinku" required minlength="6">
            </div>
            <div class="mb-4">
                <label class="form-label fw-semibold">Potvrdi lozinku</label>
                <input type="password" name="confirmPassword" class="form-control"
                       placeholder="Ponovite novu lozinku" required minlength="6">
            </div>
            <button type="submit" class="btn btn-success w-100">
                <span class="material-icons align-middle">save</span>
                Sačuvaj lozinku
            </button>
        </form>
    </div>
</div>

<jsp:include page="layout/footer.jsp"/>