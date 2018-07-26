package io.keikai.tutorial.web;

import com.google.gson.*;
import io.keikai.client.api.*;
import io.keikai.tutorial.Configuration;

import javax.servlet.ServletRequest;
import javax.servlet.http.*;

import static io.keikai.tutorial.Configuration.SPREADSHEET;

/**
 * accept "server" parameter e.g. http://localhost:8080?server=10.1.1.1:8888
 */
public class BaseServlet extends HttpServlet {
    protected Spreadsheet spreadsheet; //session scope variable
    static protected Gson gson;
    private String keikaiServerAddress = Configuration.DEFAULT_KEIKAI_SERVER;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    /**
     * get Spreadsheet from session scope first or create a new one
     * @param request
     * @return
     */
    protected Spreadsheet getSpreadsheet(ServletRequest request) {
        spreadsheet = (Spreadsheet) ((HttpServletRequest) request).getSession().getAttribute(SPREADSHEET);
        if (spreadsheet == null) {
            initSpreadsheet(request);
        }
        return spreadsheet;
    }

    protected void determineServerAddress(String serverAddress) {
        if (serverAddress != null){
            keikaiServerAddress = "http://" + serverAddress;
        }
    }

    /**
     * store spreadsheet and its javascript in a session
     *
     * @param request
     */
    protected void initSpreadsheet(ServletRequest request) {
        determineServerAddress(request.getParameter("server"));
        spreadsheet = Keikai.newClient(keikaiServerAddress);

        String keikaiJs = spreadsheet.getURI("spreadsheet"); // pass the anchor DOM element id for rendering keikai
        HttpSession session = ((HttpServletRequest) request).getSession();
        session.setAttribute(Configuration.KEIKAI_JS, keikaiJs);
        session.setAttribute(Configuration.SPREADSHEET, spreadsheet);
    }


}
