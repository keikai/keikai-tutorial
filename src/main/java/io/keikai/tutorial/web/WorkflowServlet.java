package io.keikai.tutorial.web;

import io.keikai.client.api.AbortedException;
import io.keikai.tutorial.Configuration;
import io.keikai.tutorial.app.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/workflow/*")
public class WorkflowServlet extends BaseServlet {

    public WorkflowServlet(){
        this.defaultXlsx = "workflow.xlsx";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(request, resp);
        MyWorkflow myWorkflow = new MyWorkflow(keikaiServerAddress);
        myWorkflow.init(defaultXlsx, defaultFile);

        // pass the anchor DOM element id for rendering keikai
        String keikaiJsURI = myWorkflow.getJavaScriptURI("spreadsheet");
        // store as an attribute to be accessed by EL on a JSP
        request.setAttribute(Configuration.KEIKAI_JS, keikaiJsURI);
        request.getRequestDispatcher("/myworkflow/workflow.jsp").forward(request, resp);
    }
}
