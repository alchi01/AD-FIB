/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author alumne
 */

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
@WebServlet(name = "registrarImagen", urlPatterns = {"/registrarImagen"})
public class registrarImagen extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String user = (String) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
        } else {
            response.sendRedirect("menu.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String titulo = request.getParameter("Titulo");
        String descripcion = request.getParameter("Descripcion");
        String keywords = request.getParameter("Keywords");
        String author = request.getParameter("Autor");
        String fechaCapt = request.getParameter("Fecha de creacion");
        Part filePart = request.getPart("Subir Imagen");
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("user");
        String fileName = filePart.getSubmittedFileName();

        // Validar que los campos requeridos no estén vacíos
        if (titulo == null || descripcion == null || keywords == null || author == null || fechaCapt == null ||
            titulo.trim().isEmpty() || descripcion.trim().isEmpty() || keywords.trim().isEmpty() || 
            author.trim().isEmpty() || fechaCapt.trim().isEmpty()) {
            request.setAttribute("TError", "Todos los campos son obligatorios.");
            RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }

        // Preparar la solicitud al servidor REST
        String urlString = "http://localhost:8080/RestAD/resources/jakartaee9/uploadImage"; // Cambia esto a la URL correcta
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Crear el cuerpo de la solicitud en formato JSON
            JsonObject jsonInput = Json.createObjectBuilder()
                    .add("title", titulo)
                    .add("description", descripcion)
                    .add("keywords", keywords)
                    .add("author", author)
                    .add("fechaCapt", fechaCapt)
                    .add("user", user)
                    .build();

            // Enviar los datos JSON
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Enviar el archivo de imagen
            try (InputStream input = filePart.getInputStream()) {
                // Enviar el archivo como parte de la solicitud
                OutputStream os = connection.getOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }

            // Leer la respuesta del servidor
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                request.setAttribute("message", "Se ha subido la imagen correctamente.");
                RequestDispatcher rd = request.getRequestDispatcher("submit.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("TError", "Error al subir la imagen. Código de respuesta: " + responseCode);
                RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("TError", "Error al procesar la solicitud: " + e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}