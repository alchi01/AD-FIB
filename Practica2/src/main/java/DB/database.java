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
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
           }
        }
        
        return isAuth;
    }
    
    public boolean image_upload(String titulo,String description,String keywords, String author,String user, String fechaCapt,String fechaGuard,String fileName) {
        boolean okImage = false;
        
        Connection connection = null;
        //response.setContentType("text/html;charset=UTF-8");
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            connection.setAutoCommit(false); // Set auto-commit to false
            String sql = "INSERT INTO IMAGE (title, description, keywords ,author, creator, capture_date, storage_date, filename) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,titulo);
            statement.setString(2,description);
            statement.setString(3,keywords);
            statement.setString(4,author);
            statement.setString(5,user);
            statement.setString(6,fechaCapt);
            statement.setString(7,fechaGuard);
            statement.setString(8,fileName);
            okImage = true;
            statement.executeUpdate();
            connection.commit(); // Commit the transaction
        
            
         }
        catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e.getMessage());
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
           }
        }
        return okImage;
    }
}
