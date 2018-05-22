package io.keikai.tutorial;

import org.apache.commons.io.IOUtils;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;

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
        Result result = Result.getSuccess("import success");
        try {
            out = new FileOutputStream(new File(getServletContext().getRealPath(File.separator + Configuration.DEFAULT_FILE_FOLDER)
                    + File.separator + fileName));
            inputStream = filePart.getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            spreadsheet.imports(fileName, byteArrayInputStream);

            response.setContentType("text/plain");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = Result.getErrorResult(e.getMessage());
        } finally {
            writer.print(gson.toJson(result));
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
        return "";
    }

}
