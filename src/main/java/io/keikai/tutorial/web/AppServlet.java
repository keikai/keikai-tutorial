package io.keikai.tutorial.web;

import io.keikai.client.api.*;
import io.keikai.tutorial.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet("/app/*")
public class AppServlet extends BaseServlet {

    public AppServlet(){
        this.defaultXlsx = "app.xlsx";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        req.getRequestDispatcher("/app.jsp").forward(req, resp);
    }

}
