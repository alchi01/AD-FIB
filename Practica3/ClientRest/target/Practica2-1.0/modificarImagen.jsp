<%-- 
    Document   : modificarImagen
    Created on : 17 oct 2024, 13:02:02
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
        <title>Modificar Imagen</title>
        <link rel="stylesheet" href="css/general.css">
        <link rel="stylesheet" href="css/eliminar_modificar.css">
    </head>
    <body>
        <%
            String imagenId = request.getParameter("imagenId");
            String imagenTitle = request.getParameter("imagenTitle");
            String imagenFile = request.getParameter("imagenFile");
        %>
        <h1>Modificar Imagen</h1>
        <br><!-- comment -->
                
        <form action="modificarImagen" method="POST" >
            <h2> Si algun campo esta en blanco se determinará que no se desea la modificacion </h2>
            <p align="center"><img src="<%= imagenFile %>" width="200" height="200"></p>
            <p align="center"><label for="titulo">Nuevo Título:</label><input type="text" name="newTit"></p>
            <p align="center"><label for="titulo">Nueva Descripcion:</label><input type="text" name="newDesc"></p>
            <p align="center"><label for="titulo">Nuevas Keywords:</label><input type="text" name="newKey"></p>
            <p align="center"><label for="titulo">Nuevo nombre de la imagen</label><input type="text" name="newImg"></p>
            <p align="center"><input type="submit" value="Modificar"></p>
            <input type="hidden" name="imagenId" value="<%= imagenId %>">
        </form>
        <p align="center"><button onclick="location.href='buscarImagen'"> Volver </button></p>
    </body>
</html>
