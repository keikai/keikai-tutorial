package io.keikai.tutorial;

import java.util.logging.*;

public class Configuration {
    public static final String DEFAULT_KEIKAI_SERVER = "http://localhost:8888";
    public static final String INTERNAL_FILE_FOLDER = "book";
    public static final String SPREADSHEETS = "spreadsheetList";
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
