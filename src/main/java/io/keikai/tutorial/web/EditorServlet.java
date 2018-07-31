package io.keikai.tutorial.web;

import io.keikai.client.api.*;
import io.keikai.client.api.event.*;
import io.keikai.client.api.ui.AuxAction;
import io.keikai.tutorial.*;

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
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
//        Configuration.enableSocketIOLog();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initSpreadsheet(req);
        try {
            spreadsheet.imports(defaultXlsx, defaultFile);
        } catch (DuplicateNameException e) {
            throw new IOException(e);
        } catch (AbortedException e) {
            throw new IOException(e);
        }
        req.getRequestDispatcher("/editor.jsp").forward(req, resp);
    }

}
