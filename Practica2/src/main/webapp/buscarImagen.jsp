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
         <p align="left"> <button onclick="location.href='menu.jsp'"> Menu </button>
        <form action = "buscarImagen" method = "GET">
            
            <p align="center">
            <label for="buscar">Buscar: </label>
            <input type ="text" name ="Busqueda">
            <br><br>
            </p>
        </form>
        <c:forEach var="filaImagen" items="${imagenesFiltradas}">
            <div>
                <p align="center">Title: "${filaImagen[1]}"</p>
                <p align="center"><img src="${filaImagen[8]}" width="200" height="200"></p>
                <p align="center">Description: ${filaImagen[2]}</p>
                <div align="center">
                    
                    <form action="modificarImagen.jsp" method="GET">
                        <input type="hidden" name="imagenId" value="${filaImagen[0]}" /> 
                        <input type="hidden" name="imagenTitle" value="${filaImagen[1]}" /> 
                        <input type="hidden" name="imagenFile" value="${filaImagen[8]}" />
                        <button type="submit">Modificar imagen</button>
                    </form>

                    
                    <form action="eliminarImagen.jsp" method="GET">
                        <input type="hidden" name="imagenId" value="${filaImagen[0]}" /> <!-- ID de la imagen -->
                        <input type="hidden" name="imagenTitle" value="${filaImagen[1]}" /> <!-- TITLE de la imagen -->
                        <input type="hidden" name="imagenFile" value="${filaImagen[8]}" /> <!-- FILE de la imagen -->
                        <button type="submit">Eliminar imagen</button>
                    </form>
                </div>
                


            </div>
            <hr>
        </c:forEach>
    </body>
</html>
