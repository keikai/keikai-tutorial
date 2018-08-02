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

}
