<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="layout/header.jsp"/>

<h2 class="page-title">
    <span class="material-icons align-middle">dashboard</span>
    Dashboard
</h2>

<div class="row g-3 mb-4">
    <div class="col-6 col-md-4">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-icon blue">
                    <span class="material-icons">people</span>
                </div>
                <div>
                    <div class="stat-number"><%= request.getAttribute("studentCount") %></div>
                    <div class="stat-label">Studenti</div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-6 col-md-4">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-icon green">
                    <span class="material-icons">business</span>
                </div>
                <div>
                    <div class="stat-number"><%= request.getAttribute("companyCount") %></div>
                    <div class="stat-label">Kompanije</div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-6 col-md-4">
        <div class="card stat-card">
            <div class="card-body">
                <div class="stat-icon orange">
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
    <div class="col-12 col-md-6 col-lg-3">
        <a href="<%= request.getContextPath() %>/companies" class="text-decoration-none">
            <div class="card h-100">
                <div class="card-body text-center py-4">
                    <span class="material-icons text-primary" style="font-size:2.5rem">business</span>
                    <h6 class="mt-2 mb-0">Upravljanje kompanijama</h6>
                </div>
            </div>
        </a>
    </div>
    <div class="col-12 col-md-6 col-lg-3">
        <a href="<%= request.getContextPath() %>/internships" class="text-decoration-none">
            <div class="card h-100">
                <div class="card-body text-center py-4">
                    <span class="material-icons text-success" style="font-size:2.5rem">work</span>
                    <h6 class="mt-2 mb-0">Pregled praksi</h6>
                </div>
            </div>
        </a>
    </div>
    <div class="col-12 col-md-6 col-lg-3">
        <a href="<%= request.getContextPath() %>/students" class="text-decoration-none">
            <div class="card h-100">
                <div class="card-body text-center py-4">
                    <span class="material-icons text-warning" style="font-size:2.5rem">people</span>
                    <h6 class="mt-2 mb-0">Upravljanje studentima</h6>
                </div>
            </div>
        </a>
    </div>
    <div class="col-12 col-md-6 col-lg-3">
        <a href="<%= request.getContextPath() %>/students/progress" class="text-decoration-none">
            <div class="card h-100">
                <div class="card-body text-center py-4">
                    <span class="material-icons text-danger" style="font-size:2.5rem">assessment</span>
                    <h6 class="mt-2 mb-0">Praćenje studenata</h6>
                </div>
            </div>
        </a>
    </div>
</div>

<jsp:include page="layout/footer.jsp"/>