<%-- 
    Document   : login
    Created on : 20 sept 2024, 12:43:34
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>LOGIN</title>
        <link rel="stylesheet" href="css/general.css">
        <link rel="stylesheet" href="css/login.css">
    </head>
    <body>
        <h1>LOGIN:<h1>
    </body>
    <form action = "login" method = "POST">
        <input type="text" name="User">
        <input type="password" name="Password">
        <input type="submit" value="Log in">
    </form>
</html>
