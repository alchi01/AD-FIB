<%-- 
    Document   : text
    Created on : 13 dic 2024, 12:17:08
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/easymde/dist/easymde.min.css">
    <script src="https://cdn.jsdelivr.net/npm/easymde/dist/easymde.min.js"></script>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editor Markdown</title>
    </head>
    <body>
        <h1> Editor Markdown </h1>
        
        <script>
            document.addEventListener("DOMContentLoaded", function() {
                var easyMDE = new EasyMDE({ 
                    element: document.getElementById("markdown-editor"),
                    placeholder: "Escribe aquí...",
                    spellChecker: false,
                    autosave: {
                        enabled: true,
                        uniqueId: "markdown-editor",
                        delay: 1000,
                    },
                });
                document.getElementById("markdownForm").addEventListener("submit", function () {
                document.getElementById("markdown-editor").value = easyMDE.value();
                });
            });
        </script>
        <form id="markdownForm" action="text" method="post">
            <textarea id="markdown-editor" name="markdownContent"></textarea>
            <button type="submit">Enviar</button>
        </form>
    </body>

</html>
