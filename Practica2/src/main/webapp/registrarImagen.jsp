<%-- 
    Document   : registrarImagen
    Created on : 3 oct 2024, 13:09:36
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
    </head>
    <body>
        <h1>Registrar Imagen</h1>
        <form action = "registrarImagen" method = "POST">
            <input type="text" name="Titulo">
            <input type="text" name="Descripcion">
            <input type="text" name="Autor">
            <input type="date" name="Fecha de creacion">
            <input type="file" name="Subir Imagen">
            <input type="submit" value="Subir la imagen">
        </form>
    </body>
</html>
