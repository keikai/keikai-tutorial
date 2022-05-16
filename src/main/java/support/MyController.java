package support;

import io.keikai.api.*;
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

    @Wire("checkbox")
    private Checkbox immediatelyBox;

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

    @Listen(org.zkoss.zk.ui.event.Events.ON_CLICK + "= button")
    public void process() {
        pastingRange.zOrderStream().forEach(range -> {
            String result = dataService.query(range.getCellData().getStringValue());
            range.setCellValue(result);
        });
    }
}
