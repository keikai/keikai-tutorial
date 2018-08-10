package io.keikai.tutorial.web;

import io.keikai.client.api.*;
import io.keikai.tutorial.Configuration;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * accept "server" parameter e.g. http://localhost:8080?server=10.1.1.1:8888
 */
public class BaseServlet extends HttpServlet {
    protected Spreadsheet spreadsheet;
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

        spreadsheet = Keikai.newClient(keikaiServerAddress, getSettings());
        // pass the anchor DOM element id for rendering keikai
        String keikaiJs = spreadsheet.getURI("spreadsheet");
        request.setAttribute(Configuration.KEIKAI_JS, keikaiJs);
        putIntoSession(((HttpServletRequest)request).getSession());
    }

    /**
     * put {@link Spreadsheet} object to a session to close it when the session expires.
     */
    private void putIntoSession(HttpSession session) {
        List<Spreadsheet> list = (List<Spreadsheet>)session.getAttribute(Configuration.SPREADSHEETS);
        if (list == null){
            list = new LinkedList<Spreadsheet>();
            session.setAttribute(Configuration.SPREADSHEETS, list);
        }
        list.add(spreadsheet);
    }

    protected Settings getSettings() {
        return Settings.DEFAULT_SETTINGS;
    }

}
