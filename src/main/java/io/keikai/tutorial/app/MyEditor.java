package io.keikai.tutorial.app;

import io.keikai.client.api.*;
import io.keikai.client.api.ui.UiActivity;
import io.keikai.util.Maps;

import java.io.*;

/**
 * implement the application logic
 */
public class MyEditor {
    private Spreadsheet spreadsheet;
    private String defaultXlsx = "welcome.xlsx";

    public MyEditor(String keikaiServerAddress) {
        // specify a custom toolbar settings
        spreadsheet = Keikai.newClient(keikaiServerAddress, getSettings());
        // close spreadsheet Java client when a browser disconnect to keikai server to avoid memory leak
        spreadsheet.setUiActivityCallback(new UiActivity() {
            public void onConnect() {
            }

            public void onDisconnect() {
                spreadsheet.close();
            }
        });
    }

    public String getJavaScriptURI(String elementId) {
        return spreadsheet.getURI(elementId);
    }

    public void init(File defaultFileFolder){
        try {
            spreadsheet.importAndReplace(defaultXlsx, new File(defaultFileFolder, defaultXlsx));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * hide save button. <br/>
     * The default toolbar items value is: upload,newBook,saveBook,exportToFile|paste,cut,copy|fontName,fontSize,fontItalic,fontBold,fontUnderline,fontStrike,fontColor,border,fillColor|verticalAlign,horizontalAlign,wrapText,mergeAndCenter|numberFormat|insert,delete|clear,sortAndFilter|gridlines,protectSheet|freeze,hyperlink
     *
     * @return a custom toolbar config
     */
    protected Settings getSettings() {
        Settings settings = Settings.DEFAULT_SETTINGS.clone();
        String customToolbarConfig = "{\"items\": \"upload,newBook,exportToFile|" +
                "paste,cut,copy|" +
                "fontName,fontSize,fontItalic,fontBold,fontUnderline,fontStrike," +
                "fontColor,border,fillColor|" +
                "verticalAlign,horizontalAlign,wrapText,mergeAndCenter|" +
                "numberFormat|" +
                "insert,delete|" +
                "clear,sortAndFilter|" +
                "gridlines,protectSheet|" +
                "freeze,hyperlink\"}";
        settings.set(Settings.Key.SPREADSHEET_CONFIG, Maps.toMap("toolbar", customToolbarConfig));
        return settings;
    }
}