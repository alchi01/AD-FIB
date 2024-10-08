/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpSession;
import java.nio.file.Files;
import DB.database;

/**
 *
 * @author alumne
 */

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB

@WebServlet(name = "registrarImagen", urlPatterns = {"/registrarImagen"})
public class registrarImagen extends HttpServlet {

 
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
            out.println("<title>Servlet registrarImagen</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet registrarImagen at " + request.getContextPath() + "</h1>");
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
        boolean error = true;
        String titulo = request.getParameter("Titulo");
        String description = request.getParameter("Descripcion");
        String keywords = request.getParameter("Keywords");
        String author = request.getParameter("Autor");
        String fechaCapt = request.getParameter("Fecha de creacion");
        Part filePart= request.getPart("Subir Imagen");
        String fileName = filePart.getSubmittedFileName();
        
        LocalDate fecha = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaGuard = fecha.format(format);
        
        //String UPLOAD_DIRECTORY = "C:/uploads";
        String uploadPath = getServletContext().getRealPath("") + File.separator + fileName;
        //String uploadPath = UPLOAD_DIRECTORY + File.separator + fileName;
        System.out.println("Archivo guardado en: " + uploadPath);
        File file = new File(uploadPath);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath());
            System.out.println("Archivo " + fileName + " ha sido subido corectamente");
            error = false;
        }
        database db  = new database();
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("user");
        
        boolean okImage = db.image_upload(titulo, description, keywords, author, user,fechaCapt,fechaGuard,fileName); 
        
        if (!error) {
            if (okImage) {
                PrintWriter out = response.getWriter();
                out.println("<h1>Se ha registrado la imagen correctamente</h1>");
                out.println("<script>");
                out.println("  setTimeout(function() {");
                out.println("    window.location.href = '" + request.getContextPath() + "/menu.jsp';");
                out.println("  }, 2000);"); 
                out.println("</script>");
                out.close();
            } else {
                request.setAttribute("TError", "image_error");
                RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
            }
        }
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
