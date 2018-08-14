package io.keikai.tutorial.web;

import io.keikai.client.api.AbortedException;
import io.keikai.tutorial.*;
import io.keikai.tutorial.app.MyApp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/app/*")
public class AppServlet extends BaseServlet {

    public AppServlet(){
        this.defaultXlsx = "app.xlsx";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(request, resp);
        MyApp myApp = new MyApp(keikaiServerAddress);
        // pass the anchor DOM element id for rendering keikai
        String keikaiJs = myApp.getJavaScriptURI("spreadsheet");
        // store as an attribute to be accessed by EL on a JSP
        request.setAttribute(Configuration.KEIKAI_JS, keikaiJs);
        try {
            myApp.init(defaultXlsx, defaultFile);
        } catch (AbortedException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("/app.jsp").forward(request, resp);
    }
}
