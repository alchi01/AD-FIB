/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "login", urlPatterns = {"/login"})
public class login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet login</title>");            
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Servlet login at " + request.getContextPath() + "</h1>");
                out.println("</body>");
                out.println("</html>");
            }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("User ");
        String password = request.getParameter("Password");

        // Validación de entrada
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("TError", "Por favor, ingrese su nombre de usuario y contraseña.");
            RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
            rd.forward(request, response);
            return;
        }

        // Construcción de la URL para la autenticación en el servidor remoto
        String apiUrl = "http://localhost:8080/RestAD/resources/jakartaee9/login?username=" + username + "&password=" + password;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");

            // Comprobar el código de respuesta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Procesar la respuesta JSON del servidor
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder responseString = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        responseString.append(inputLine);
                    }

                    JsonObject jsonResponse = Json.createReader(new InputStreamReader(connection.getInputStream())).readObject();
                    boolean auth = jsonResponse.getBoolean("authenticated", false);

                    if (auth) {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", username);
                        response.sendRedirect("menu.jsp");
                    } else {
                        request.setAttribute("TError", "Credenciales incorrectas. Intente de nuevo.");
                        RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
                        rd.forward(request, response);
                    }
                }
            } else {
                request.setAttribute("TError", "Error en la autenticación del servidor. Código de respuesta: " + responseCode);
                RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
                rd.forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("TError", "Error en la conexión: " + e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
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