package io.keikai.tutorial.web;

import io.keikai.client.api.event.*;
import io.keikai.client.api.ui.AuxAction;
import io.keikai.tutorial.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/editor/*")
public class EditorServlet extends BaseServlet {
    private static final String BLANK_XLSX = "blank.xlsx";
    private static final String DEFAULT_XLSX = "welcome.xlsx";
    private File defaultExportFolder;
    private File defaultBookFolder;
    private File defaultFile;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        defaultExportFolder = new File(getServletContext().getRealPath(Configuration.DEFAULT_FILE_FOLDER));
        defaultBookFolder = new File(getServletContext().getRealPath("/WEB-INF" +
                File.separator + Configuration.INTERNAL_FILE_FOLDER + File.separator + BLANK_XLSX));
        defaultFile = new File(getServletContext().getRealPath("/WEB-INF" +
                File.separator + Configuration.INTERNAL_FILE_FOLDER + File.separator + DEFAULT_XLSX));
//        Configuration.enableSocketIOLog();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        spreadsheet = getSpreadsheet(req);
        Result result = Result.getSuccess("success");
        resp.setContentType("text/plain");
        try {
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.getErrorResult(e.toString());
        }
        resp.getWriter().print(gson.toJson(result));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initSpreadsheet(req);
        req.getRequestDispatcher("/editor.jsp").forward(req, resp);
    }

    @Override
    protected void initSpreadsheet(ServletRequest request) {
        super.initSpreadsheet(request);
        addToolbarEventListeners();
        spreadsheet.imports(DEFAULT_XLSX, defaultFile);
    }


    protected void addToolbarEventListeners() {
        ExceptionalConsumer listener = (event) -> {
            final AuxActionEvent auxActionEvent = (AuxActionEvent) event;
            final String action = auxActionEvent.getAction();
            if (action.equals("saveBook")) {

            } else if (action.equals(AuxAction.NEW_BOOK.getAction())) {
                spreadsheet.containsWorkbook(BLANK_XLSX).thenAccept(existed -> {
                    spreadsheet.deleteWorkbook(BLANK_XLSX);
                    spreadsheet.imports(BLANK_XLSX, defaultBookFolder).exceptionally(throwable -> {
                        throwable.printStackTrace();
                        return null;
                    });
                });
            }
        };

        spreadsheet.addEventListener(Events.ON_AUX_ACTION, listener::accept);
    }
}
