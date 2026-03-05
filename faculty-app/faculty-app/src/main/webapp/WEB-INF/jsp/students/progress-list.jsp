<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="ba.etf.faculty.model.Student" %>
<jsp:include page="../layout/header.jsp"/>

<div class="d-flex align-items-center mb-3">
    <h2 class="page-title mb-0">
        <span class="material-icons align-middle">assessment</span>
        Praćenje studenata
    </h2>
</div>

<div class="card">
    <div class="table-responsive">
        <table class="table table-hover mb-0">
            <thead>
            <tr>
                <th>Student</th>
                <th class="d-none d-md-table-cell">Email</th>
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
                <td><strong><%= student.getFirstName() %> <%= student.getLastName() %></strong></td>
                <td class="d-none d-md-table-cell"><%= student.getEmail() != null ? student.getEmail() : "-" %></td>
                <td>
                    <a href="<%= request.getContextPath() %>/students/<%= student.getId() %>/progress"
                       class="btn btn-sm btn-primary btn-action">
                        <span class="material-icons align-middle" style="font-size:1rem">visibility</span>
                        Pregled
                    </a>
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

<jsp:include page="../layout/footer.jsp"/>