<%-- 
    Document   : submit
    Created on : 17 oct 2024, 21:19:50
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="css/general.css">
        <link rel="stylesheet" href="css/submit.css">
    </head>
    <body>
        <% String message = (String) request.getAttribute("message");%>
           
        <h1><%= message %></h1>        
        <button onclick="location.href='menu.jsp'">Return to menu</button>
    </body>
</html>
