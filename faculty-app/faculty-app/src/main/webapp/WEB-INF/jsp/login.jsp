<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="bs">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Prijava - ETF Prakse</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #1565c0 0%, #0d47a1 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-card {
            border-radius: 16px;
            border: none;
            box-shadow: 0 8px 32px rgba(0,0,0,0.2);
            width: 100%;
            max-width: 400px;
        }
        .login-header {
            background: #1565c0;
            color: white;
            border-radius: 16px 16px 0 0;
            padding: 32px;
            text-align: center;
        }
        .login-header .material-icons {
            font-size: 3rem;
            margin-bottom: 8px;
        }
        .login-body {
            padding: 32px;
        }
    </style>
</head>
<body>
<div class="login-card">
    <div class="login-header">
        <div class="material-icons">school</div>
        <h4 class="mb-0">ETF Studentske prakse</h4>
        <small class="opacity-75">Aplikacija za fakultet</small>
    </div>
    <div class="login-body bg-white rounded-bottom">
        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger alert-dismissible">
            <span class="material-icons align-middle">error</span>
            <%= request.getAttribute("error") %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/login">
            <div class="mb-3">
                <label class="form-label fw-semibold">Korisničko ime</label>
                <div class="input-group">
                    <span class="input-group-text">
                        <span class="material-icons" style="font-size:1.2rem">person</span>
                    </span>
                    <input type="text" name="username" class="form-control"
                           placeholder="Unesite korisničko ime" required autofocus>
                </div>
            </div>
            <div class="mb-4">
                <label class="form-label fw-semibold">Lozinka</label>
                <div class="input-group">
                    <span class="input-group-text">
                        <span class="material-icons" style="font-size:1.2rem">lock</span>
                    </span>
                    <input type="password" name="password" class="form-control"
                           placeholder="Unesite lozinku" required>
                </div>
            </div>
            <button type="submit" class="btn btn-primary w-100 py-2">
                <span class="material-icons align-middle">login</span>
                Prijava
            </button>
        </form>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>