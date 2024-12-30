package DB;


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
    
    public boolean login(String user, String password) {
        Connection connection = null;
        //response.setContentType("text/html;charset=UTF-8");
        
        boolean isAuth = false; 
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");

        // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr5;user=pr5;password=pr5");
            String sql = "SELECT * FROM USUARIOS WHERE ID_USUARIO = ? AND PASSWORD = ? " ;
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
    
    public boolean register(String user, String password, String email) {
        Connection connection = null;
        boolean isRegistered = false; 
        
        try {
             Class.forName("org.apache.derby.jdbc.ClientDriver");

            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr5;user=pr5;password=pr5");
            String sql = "INSERT INTO USUARIOS (ID_USUARIO, PASSWORD, EMAIL) VALUES (?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user);
            statement.setString(2, password);
            statement.setString(3,email);


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
    public boolean save(String markdown, String user) {
        Connection connection = null;
        boolean isSaved = false; 
        
        try {
             Class.forName("org.apache.derby.jdbc.ClientDriver");

            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr5;user=pr5;password=pr5");
            String sql = "INSERT INTO DOCUMENTOS (ID_USUARIO, CONTENT) VALUES (?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user);
            statement.setString(2, markdown);


            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                isSaved = true; 
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
        
        return isSaved;
    }
    
    public String load(String user) {
       Connection connection = null;
    String markdown = ""; // Aquí almacenaremos el contenido del markdown
    
        try {
            // Cargar el driver de la base de datos (JDBC)
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            // Establecer conexión con la base de datos
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr5;user=pr5;password=pr5");

            // SQL para obtener el campo CONTENT relacionado con el ID_USUARIO
            String sql = "SELECT CONTENT FROM DOCUMENTOS WHERE ID_USUARIO = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user); // Establecer el parámetro ID_USUARIO en la consulta

            // Ejecutar la consulta y obtener el resultado
            ResultSet rs = statement.executeQuery();

            // Si hay un resultado, obtener el contenido del campo CONTENT
            if (rs.next()) {
                markdown = rs.getString("CONTENT"); // Obtener el contenido de la columna CONTENT
            }
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
        
        return markdown;
    }
    
}
