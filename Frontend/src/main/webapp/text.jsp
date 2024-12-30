<%-- 
    Document   : text
    Created on : 13 dic 2024, 12:17:08
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/easymde/dist/easymde.min.css">
    <link rel="stylesheet" href="styles/editor.css">
    <script src="https://cdn.jsdelivr.net/npm/easymde/dist/easymde.min.js"></script>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editor Markdown</title>
    </head>
    <body>
        <div class="top-buttons" align="right">
            <button type="button" class="login-menu-btn" onclick="window.location.href='login.jsp'">Log In</button>
            <button type="button" class="signup-menu-btn" onclick="window.location.href='signUp.jsp'">Sign Up</button>
        </div>

        <div class="editor-container">
            
            <h1>Editor Markdown</h1>
            <form id="markdownForm" action="text" method="post">
                <textarea id="markdown-editor" name="markdownContent"></textarea>
                <div class="form-footer">
                    <select id="exportFormat" name="exportFormat" class="export-button" onchange="submitForm()">
                        <option value="" disabled selected>Exportar</option>
                        <option value="html">HTML</option>
                        <option value="pdf">PDF</option>
                        <option value="docx">DOCX</option>
                    </select>
                </div>
            </form>
        </div>
        <button type="button" class="save-btn">Guardar</button>

        <script>
            function submitForm() {
                const form = document.getElementById("markdownForm");
                form.submit(); // Enviar el formulario al servlet
            }
        </script>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
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
    </body>

</html>
