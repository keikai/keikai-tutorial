package io.keikai.tutorial;

import com.google.gson.*;
import io.keikai.client.api.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

import static io.keikai.tutorial.Configuration.SPREADSHEET;

/**
 * contains common methods.
 */
public class BaseServlet extends HttpServlet {
    protected Spreadsheet spreadsheet; //session scope variable
    static protected Gson gson;

    static{
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    protected Spreadsheet getSpreadsheet(ServletRequest request) {
        spreadsheet = (Spreadsheet) ((HttpServletRequest) request).getSession().getAttribute(SPREADSHEET);
        if (spreadsheet == null) {
            initSpreadsheet(request);
        }
        return spreadsheet;
    }

    /**
     * store spreadsheet and its javascript in a session
     * @param request
     */
    protected void initSpreadsheet(ServletRequest request) {
        spreadsheet = Keikai.newClient(Configuration.INTERNAL_KEIKAI_SERVER);
        String keikaiJs = spreadsheet.getURI("spreadsheet"); // pass the anchor DOM element id for rendering keikai
        HttpSession session = ((HttpServletRequest) request).getSession();
        session.setAttribute(Configuration.KEIKAI_JS, keikaiJs);
        session.setAttribute(Configuration.SPREADSHEET, spreadsheet);
    }
}
