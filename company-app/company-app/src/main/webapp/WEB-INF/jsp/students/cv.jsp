<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ba.etf.company.model.Student" %>
<%@ page import="ba.etf.company.model.CvEntry" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<jsp:include page="../layout/header.jsp"/>

<%
    Student student = (Student) request.getAttribute("student");
    List<CvEntry> cvEntries = (List<CvEntry>) request.getAttribute("cvEntries");
    String back = request.getParameter("back");
    String backUrl = back != null && !back.isEmpty() ? back : request.getContextPath() + "/internships";
%>

<div class="d-flex justify-content-between align-items-center mb-3">
    <div class="d-flex align-items-center">
        <a href="<%= backUrl %>" class="btn btn-outline-secondary me-3">
            <span class="material-icons align-middle">arrow_back</span>
        </a>
        <h2 class="page-title mb-0">
            <span class="material-icons align-middle">description</span>
            CV — <%= student.getFirstName() %> <%= student.getLastName() %>
        </h2>
    </div>
    <a href="<%= request.getContextPath() %>/students/<%= student.getId() %>/pdf"
       class="btn btn-outline-danger">
        <span class="material-icons align-middle">picture_as_pdf</span>
        Preuzmi PDF
    </a>
</div>

<div class="row g-3">
    <div class="col-12 col-md-4">
        <div class="card">
            <div class="card-body text-center">
                <% if (student.getPhotoPath() != null && !student.getPhotoPath().isEmpty()) { %>
                <img src="http://localhost:8080/<%= student.getPhotoPath() %>"
                     class="rounded-circle mb-3" width="100" height="100"
                     style="object-fit:cover; border: 3px solid #2e7d32">
                <% } else { %>
                <span class="material-icons text-secondary" style="font-size:5rem">account_circle</span>
                <% } %>
                <h5><%= student.getFirstName() %> <%= student.getLastName() %></h5>
                <p class="text-muted small"><%= student.getEmail() != null ? student.getEmail() : "" %></p>
                <p class="text-muted small"><%= student.getPhone() != null ? student.getPhone() : "" %></p>
                <p class="text-muted small"><%= student.getAddress() != null ? student.getAddress() : "" %></p>
            </div>
        </div>
    </div>

    <div class="col-12 col-md-8">
        <%
            String[] types = {"EDUCATION", "EXPERIENCE", "INTERNSHIP", "SKILL", "INTEREST"};
            String[] labels = {"Obrazovanje", "Radno iskustvo", "Prakse", "Vještine", "Interesovanja"};
            String[] icons = {"school", "work", "business_center", "code", "favorite"};

            for (int i = 0; i < types.length; i++) {
                final String type = types[i];
                List<CvEntry> filtered = cvEntries.stream()
                        .filter(e -> type.equals(e.getType()))
                        .collect(Collectors.toList());
                if (!filtered.isEmpty()) {
        %>
        <div class="card mb-3">
            <div class="card-header d-flex align-items-center gap-2">
                <span class="material-icons text-success"><%= icons[i] %></span>
                <strong><%= labels[i] %></strong>
            </div>
            <div class="card-body">
                <% for (CvEntry entry : filtered) { %>
                <div class="mb-3">
                    <div class="fw-semibold"><%= entry.getTitle() %></div>
                    <% if (entry.getDescription() != null && !entry.getDescription().isEmpty()) { %>
                    <div class="text-muted small"><%= entry.getDescription() %></div>
                    <% } %>
                    <% if (entry.getExtra() != null && !entry.getExtra().isEmpty()) { %>
                    <span class="badge bg-secondary"><%= entry.getExtra() %></span>
                    <% } %>
                    <% if (entry.getStartDate() != null) { %>
                    <div class="text-muted small">
                        <%= entry.getStartDate() %>
                        <% if (entry.getEndDate() != null) { %> — <%= entry.getEndDate() %><% } %>
                    </div>
                    <% } %>
                </div>
                <% } %>
            </div>
        </div>
        <%
                }
            }
        %>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>