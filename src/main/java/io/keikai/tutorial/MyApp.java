package io.keikai.tutorial;

import io.keikai.client.api.*;
import io.keikai.client.api.event.*;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * implement the application logic
 */
public class MyApp {
    private Spreadsheet spreadsheet;
    private int categoryColumn = 1;

    public MyApp(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;
        addEventListeners();
        try {
            fillExpense();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillExpense() throws SQLException {
        List<Expense> list = SampleDataDao.queryAll();
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


    private void addExpense() {
        int startingRow = 3;
        readExpense(startingRow, 1).thenAccept(expense -> {
            System.out.println(">>" + expense);
            SampleDataDao.insert(expense);
        }).thenRun(() -> {
            spreadsheet.applyActiveWorksheet(0);
        });
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
        return CompletableFuture.allOf(categoryLoader, subtotalLoader).thenApply(aVoid -> {
            System.out.println(expense);
            return expense;
        });
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
