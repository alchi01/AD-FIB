/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author alumne
 */
@WebServlet(name = "menu", urlPatterns = {"/menu"})
public class menu extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String user = (session != null) ? (String) session.getAttribute("user") : null;

        if (user == null) {
            // Si no hay usuario en la sesión, redirigir al login
            response.sendRedirect("login.jsp");
        } else {
            // Si hay un usuario, redirigir al menú
            RequestDispatcher rd = request.getRequestDispatcher("menu.jsp");
            rd.forward(request, response);
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
        String destino = "";

        if (request.getParameter("regImagen") != null) {
            destino = "registrarImagen.jsp";
        } else if (request.getParameter("busImagen") != null) {
            destino = "buscarImagen.jsp"; // Asegúrate de que el destino tenga la extensión correcta
        } else if (request.getParameter("CerrSesion") != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // Invalida la sesión si existe
            }
            destino = "login.jsp";
        }

        // Redirigir a la página de destino
        if (!destino.isEmpty()) {
            response.sendRedirect(destino);
        } else {
            // Si no hay destino, redirigir de nuevo al menú
            response.sendRedirect("menu.jsp");
        }
    }
}