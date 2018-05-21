package io.keikai.tutorial;

import io.keikai.client.api.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

import static io.keikai.tutorial.Configuration.SPREADSHEET;

@WebServlet("/editor/*")
public class EditorServlet extends BaseServlet {
    private File defaultExportFolder;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        defaultExportFolder = new File(getServletContext().getRealPath(Configuration.DEFAULT_FILE_FOLDER));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        spreadsheet = getSpreadsheet(req);
        if (isAction(req, "export")) {
            try {
                export(new File(defaultExportFolder, "export.xlsx"));
            } catch (FileNotFoundException e) {
                //TODO response back to clients
                e.printStackTrace();
            }
        } else {
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getSpreadsheet(req);
        req.getRequestDispatcher("/editor.jsp").forward(req, resp);
    }

    private void export(File file) throws FileNotFoundException {
        FileOutputStream outputStream = new FileOutputStream(file);
        spreadsheet.export(spreadsheet.getCurrentWorkbook(), outputStream).whenComplete((obj, throwable) -> {
        });
    }

    private boolean isAction(HttpServletRequest request, String action) {
        return request.getPathInfo().endsWith(action);
    }
}
