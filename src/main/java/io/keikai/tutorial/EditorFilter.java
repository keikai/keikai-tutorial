package io.keikai.tutorial;

import io.keikai.client.api.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/editor.html")
public class EditorFilter implements Filter {
    private Spreadsheet spreadsheet;
    static public String SPREADSHEET = "spreadsheet"; //the key to store Spreadsheet component


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        initSpreadsheet(request);
    }
    
    private void initSpreadsheet(ServletRequest request) {
        spreadsheet = Keikai.newClient(Configuration.INTERNAL_KEIKAI_SERVER);
        String jsAPI = spreadsheet.getURI("spreadsheet"); // pass the anchor DOM element id for rendering keikai
        request.setAttribute("jsAPI", jsAPI);
        ((HttpServletRequest)request).getSession().setAttribute(SPREADSHEET, spreadsheet);
    }

    @Override
    public void destroy() {

    }
}
