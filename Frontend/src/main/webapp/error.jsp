<%-- 
    Document   : error.jsp
    Created on : 30 dic 2024, 18:42:09
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ERROR</title>
        <link rel="stylesheet" href="styles/editor.css">
        <link rel="stylesheet" href="styles/error.css">
    </head>
    <body>
        <% String TError = (String) request.getAttribute("TError");%> 

        <div class="error-container">
            <% if (TError.equals("login_error")) {%>
                <h2>Error: Wrong username and/or password.</h2>
                <button class="error-button" onclick="location.href='login.jsp'">Try again</button>
            <% } else if (TError.equals("register_error")) { %>
                <h2>Error: Username already taken or empty values.</h2>
                <button class="error-button" onclick="location.href='signUp.jsp'">Try again</button>
            <% } else if (TError.equals("register_error_password")) { %>
                <h2>Error: Passwords don't match.</h2>
                <button class="error-button" onclick="location.href='signUp.jsp'">Try again</button>
            <% } else if (TError.equals("load_error")) { %>
                <h2>Error: There isn't any document saved.</h2>
                <button class="error-button" onclick="location.href='text.jsp'">Return</button>
            <% } %>
        </div>
    </body>
</html>
