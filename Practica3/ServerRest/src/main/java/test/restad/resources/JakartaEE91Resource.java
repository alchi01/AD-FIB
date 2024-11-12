package test.restad.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


//Añadidos
import DB.database;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
 * POST method to register a new image – File is not uploaded
 * @param title
 * @param description
 * @param keywords
 * @param author
 * @param creator
 * @param capt_date
 * @return
 */
 @Path("register")
 @POST
 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
 @Produces(MediaType.APPLICATION_JSON)
 public Response registerImage (@FormParam("title") String title,
                                @FormParam("description") String description,
                                @FormParam("keywords") String keywords,
                                @FormParam("author") String author,
                                @FormParam("creator") String creator,
                                @FormParam("capture") String capt_date) {
     
     database db = new database();
     boolean okRegister = true;
     
     LocalDate fechaActual = LocalDate.now();
     DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
     String save_date = fechaActual.format(format);
     LocalDate fechaCapt = LocalDate.parse(capt_date, format);
     
     // Validar que los campos requeridos no estén vacíos
     if (title == null || description == null || keywords == null || author == null || capt_date == null ||
            title.trim().isEmpty() || description.trim().isEmpty() || keywords.trim().isEmpty() || 
            author.trim().isEmpty() || capt_date.trim().isEmpty() || !fechaCapt.isAfter(fechaActual)) 
         okRegister = false;
     else
         okRegister = db.image_upload(title, description, keywords, author, creator, capt_date, save_date, title);
     
     if (okRegister)
         return Response.ok().build();
     else
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
         
     
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
     
     boolean okMod  = db.image_modify(title, description, keywords, author, Integer.parseInt(id));
     
     if (okMod) 
         return Response.ok().build();
     else
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
     
 }
 /**
 * POST method to delete an existing image
 * @param id
 * @return
 */
 @Path("delete")
 @POST
 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
 @Produces(MediaType.APPLICATION_JSON)
 public Response deleteImage (@FormParam("id") String id,
                              @FormParam("creator") String creator) {
     database db = new database();
     
     
     boolean okDelete = db.eliminate(Integer.parseInt(id));
     
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
 /*@Path("searchID/{id}")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response searchByID (@PathParam("id") int id){
     
 }
 /**
 * GET method to search images by title
 * @param title
 * @return
 */
/*@Path("searchTitle/{title}")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response searchByTitle (@PathParam("title") String title){
     
 }
 /**
 * GET method to search images by creation date. Date format should be
 * yyyy-mm-dd
 * @param date
 * @return
 */
/* @Path("searchCreationDate/{date}")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response searchByCreationDate (@PathParam("date") String date)
 /**
 * GET method to search images by author
 * @param author
 * @return
 */
/* @Path("searchAuthor/{author}")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response searchByAuthor (@PathParam("author") String author)
 /**
 * GET method to search images by keyword
 * @param keywords
 * @return
 */
/* @Path("searchKeywords/{keywords}")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 public Response searchByKeywords (@PathParam("keywords") String keywords)*/
}
