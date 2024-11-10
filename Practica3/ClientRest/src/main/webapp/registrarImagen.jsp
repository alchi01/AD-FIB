<%-- 
    Document   : registrarImagen
    Created on : 3 oct 2024, 13:09:36
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
        <title>Register</title>
        <link rel="stylesheet" href="css/general.css">
        <link rel="stylesheet" href="css/registrarImagen.css">
    </head>
    <body>
        <h1>Registrar Imagen</h1>
        <form action = "registrarImagen" method = "POST" enctype="multipart/form-data">
            <label for="titulo">Título:</label>
            <input type="text" name="Titulo">
            <br><br>
            <label for="descripcion">Descripcion:</label>
            <input type="text" name="Descripcion">
            <br><br>
            <label for="descripcion">Palabra clave:</label>
            <input type="text" name="Keywords">
            <br><br>
            <label for="autor">Autor:</label>
            <input type="text" name="Autor">
            <br><br>
            <label for="fecha de creacion">Fecha de creacion:</label>
            <input type="date" name="Fecha de creacion">
            <br><br>
            <label for="file">Imagen:</label>
            <input type="file" name="Subir Imagen">
            <br><br>
            <input type="submit" value="Subir la imagen">
        </form>
        <button onclick="location.href='menu.jsp'"> Volver </button>
    </body>
</html>
