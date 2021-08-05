package io.keikai.tutorial;

import io.keikai.api.*;
import io.keikai.api.model.Sheet;
import io.keikai.ui.Spreadsheet;
import io.keikai.ui.event.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;

public class TabsComposer extends SelectorComposer<Component> {
    @Wire
    private Spreadsheet spreadsheet;

    /**
     * hide/unhide children sheets
     * https://doc.keikai.io/dev-ref/handling_events/Sheet_Event
     */
    @Listen(Events.ON_SHEET_SELECT + "= #spreadsheet")
    public void showGroupSheet(SheetSelectEvent event){
        int nSheet = spreadsheet.getBook().getNumberOfSheets();
        String activeColor = event.getSheet().getInternalSheet().getTabColor();
        for (int i = 4 ; i < nSheet ; i++) {
            Sheet childSheet = spreadsheet.getBook().getSheetAt(i);
            if (childSheet.getInternalSheet().getTabColor().equals(activeColor)){
                if (childSheet.isHidden())
                    Ranges.range(childSheet).setSheetVisible(Range.SheetVisible.VISIBLE);
            }else{
                if (!childSheet.isHidden())
                    Ranges.range(childSheet).setSheetVisible(Range.SheetVisible.HIDDEN);
            }
        }
    }
}
