<%-- 
    Document   : menu.jsp
    Created on : 2 oct 2024, 19:43:00
    Author     : alumne
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% 
    HttpSession usersession = request.getSession(false);
    String user = (String) usersession.getAttribute("user");
    if (user == null) response.sendRedirect("login.jsp");
            
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Menu</title>
    </head>
    <body>
        <h1>Menu</h1>
        <form action="menu" method="post">
            <button type="submit" name="regImagen">Registrar imagen</button>
            <button type="submit" name="busImagen">Buscar imagen</button>
            <button type="submit" name="CerrSesion">Cerrar Sesion</button>
        </form>
    </body>
</html>
