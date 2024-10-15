<%-- 
    Document   : buscarImagen
    Created on : 3 oct 2024, 13:10:23
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Imágenes del Usuario</h2>
        <form action = "buscarImagen" method = "GET">
            <p align="center">
            <label for="buscar">Buscar: </label>
            <input type ="text" name ="Busqueda">
            <br><br>
            </p>
            <c:forEach var="filaImagen" items="${imagenesFiltradas}">
                <div>
                    <p align="center">Title: "${filaImagen[1]}"</p>
                    <p align="center"><img src="/home/alumne/AD-FIB/Practica2/target/Practica2-1.0/${filaImagen[8]}" width="200" height="200"></p>
                    <p align="center">Description: ${filaImagen[2]}</p>
                    <p align="center">
                        <button type="submit" name="modImagen">Modificar imagen</button>
                        <button type="submit" name="elimImagen" >Eliminar imagen</button>
                    </p>
                    
                </div>
                <hr>
            </c:forEach>
            
        </form>
    </body>
</html>
