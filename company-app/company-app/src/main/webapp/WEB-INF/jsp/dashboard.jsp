<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="layout/header.jsp"/>

<h2 class="page-title">
    <span class="material-icons align-middle">dashboard</span>
    Dashboard — <%= request.getAttribute("companyName") %>
</h2>

<div class="row g-3 mb-4">
    <div class="col-6 col-md-4">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-icon blue">
                    <span class="material-icons">work</span>
                </div>
                <div>
                    <div class="stat-number"><%= request.getAttribute("internshipCount") %></div>
                    <div class="stat-label">Prakse</div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="row g-3">
    <div class="col-12 col-md-6 col-lg-4">
        <a href="<%= request.getContextPath() %>/internships" class="text-decoration-none">
            <div class="card h-100">
                <div class="card-body text-center py-4">
                    <span class="material-icons text-success" style="font-size:2.5rem">work</span>
                    <h6 class="mt-2 mb-0">Upravljanje praksama</h6>
                </div>
            </div>
        </a>
    </div>
    <div class="col-12 col-md-6 col-lg-4">
        <a href="<%= request.getContextPath() %>/change-password" class="text-decoration-none">
            <div class="card h-100">
                <div class="card-body text-center py-4">
                    <span class="material-icons text-warning" style="font-size:2.5rem">lock</span>
                    <h6 class="mt-2 mb-0">Promjena lozinke</h6>
                </div>
            </div>
        </a>
    </div>
</div>

<jsp:include page="layout/footer.jsp"/>