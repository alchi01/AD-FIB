/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
@WebServlet(name = "eliminarImagen", urlPatterns = {"/eliminarImagen"})
public class eliminarImagen extends HttpServlet {

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
        String imagenIdStr = request.getParameter("imagenId");
        String message;

        int imagenId = Integer.parseInt(imagenIdStr);

        // Construcción de la URL para la eliminación en el servidor remoto
        String apiUrl = "http://localhost:8080/RestAD/resources/jakartaee9/eliminarImagen?id=" + imagenId;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Procesar la respuesta JSON si es necesario
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder responseString = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        responseString.append(inputLine);
                    }
                    // Aquí puedes procesar la respuesta JSON si es necesario
                    JsonObject jsonResponse = Json.createReader(new InputStreamReader(connection.getInputStream())).readObject();
                    // Puedes manejar la respuesta JSON según sea necesario
                    message = jsonResponse.getString("message", "Se ha eliminado la imagen correctamente");
                }
            } else {
                message = "Error al eliminar la imagen en el servidor, código de respuesta: " + responseCode;
            }
        } catch (Exception e) {
            message = "Error en la conexión: " + e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        request.setAttribute("message", message);
        RequestDispatcher rd = request.getRequestDispatcher("submit.jsp");
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}