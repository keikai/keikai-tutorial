package io.keikai.tutorial;

import io.keikai.client.api.*;
import io.keikai.client.api.event.*;

/**
 * implement the application logic
 */
public class MyApp {
    private Spreadsheet spreadsheet;
    private int categoryColumn = 1;

    public MyApp(Spreadsheet spreadsheet){
        this.spreadsheet = spreadsheet;
        addEventListeners();
    }

    private void addEventListeners() {
        ExceptionalConsumer<RangeEvent> listener = (event) -> {
            if (isAddButtonClicked(event.getRange())){
                spreadsheet.applyActiveWorksheet(1);
            }else if (isDoneButtonClicked(event.getRange())){
                addExpense();
                spreadsheet.applyActiveWorksheet(0);
            }
        };
        spreadsheet.addEventListener(Events.ON_CELL_CLICK,  listener::accept);
    }

    private void addExpense() {

    }

    private boolean isAddButtonClicked(Range range){
        return (range.getRow() >= 24 && range.getRow() <= 27
            && range.getColumn() >= 3 && range.getColumn() <= 4);
    }

    private boolean isDoneButtonClicked(Range range){
        return (range.getRow() >= 8 && range.getRow() <= 10
            && range.getColumn() >= 3 && range.getColumn() <= 4);
    }
}
