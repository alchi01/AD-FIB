<%-- 
    Document   : error.jsp
    Created on : 2 oct 2024, 19:33:48
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ERROR</title>
    </head>
    <body>
        <% String TError = (String) request.getAttribute("TError");%> 
        
        <% if (TError.equals("login_error")) {%>
            <h2>Error: Wrong username and/or password. <h2>
             <button onclick="location.href='login.jsp'">Try again</button>
        <% } else if (TError.equals("image_error")) { %>
            <h2> Error registering, modifying or removing the image. <h2>
            <button onclick="location.href='menu.jsp'">Return to menu</button>
        <% } %>
    </body>
</html>
