package io.keikai.tutorial;

import com.google.gson.*;
import io.keikai.client.api.Spreadsheet;
import org.apache.commons.io.IOUtils;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;

import static io.keikai.tutorial.Configuration.SPREADSHEET;

@WebServlet("/editor/upload")
@MultipartConfig(maxFileSize = 1024 * 1024 * 50, //50MB
        location = "/" + Configuration.DEFAULT_FILE_FOLDER)
public class UploadServlet extends BaseServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        spreadsheet = getSpreadsheet(request);

        // Create path components to save the file
        final Part filePart = request.getPart("file");
        final String fileName = extractFileName(filePart);

        final PrintWriter writer = response.getWriter();
        OutputStream out = null;
        InputStream inputStream = null;
        try {
            out = new FileOutputStream(new File(request.getServletContext().getRealPath(File.separator + Configuration.DEFAULT_FILE_FOLDER)
                    + File.separator + fileName));
            inputStream = filePart.getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            spreadsheet.imports(fileName, byteArrayInputStream);

            response.setContentType("text/plain");

            writer.print(getJsonConverter().toJson("success"));
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    private String extractFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private Gson getJsonConverter(){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        return builder.create();
    }

}
