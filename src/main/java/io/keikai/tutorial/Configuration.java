package io.keikai.tutorial;

import java.io.File;
import java.util.logging.*;

public class Configuration {
    public static final String DEFAULT_KEIKAI_SERVER = "http://localhost:8888";
    public static final String SPREADSHEET = "spreadsheet"; //the key to store Spreadsheet component
    public static final String DEFAULT_FILE_FOLDER = "export";
    public static final String INTERNAL_FILE_FOLDER = "book";
    public static final String KEIKAI_JS = "keikaiJs";


    static public void enableSocketIOLog() {
        Logger log = java.util.logging.Logger.getLogger("");
        log.setLevel(Level.FINER);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
    }
}
