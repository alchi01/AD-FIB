<%-- 
    Document   : modificarImagen
    Created on : 3 oct 2024, 13:08:37
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modificar imagen</title>
    </head>
    
        <h1>Modificar Imagen!</h1>
        
        <form action = "modificarImagen" method = "POST" enctype= "multipart/form-data">
            <br><br>
            <input type="text" name="newTit" id="newTit">
            <br>
            <input type="text" name="newDesc" id="newDesc">
            <br>
            <input type="text" name="newKey" id="newKey">
            <br>
            <input type="text" name="newImg" id="newImg">
            <br>
            <button id="enviar">Enviar</button>
            </form>

    
</html>
