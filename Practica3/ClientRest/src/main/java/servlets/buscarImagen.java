/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
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
@WebServlet(name = "buscarImagen", urlPatterns = {"/buscarImagen"})
public class buscarImagen extends HttpServlet {

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
            HttpSession session = request.getSession(false);
            String user = (String) session.getAttribute("user");
            if (user == null) response.sendRedirect("login.jsp");
            else response.sendRedirect("menu.jsp");
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

        // Comprobación de la sesión del usuario
        HttpSession session = request.getSession(false);
        String user = (String) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String searchUrl = "";
        // Obtener los parámetros de búsqueda
        String buscarTitulo = request.getParameter("buscarTitulo");
        String buscarDescripcion = request.getParameter("buscarDescripcion");
        String buscarId = request.getParameter("buscarId");
        String buscarAuthor = request.getParameter("buscarAutor");
        String buscarDate = request.getParameter("buscarDate");
        String buscarKeywords = request.getParameter("buscarKeywords");

        // Validación de los parámetros de búsqueda
        if (buscarTitulo == null || buscarDescripcion == null 
            || buscarId == null || buscarAuthor == null 
            || buscarDate == null || buscarKeywords == null) {
            response.sendRedirect("menu.jsp");
            return;
        }

        List<JsonObject> listaImagenes = new ArrayList<>();
        HttpURLConnection connection = null;
        if (buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
            && buscarId.trim().isEmpty() && buscarAuthor.trim().isEmpty()
            && buscarDate.trim().isEmpty() && buscarKeywords.trim().isEmpty())
        {
            System.out.println("1");
            searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/showImages";
        }
        else if (!buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
            && buscarId.trim().isEmpty() && buscarAuthor.trim().isEmpty()
            && buscarDate.trim().isEmpty() && buscarKeywords.trim().isEmpty())
        {
            System.out.println("2");
            searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/searchTitle"+ buscarTitulo;

        }
        else if (buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
            && !buscarId.trim().isEmpty() && buscarAuthor.trim().isEmpty()
            && buscarDate.trim().isEmpty() && buscarKeywords.trim().isEmpty())
        {
            System.out.println("3");
            searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/searchId"+ buscarId;
        }
        else if (buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
            && buscarId.trim().isEmpty() && !buscarAuthor.trim().isEmpty()
            && buscarDate.trim().isEmpty() && buscarKeywords.trim().isEmpty())
        {
            System.out.println("4");            
            searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/searchAuthor"+ buscarAuthor;
 
        }
        else if (buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
            && buscarId.trim().isEmpty() && buscarAuthor.trim().isEmpty()
            && !buscarDate.trim().isEmpty() && buscarKeywords.trim().isEmpty())
        {
            System.out.println("5");
            searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/searchDate"+ buscarDate;
        }
        else if (buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
            && buscarId.trim().isEmpty() && buscarAuthor.trim().isEmpty()
            && buscarDate.trim().isEmpty() && !buscarKeywords.trim().isEmpty())
        {
            System.out.println("6");            
            searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/searchTitle"+ buscarKeywords;
        }
        else{
            System.out.println("7");           
            searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/searchCombined"+ buscarTitulo + buscarId + buscarDescripcion +buscarAuthor + buscarDate + buscarKeywords;

        }
                                

        try {
            // Construcción de la URL para la búsqueda en el servidor remoto
            

            URL url = new URL(searchUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("8");           

                // Procesamiento de la respuesta JSON del servidor
                try (JsonReader jsonReader = Json.createReader(new InputStreamReader(connection.getInputStream()))) {
                    JsonArray jsonArray = jsonReader.readArray();
                    System.out.println("9");           

                    // Iteración de cada objeto JSON y almacenamiento en la lista de imágenes
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonImage = jsonArray.getJsonObject(i);
                        
                        // Solo añadir las imágenes del usuario actual
                        if (jsonImage.getString("creator").equals(user)) {
                            System.out.println("11");  
                            listaImagenes.add(jsonImage);
                        }
                    }
                }
            } else {
                // Error de conexión, redirigir a una página de error
                request.setAttribute("TError", "image_error");
                RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
                rd.forward(request, response);
                return;
            }
            connection.disconnect();
        } catch (Exception e) {
            // Error en la conexión HTTP o en el procesamiento de JSON
            System.out.println("10");           
            request.setAttribute("TError", "image_error");
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
            rd.forward(request, response);
            return;
        } 
        

        // Almacenar los resultados de búsqueda en el request y reenviar a buscarImagen.jsp
        request.setAttribute("imagenesFiltradas", listaImagenes);
        request.getRequestDispatcher("buscarImagen.jsp").forward(request, response);
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
        processRequest(request, response);
            
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
