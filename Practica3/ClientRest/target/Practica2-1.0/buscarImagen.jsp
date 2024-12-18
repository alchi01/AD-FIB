<%-- 
    Document   : buscarImagen
    Created on : 3 oct 2024, 13:10:23
    Author     : alumne
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% 
    HttpSession usersession = request.getSession(false);
    String user = (String) usersession.getAttribute("user");
    if (user == null) response.sendRedirect("login.jsp");
            
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="css/general.css">
        <link rel="stylesheet" href="css/buscarImagen.css">
    </head>
    <body>
        <h2>Imágenes del Usuario</h2>
         <p align="left"> <button onclick="location.href='menu.jsp'"> Menu </button>
        <form action = "buscarImagen" method = "GET">
            
            <p align="center">
                <label for="buscarTitulo">Título:</label>
                <input type="text" name="buscarTitulo" value="${param.buscarTitulo}">
                <label for="buscarDescripcion">Descripción:</label>
                <input type="text" name="buscarDescripcion" value="${param.buscarDescripcion}">
                <button type="submit">Buscar</button>
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
