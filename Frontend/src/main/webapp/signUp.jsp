<%-- 
    Document   : registerUser
    Created on : 29 dic 2024, 17:10:01
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sign Up</title>
        <link rel="stylesheet" href="styles/editor.css"> <!-- Estilos existentes -->
        <link rel="stylesheet" href="styles/signUp.css"> <!-- Estilos específicos para el registro -->
    </head>
    <body>
        <div class="signup-container">
            <h2>Sign Up</h2>
            <form action="procesar_registro.jsp" method="post">
                <input type="email" id="email" name="email" placeholder="Email Address" required>
                <input type="text" id="username" name="username" placeholder="Username" required>
                <input type="password" id="password" name="password" placeholder="Password" required>
                <input type="password" id="confirm-password" name="confirm-password" placeholder="Repeat Password" required>

                <div class="button-group">
                    <button type="submit" class="create-btn">Create</button>
                    <button type="button" class="cancel-btn" onclick="window.location.href='text.jsp'">Cancel</button>
                </div>
            </form>
        </div>
    </body>
</html>
