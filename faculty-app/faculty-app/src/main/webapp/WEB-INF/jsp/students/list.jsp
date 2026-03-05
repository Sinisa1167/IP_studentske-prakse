<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="ba.etf.faculty.model.Student" %>
<jsp:include page="../layout/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">people</span>
        Studenti
    </h2>
    <div class="d-flex gap-2">
        <button class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#csvModal">
            <span class="material-icons align-middle">upload_file</span>
            Import CSV
        </button>
        <a href="<%= request.getContextPath() %>/students/new" class="btn btn-primary">
            <span class="material-icons align-middle">add</span>
            Novi student
        </a>
    </div>
</div>

<% if (session.getAttribute("importMsg") != null) { %>
<div class="alert alert-success alert-dismissible">
    <span class="material-icons align-middle">check_circle</span>
    <%= session.getAttribute("importMsg") %>
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
</div>
<% session.removeAttribute("importMsg"); %>
<% } %>

<div class="search-bar">
    <form method="get" action="<%= request.getContextPath() %>/students">
        <div class="input-group">
            <span class="input-group-text">
                <span class="material-icons" style="font-size:1.2rem">search</span>
            </span>
            <input type="text" name="search" class="form-control"
                   placeholder="Pretraži po prezimenu..."
                   value="<%= request.getAttribute("search") != null ? request.getAttribute("search") : "" %>">
            <button type="submit" class="btn btn-primary">Pretraži</button>
            <a href="<%= request.getContextPath() %>/students" class="btn btn-outline-secondary">Reset</a>
        </div>
    </form>
</div>

<div class="card">
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>Ime i prezime</th>
                <th class="d-none d-md-table-cell">Email</th>
                <th class="d-none d-md-table-cell">Telefon</th>
                <th class="d-none d-lg-table-cell">Adresa</th>
                <th>Akcije</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Student> students = (List<Student>) request.getAttribute("students");
                if (students != null) {
                    for (Student student : students) {
            %>
            <tr>
                <td>
                    <div class="d-flex align-items-center gap-2">
                        <% if (student.getPhotoPath() != null && !student.getPhotoPath().isEmpty()) { %>
                        <img src="http://localhost:8080/<%= student.getPhotoPath() %>"
                             class="rounded-circle" width="32" height="32"
                             style="object-fit:cover">
                        <% } else { %>
                        <span class="material-icons text-secondary">account_circle</span>
                        <% } %>
                        <strong><%= student.getFirstName() %> <%= student.getLastName() %></strong>
                    </div>
                </td>
                <td class="d-none d-md-table-cell"><%= student.getEmail() != null ? student.getEmail() : "-" %></td>
                <td class="d-none d-md-table-cell"><%= student.getPhone() != null ? student.getPhone() : "-" %></td>
                <td class="d-none d-lg-table-cell"><%= student.getAddress() != null ? student.getAddress() : "-" %></td>
                <td>
                    <div class="d-flex gap-1 flex-wrap">
                        <a href="<%= request.getContextPath() %>/students/<%= student.getId() %>/edit"
                           class="btn btn-sm btn-outline-primary btn-action">
                            <span class="material-icons" style="font-size:1rem">edit</span>
                        </a>
                        <a href="<%= request.getContextPath() %>/students/<%= student.getId() %>/progress?from=list"
                           class="btn btn-sm btn-outline-info btn-action">
                            <span class="material-icons" style="font-size:1rem">assessment</span>
                        </a>
                        <form method="post"
                              action="<%= request.getContextPath() %>/students/<%= student.getId() %>/delete"
                              style="display:inline">
                            <button type="submit" class="btn btn-sm btn-outline-danger btn-action"
                                    onclick="return confirm('Obrisati studenta?')">
                                <span class="material-icons" style="font-size:1rem">delete</span>
                            </button>
                        </form>
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
               href="<%= request.getContextPath() %>/students?page=<%= i %><%= request.getAttribute("search") != null ? "&search=" + request.getAttribute("search") : "" %>">
                <%= i + 1 %>
            </a>
        </li>
        <% } %>
    </ul>
</nav>
<% } %>

<!-- CSV Modal -->
<div class="modal fade" id="csvModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">
                    <span class="material-icons align-middle">upload_file</span>
                    Import studenata iz CSV
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p class="text-muted small">
                    Format CSV: <code>firstName, lastName, email, phone, dateOfBirth, address, username, password</code>
                </p>
                <form method="post" action="<%= request.getContextPath() %>/students"
                      enctype="multipart/form-data">
                    <div class="mb-3">
                        <label class="form-label">CSV fajl</label>
                        <input type="file" name="csvFile" class="form-control" accept=".csv" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">
                        <span class="material-icons align-middle">upload</span>
                        Uvezi
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>