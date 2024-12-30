package test.backend.resources;
import DB.database;
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
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.OutputStream;

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
 
 @Path("register")
 @POST
 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
 @Produces(MediaType.APPLICATION_JSON)
 public Response register(@FormParam("username") String username,
                          @FormParam("email") String email,
                          @FormParam("password") String password) {
     
     database db = new database();
     
     if (db.register(username,password,email)) 
          return Response.ok().build();
     else  
         return Response.status(Response.Status.UNAUTHORIZED).build();
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
        System.out.println(markdownContent);
        String htmlResponse = convertirMarkdownAHtml(markdownContent);
        System.out.println(htmlResponse);
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


@Path("downloaddocx")
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
public Response download_docx(String JSONmarkdownContent) {
    try {
        JsonReader reader = Json.createReader(new StringReader(JSONmarkdownContent));
        JsonObject jsonObject = reader.readObject();
        String markdownContent = jsonObject.getString("markdownContent");

        String htmlContent = convertirMarkdownAHtml(markdownContent);

        ByteArrayOutputStream docxOutputStream = new ByteArrayOutputStream();
        generarDocxDesdeHtml(htmlContent, docxOutputStream);

        InputStream archivoDocx = new ByteArrayInputStream(docxOutputStream.toByteArray());
        return Response.ok(archivoDocx)
                .header("Content-Disposition", "attachment; filename=documento.docx")
                .type("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .build();
    } catch (Exception e) {
        return Response.serverError().entity("Error al generar el archivo DOCX").build();
    }
}


public void generarDocxDesdeHtml(String htmlContent, OutputStream outputStream) throws Exception {
    try {
        // Crear un documento en blanco usando Apache POI
        XWPFDocument document = new XWPFDocument();

        // Parsear el contenido HTML usando Jsoup
        Document doc = Jsoup.parse(htmlContent);

        // Manejar encabezados (h1-h6)
        for (Element header : doc.select("h1, h2, h3, h4, h5, h6")) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();

            String headerText = header.text();
            run.setText(headerText);

            // Aplicar estilos dependiendo del nivel del encabezado
            switch (header.tagName()) {
                case "h1":
                    run.setBold(true);
                    run.setFontSize(18);
                    break;
                case "h2":
                    run.setBold(true);
                    run.setFontSize(16);
                    break;
                case "h3":
                    run.setBold(true);
                    run.setFontSize(14);
                    break;
                case "h4":
                    run.setBold(true);
                    run.setFontSize(12);
                    break;
                case "h5":
                    run.setBold(true);
                    run.setFontSize(10);
                    break;
                case "h6":
                    run.setBold(true);
                    run.setFontSize(8);
                    break;
            }
        }

        // Manejar párrafos
        for (Element paragraph : doc.select("p")) {
            XWPFParagraph xwpfParagraph = document.createParagraph();
            XWPFRun run = xwpfParagraph.createRun();
            processInlineStyles(paragraph, run);
        }

        // Manejar listas ordenadas y desordenadas
        for (Element list : doc.select("ul, ol")) {
            for (Element listItem : list.select("li")) {
                XWPFParagraph listParagraph = document.createParagraph();
                XWPFRun run = listParagraph.createRun();
                run.setText("• " + listItem.text()); // Usa "•" para listas desordenadas
            }
        }

        // Escribir el contenido en el OutputStream
        document.write(outputStream);
        document.close();
    } catch (Exception e) {
        throw new Exception("Error al generar el archivo DOCX", e);
    }
}

private void processInlineStyles(Element element, XWPFRun run) {
    Elements strongs = element.select("strong");
    Elements ems = element.select("em");

    if (!strongs.isEmpty() && !ems.isEmpty()) {
        run.setBold(true);
        run.setItalic(true);
    } else if (!strongs.isEmpty()) {
        run.setBold(true);
    } else if (!ems.isEmpty()) {
        run.setItalic(true);
    }

    run.setText(element.text());
}


}