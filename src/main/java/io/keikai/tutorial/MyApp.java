package io.keikai.tutorial;

import io.keikai.client.api.*;
import io.keikai.client.api.event.*;
import io.keikai.client.api.ui.UiActivity;

import java.io.*;
import java.util.*;

/**
 * implement the application logic
 */
public class MyApp {
    private Spreadsheet spreadsheet;
    private static final int CATEGORY_COLUMN = 1;
    private static final int STARTING_ROW = 3; //the row index that a user should start to input the expense record
    private int nExpense = 0; // number of user-input expense

    public MyApp(String keikaiServerAddress) {
        spreadsheet = Keikai.newClient(keikaiServerAddress);
        // close spreadsheet Java client when a browser disconnect to keikai server to avoid memory leak
        spreadsheet.setUiActivityCallback(new UiActivity() {
            public void onConnect() {
            }

            public void onDisconnect() {
                spreadsheet.close();
            }
        });
    }

    public String getJavaScriptURI(String elementId) {
        return spreadsheet.getURI(elementId);
    }

    public void init(String bookName, File xlsxFile) throws FileNotFoundException, AbortedException {
        spreadsheet.importAndReplace(bookName, xlsxFile);
        addEventListeners();
        loadExpenseToSheet();
    }

    /**
     * Load expense list from the database to a sheet
     */
    private void loadExpenseToSheet() {
        List<Expense> list = SampleDataDao.queryByCategory();
        int row = 18;
        int col = 0;
        for (Expense expense : list) {
            Range categoryCell = spreadsheet.getRange(row, col);
            categoryCell.setValue(expense.getCategory());
            Range quantityCell = spreadsheet.getRange(row, col + 1);
            quantityCell.setValue(expense.getQuantity());
            Range subtotalCell = spreadsheet.getRange(row, col + 2);
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
        for (int rowIndex = STARTING_ROW; rowIndex < STARTING_ROW + 4; rowIndex++) {
            Expense expense = readExpense(rowIndex, CATEGORY_COLUMN);
            if (!validate(expense)) {
                break;
            }
            SampleDataDao.insert(expense);
            nExpense++;
        }
        clearInputExpense();
    }


    private boolean validate(Expense expense) {
        return expense.getCategory() != null
                && expense.getQuantity() > 0
                && expense.getSubtotal() > 0;
    }

    private void clearInputExpense() {
        spreadsheet.getRange(STARTING_ROW, 0, nExpense, 4).clearContents();
    }

    private Expense readExpense(int row, int col) {
        Expense expense = new Expense();
        getCellValue(spreadsheet.getRange(row, col)).ifPresent(cellValue -> {
            expense.setCategory(cellValue.getStringValue());
        });
        getCellValue(spreadsheet.getRange(row, col + 1)).ifPresent(cellValue -> {
            expense.setQuantity(cellValue.getDoubleValue().intValue());
        });
        getCellValue(spreadsheet.getRange(row, col + 3)).ifPresent(cellValue -> {
            expense.setSubtotal(cellValue.getDoubleValue().intValue());
        });
        return expense;
    }

    private Optional<CellValue> getCellValue(Range range){
        CellValue value = range.getRangeValue().getCellValue();
        if (range.getValue() == null){
            return Optional.empty();
        }else{
            return Optional.of(value);
        }
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
