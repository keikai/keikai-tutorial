package io.keikai.tutorial.web;

import io.keikai.client.api.Spreadsheet;
import io.keikai.tutorial.Configuration;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.List;

@WebListener
public class SpreadsheetCloseListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        List<Spreadsheet> list = (List<Spreadsheet>)event.getSession().getAttribute(Configuration.SPREADSHEETS);
        for (Spreadsheet spreadsheet: list){
            spreadsheet.close();
        }

    }
}
