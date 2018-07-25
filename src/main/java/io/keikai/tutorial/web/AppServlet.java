package io.keikai.tutorial.web;

import io.keikai.client.api.*;
import io.keikai.tutorial.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/app/*")
public class AppServlet extends BaseServlet {
    private static final String DEFAULT_XLSX = "app.xlsx";
    private File defaultFile;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        defaultFile = new File(getServletContext().getRealPath("/WEB-INF" +
                File.separator + Configuration.INTERNAL_FILE_FOLDER + File.separator + DEFAULT_XLSX));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            initSpreadsheet(req);
            spreadsheet.imports(DEFAULT_XLSX, defaultFile);
            new MyApp(spreadsheet);
        } catch (DuplicateNameException e) {
            throw new IOException(e);
        } catch (AbortedException e) {
            throw new IOException(e);
        }
        req.getRequestDispatcher("/app.jsp").forward(req, resp);
    }

}
