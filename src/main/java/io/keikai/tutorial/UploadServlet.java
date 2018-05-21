package io.keikai.tutorial;

import io.keikai.client.api.Spreadsheet;

import javax.security.auth.callback.ConfirmationCallback;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;

import static io.keikai.tutorial.Configuration.SPREADSHEET;

@WebServlet("/editor/upload")
@MultipartConfig(maxFileSize = 1024 * 1024 * 50, //50MB
        location = "/" + Configuration.DEFAULT_FILE_FOLDER)
public class UploadServlet extends HttpServlet {
    private Spreadsheet spreadsheet;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        spreadsheet = (Spreadsheet) ((HttpServletRequest) request).getSession().getAttribute(SPREADSHEET);
        response.setContentType("text/html;charset=UTF-8");

            // Create path components to save the file
            final Part filePart = request.getPart("file");
            final String fileName = getFileName(filePart);

            final PrintWriter writer = response.getWriter();
            OutputStream out = null;
            InputStream filecontent = null;
        try {
            out = new FileOutputStream(new File( request.getServletContext().getRealPath(File.separator + Configuration.DEFAULT_FILE_FOLDER)
                    + File.separator + fileName));
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            writer.println("New file " + fileName + " created at " + Configuration.DEFAULT_FILE_FOLDER);
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
//        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
}
