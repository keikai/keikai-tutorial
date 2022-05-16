package support;

import io.keikai.api.*;
import io.keikai.api.model.Sheet;
import io.keikai.ui.*;
import io.keikai.ui.event.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.Checkbox;

public class MyController extends SelectorComposer<Component> {

    private MyDataService dataService = new MyDataService();
    @Wire("spreadsheet")
    private Spreadsheet spreadsheet;

    @Wire
    private Checkbox immediatelyBox;
    @Wire
    private Checkbox newSheetBox;

    private Range pastingRange;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //initialize components here
    }

    @Listen(Events.ON_CLIPBOARD_PASTE + " = spreadsheet")
    public void process(ClipboardPasteEvent event) {
        pastingRange = Ranges.range(spreadsheet.getSelectedSheet(), event.getArea());
        if (immediatelyBox.isChecked()) {
            process();
        }
    }

    private Sheet getTargetSheet() {
        if (newSheetBox.isChecked()) {
            return Ranges.range(spreadsheet.getBook()).createSheet("result " + spreadsheet.getBook().getNumberOfSheets());
        }else{
            return pastingRange.getSheet();
        }
    }

    @Listen(org.zkoss.zk.ui.event.Events.ON_CLICK + "= button")
    public void process() {
        Sheet targetSheet = getTargetSheet();
        pastingRange.zOrderStream().forEach(range -> {
            Range targetCell = Ranges.range(targetSheet, range.getRow(), range.getColumn());
            String result = dataService.query(range.getCellData().getStringValue());
            targetCell.setCellValue(result);
        });
    }
}
