/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import DB.database;
import jakarta.servlet.RequestDispatcher;
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
        database db  = new database();
 
        String imagenIdStr = request.getParameter("imagenId");
       
        
        int imagenId = Integer.parseInt(imagenIdStr);
        
        boolean elim = db.eliminate(imagenId);
        
        if (elim){
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head></head>");
            out.println("<body>");
            out.println("<h1>Se ha eliminado la imagen correctamente</h1>");
            out.println("<form action='menu.jsp' method='get'>");
            out.println("<input type='submit' value='Volver al menú'>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
            out.close();  
        }
        else {
            request.setAttribute("TError", "image_error");
            RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
        }
               
    }
    
}
