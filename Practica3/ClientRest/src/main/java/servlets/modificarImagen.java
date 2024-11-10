/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author alumne
 */
@WebServlet(name = "modificarImagen", urlPatterns = {"/modificarImagen"})
public class modificarImagen extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String user = (session != null) ? (String) session.getAttribute("user") : null;

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
        String titulo = request.getParameter("newTit");
        String descripcion = request.getParameter("newDesc");
        String keywords = request.getParameter("newKey");
        String imagen = request.getParameter("newImg");
        String imagenIdStr = request.getParameter("imagenId");

        // Validar que los parámetros requeridos no sean nulos o vacíos
        if (titulo == null || descripcion == null || keywords == null || imagen == null || imagenIdStr == null) {
            request.setAttribute("TError", "Todos los campos son obligatorios.");
            RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }

        int imagenId;
        try {
            imagenId = Integer.parseInt(imagenIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("TError", "ID de imagen inválido.");
            RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
            return;
        }

        // Preparar la solicitud al servidor REST
        String urlString = "http://localhost:8080/RestAD/resources/jakartaee9/images/" + imagenId; // Cambia esto a la URL correcta
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT"); // Usar PUT para modificar
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Crear el cuerpo de la solicitud en formato JSON
            JsonObject jsonInput = Json.createObjectBuilder()
                    .add("title", titulo)
                    .add("description", descripcion)
                    .add("keywords", keywords)
                    .add("imagen", imagen)
                    .build();

            // Enviar la solicitud
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leer la respuesta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                request.setAttribute("message", "Se ha modificado la imagen correctamente.");
                RequestDispatcher rd = request.getRequestDispatcher("submit.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("TError", "Error al modificar la imagen. Código de respuesta: " + responseCode);
                RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
            }
        } catch (Exception e) {
            // Manejo de excepciones
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