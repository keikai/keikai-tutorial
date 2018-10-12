package io.keikai.tutorial;

import java.io.File;
import java.util.logging.*;

public class Configuration {
    public static final String DEFAULT_KEIKAI_SERVER = "http://localhost:8888";
    public static final String INTERNAL_FILE_FOLDER = "book";
    public static final String KEIKAI_JS = "keikaiJs";

    /**
     * http://hsqldb.org/doc/guide/dbproperties-chapt.html
     * shutdown=true, Automatic Shutdown, shut down the database when the last connection is closed
     */
    public static final String HSQLDB_CONNECTION_STRING = "jdbc:hsqldb:file:database/tutorial;shutdown=true";
    public static final String JDBC_DRIVER = "org.hsqldb.jdbc.JDBCDriver";


    static public void enableSocketIOLog() {
        Logger log = java.util.logging.Logger.getLogger("");
        log.setLevel(Level.FINER);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
    }

    static public String getDefaultFileFolder(){
        return "/WEB-INF" + File.separator + Configuration.INTERNAL_FILE_FOLDER + File.separator;
    }
}
