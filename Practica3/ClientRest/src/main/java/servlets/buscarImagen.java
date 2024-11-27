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
import java.net.URLEncoder;
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
import java.io.File;
import java.io.FileOutputStream;

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
        String searchUrl;
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
        //BUsqueda por defecto, muestra todas las imagenes
        if (buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
            && buscarId.trim().isEmpty() && buscarAuthor.trim().isEmpty()
            && buscarDate.trim().isEmpty() && buscarKeywords.trim().isEmpty())
        {
            
            searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/showImages";
        }
        else{
            //Busqueda por titulo
            if (!buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
                && buscarId.trim().isEmpty() && buscarAuthor.trim().isEmpty()
                && buscarDate.trim().isEmpty() && buscarKeywords.trim().isEmpty())
            {
                
                searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/searchTitle/"+ buscarTitulo;
            }
            //Busqueda por ID
            else if (buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
                && !buscarId.trim().isEmpty() && buscarAuthor.trim().isEmpty()
                && buscarDate.trim().isEmpty() && buscarKeywords.trim().isEmpty())
            {
                
                int id = Integer.parseInt(buscarId);
                searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/searchID/" + id;
            }
            //Busqueda por autor
            else if (buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
                && buscarId.trim().isEmpty() && !buscarAuthor.trim().isEmpty()
                && buscarDate.trim().isEmpty() && buscarKeywords.trim().isEmpty())
            {
                        
                searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/searchAuthor/"+ buscarAuthor;

            }
            //Busqueda por Date
            else if (buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
                && buscarId.trim().isEmpty() && buscarAuthor.trim().isEmpty()
                && !buscarDate.trim().isEmpty() && buscarKeywords.trim().isEmpty())
            {
                
                searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/searchCreationDate/"+ buscarDate;
            }
            //Busqueda por Keywords
            else if (buscarTitulo.trim().isEmpty() && buscarDescripcion.trim().isEmpty() 
                && buscarId.trim().isEmpty() && buscarAuthor.trim().isEmpty()
                && buscarDate.trim().isEmpty() && !buscarKeywords.trim().isEmpty())
            {
                        
                searchUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/searchTitle/"+ buscarKeywords;
            }
            //Busqueda por combinacion
            else{
                          
                StringBuilder searchUrlComb = new StringBuilder("http://localhost:8080/ServerRest/resources/jakartaee9/searchCombined?");

                
                if (!buscarTitulo.trim().isEmpty()) {
                    searchUrlComb.append("title=").append(URLEncoder.encode(buscarTitulo, "UTF-8")).append("&");
                }
                if (!buscarDescripcion.trim().isEmpty()) {
                    searchUrlComb.append("description=").append(URLEncoder.encode(buscarDescripcion, "UTF-8")).append("&");
                }
                if (!buscarAuthor.trim().isEmpty()) {
                    searchUrlComb.append("author=").append(URLEncoder.encode(buscarAuthor, "UTF-8")).append("&");
                }
                if (!buscarDate.trim().isEmpty()) {
                    searchUrlComb.append("date=").append(URLEncoder.encode(buscarDate, "UTF-8")).append("&");
                }
                if (!buscarKeywords.trim().isEmpty()) {
                    searchUrlComb.append("keywords=").append(URLEncoder.encode(buscarKeywords, "UTF-8")).append("&");
                }

                if (searchUrlComb.charAt(searchUrlComb.length() - 1) == '&') {
                    searchUrlComb.deleteCharAt(searchUrlComb.length() - 1);
                }
                searchUrl = searchUrlComb.toString();

            }
        }
                                

        try {
            // Construcción de la URL para la búsqueda en el servidor remoto
     
            URL url = new URL(searchUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {                                         
                // Procesamiento de la respuesta JSON del servidor
                try (JsonReader jsonReader = Json.createReader(new InputStreamReader(connection.getInputStream()))) {
                    JsonArray jsonArray = jsonReader.readArray();
                              

                    // Iteración de cada objeto JSON y almacenamiento en la lista de imágenes
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonImage = jsonArray.getJsonObject(i);
                          System.out.println("ENTRO");
                        // Solo añadir las imágenes del usuario actual
                        if (jsonImage.getString("creator").equals(user)) {
                            String filename = jsonImage.getString("filename");
                           
                            File tempImageFile = descargarImagenDesdeRest(filename);

                            // Si necesitas usar una URL accesible desde el cliente (por ejemplo, en el navegador)
                            if (tempImageFile != null) {
                                 System.out.println("ENTRO2");
                  
                                listaImagenes.add(jsonImage);
                            }
                        }
                    }
                }
            }else {
                System.out.println("ENTRO1");
                // Error de conexión, redirigir a una página de error
                request.setAttribute("TError", "image_error");
                RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
                rd.forward(request, response);
                return;
            }
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("ENTRO2");
            // Error en la conexión HTTP o en el procesamiento de JSON          
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
    
    private File descargarImagenDesdeRest(String filename) throws IOException {
        
        String fileUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/getImage/" + filename;

        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        System.out.println("ENTRO1");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            
            System.out.println("ENTRO3");

            // Obtener la ruta del directorio tmp en el servidor web
            String tmpDirPath = getServletContext().getRealPath("");

            // Crear la carpeta tmp si no existe
            File tmpDir = new File(tmpDirPath);
            if (!tmpDir.exists()) {
                tmpDir.mkdirs();  // Si no existe, la creamos
            }

            // Crear el archivo en el directorio tmp con el nombre original
            File tempFile = new File(tmpDir, filename);

            // Descargar el archivo de la URL y guardarlo en el archivo local
            try (var inputStream = connection.getInputStream();
                 var outputStream = new FileOutputStream(tempFile)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            System.out.println(tempFile.getAbsolutePath());
            // Retornamos el archivo descargado
            return tempFile;
        }

        // Si la conexión no fue exitosa, desconectamos y retornamos null
        connection.disconnect();
        return null;
    }

}
