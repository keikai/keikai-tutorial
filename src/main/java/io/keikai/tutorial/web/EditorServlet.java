package io.keikai.tutorial.web;

import io.keikai.client.api.*;
import io.keikai.client.api.event.*;
import io.keikai.client.api.ui.AuxAction;
import io.keikai.tutorial.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/editor/*")
public class EditorServlet extends BaseServlet {
    private static final String DEFAULT_XLSX = "welcome.xlsx";
    private File defaultFile;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        defaultFile = new File(getServletContext().getRealPath("/WEB-INF" +
                File.separator + Configuration.INTERNAL_FILE_FOLDER + File.separator + DEFAULT_XLSX));
//        Configuration.enableSocketIOLog();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initSpreadsheet(req);
        try {
            spreadsheet.imports(DEFAULT_XLSX, defaultFile);
        } catch (DuplicateNameException e) {
            throw new IOException(e);
        } catch (AbortedException e) {
            throw new IOException(e);
        }
        req.getRequestDispatcher("/editor.jsp").forward(req, resp);
    }

}
