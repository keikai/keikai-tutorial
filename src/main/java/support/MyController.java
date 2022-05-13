package support;

import io.keikai.api.*;
import io.keikai.ui.*;
import io.keikai.ui.event.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;

public class MyController extends SelectorComposer<Component> {

	private MyDataService dataService = new MyDataService();
	@Wire("spreadsheet")
	private Spreadsheet spreadsheet;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		//initialize components here
	}

	@Listen(Events.ON_CLIPBOARD_PASTE + " = spreadsheet")
	public void process(ClipboardPasteEvent event){
		Range pastingRange = Ranges.range(spreadsheet.getSelectedSheet(), event.getArea());

		pastingRange.zOrderStream().forEach(range->{
			String result = dataService.query(range.getCellData().getStringValue());
			range.setCellValue(result);
		});
	}
}
