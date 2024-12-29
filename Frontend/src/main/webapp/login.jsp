<%-- 
    Document   : login
    Created on : 29 dic 2024, 17:09:43
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Iniciar Sesión</title>
        <link rel="stylesheet" href="styles/editor.css">
        <link rel="stylesheet" href="styles/login.css">
    </head>
    <body>
        <div class="login-container">
            <h2>Log In</h2>
            <form action="login" method="post">
                <input type="email" id="email" name="email" placeholder="Email Address" required>
                <input type="password" id="password" name="password" placeholder="Password" required>

                <div class="remember-me">
                    <input type="checkbox" id="remember" name="remember">
                    <label for="remember">Remember me for 30 days</label>
                </div>

                <button type="submit" class="login-btn">Log In</button>

                <div class="divider">or</div>

                <button type="button" class="google-btn">
                    <img src="tmp/google_logo.png" alt="Google Logo">
                    Sign in with Google
                </button>
                
                <button type="button" class="manual-btn" onclick="window.location.href='signUp.jsp'">Sign Up</button>
                 
                <a href="#" class="forgot-password">Forgot your password?</a>
            </form>
        </div>
    </body>
</html>