package io.keikai.tutorial.app;

import io.keikai.client.api.*;
import io.keikai.client.api.ctrl.Button;
import io.keikai.client.api.event.*;
import io.keikai.client.api.ui.*;
import io.keikai.tutorial.persistence.*;
import io.keikai.tutorial.web.AppContextListener;
import io.keikai.util.DateUtil;
import org.slf4j.*;

import java.io.*;
import java.time.*;
import java.util.*;

/**
 * implement the workflow logic
 *
 * @author Hawk Chen
 */
public class MyWorkflow {
    private static final Logger logger = LoggerFactory.getLogger(MyWorkflow.class);

    static private final String ROLE_EMPLOYEE = "Employee";

    static private final String BUTTON_SUBMIT = "submit";
    static private final String BUTTON_CANCEL = "cancel";
    static private final String BUTTON_APPROVE = "approve";
    static private final String BUTTON_REJECT = "reject";
    private static final String BUTTON_ENTER = "enter";
    private static final String BUTTON_LEAVE = "leave";
    public static final String XLSX = ".xlsx";

    static private String SHEET_MAIN = "main";
    static private String SHEET_FORM = "form list";
    static private String SHEET_SUBMISSION = "submission list";

    static private final int STARTING_COLUMN = 2;
    static private final int STARTING_ROW = 5;
    static private final String ROLE_CELL = "E6";

    private Spreadsheet spreadsheet;
    private boolean submissionPopulated = false;
    private String role;
    private String entryBookName;
    private File entryFile;
    private Submission submissionToReview = null;


    public MyWorkflow(String keikaiServerAddress) {
        spreadsheet = Keikai.newClient(keikaiServerAddress);
        // close spreadsheet Java client when a browser disconnects to keikai server to avoid memory leak
        spreadsheet.setUiActivityCallback(new UiActivity() {
            public void onConnect() {
            }

            public void onDisconnect() {
                spreadsheet.close();
            }
        });
        spreadsheet.addExceptionHandler(throwable -> {
            logger.error("Oops! something wrong in Spreadsheet", throwable);
        });
    }

    public String getJavaScriptURI(String elementId) {
        return spreadsheet.getURI(elementId);
    }

    public void init(String bookName, File xlsxFile) {
        this.entryBookName = bookName;
        this.entryFile = xlsxFile;
        start();
        navigateTo(SHEET_MAIN);
    }

    /**
     * start this workflow
     */
    private void start() {
        try {
            spreadsheet.clearEventListeners();
            spreadsheet.importAndReplace(this.entryBookName, this.entryFile);
            submissionPopulated = false;
            addEnterLeaveListeners();
        } catch (FileNotFoundException | AbortedException e) {
            logger.error("An error happens at starting a workflow: " + e);
        }
    }

    private void addEnterLeaveListeners() {
        spreadsheet.getWorksheet(SHEET_MAIN).getButton(BUTTON_ENTER).addAction(shapeMouseEvent -> {
            this.role = spreadsheet.getRange(ROLE_CELL).getValue().toString();
            navigateByRole();
            showList();
        });
        spreadsheet.getWorksheet(SHEET_FORM).getButton(BUTTON_LEAVE).addAction(shapeMouseEvent -> {
            leave();
        });
        spreadsheet.getWorksheet(SHEET_SUBMISSION).getButton(BUTTON_LEAVE).addAction(shapeMouseEvent -> {
            leave();
        });
    }

    private void showForm(File formFile) throws FileNotFoundException, AbortedException {
        spreadsheet.clearEventListeners();
        spreadsheet.importAndReplace(formFile.getName(), formFile);
        setupButtonsUponRole(spreadsheet.getWorksheet());
    }


    private void showSubmittedForm(Submission s) throws AbortedException {
        spreadsheet.clearEventListeners();
        spreadsheet.importAndReplace(s.getFormName(), new ByteArrayInputStream(s.getForm().toByteArray()));
        setupButtonsUponRole(spreadsheet.getWorksheet());
        spreadsheet.getWorksheet().protect(new SheetProtection.Builder().build());
    }

    private void setupButtonsUponRole(Worksheet worksheet) {
        Button submit = worksheet.getButton(BUTTON_SUBMIT);
        Button cancel = worksheet.getButton(BUTTON_CANCEL);
        Button approve = worksheet.getButton(BUTTON_APPROVE);
        Button reject = worksheet.getButton(BUTTON_REJECT);
        if (role.equals(ROLE_EMPLOYEE)) {
            submit.setVisible(true);
            cancel.setVisible(true);
            approve.setVisible(false);
            reject.setVisible(false);
            cancel.addAction(shapeMouseEvent -> {
                navigateTo(SHEET_FORM);
            });
            submit.addAction(shapeMouseEvent -> {
                submit();
                navigateTo(SHEET_FORM);
            });
        } else {
            submit.setVisible(false);
            cancel.setVisible(false);
            approve.setVisible(true);
            reject.setVisible(true);
            approve.addAction(shapeMouseEvent -> {
                approve();
                navigateTo(SHEET_SUBMISSION);
            });
            reject.addAction(shapeMouseEvent -> {
                reject();
                navigateTo(SHEET_SUBMISSION);
            });
        }
    }

    private void reject() {
        submissionToReview.setLastUpdate(LocalDateTime.now());
        submissionToReview.setState(Submission.State.REJECTED);
        WorkflowDao.update(submissionToReview);
        submissionToReview = null;
    }

    private void approve() {
        submissionToReview.setLastUpdate(LocalDateTime.now());
        submissionToReview.setState(Submission.State.APPROVED);
        WorkflowDao.update(submissionToReview);
        submissionToReview = null;
    }

    /**
     * submit a form and create a submission in the database
     */
    private void submit() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        spreadsheet.export(spreadsheet.getBookName(), outputStream);
        Submission submission = new Submission();
        submission.setForm(outputStream);
        submission.setFormName(spreadsheet.getBookName().replace(XLSX, ""));
        submission.setOwner(this.role);
        WorkflowDao.insert(submission);
    }

    /**
     * show form or submission list according to the role
     */
    private void showList() {
        Worksheet sheet = spreadsheet.getWorksheet();
        if (role.equals(ROLE_EMPLOYEE)) {
            // need to unprotect a sheet before populating a list into cells
            if (sheet.isProtected()) {
                sheet.unprotect("");
            }
            showFormList();
            addFormSelectionListener();
            sheet.protect(new SheetProtection.Builder().setPassword("")
                    .setAllowSelectLockedCells(true)
                    .setAllowSelectUnlockedCells(true)
                    .build());
        } else { //supervisor
            if (!submissionPopulated) {
                if (sheet.isProtected()) {
                    sheet.unprotect("");
                }
                showSubmissionList();
                //allow filter and sorting
                sheet.protect(new SheetProtection.Builder().setPassword("")
                        .setAllowFiltering(true)
                        .setAllowSorting(true)
                        .setAllowSelectLockedCells(true)
                        .setAllowSelectUnlockedCells(true)
                        .build());
            }
        }
    }

    private void leave() {
        role = null;
        navigateTo(SHEET_MAIN);
    }

    private void addFormSelectionListener() {
        RangeEventListener formSelectionListener = new RangeEventListener() {

            @Override
            public void onEvent(RangeEvent rangeEvent) throws Exception {
                int fileIndex = rangeEvent.getRow() - STARTING_ROW;
                if (spreadsheet.getWorksheet().getName().equals(SHEET_FORM)
                        && rangeEvent.getColumn() == 2
                        && rangeEvent.getRow() >= STARTING_ROW
                        && fileIndex < AppContextListener.getFormList().size()) {
                    File form = AppContextListener.getFormList().get(fileIndex);
                    showForm(form);
                }
            }
        };
        spreadsheet.addEventListener(Events.ON_CELL_CLICK, formSelectionListener);
    }

    private void showFormList() {
        int row = STARTING_ROW;
        for (File file : AppContextListener.getFormList()) {
            spreadsheet.getRange(row, STARTING_COLUMN).setValue(file.getName().replace(XLSX, ""));
            row++;
        }
    }

    /**
     * populate submissions into cells from the database
     */
    private void showSubmissionList() {
        List<Submission> submissionList = WorkflowDao.queryAll();
        //create table rows first
        //the table contains 2 rows initially for copying date format when inserting rows
        for (int r = STARTING_ROW + 1; r < (STARTING_ROW + 1) + submissionList.size() - 2; r++) {
            spreadsheet.getRange(r, 0).getEntireRow().insert(Range.InsertShiftDirection.ShiftDown, Range.InsertFormatOrigin.LeftOrAbove);
        }

        int row = STARTING_ROW;
        for (Submission s : submissionList) {
            spreadsheet.getRange(row, STARTING_COLUMN).setValue(s.getId());
            spreadsheet.getRange(row, STARTING_COLUMN + 1).setValue(s.getFormName());
            spreadsheet.getRange(row, STARTING_COLUMN + 2).setValue(s.getOwner());
            spreadsheet.getRange(row, STARTING_COLUMN + 3).setValue(s.getState());
            spreadsheet.getRange(row, STARTING_COLUMN + 4).setValue(DateUtil.getExcelDate(Date.from(s.getLastUpdate().atZone(ZoneId.systemDefault()).toInstant())));
            row++;
        }
        submissionPopulated = true;

        RangeEventListener submissionSelectionListener = new RangeEventListener() {

            @Override
            public void onEvent(RangeEvent rangeEvent) throws Exception {
                if (!rangeEvent.getWorksheet().getName().equals(SHEET_SUBMISSION)
                        || (rangeEvent.getColumn() < 2 || rangeEvent.getColumn() > 6)) { //inside table columns
                    return;
                }
                Range idCell = spreadsheet.getRange(rangeEvent.getRange().getRow(), STARTING_COLUMN);
                int id = idCell.getRangeValue().getCellValue().getDoubleValue().intValue();
                for (Submission s : submissionList) {
                    if (s.getId() == id
                            && s.getState() == Submission.State.WAITING) {
                        submissionToReview = s;
                        showSubmittedForm(s);
                        submissionPopulated = false;
                        break;
                    }
                }
            }
        };
        spreadsheet.addEventListener(Events.ON_CELL_CLICK, submissionSelectionListener);
    }

    private void navigateByRole() {
        if (role.equals(ROLE_EMPLOYEE)) {
            spreadsheet.setActiveWorksheet(SHEET_FORM);
        } else {
            spreadsheet.setActiveWorksheet(SHEET_SUBMISSION);
        }
    }

    /**
     * show the target sheet, if it's necessary, import the entry file.
     */
    private Worksheet navigateTo(String sheetName) {
        if (spreadsheet.getBookName().equals(entryBookName)) {
            Worksheet currentSheet = spreadsheet.getWorksheet();
            if (!currentSheet.getName().equals(sheetName)) {
                spreadsheet.setActiveWorksheet(sheetName);
            }
        } else {
            start();
            navigateByRole();
            showList();
        }
        return spreadsheet.getWorksheet();

    }
}