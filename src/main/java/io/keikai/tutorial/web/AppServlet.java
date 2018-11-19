package io.keikai.tutorial.web;

import io.keikai.tutorial.Configuration;
import io.keikai.tutorial.app.MyApp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/app/*")
public class AppServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(request, resp);
        MyApp myApp = new MyApp(keikaiServerAddress);
        myApp.init(defaultFileFolder);
        // pass the anchor DOM element id for rendering keikai
        String keikaiJs = myApp.getJavaScriptURI("spreadsheet");
        // store as an attribute to be accessed by EL on a JSP
        request.setAttribute(Configuration.KEIKAI_JS, keikaiJs);
        request.getRequestDispatcher("/myapp/app.jsp").forward(request, resp);
    }
}
