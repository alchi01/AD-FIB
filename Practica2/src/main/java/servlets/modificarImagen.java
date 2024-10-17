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
import java.io.File;
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
        String titulo = request.getParameter("newTit");
        String descripcion = request.getParameter("newDesc");
        String keywords = request.getParameter("newKey");
        String imagen = request.getParameter("newImg");
        String imagenIdStr = request.getParameter("imagenId");
        int imagenId = Integer.parseInt(imagenIdStr);

        database db = new database();
        
        
        /*HttpSession session = request.getSession();
        int id = (int) session.getAttribute("id");*/
        // Procesar los valores obtenidos
        boolean okMod  = db.image_modify(titulo, descripcion, keywords, imagen, imagenId);
        
        
        if (okMod && !imagen.isEmpty()) {
            String imagenAnt = db.consulta_imagen(imagenId);
            String oldPath = getServletContext().getRealPath("") + File.separator + imagenAnt;
            String newPath = getServletContext().getRealPath("") + File.separator + imagen;
            File file = new File(oldPath);
            File newfile = new File(newPath);
            try { file.renameTo(newfile);}
            catch (Exception e) {
                okMod = false;
            }
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
