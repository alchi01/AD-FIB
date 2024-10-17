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
import java.util.ArrayList;
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
    public ArrayList<Object[]> show_images(){
        ArrayList<Object[]> listaImagenes = new ArrayList<>();
        
         Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            connection.setAutoCommit(false); // Deshabilitar auto-commit
            String sql = "SELECT * FROM IMAGE";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Object[] filaImagen = new Object[9];
                filaImagen[0] = resultSet.getInt("ID");
                filaImagen[1] = resultSet.getString("TITLE");
                filaImagen[2] = resultSet.getString("DESCRIPTION");
                filaImagen[3] = resultSet.getString("KEYWORDS");
                filaImagen[4] = resultSet.getString("AUTHOR");
                filaImagen[5] = resultSet.getString("CREATOR");
                filaImagen[6] = resultSet.getString("CAPTURE_DATE");
                filaImagen[7] = resultSet.getString("STORAGE_DATE");
                filaImagen[8] = resultSet.getString("FILENAME");

                // Añadir la fila al ArrayList
                listaImagenes.add(filaImagen);
            }
            connection.commit(); // Commit the transaction
        } catch (ClassNotFoundException | SQLException e) {
        System.out.println("Error: " + e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    return listaImagenes;
    }
    
    
    public boolean eliminate(int ID) {
        Connection connection = null;
        //response.setContentType("text/html;charset=UTF-8");
        
        boolean correct_elim = false; 
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");

        // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            String sql = "DELETE FROM IMAGE WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Asignar el valor de ID en la consulta
            statement.setInt(1, ID);
            int rowsAffected = statement.executeUpdate();

            correct_elim = (rowsAffected > 0); // Si al menos una fila fue afectada, la eliminación fue correcta
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
        
        return correct_elim;
    }
      public boolean image_modify(String titulo, String descripcion, String keywords, String imagen, int id) {
        boolean okMod = false;
        
        Connection connection = null;
        //response.setContentType("text/html;charset=UTF-8");
        
        try {
            int cont = 1;
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            String sql = "UPDATE IMAGE SET ";
            if (titulo != null)  sql += "title = ?, ";
            if (descripcion != null) sql += "description = ?, ";
            if (keywords != null) sql += "keywords = ?, ";
            if (imagen != null) sql += "filename = ? ";
            sql += "WHERE id = ?";
            System.out.println(sql);
            PreparedStatement statement = connection.prepareStatement(sql);
            if (titulo != null) {
                statement.setString(cont, titulo);
                ++cont;
            }
            if (descripcion != null) {
                statement.setString(cont, descripcion);
                ++cont;
            }
             if (keywords != null) {
                 statement.setString(cont, keywords);
                 ++cont;
             }
            if (imagen != null) {
                statement.setString(cont, imagen);
                ++cont;
            }
            statement.setInt(cont, id);
            okMod = true;
            statement.executeUpdate();
            connection.commit();
            
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
        return okMod;
    }
}
