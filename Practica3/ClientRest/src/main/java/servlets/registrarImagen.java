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
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

/**
 *
 * @author alumne
 */

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
@WebServlet(name = "registrarImagen", urlPatterns = {"/registrarImagen"})
public class registrarImagen extends HttpServlet {
    
    int cont = 0;

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
        Part filep = request.getPart("Subir Imagen");
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("user");
        String fileName = filep.getSubmittedFileName();
        
        
        //String uploadPath = UPLOAD_DIRECTORY + File.separator + fileName;
        String contentType = filep.getContentType();
        String extensio;
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/gif")) {
            
        }

        /*switch (contentType) {
            case "image/gif":
                extensio = "gif";
                break;
            case "image/png":
                extensio = "png";
                break;
            default:
                extensio = "jpeg";
                break;
        }*/


        try {
            
            /*String uploadPath = System.getProperty("java.io.tmpdir") + File.separator + cont + "." + extensio;
            System.out.println("Archivo guardado en: " + uploadPath);
            File file = new File(uploadPath);
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, file.toPath());
                System.out.println("Archivo " + fileName + " ha sido subido corectamente");
            } catch (Exception e) {
                request.setAttribute("TError", "image_error");
                RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
            }*/
            String apiUrl = "http://localhost:8080/ServerRest/resources/jakartaee9/register";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            
            connection.setDoOutput(true);

            /*String postData = "title=" + titulo + "&description=" + descripcion + "&keywords=" + keywords +
                              "&author=" + author + "&creator=" + user + "&capture=" + fechaCapt;

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = postData.getBytes("utf-8");
                os.write(input, 0, input.length);

            }*/
            
            final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
                StreamDataBodyPart filePart = new StreamDataBodyPart("file", filep.getInputStream());
                FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
                final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart
                        .field("title", titulo, MediaType.TEXT_PLAIN_TYPE)
                        .field("description", descripcion, MediaType.TEXT_PLAIN_TYPE)
                        .field("keywords", keywords, MediaType.TEXT_PLAIN_TYPE)
                        .field("author", author, MediaType.TEXT_PLAIN_TYPE)
                        .field("creator", user, MediaType.TEXT_PLAIN_TYPE)
                        .field("capture", fechaCapt, MediaType.TEXT_PLAIN_TYPE)
                        .field("filename",fileName, MediaType.TEXT_PLAIN_TYPE)
                        .bodyPart(filePart);

                final WebTarget target = client.target(apiUrl);
                final Response responseCode = target.request().post(Entity.entity(multipart, multipart.getMediaType()));
                
                formDataMultiPart.close();
                multipart.close();
            

            // Leer la respuesta del servidor
            if (responseCode.getStatus() == 201) {
                ++cont;
                request.setAttribute("message", "Se ha subido la imagen correctamente.");
                if (!response.isCommitted()) {
                    RequestDispatcher rd = request.getRequestDispatcher("submit.jsp");
                    rd.forward(request, response);
                }
            } else {
                request.setAttribute("TError", "image_error");
                if (!response.isCommitted()) {
                    RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
                    rd.forward(request, response);
                }
            }
            connection.disconnect();
        } catch (ServletException | IOException e) {
            request.setAttribute("TError", "image_error");
            if (!response.isCommitted()) {
                RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
            }
        } 
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}