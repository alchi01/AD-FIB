/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DB;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 *
 * @author alumne
 */
public class database {
    
    public database(){}
    
    /*public void connection() {
        Connection connection = null;
        //response.setContentType("text/html;charset=UTF-8");
        
        String query;
        PreparedStatement statement;
            
        Class.forName("org.apache.derby.jdbc.ClientDriver");

        // create a database connection
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
    }*/
    
    public boolean login(String user, String password ) {
        Connection connection = null;
        //response.setContentType("text/html;charset=UTF-8");
        
        String query;
        boolean isAuth = false; 
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");

        // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            String sql = "SELECT * FROM USUARIOS WHERE ID_USUARIO = ? AND PASSWORD = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,user);
            statement.setString(2,password);
            
            ResultSet rs = statement.executeQuery();
            if (rs.next()) isAuth = true;
        }
        catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e.getMessage());
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }            
        
        return isAuth;
    }
}
