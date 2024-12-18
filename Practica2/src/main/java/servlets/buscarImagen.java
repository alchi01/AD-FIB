/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
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
import java.util.ArrayList;


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
        System.out.println("Entrando al servlet BuscarImagen");

        // Obtener los parámetros del formulario
        String buscarTitulo = request.getParameter("buscarTitulo");
        String buscarDescripcion = request.getParameter("buscarDescripcion");

        // Obtener el usuario de la sesión
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("user");

        // Instancia de la base de datos
        database db = new database();

        // Obtener las imágenes filtradas usando la nueva función
        ArrayList<Object[]> listaImagenes = db.show_images(buscarTitulo, buscarDescripcion);

        // Filtrar solo las imágenes del usuario actual
        ArrayList<Object[]> imagenesFiltradas = new ArrayList<>();
        for (Object[] filaImagen : listaImagenes) {
            String usuarioImagen = (String) filaImagen[5];  // El usuario está en la posición 5 del array
            if (usuarioImagen.equals(user)) {
                imagenesFiltradas.add(filaImagen);
            }
        }

        // Pasar las imágenes filtradas al JSP
        request.setAttribute("imagenesFiltradas", imagenesFiltradas);

        // Redirigir a buscarImagen.jsp para mostrar los resultados
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
