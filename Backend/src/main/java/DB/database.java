package Auxclasses.DB;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author alumne
 */
public class database {
    
    public database(){}
    
    public boolean login(String user, String password ) {
        Connection connection = null;
        //response.setContentType("text/html;charset=UTF-8");
        
        boolean isAuth = false; 
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");

        // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr5;user=pr5;password=pr5");
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
    
    public boolean register(String user, String password) {
        Connection connection = null;
        boolean isRegistered = false; 
        
        try {
             Class.forName("org.apache.derby.jdbc.ClientDriver");

            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr5;user=pr5;password=pr5");
            String sql = "INSERT INTO USUARIOS (ID_USUARIO, PASSWORD) VALUES (?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user);
            statement.setString(2, password);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                isRegistered = true; 
            }
        } catch (ClassNotFoundException e) {
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
        
        return isRegistered;
    }
}
