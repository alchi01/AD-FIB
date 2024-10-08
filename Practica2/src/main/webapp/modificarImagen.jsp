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
    <style>
        .hidden {
            display: none; /* Ocultar el campo por defecto */
        }
        .visible {
            display: block; /* Mostrar el campo de texto */
        }
    </style>
    <body>
        <h1>Modificar Imagen!</h1>
        
        <form action = "registrarImagen" method = "POST" enctype="multipart/form-data">
            <label>
                <input type="checkbox" id="commentCheckbox" onclick="mostrarTextField()"> 
                Nuevo titulo
            </label><br>
            <textarea id="commentText" class="hidden"/textarea><br>
            <label>
                <input type="checkbox" id="commentCheckbox" onclick="mostrarTextField()"> 
                Nueva descripcion
            </label><br>
            <textarea id="commentText" class="hidden"/textarea><br>
            <label>
                <input type="checkbox" id="commentCheckbox" onclick="mostrarTextField()"> 
                Nuevas palabras clave
            </label><br>
            <textarea id="commentText" class="hidden"/textarea><br>
            <label>
                <input type="checkbox" id="commentCheckbox" onclick="mostrarTextField()"> 
                Nuevo nombre de la imagen
            </label><br>
            <textarea id="commentText" class="hidden"/textarea><br>

            </form>

            <script>
            function mostrarTextField() {
                const checkbox = document.getElementById('commentCheckbox');
                const textField = document.getElementById('commentText');
                // Mostrar u ocultar el campo de texto según el estado de la casilla
                if (checkbox.checked) {
                    textField.classList.remove('hidden'); // Mostrar campo de texto
                } else {
                    textField.classList.add('hidden'); // Ocultar campo de texto
                    textField.value = ''; // Limpiar el campo de texto si se deselecciona
                }
            }
            </script>
    </body>
</html>
