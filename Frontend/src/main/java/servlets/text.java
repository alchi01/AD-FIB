/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import jakarta.json.Json;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpSession;
import java.io.InputStream;
import java.net.URLEncoder;
import jakarta.servlet.RequestDispatcher;
import org.json.JSONObject;



/**
 *
 * @author alumne
 */
@WebServlet(name = "text", urlPatterns = {"/text"})
public class text extends HttpServlet {

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
            out.println("<title>Servlet text</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet text at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");
        String markdownContent = request.getParameter("markdownContent");
         HttpSession session = request.getSession(false);
        String user = (session != null) ? (String) session.getAttribute("user") : null;
        System.out.println(user);

        if ("guardar".equals(action)) {
            // Crear la cadena de parámetros en formato "application/x-www-form-urlencoded"
            String data = "markdown=" + URLEncoder.encode(markdownContent, "UTF-8")
                        + "&username=" + URLEncoder.encode(user, "UTF-8");

            // Nuevo endpoint para guardar el contenido
            String apiUrl = "http://localhost:8080/Backend/resources/jakartaee9/saveContent";

            // Conexión al endpoint
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                // Convertir la cadena a bytes y escribir en el cuerpo de la solicitud
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response.setContentType("text/plain");
                response.getWriter().write("Contenido guardado exitosamente.");
            } else {
                response.setStatus(responseCode);
                response.getWriter().write("Error al guardar el contenido.");
            }

            connection.disconnect();
            return;
        }
        else if ("cargar".equals(action)) {
            // Lógica para cargar el contenido desde el backend
            String apiUrl = "http://localhost:8080/Backend/resources/jakartaee9/loadContent"; // Endpoint de carga
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // Enviar los parámetros de usuario en el cuerpo de la solicitud, si es necesario
            String data = "username=" + URLEncoder.encode(user, "UTF-8");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(data.getBytes("UTF-8"));
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                StringBuilder content = new StringBuilder();
                int byteRead;
                while ((byteRead = inputStream.read()) != -1) {
                    content.append((char) byteRead);
                }
                        
               // Convertir el contenido del archivo en un objeto JSON
                JSONObject jsonObject = new JSONObject(content.toString());

                // Extraer el valor del campo "content"
                String markdown = jsonObject.getString("content");
                System.out.println(jsonObject);
                // Colocar el contenido en la solicitud como atributo
                request.setAttribute("markdownContent", markdown.toString());

                // Redirigir a la misma página (por ejemplo, text.jsp) con el contenido cargado
                RequestDispatcher dispatcher = request.getRequestDispatcher("text.jsp");
                dispatcher.forward(request, response);
            } else {
                response.getWriter().write("Error al cargar el contenido.");
            }

            connection.disconnect();
            return;
        }

        
        String format = request.getParameter("exportFormat");
        
        System.out.println();
        
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("markdownContent", markdownContent);
        JsonObject json = builder.build();

        
        String apiUrl = "http://localhost:8080/Backend/resources/jakartaee9/";
        
        if (format.equals("pdf")) apiUrl += "downloadpdf";
        else if (format.equals("html")) apiUrl += "downloadhtml";
        else if (format.equals("docx")) apiUrl += "downloaddocx";
        
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        
        String jsonPayload = json.toString();
        
        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonPayload.getBytes());
            os.flush();
        }
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            if (format.equals("pdf")) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=documento.pdf");
            }
            else if (format.equals("html")) {
                response.setContentType("text/html");
                response.setHeader("Content-Disposition", "attachment; filename=documento.html");
            }
            else if (format.equals("docx")) {
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                response.setHeader("Content-Disposition", "attachment; filename=documento.docx");
            }
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        else System.out.println("MAAAAL");
        connection.disconnect();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
