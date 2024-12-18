package test.backend.resources;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import com.itextpdf.html2pdf.HtmlConverter;

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
 * POST method to login in the application
     * @param JSONmarkdownContent
 * @return
 */
 @Path("downloadhtml")
 @POST
 @Consumes(MediaType.APPLICATION_JSON)
 @Produces("text/html")
 public Response download_html(String JSONmarkdownContent) {
     
     try {
         
        JsonReader reader = Json.createReader(new StringReader(JSONmarkdownContent));
        JsonObject jsonObject = reader.readObject();
        String markdownContent = jsonObject.getString("markdownContent");
        //System.out.println(markdownContent);
        String htmlResponse = convertirMarkdownAHtml(markdownContent);
        //System.out.println(htmlResponse);
        InputStream archivoHtml = new ByteArrayInputStream(htmlResponse.getBytes());
        return Response.ok(archivoHtml)
                .header("Content-Disposition", "attachment; filename=documento.html") 
                .type("text/html").build();
     } catch (Exception e) {
         return Response.serverError().entity("Error al generar el archivo HTML").build();
     }
 }
 
   private String convertirMarkdownAHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document); 
    }
   
@Path("downloadpdf")
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces("application/pdf")
public Response download_pdf(String JSONmarkdownContent) {
    try {
        JsonReader reader = Json.createReader(new StringReader(JSONmarkdownContent));
        JsonObject jsonObject = reader.readObject();
        String markdownContent = jsonObject.getString("markdownContent");

        String htmlContent = convertirMarkdownAHtml(markdownContent);

        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        generarPdfDesdeHtml(htmlContent, pdfOutputStream);

        InputStream archivoPdf = new ByteArrayInputStream(pdfOutputStream.toByteArray());
        return Response.ok(archivoPdf)
                .header("Content-Disposition", "attachment; filename=documento.pdf")
                .type("application/pdf")
                .build();
    } catch (Exception e) {

        return Response.serverError().entity("Error al generar el archivo PDF").build();
    }
}

private void generarPdfDesdeHtml(String htmlContent, OutputStream outputStream) throws Exception {
    try {
        // Convertir el HTML en un archivo PDF
        HtmlConverter.convertToPdf(htmlContent, outputStream);
    } catch (Exception e) {
        throw new Exception("Error al generar el PDF", e);
    }
}
}
