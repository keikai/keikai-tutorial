package io.keikai.tutorial;

import io.keikai.client.api.*;
import io.keikai.client.api.event.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

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
        fillExpense();
    }

    private void fillExpense() {
        List<Expense> list = SampleDataDao.queryByCategory();
        int row = 18;
        int col = 0;
        for (Expense expense : list) {
            Range categoryCell = spreadsheet.getRange(row, col);
            categoryCell.applyValue(expense.getCategory());
            Range quantityCell = spreadsheet.getRange(row, col + 1);
            quantityCell.applyValue(expense.getQuantity());
            Range subtotalCell = spreadsheet.getRange(row, col + 2);
            subtotalCell.applyValue(expense.getSubtotal());
            row++;
        }
    }

    private void addEventListeners() {
        ExceptionalConsumer<RangeEvent> listener = (event) -> {
            spreadsheet.loadActiveWorksheet().thenAccept(worksheet -> {
//            event.getRange().loadWorksheet().thenAccept(worksheet -> {
                if (isAddButtonClicked(worksheet.getIndex(), event.getRange())) {
                    spreadsheet.applyActiveWorksheet(1);
                    spreadsheet.setCurrentWorksheet(1); //apply active worksheet doesn't set current worksheet accordingly
                } else if (isDoneButtonClicked(worksheet.getIndex(), event.getRange())) {
                    addExpense();
                }
            });
        };
        spreadsheet.addEventListener(Events.ON_CELL_CLICK, listener::accept);
    }

    /**
     * read expense in a specific range
     */
    private void addExpense() {
        List<CompletableFuture> futures = new LinkedList<>();
        for (int rowIndex = STARTING_ROW ; rowIndex < STARTING_ROW + 4 ; rowIndex++) {
            CompletableFuture readFuture = readExpense(rowIndex, CATEGORY_COLUMN).thenAccept(expense -> {
                if (validate(expense)) {
                    SampleDataDao.insert(expense);
                }
            });
            futures.add(readFuture);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).thenRun(() -> {
            spreadsheet.setCurrentWorksheet(0) //apply active worksheet doesn't set current worksheet accordingly
                .thenRun(() -> {
                    fillExpense();
                });
            clearExpense();
            spreadsheet.applyActiveWorksheet(0);
        });
    }


    private boolean validate(Expense expense) {
        return expense.getCategory()!= null;
    }

    private void clearExpense() {
        System.out.println("clear");
    }

    private CompletableFuture<Expense> readExpense(int row, int col) {
        Expense expense = new Expense();
        CompletableFuture<RangeValue> categoryLoader = spreadsheet.getRange(row, col).loadValue();
        categoryLoader.thenAccept(rangeValue -> {
            expense.setCategory(rangeValue.getCellValue().getStringValue());
        });
        CompletableFuture<RangeValue> quantityLoader = spreadsheet.getRange(row, col + 1).loadValue();
        quantityLoader.thenAccept(rangeValue -> {
            expense.setQuantity(rangeValue.getCellValue().getDoubleValue().intValue());
        });
        CompletableFuture<RangeValue> subtotalLoader = spreadsheet.getRange(row, col + 3).loadValue();
        subtotalLoader.thenAccept(rangeValue -> {
            expense.setSubtotal(rangeValue.getCellValue().getDoubleValue().intValue());
        });
        return CompletableFuture.allOf(categoryLoader, subtotalLoader).thenApply(aVoid -> expense);
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
