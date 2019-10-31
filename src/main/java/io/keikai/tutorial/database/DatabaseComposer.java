package io.keikai.tutorial.database;

import io.keikai.api.*;
import io.keikai.api.model.*;
import io.keikai.ui.*;
import io.keikai.ui.event.StopEditingEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.util.Map;

public class DatabaseComposer extends SelectorComposer<Component> {

	private MyDataService dataService = new MyDataService();
	@Wire
	private Spreadsheet ss;
	@Wire
	private Grid tableGrid;
	@Wire("#load")
	private Button loadButton;

	// store those Trade modified by a user. key is trade ID
	private Map<Integer, Trade> modifiedTrades;
	//column index
	public static int ID = 0;
	public static int TYPE = 1;
	public static int SALESPERSON = 2;
	public static int SALES = 3;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		reload();
		Clients.showNotification("the data is loaded from the db");
	}
	
	private void load(Trade trade, int row) {
		Sheet sheet = ss.getSelectedSheet();
		Ranges.range(sheet, row, ID).setCellValue(trade.getId());
		Ranges.range(sheet, row, TYPE).setCellValue(trade.getType());
		Ranges.range(sheet, row, SALESPERSON).setCellValue(trade.getSalesPerson());
		Ranges.range(sheet, row, SALES).setCellValue(trade.getSale());
	}
	
	/**
	 * extract data in cells into a {@link Trade}
	 * @param row
	 */
	private Trade extract(int row ){
		Sheet sheet = ss.getSelectedSheet();
		Trade trade = new Trade(extractInt(Ranges.range(sheet, row, ID)));
		trade.setType(Ranges.range(sheet, row, TYPE).getCellEditText());
		trade.setSalesPerson(Ranges.range(sheet, row, SALESPERSON).getCellEditText());
		trade.setSale(extractInt(Ranges.range(sheet, row, SALES)));
		return trade;
	}

	private int extractInt(Range cell){
		CellData cellData = cell.getCellData();
		return cellData.getDoubleValue() == null ? 0 : cellData.getDoubleValue().intValue();
	}
	
	@Listen("onClick = #load")
	public void load(){
		reload();
		modifiedTrades.clear();
	}
	
	@Listen("onClick = #save")
	public void save(){
		dataService.save(modifiedTrades);
		tableGrid.setModel(new ListModelList<Trade>(dataService.queryAll().values()));
		loadButton.setDisabled(false);
	}

	/**
	 * When onStopEditing fired, the cell value in the book model is still not updated. So we don't extract cell data into a Trade here.
	 * @param event
	 */
	@Listen("onStopEditing = #ss")
	public void update(StopEditingEvent event){
		//validate input with Excel validation
		Events.postEvent("onAfterEditing", ss, event.getRow());
	}
	

	@Listen("onAfterEditing = #ss")
	public void update(Event event){
		Integer row = (Integer)event.getData();
		//the header row is locked
		Trade trade = extract(row);
		modifiedTrades.put(trade.getId(), trade);
		loadButton.setDisabled(false);
	}

	/**
	 * load data to the sheet
	 */
	public void reload(){
		ss.setSrc(null);
		ss.setSrc("/WEB-INF/books/tradeTemplate.xlsx");
		int row = 1;
		modifiedTrades = dataService.queryAll();
		for (Map.Entry<Integer, Trade> entry : modifiedTrades.entrySet()){
			load(entry.getValue(), row);
			row++;
		}
		ss.setMaxVisibleRows(modifiedTrades.size()+1);
		
		tableGrid.setModel(new ListModelList<Trade>(modifiedTrades.values()));

		applyAccessPolicy();
		loadButton.setDisabled(true);
	}
	
	private void applyAccessPolicy() {
		ss.disableUserAction(AuxAction.PROTECT_SHEET, true);
		ss.disableUserAction(AuxAction.ADD_SHEET, true);
		ss.disableUserAction(AuxAction.DELETE_SHEET, true);
		ss.disableUserAction(AuxAction.HIDE_SHEET, true);
		ss.disableUserAction(AuxAction.COPY_SHEET, true);
		Ranges.range(ss.getSelectedSheet()).protectSheet("", true, true, false, false, false, false, false, false, false, true, false, true, false, false, false);
	}
}
