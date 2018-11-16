package io.keikai.tutorial.app;

import io.keikai.client.api.*;
import io.keikai.client.api.event.*;
import io.keikai.client.api.ui.UiActivity;
import io.keikai.tutorial.persistence.*;

import java.io.*;
import java.util.*;

/**
 * implement the application logic
 */
public class BigData {
    private Spreadsheet spreadsheet;
    private String defaultXlsx = "bigData.xlsx";
    private File defaultFileFolder;
    static private HashMap<String, String> buttonFileMap = new HashMap<>();

    static{
        buttonFileMap.put("3millions", "formula-120k.xlsx");
        buttonFileMap.put("filter", "flight-10kRow.xlsx");
    }

    public BigData(String keikaiServerAddress) {
        spreadsheet = Keikai.newClient(keikaiServerAddress);
        // close spreadsheet Java client when a browser disconnect to keikai server to avoid memory leak
        spreadsheet.setUiActivityCallback(new UiActivity() {
            public void onConnect() {
            }

            public void onDisconnect() {
                spreadsheet.close();
            }
        });
    }

    /**
     * pass the anchor DOM element ID for rendering a Keikai spreadsheet
     * @param elementId
     * @return
     */
    public String getJavaScriptURI(String elementId) {
        return spreadsheet.getURI(elementId);
    }

    public void init(File defaultFileFolder){
        try {
            this.defaultFileFolder = defaultFileFolder;
            spreadsheet.importAndReplace(defaultXlsx, new File(defaultFileFolder, defaultXlsx));
            addEventListeners();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addEventListeners() {
        buttonFileMap.forEach((buttonName, fileName) -> {
            spreadsheet.getWorksheet().getButton(buttonName).addAction(buttonShapeMouseEvent -> {
                spreadsheet.importAndReplace(defaultXlsx, new File(defaultFileFolder, fileName));
            });
        });
    }
}
