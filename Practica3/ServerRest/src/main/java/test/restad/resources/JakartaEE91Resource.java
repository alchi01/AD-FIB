package test.restad.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


//Añadidos
import DB.database;
import jakarta.activation.MimetypesFileTypeMap;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.HttpHeaders;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author 
 */
@Path("jakartaee9")
public class JakartaEE91Resource {
    
    @GET
    public Response ping(){
        return Response
                .ok("ping Jakarta EE")
                .build();
    }
    
    final public static String uploadDir = "/var/webapp/images/";
    
    /**
 * OPERACIONES DEL SERVICIO REST
 */

 /**
 * POST method to login in the application
 * @param username
 * @param password
 * @return
 */
 @Path("login")
 @POST
 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
 @Produces(MediaType.APPLICATION_JSON)
 public Response login(@FormParam("username") String username,
                       @FormParam("password") String password) {
     
     database db = new database();
     
     if (db.login(username,password)) 
          return Response.ok().build();
     else  
         return Response.status(Response.Status.UNAUTHORIZED).build();
 }
 
 /**
 * POST method to check the user is the creator of an image
 * @param username
 * @param id
 * @return
 */
 /*@Path("checkUser")
 @POST
 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
 @Produces(MediaType.APPLICATION_JSON)
 public Response checkUserConnected(@FormParam("id") String id,
                           @FormParam("username") String username){
     
     database db = new database();
     
     if (db.creator_connected(Integer.parseInt(id),username)) 
          return Response.ok().build();
     else  
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
 }*/
 
 private static void makeDirIfNotExists() {
        File dir = new File(uploadDir);
        // Creamos directorio si no existe.
        if (! dir.exists() ) {
           dir.mkdirs();
        }
    }
 
 public static Boolean writeImage(String file_name, InputStream fileInputStream)  {
        try{
            makeDirIfNotExists();
            File targetfile = new File(uploadDir + file_name);
        
            java.nio.file.Files.copy(
                    fileInputStream,
                    targetfile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );  
        }catch (IOException e){
            return false;
        }
        return true; 
    }
    
    
 
 
 /**
 * POST method to register a new image – File is not uploaded
 * @param title
 * @param description
 * @param keywords
 * @param author
 * @param creator
 * @param capt_date
 * @param filename
 * @param fileInputStream
 * @param fileMetaData
 * @return
 */
 @Path("register")
 @POST
 @Consumes(MediaType.MULTIPART_FORM_DATA)
 @Produces(MediaType.APPLICATION_JSON)
 public Response registerImage (@FormDataParam("title") String title,
                                @FormDataParam("description") String description,
                                @FormDataParam("keywords") String keywords,
                                @FormDataParam("author") String author,
                                @FormDataParam("creator") String creator,
                                @FormDataParam("capture") String capt_date,
                                @FormDataParam("filename") String filename,
                                @FormDataParam("file") InputStream fileInputStream,
                                @FormDataParam("file") FormDataContentDisposition fileMetaData) {               
        /*if (!writeImage(filename, fileInputStream)) { //no sha pogut guardar la imatge            
            StatusCode = 500; //fallada server
            //String ErrorName = "general";
        }
        
        /*return Response
            .status(StatusCode)
            .build();*/
        
     database db = new database();
     boolean okRegister = true;
     
     LocalDate fechaActual = LocalDate.now();
     DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
     DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
     String save_date = fechaActual.format(format1);
     LocalDate fechaCapt = LocalDate.parse(capt_date, format2);
     String captura_date = fechaCapt.format(format1);
     
     // Validar que los campos requeridos no estén vacíos
     if (title == null || description == null || keywords == null || author == null || capt_date == null ||
            title.trim().isEmpty() || description.trim().isEmpty() || keywords.trim().isEmpty() || 
            author.trim().isEmpty() || capt_date.trim().isEmpty() || fechaCapt.isAfter(fechaActual)) 
         okRegister = false;
     else
         okRegister = db.image_upload(title, description, keywords, author, creator, captura_date, save_date, filename)
         && writeImage(filename, fileInputStream);
     if (okRegister)
         return Response.status(201).build();
     else
         return Response.status(500).build();
         
     
 }
 /**
 * POST method to modify an existing image
 * @param id
 * @param title
 * @param description
 * @param keywords
 * @param author
 * @param creator, used for checking image ownership
 * @param capt_date
 * @return
 */
 @Path("modify")
 @POST
 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
 @Produces(MediaType.APPLICATION_JSON)
 public Response modifyImage (@FormParam("id") String id,
                              @FormParam("title") String title,
                              @FormParam("description") String description,
                              @FormParam("keywords") String keywords,
                              @FormParam("author") String author,
                              @FormParam("creator") String creator,
                              @FormParam("capture") String capt_date) {
     database db = new database();
     boolean okMod = false;
     if (db.creator_connected(Integer.parseInt(id), creator))
        okMod  = db.image_modify(title, description, keywords, author, Integer.parseInt(id));
     
     if (okMod) 
         return Response.ok().build();
     else
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
     
 }
 
 public static Boolean deleteImage(String file_name) 
    {
        makeDirIfNotExists();
        
        File targetfile = new File(uploadDir + file_name);
        if(! targetfile.delete()) {
            System.out.println("ERROR: Failed to delete " + targetfile.getAbsolutePath());
            return false;
        }
       
        System.out.println("SUCCESS: deleted " + targetfile.getAbsolutePath());
        return true;
    }
 
 /**
 * POST method to delete an existing image
 * @param id
 * @param creator
 * @return
 */
 @Path("delete")
 @POST
 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
 @Produces(MediaType.APPLICATION_JSON)
 public Response deleteImage (@FormParam("id") String id,
                        @FormParam("creator") String creator) {
     database db = new database();
     boolean okDelete = false;
     System.out.println(id);
     if (db.creator_connected(Integer.parseInt(id), creator)) {
        String filename = db.consulta_imagen(Integer.parseInt(id));
        okDelete = db.eliminate(Integer.parseInt(id)) && deleteImage(filename);
     }
     
     
     if (okDelete)
         return Response.ok().build();
     else
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
 }
 /**
 * GET method to search all images
 * @return
 */
 @Path("showImages")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response showAllImages (){
     database db = new database();
     
     JsonArray listaImagenes = db.show_images();
     if (listaImagenes != null)
         return Response.ok(listaImagenes.toString(),MediaType.APPLICATION_JSON).build();
     else
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
 }
 /**
 * GET method to search images by id
 * @param id
 * @return
 */
 @Path("searchID/{id}")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response searchByID (@PathParam("id") int id){
     database db = new database();
     
     JsonObject listaImagenes = db.show_images_by_id(id);
     if (listaImagenes != null)
         return Response.ok(listaImagenes.toString(),MediaType.APPLICATION_JSON).build();
     else
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
 }
 /**
 * GET method to search images by title
 * @param title
 * @return
 */
@Path("searchTitle/{title}")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response searchByTitle (@PathParam("title") String title){
     
    database db = new database();
     
     JsonArray listaImagenes = db.show_images_by_title(title);
     if (listaImagenes != null)
         return Response.ok(listaImagenes.toString(),MediaType.APPLICATION_JSON).build();
     else
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
 }
 /**
 * GET method to search images by creation date. Date format should be
 * yyyy-mm-dd
 * @param date
 * @return
 */
@Path("searchCreationDate/{date}")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response searchByCreationDate (@PathParam("date") String date) {
     database db = new database();
     DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
     DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
     LocalDate fechaCapt = LocalDate.parse(date, format2);
     String date1 = fechaCapt.format(format1);
     
     
     
     JsonArray listaImagenes = db.show_images_by_date(date1);
     if (listaImagenes != null && !fechaCapt.isAfter(LocalDate.now()))
         return Response.ok(listaImagenes.toString(),MediaType.APPLICATION_JSON).build();
     else
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
 }
 
 /**
 * GET method to search images by author
 * @param author
 * @return
 */
 @Path("searchAuthor/{author}")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response searchByAuthor (@PathParam("author") String author){
     database db = new database();
     
     JsonArray listaImagenes = db.show_images_by_author(author);
     if (listaImagenes != null)
         return Response.ok(listaImagenes.toString(),MediaType.APPLICATION_JSON).build();
     else
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
 
 }
 /**
 * GET method to search images by keyword
 * @param keywords
 * @return
 */
 @Path("searchKeywords/{keywords}")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response searchByKeywords (@PathParam("keywords") String keywords){
     database db = new database();
     
     JsonArray listaImagenes = db.show_images_by_keywords(keywords);
     if (listaImagenes != null)
         return Response.ok(listaImagenes.toString(),MediaType.APPLICATION_JSON).build();
     else
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

 }
 
 /**
 * GET method to search images by combination
 * @param title
 * @param description
 * @param author
 * @param date
 * @param keywords
 * @return
 */
 @Path("searchCombined")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response searchByCombination (@QueryParam("title") String title,
                                   @QueryParam("description") String description,
                                   @QueryParam("author") String author,
                                   @QueryParam("date") String date,
                                   @QueryParam("keywords") String keywords){
     database db = new database();
     
     JsonArray listaImagenes = db.show_images_combined(title, description, author, date, keywords);
     if (listaImagenes != null)
         return Response.ok(listaImagenes.toString(),MediaType.APPLICATION_JSON).build();
     else
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

 }
 
  @Path("getImage/{filename}")
    @GET
    @Produces("image/*")
    public Response getImage(@PathParam("filename") String filename) {
        try {
            File f = new File(uploadDir + filename);
            if (!f.exists()) return Response.status(Response.Status.FORBIDDEN).build();
            String mt = new MimetypesFileTypeMap().getContentType(f);
            return Response.ok(f, mt).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

