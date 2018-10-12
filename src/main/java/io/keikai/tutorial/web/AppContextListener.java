package io.keikai.tutorial.web;

import io.keikai.tutorial.Configuration;
import io.keikai.tutorial.persistence.*;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.io.*;
import java.util.*;

@WebListener
public class AppContextListener implements ServletContextListener {

   static private List<File> formList = new LinkedList<>();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        SampleDataDao.initDatabase();
        WorkflowDao.initDatabase();
        loadFormList(servletContextEvent.getServletContext());
    }

    static private void loadFormList(ServletContext context) {
        formList.add(new File(context.getRealPath(Configuration.getDefaultFileFolder() + "Leave Application.xlsx")));
        formList.add(new File(context.getRealPath(Configuration.getDefaultFileFolder() + "Business Trip.xlsx")));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    static public List<File> getFormList() {
        return formList;
    }
}