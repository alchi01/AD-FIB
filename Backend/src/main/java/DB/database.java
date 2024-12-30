package DB;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONObject;

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
    public boolean save(String markdown, String username) {
         boolean isSaved = false; 

         try {
             // Crear el objeto JSON con el contenido
             JSONObject jsonObject = new JSONObject();
             jsonObject.put("username", username);
             jsonObject.put("content", markdown);

             // Definir el directorio temporal del backend
             String tempDir = System.getProperty("java.io.tmpdir");
             System.out.println(tempDir);
             File jsonFile = new File(tempDir, username + ".json");

             // Escribir el JSON en el archivo
             try (FileWriter writer = new FileWriter(jsonFile)) {
                 writer.write(jsonObject.toString(4)); // Indentación de 4 espacios para mejor legibilidad
             }

             // Confirmar que el archivo fue guardado exitosamente
             if (jsonFile.exists()) {
                 isSaved = true;
             }
         } catch (IOException e) {
             System.out.println("IOException: " + e.getMessage());
         }

         return isSaved;
     }
    
    public String load(String user) {
        String markdown = ""; // Variable para almacenar el contenido del campo "content"

        try {
            // Obtener el directorio temporal del sistema
            String tempDir = System.getProperty("java.io.tmpdir");
            File jsonFile = new File(tempDir, user + ".json");

            // Verificar si el archivo existe
            if (!jsonFile.exists()) {
                System.out.println("Archivo no encontrado: " + jsonFile.getAbsolutePath());
                return markdown; // Retornar vacío si el archivo no existe
            }

            // Leer el archivo JSON
            try (FileReader reader = new FileReader(jsonFile)) {
                StringBuilder jsonContent = new StringBuilder();
                int c;
                while ((c = reader.read()) != -1) {
                    jsonContent.append((char) c);
                }

                // Convertir el contenido del archivo en un objeto JSON
                JSONObject jsonObject = new JSONObject(jsonContent.toString());

                // Extraer el valor del campo "content"
                markdown = jsonObject.getString("content");
                System.out.println(jsonObject);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return markdown;
    }

}
