/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;
import DB.database;
/**
 *
 * @author alumne
 */
@WebServlet(name = "modificarImagen", urlPatterns = {"/modificarImagen"})
public class modificarImagen extends HttpServlet {

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
            out.println("<title>Servlet modificarImagen</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet modificarImagen at " + request.getContextPath() + "</h1>");
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
        String titulo = request.getParameter("newTit");
                 if (titulo == null) {
        System.out.println("El parámetro newTit no se está enviando correctamente");
    } else {
        System.out.println("El parámetro newTit se está enviando correctamente: " + titulo);
    }
        String descripcion = request.getParameter("newDesc");
        String keywords = request.getParameter("newKey");
        String imagen = request.getParameter("newImg");
        database db = new database();
        
        
        /*HttpSession session = request.getSession();
        int id = (int) session.getAttribute("id");*/
        // Procesar los valores obtenidos
        boolean okMod  = db.image_modify(titulo, descripcion, keywords, imagen, 1);
        if ((titulo != null && titulo.trim().isEmpty()) || (descripcion != null && descripcion.trim().isEmpty()) || (keywords != null && keywords.trim().isEmpty()) || (imagen != null && imagen.trim().isEmpty())) {
            okMod = false;
        } 
        
        if (okMod) { 
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head></head>");
        out.println("<body>");
        out.println("<h1>Se ha modificado la imagen correctamente</h1>");
        out.println("<form action='menu.jsp' method='get'>");
        out.println("<input type='submit' value='Volver al menú'>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
        } else {
            request.setAttribute("TError", "image_error");
            RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
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
