<%-- 
    Document   : eliminarImagen
    Created on : 3 oct 2024, 13:10:55
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
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Eliminar Imagen!</h1>
        <%
            String imagenId = request.getParameter("imagenId");
            String imagenTitle = request.getParameter("imagenTitle");
            String imagenFile = request.getParameter("imagenFile");
        %>
        <form action = "eliminarImagen" method = "POST">
            <p align ="center"> Esta seguro de eliminar esta imagen? </p>
            <p align="center">Title: <%= imagenTitle %></p>
            <p align="center"><img src="<%= imagenFile %>" width="200" height="200"></p>
            <input type="hidden" name="imagenId" value="<%= imagenId %>" />
            <p align="center"><input type="submit" value="Eliminar"></p>
        </form>
        
    </body>
</html>
