/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import jakarta.json.Json;
import jakarta.json.JsonObject;
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
@WebServlet(name = "modificarImagen", urlPatterns = {"/modificarImagen"})
public class modificarImagen extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String user = (session != null) ? (String) session.getAttribute("user") : null;

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
        String titulo = request.getParameter("newTit");
        String descripcion = request.getParameter("newDesc");
        String keywords = request.getParameter("newKey");
        String autor = request.getParameter("autor");
        String imagenIdStr = request.getParameter("imagenId");


        try {
            String urlString = "http://localhost:8080/RestAD/resources/jakartaee9/modify"; 
            HttpURLConnection connection = null;
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST"); 
            connection.setDoOutput(true);
            
            String Data = "id=" + imagenIdStr + "&title=" + titulo + "&description=" + descripcion + "&keywords=" + keywords + "&author=" + autor + "&creator " + "&capt_date= ";  ;
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = Data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                request.setAttribute("message", "Se ha modificado la imagen correctamente.");
                RequestDispatcher rd = request.getRequestDispatcher("submit.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("TError", "image_error");
                RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
            }
            connection.disconnect();
        } catch (Exception e) {
            request.setAttribute("TError", "image_error");
            RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
        } 
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}