package io.keikai.tutorial.web;

import io.keikai.client.api.*;
import io.keikai.tutorial.Configuration;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;

/**
 * accept "server" parameter e.g. http://localhost:8080?server=10.1.1.1:8888
 */
public class BaseServlet extends HttpServlet {
    protected Spreadsheet spreadsheet; //session scope variable
    protected String keikaiServerAddress = Configuration.DEFAULT_KEIKAI_SERVER;
    protected File defaultFileFolder;
    protected File defaultFile;
    protected String defaultXlsx = "welcome.xlsx";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        defaultFileFolder = new File(getServletContext().getRealPath("/WEB-INF" + File.separator + Configuration.INTERNAL_FILE_FOLDER));
        defaultFile = new File(defaultFileFolder, defaultXlsx);
//        Configuration.enableSocketIOLog();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initSpreadsheet(req);
        try {
            spreadsheet.importAndReplace(defaultXlsx, defaultFile);
        } catch (AbortedException e) {
            throw new IOException(e);
        }
    }

    protected void determineServerAddress(String serverAddress) {
        if (serverAddress != null){
            keikaiServerAddress = "http://" + serverAddress;
        }
    }

    /**
     * Create {@link Spreadsheet} object and Keikai client javascript URL. Store spreadsheet and its javascript in a session
     *
     * @param request
     */
    protected void initSpreadsheet(ServletRequest request) {
        determineServerAddress(request.getParameter("server"));
        spreadsheet = Keikai.newClient(keikaiServerAddress);

        String keikaiJs = spreadsheet.getURI("spreadsheet"); // pass the anchor DOM element id for rendering keikai
        request.setAttribute(Configuration.KEIKAI_JS, keikaiJs);
    }


}
