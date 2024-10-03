<%-- 
    Document   : menu.jsp
    Created on : 2 oct 2024, 19:43:00
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Menu</title>
    </head>
    <body>
        <h1>Menu!</h1>
        <form action="menu" method="post">
            <button type="submit" name="regImagen">Registrar imagen</button>
            <button type="submit" name="modImagen">Modificar imagen</button>
            <button type="submit" name="busImagen">Buscar imagen</button>
            <button type="submit" name="elimImagen" >Eliminar imagen</button>
        </form>
    </body>
</html>
