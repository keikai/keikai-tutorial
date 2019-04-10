package io.keikai.tutorial.app;

import io.keikai.client.api.*;
import io.keikai.client.api.event.*;
import io.keikai.client.api.ui.UIActivity;
import io.keikai.tutorial.persistence.*;

import java.io.File;
import java.util.*;

/**
 * implement the application logic
 */
public class MyApp {
    private Spreadsheet spreadsheet;
    private String defaultXlsx = "app.xlsx";
    private static final int CATEGORY_COLUMN = 1;
    private static final int STARTING_ROW = 3; //the row index that a user should start to input the expense record

    /**
     * pass the anchor DOM element ID for rendering a Keikai spreadsheet
     * @param elementId
     * @return
     */
    public String getJavaScriptURI(String elementId) {
        return spreadsheet.getURI(elementId);
    }

    public MyApp(String keikaiServerAddress) {
        spreadsheet = Keikai.newClient(keikaiServerAddress);
        // close spreadsheet Java client when a browser disconnect to keikai server to avoid memory leak
        spreadsheet.setUIActivityCallback(new UIActivity() {
            public void onConnect() {
            }

            public void onDisconnect() {
                spreadsheet.close();
            }
        });
    }

    public void init(File defaultFileFolder){
        try {
            spreadsheet.importAndReplace(defaultXlsx, new File(defaultFileFolder, defaultXlsx));
            addEventListeners();
            loadExpenseToSheet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load expense list from the database to a sheet
     */
    private void loadExpenseToSheet() {
        List<Expense> list = SampleDataDao.queryByCategory();
        int row = 18;
        int col = 0;
        String bookName = spreadsheet.getBookName();
        for (Expense expense : list) {
            Range quantityCell = spreadsheet.getRange(bookName, 0, row, col + 1);
            quantityCell.setValue(expense.getQuantity());
            Range subtotalCell = spreadsheet.getRange(bookName, 0, row, col + 2);
            subtotalCell.setValue(expense.getSubtotal());
            row++;
        }
    }

    private void addEventListeners() {
        RangeEventListener rangeEventListener = new RangeEventListener() {
            @Override
            public void onEvent(RangeEvent rangeEvent) throws Exception {
                if (isAddButtonClicked(rangeEvent.getWorksheet().getIndex(), rangeEvent.getRange())) {
                    spreadsheet.setActiveWorksheet(1); //switch to expense sheet
                } else if (isDoneButtonClicked(rangeEvent.getWorksheet().getIndex(), rangeEvent.getRange())) {
                    saveExpense();
                    spreadsheet.setActiveWorksheet(0);
                    loadExpenseToSheet();
                }
            }
        };
        spreadsheet.addEventListener(Events.ON_CELL_CLICK, rangeEventListener);
    }

    /**
     * save expense list in a specific range into the database
     */
    private void saveExpense() {
        int nExpense = 0; // number of user-input expense
        for (int rowIndex = STARTING_ROW; rowIndex < STARTING_ROW + 4; rowIndex++) {
            Expense expense = readExpense(rowIndex, CATEGORY_COLUMN);
            if (!validate(expense)) {
                break;
            }
            SampleDataDao.insert(expense);
            nExpense++;
        }
        clearInputExpense(nExpense);
    }


    private boolean validate(Expense expense) {
        return expense.getCategory() != null
                && expense.getQuantity() > 0
                && expense.getSubtotal() > 0;
    }

    private void clearInputExpense(int nExpense) {
        if (nExpense > 0) {
            spreadsheet.getRange(STARTING_ROW, 0, nExpense, 4).clearContents();
        }
    }

    /**
     * Notice blank cells.
     */
    private Expense readExpense(int row, int col) {
        Expense expense = new Expense();
        List cellValues = spreadsheet.getRange(row, col, 1, 4).getValues();
        Optional.ofNullable(cellValues.get(0)).ifPresent( cellValue ->{
            if (!cellValue.toString().isEmpty()) {
                expense.setCategory(cellValue.toString());
            }
        });
        Optional.ofNullable(cellValues.get(1)).ifPresent( cellValue ->{
            if (!cellValue.toString().isEmpty()) {
                expense.setQuantity(Integer.parseInt(cellValue.toString()));
            }
        });
        Optional.ofNullable(cellValues.get(3)).ifPresent( cellValue ->{
            if (!cellValue.toString().isEmpty()) {
                expense.setSubtotal(Integer.parseInt(cellValue.toString()));
            }
        });
        return expense;
    }

    private boolean isAddButtonClicked(int sheetIndex, Range range) {
        return sheetIndex == 0
                && range.getRow() >= 24 && range.getRow() <= 27
                && range.getColumn() >= 2 && range.getColumn() <= 3;
    }

    private boolean isDoneButtonClicked(int sheetIndex, Range range) {
        return sheetIndex == 1
                && range.getRow() >= 8 && range.getRow() <= 10
                && range.getColumn() >= 3 && range.getColumn() <= 4;
    }
}
