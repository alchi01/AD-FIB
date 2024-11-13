/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DB;

import java.io.IOException;
import java.util.List;
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
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
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
    
    public boolean creator_connected(int id, String user){
        boolean isUser = false;
        String creator = null;
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
                
            String sql = "SELECT * FROM IMAGE WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            
            ResultSet resultSet = statement.executeQuery();
             if (resultSet.next()) {
                 creator = resultSet.getString("CREATOR");
             }
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
        isUser = user.equals(creator);
        return isUser;

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
    public JsonArray show_images_combined(String title, String description, String author, String date, String keywords){
        
        JsonArrayBuilder listaImagenes = Json.createArrayBuilder();
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");

            String sql = "SELECT * FROM IMAGE WHERE 1=1";

            if (title != null && !title.isEmpty()) {
                sql += " AND TITLE LIKE ?";
            }
            if (description != null && !description.isEmpty()) {
                sql += " AND DESCRIPTION LIKE ?";
            }
            if (author != null && !author.isEmpty()) {
                sql += " AND AUTHOR LIKE ?";
            }
            
            if (date != null && !date.isEmpty()) {
                sql += " AND CAPTURE_DATE LIKE ?";
            }
            if (keywords != null && !keywords.isEmpty()) {
                sql += " AND KEYWORDS LIKE ?";
            }


            PreparedStatement statement = connection.prepareStatement(sql);
            int paramIndex = 1;

            if (title != null && !title.isEmpty()) {
                statement.setString(paramIndex++, "%" + title + "%");
            }
            
            if (description != null && !description.isEmpty()) {
                statement.setString(paramIndex++, "%" + description + "%");
            }
            if (author != null && !author.isEmpty()) {
                statement.setString(paramIndex++, "%" + author + "%");
            }
            
            if (date != null && !date.isEmpty()) {
                statement.setString(paramIndex++, "%" + date + "%");
            }
            if (keywords != null && !keywords.isEmpty()) {
                statement.setString(paramIndex++, "%" + keywords + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                JsonObjectBuilder listaObjectImagenes = Json.createObjectBuilder()
                .add("id", resultSet.getString("ID"))
                .add("title", resultSet.getString("TITLE"))
                .add("description", resultSet.getString("DESCRIPTION"))
                .add("keywords", resultSet.getString("KEYWORDS"))
                .add("author", resultSet.getString("AUTHOR"))
                .add("creator", resultSet.getString("CREATOR"))
                .add("captureDate", resultSet.getString("CAPTURE_DATE"))
                .add("storageDate", resultSet.getString("STORAGE_DATE"))
                .add("filename", resultSet.getString("FILENAME"));
                listaImagenes.add(listaObjectImagenes.build());
            }

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

        return listaImagenes.build();
    }

    public JsonArray show_images_by_title(String title){
        JsonArrayBuilder listaImagenes = Json.createArrayBuilder();
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");

            String sql = "SELECT * FROM IMAGE WHERE 1=1";

            if (title != null && !title.isEmpty()) {
                sql += " AND TITLE LIKE ?";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            int paramIndex = 1;

            if (title != null && !title.isEmpty()) {
                statement.setString(paramIndex++, "%" + title + "%");
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                 JsonObjectBuilder listaObjectImagenes = Json.createObjectBuilder()
                .add("id", resultSet.getString("ID"))
                .add("title", resultSet.getString("TITLE"))
                .add("description", resultSet.getString("DESCRIPTION"))
                .add("keywords", resultSet.getString("KEYWORDS"))
                .add("author", resultSet.getString("AUTHOR"))
                .add("creator", resultSet.getString("CREATOR"))
                .add("captureDate", resultSet.getString("CAPTURE_DATE"))
                .add("storageDate", resultSet.getString("STORAGE_DATE"))
                .add("filename", resultSet.getString("FILENAME"));
                listaImagenes.add(listaObjectImagenes.build());

            }

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

        return listaImagenes.build();
    }
    
    public JsonObject show_images_by_id(int id){
        JsonObjectBuilder listaObjectImagenes = Json.createObjectBuilder();
        Connection connection = null;
      
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");

            String sql = "SELECT * FROM IMAGE WHERE 1=1";

            
            PreparedStatement statement = connection.prepareStatement(sql);

            
            statement.setInt(1, id);
            
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                listaObjectImagenes
                .add("id", id)
                .add("title", resultSet.getString("TITLE"))
                .add("description", resultSet.getString("DESCRIPTION"))
                .add("keywords", resultSet.getString("KEYWORDS"))
                .add("author", resultSet.getString("AUTHOR"))
                .add("creator", resultSet.getString("CREATOR"))
                .add("captureDate", resultSet.getString("CAPTURE_DATE"))
                .add("storageDate", resultSet.getString("STORAGE_DATE"))
                .add("filename", resultSet.getString("FILENAME"));

               
            }

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

        return listaObjectImagenes.build();
    }
    
    public JsonArray show_images_by_author(String author){
                JsonArrayBuilder listaImagenes = Json.createArrayBuilder();
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");

            String sql = "SELECT * FROM IMAGE WHERE 1=1";

            if (author != null && !author.isEmpty()) {
                sql += " AND AUTHOR LIKE ?";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            int paramIndex = 1;

            if (author != null && !author.isEmpty()) {
                statement.setString(paramIndex++, "%" + author + "%");
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                 JsonObjectBuilder listaObjectImagenes = Json.createObjectBuilder()
                .add("id", resultSet.getString("ID"))
                .add("title", resultSet.getString("TITLE"))
                .add("description", resultSet.getString("DESCRIPTION"))
                .add("keywords", resultSet.getString("KEYWORDS"))
                .add("author", resultSet.getString("AUTHOR"))
                .add("creator", resultSet.getString("CREATOR"))
                .add("captureDate", resultSet.getString("CAPTURE_DATE"))
                .add("storageDate", resultSet.getString("STORAGE_DATE"))
                .add("filename", resultSet.getString("FILENAME"));
                listaImagenes.add(listaObjectImagenes.build());

            }

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

        return listaImagenes.build();
    }
    
    public JsonArray show_images_by_date(String date){
                JsonArrayBuilder listaImagenes = Json.createArrayBuilder();
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");

            String sql = "SELECT * FROM IMAGE WHERE 1=1";

            if (date != null && !date.isEmpty()) {
                sql += " AND CAPTURE_DATE LIKE ?";
                System.out.println(date);
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            int paramIndex = 1;

            if (date != null && !date.isEmpty()) {
                statement.setString(paramIndex++, "%" + date + "%");
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                 JsonObjectBuilder listaObjectImagenes = Json.createObjectBuilder()
                .add("id", resultSet.getString("ID"))
                .add("title", resultSet.getString("TITLE"))
                .add("description", resultSet.getString("DESCRIPTION"))
                .add("keywords", resultSet.getString("KEYWORDS"))
                .add("author", resultSet.getString("AUTHOR"))
                .add("creator", resultSet.getString("CREATOR"))
                .add("captureDate", resultSet.getString("CAPTURE_DATE"))
                .add("storageDate", resultSet.getString("STORAGE_DATE"))
                .add("filename", resultSet.getString("FILENAME"));
                listaImagenes.add(listaObjectImagenes.build());

            }

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

        return listaImagenes.build();
    }
    
    public JsonArray show_images_by_keywords(String keywords){
                JsonArrayBuilder listaImagenes = Json.createArrayBuilder();
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");

            String sql = "SELECT * FROM IMAGE WHERE 1=1";

            if (keywords != null && !keywords.isEmpty()) {
                sql += " AND KEYWORDS LIKE ?";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            int paramIndex = 1;

            if (keywords != null && !keywords.isEmpty()) {
                statement.setString(paramIndex++, "%" + keywords + "%");
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                 JsonObjectBuilder listaObjectImagenes = Json.createObjectBuilder()
                .add("id", resultSet.getString("ID"))
                .add("title", resultSet.getString("TITLE"))
                .add("description", resultSet.getString("DESCRIPTION"))
                .add("keywords", resultSet.getString("KEYWORDS"))
                .add("author", resultSet.getString("AUTHOR"))
                .add("creator", resultSet.getString("CREATOR"))
                .add("captureDate", resultSet.getString("CAPTURE_DATE"))
                .add("storageDate", resultSet.getString("STORAGE_DATE"))
                .add("filename", resultSet.getString("FILENAME"));
                listaImagenes.add(listaObjectImagenes.build());

            }

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

        return listaImagenes.build();
    }
    
     public JsonArray show_images(){
        
        JsonArrayBuilder listaImagenes = Json.createArrayBuilder();
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");

            String sql = "SELECT * FROM IMAGE ";          
            PreparedStatement statement = connection.prepareStatement(sql);          
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                JsonObjectBuilder listaObjectImagenes = Json.createObjectBuilder()
                .add("id", resultSet.getString("ID"))
                .add("title", resultSet.getString("TITLE"))
                .add("description", resultSet.getString("DESCRIPTION"))
                .add("keywords", resultSet.getString("KEYWORDS"))
                .add("author", resultSet.getString("AUTHOR"))
                .add("creator", resultSet.getString("CREATOR"))
                .add("captureDate", resultSet.getString("CAPTURE_DATE"))
                .add("storageDate", resultSet.getString("STORAGE_DATE"))
                .add("filename", resultSet.getString("FILENAME"));
                listaImagenes.add(listaObjectImagenes.build());
            }

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

        return listaImagenes.build();
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
      public boolean image_modify(String titulo, String descripcion, String keywords, String autor, int id) {
        boolean okMod = false;
        Connection connection = null;
        //response.setContentType("text/html;charset=UTF-8");
        
        try {
            int cont = 1;
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            StringBuilder sql = new StringBuilder("UPDATE IMAGE SET ");
            List<String> updates = new ArrayList<>(); // Para almacenar las partes de la consulta

            if (!titulo.trim().isEmpty()) {
                updates.add("title = ?");
            }
            if (!descripcion.trim().isEmpty()) {
                updates.add("description = ?");
            }
            if (!keywords.trim().isEmpty()) {
                updates.add("keywords = ?");
            }
            if (!autor.trim().isEmpty()) {
                updates.add("author = ?");
            }

            sql.append(String.join(", ", updates));
            sql.append(" WHERE id = ?");

            System.out.println(sql.toString());

            PreparedStatement statement = connection.prepareStatement(sql.toString());

            if (!titulo.trim().isEmpty()) {
                    statement.setString(cont, titulo);
                    ++cont;
                }
                if (!descripcion.trim().isEmpty()) {
                    statement.setString(cont, descripcion);
                    ++cont;
                }
                 if (!keywords.trim().isEmpty()) {
                     statement.setString(cont, keywords);
                     ++cont;
                 }
                if (!autor.trim().isEmpty()) {
                    statement.setString(cont, autor);
                    ++cont;
                }
            statement.setInt(cont, id);
            okMod = true;
            statement.executeUpdate();
            connection.commit();
            
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
        return okMod;
    }
      
     public String consulta_imagen(int id) {
         Connection connection = null;
         String filename = null;
         try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            String sql = "SELECT filename FROM image WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                filename = resultSet.getString("filename"); // Obtener el valor del campo filename
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
         return filename;
     }
}
