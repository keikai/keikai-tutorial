package io.keikai.tutorial.web;

import com.google.gson.*;
import io.keikai.client.api.*;
import io.keikai.tutorial.Configuration;
import io.keikai.util.Maps;

import javax.servlet.ServletRequest;
import javax.servlet.http.*;

import static io.keikai.tutorial.Configuration.SPREADSHEET;

/**
 * contains common methods.
 */
public class BaseServlet extends HttpServlet {
    protected Spreadsheet spreadsheet; //session scope variable
    static protected Gson gson;

    static {
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
     *
     * @param request
     */
    protected void initSpreadsheet(ServletRequest request) {
        Settings settings = Settings.DEFAULT_SETTINGS.clone();
        settings.set(Settings.Key.SPREADSHEET_CONFIG, Maps.toMap("toolbar", getToolBarConfig()));

        spreadsheet = Keikai.newClient(Configuration.INTERNAL_KEIKAI_SERVER, settings);
        String keikaiJs = spreadsheet.getURI("spreadsheet"); // pass the anchor DOM element id for rendering keikai
        HttpSession session = ((HttpServletRequest) request).getSession();
        session.setAttribute(Configuration.KEIKAI_JS, keikaiJs);
        session.setAttribute(Configuration.SPREADSHEET, spreadsheet);
    }


    /**
     * enable upload button
     *
     * @return
     */
    protected String getToolBarConfig() {
        return "{\"items\": \"newBook,saveBook,upload,exportToFile|paste,cut,copy|fontName,fontSize,fontItalic,fontBold,fontUnderlined,fontStrike,fontColor,border,fillColor|verticalAlign,horizontalAlign,wrapText,mergeAndCenter|insert,delete|clear,sortAndFilter|protectSheet|freeze,hyperlink\"}";
    }
}
