package io.keikai.tutorial;

import io.keikai.client.api.*;
import io.keikai.client.api.event.*;

import java.util.*;

/**
 * implement the application logic
 */
public class MyApp {
    private Spreadsheet spreadsheet;
    private final int CATEGORY_COLUMN = 1;
    private final int STARTING_ROW = 3; //the row index that a user should start to input the expense record

    public MyApp(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
        addEventListeners();
        loadExpenseToSheet();
    }

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
        ExceptionalConsumer<RangeEvent> listener = (event) -> {
            if (isAddButtonClicked(event.getWorksheet().getIndex(), event.getRange())) {
                spreadsheet.setActiveWorksheet(1); //switch to expense sheet
            } else if (isDoneButtonClicked(event.getWorksheet().getIndex(), event.getRange())) {
                addExpense();
                spreadsheet.setActiveWorksheet(0);
                loadExpenseToSheet();
            }
        };
        spreadsheet.addEventListener(Events.ON_CELL_CLICK, listener::accept);
    }

    /**
     * save expense records in a specific range
     */
    private void addExpense() {
        for (int rowIndex = STARTING_ROW; rowIndex < STARTING_ROW + 4; rowIndex++) {
            Expense expense = readExpense(rowIndex, CATEGORY_COLUMN);
            if (validate(expense)) {
                SampleDataDao.insert(expense);
            } else {
                break;
            }
        }
        clearExpense();
    }


    private boolean validate(Expense expense) {
        return expense.getCategory() != null;
    }

    private void clearExpense() {
        System.out.println("clear");
    }

    private Expense readExpense(int row, int col) {
        Expense expense = new Expense();
        expense.setCategory(spreadsheet.getRange(row, col).getValue());
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
