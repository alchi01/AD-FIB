<%-- 
    Document   : buscarImagen
    Created on : 3 oct 2024, 13:10:23
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Buscar Imagen!</h1>
        <form action = "registrarImagen" method = "GET">
            <p align="center">
            <label for="buscar">Buscar: </label>
            <input type ="text" name ="Busqueda">
            <br><br>
            ${message}
            </p>
            
        </form>
    </body>
</html>