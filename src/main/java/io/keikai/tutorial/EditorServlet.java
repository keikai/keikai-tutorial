package io.keikai.tutorial;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.concurrent.ExecutionException;

@WebServlet("/editor/*")
public class EditorServlet extends BaseServlet {
    private static final String BLANK_XLSX = "blank.xlsx";
    private File defaultExportFolder;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        defaultExportFolder = new File(getServletContext().getRealPath(Configuration.DEFAULT_FILE_FOLDER));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        spreadsheet = getSpreadsheet(req);
        Result result = Result.getSuccess("success");
        resp.setContentType("text/plain");
        try {
            if (isAction(req, "export")) {
                export();
            } else if (isAction(req, "new")) {
                if (spreadsheet.containsWorkbook(BLANK_XLSX).get()) {
                    spreadsheet.deleteWorkbook(BLANK_XLSX);
                }
                spreadsheet.imports(BLANK_XLSX, req.getServletContext().getResourceAsStream("/WEB-INF" +
                        File.separator + Configuration.INTERNAL_FILE_FOLDER + File.separator + BLANK_XLSX));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.getErrorResult(e.toString());
        }
        resp.getWriter().print(gson.toJson(result));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getSpreadsheet(req);
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
}
