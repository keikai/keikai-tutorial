package io.keikai.tutorial.web;

import io.keikai.tutorial.Configuration;
import io.keikai.tutorial.app.BigData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/bigdata/*")
public class BigDataServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(request, resp);
        BigData bigData = new BigData(keikaiServerAddress);
        bigData.init(defaultFileFolder);
        // pass the anchor DOM element id for rendering keikai
        String keikaiJs = bigData.getJavaScriptURI("spreadsheet");
        // store as an attribute to be accessed by EL on a JSP
        request.setAttribute(Configuration.KEIKAI_JS, keikaiJs);
        request.getRequestDispatcher("/mybigdata/bigdata.jsp").forward(request, resp);
    }
}
