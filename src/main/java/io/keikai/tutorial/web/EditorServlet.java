package io.keikai.tutorial.web;

import io.keikai.tutorial.Configuration;
import io.keikai.tutorial.app.MyEditor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/editor/*")
public class EditorServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(request, resp);
        MyEditor myEditor = new MyEditor(keikaiServerAddress);
        myEditor.init(defaultFileFolder);
        // pass the anchor DOM element id for rendering keikai
        String keikaiJs = myEditor.getJavaScriptURI("spreadsheet");
        // store as an attribute to be accessed by EL on a JSP
        request.setAttribute(Configuration.KEIKAI_JS, keikaiJs);
        request.getRequestDispatcher("/myeditor/editor.jsp").forward(request, resp);
    }
}
