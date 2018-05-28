package io.keikai.tutorial;

import io.keikai.client.api.*;
import io.keikai.client.api.event.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.concurrent.ExecutionException;

@WebServlet("/editor/*")
public class EditorServlet extends BaseServlet {
    private static final String BLANK_XLSX = "blank.xlsx";
    private File defaultExportFolder;
    private File defaultBookFolder;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        defaultExportFolder = new File(getServletContext().getRealPath(Configuration.DEFAULT_FILE_FOLDER));
        defaultBookFolder = new File(getServletContext().getRealPath("/WEB-INF" +
                File.separator + Configuration.INTERNAL_FILE_FOLDER + File.separator + BLANK_XLSX));
//        Configuration.enableSocketIOLog();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        spreadsheet = getSpreadsheet(req);
        Result result = Result.getSuccess("success");
        resp.setContentType("text/plain");
        try {
            if (isAction(req, "export")) {
                export();
            }
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

    private void export() throws FileNotFoundException, ExecutionException, InterruptedException {
        String fileName = spreadsheet.loadWorkbook().get().getName();
        FileOutputStream outputStream = new FileOutputStream(new File(getServletContext().getRealPath(File.separator +
                Configuration.DEFAULT_FILE_FOLDER + File.separator + fileName)));
        spreadsheet.export(spreadsheet.getCurrentWorkbook(), outputStream).whenComplete((obj, throwable) -> {
        });
    }

    private boolean isAction(HttpServletRequest request, String action) {
        return request.getPathInfo().endsWith(action);
    }

    @Override
    protected void initSpreadsheet(ServletRequest request) {
        super.initSpreadsheet(request);
        registerToolbarButtonsHandlers();
    }

    protected void registerToolbarButtonsHandlers() {
        ExceptionalConsumer listener = (event) -> {
            final AuxActionEvent auxActionEvent = (AuxActionEvent) event;
            final String action = auxActionEvent.getAction();
            if (action.equals("saveBook")) {

            }else if (action.equals("newBook")){
                spreadsheet.containsWorkbook(BLANK_XLSX).thenAccept(existed ->{
                    spreadsheet.deleteWorkbook(BLANK_XLSX);
                    spreadsheet.imports(BLANK_XLSX, defaultBookFolder).exceptionally(throwable -> {
                        throwable.printStackTrace();
                        return null;
                    });
                });
            }else if (action.equals("exportToFile")){

            }
            System.out.println(action);
        };

        ExceptionableFunction listener2 = (event) -> {
            final AuxActionEvent auxActionEvent = (AuxActionEvent) event;
            final String action = auxActionEvent.getAction();
            final Range range = auxActionEvent.getActiveCell();
            if (!action.equals("saveBook")) {

            }
            System.out.println(action);
            range.loadWorkbook().thenAcceptBoth(range.loadWorksheet(), (Workbook workbook, Worksheet worksheet) -> {
                System.out.println("Save [ " + workbook.getName() + "] " + worksheet.getName());
            });
            return null;
        };
        spreadsheet.addEventListener(Events.ON_AUX_ACTION, listener::accept);

    }
}
