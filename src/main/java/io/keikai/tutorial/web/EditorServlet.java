package io.keikai.tutorial.web;

import io.keikai.client.api.AbortedException;
import io.keikai.tutorial.*;
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
        // pass the anchor DOM element id for rendering keikai
        String keikaiJs = myEditor.getJavaScriptURI("spreadsheet");
        // store as an attribute to be accessed by EL on a JSP
        request.setAttribute(Configuration.KEIKAI_JS, keikaiJs);
        try {
            myEditor.init(defaultFileFolder);
        } catch (AbortedException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("/myeditor/editor.jsp").forward(request, resp);
    }
}
