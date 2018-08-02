package io.keikai.tutorial.web;

import io.keikai.client.api.*;
import io.keikai.client.api.event.*;
import io.keikai.client.api.ui.AuxAction;
import io.keikai.tutorial.*;
import io.keikai.util.Maps;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/editor/*")
public class EditorServlet extends BaseServlet {

    public EditorServlet(){
        this.defaultXlsx = "welcome.xlsx";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        req.getRequestDispatcher("/editor.jsp").forward(req, resp);
    }

    /**
     * @return a custom toolbar config
     */
    @Override
    protected Settings getSettings() {
        Settings settings = Settings.DEFAULT_SETTINGS.clone();
        settings.set(Settings.Key.SPREADSHEET_CONFIG, Maps.toMap("toolbar", getToolBarConfig()));
        return settings;
    }
    /**
     * hide save button. <br/>
     * The default toolbar items value is: upload,newBook,saveBook,exportToFile|paste,cut,copy|fontName,fontSize,fontItalic,fontBold,fontUnderline,fontStrike,fontColor,border,fillColor|verticalAlign,horizontalAlign,wrapText,mergeAndCenter|numberFormat|insert,delete|clear,sortAndFilter|gridlines,protectSheet|freeze,hyperlink
     */
    protected String getToolBarConfig() {
        return "{\"items\": \"upload,newBook,exportToFile|" +
                "paste,cut,copy|" +
                "fontName,fontSize,fontItalic,fontBold,fontUnderline,fontStrike," +
                "fontColor,border,fillColor|" +
                "verticalAlign,horizontalAlign,wrapText,mergeAndCenter|" +
                "numberFormat|" +
                "insert,delete|" +
                "clear,sortAndFilter|" +
                "gridlines,protectSheet|" +
                "freeze,hyperlink\"}";
    }

}
