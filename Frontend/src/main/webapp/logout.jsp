<%-- 
    Document   : logout
    Created on : 30 dic 2024, 21:08:26
    Author     : alumne
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    HttpSession usersession = request.getSession(false);
    usersession.removeAttribute("user"); 
    response.sendRedirect("text.jsp");
%>

