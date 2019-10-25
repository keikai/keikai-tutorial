package io.keikai.tutorial.database;

import java.util.*;

/**
 * A class that simulates accessing a table of a database.
 * @author hawk
 *
 */
public class MyDataService {

	private TreeMap<Integer, Trade> tradeTable = new TreeMap<Integer, Trade>();
	private int sequence = 0;
	
	public MyDataService(){
		save(new Trade("Beverages", "Suyama", 5122));
		save(new Trade("Meat", "Davolio", 450));
		save(new Trade("Produce", "Buchanan", 6328));
		save(new Trade("Tools", "Suyama", 230));
		save(new Trade("Vegetables", "John", 947));
	}

	public void save(Map<Integer, Trade> changedMap){
		for(Map.Entry<Integer, Trade> entry: changedMap.entrySet()) {
			Trade changedTrade = entry.getValue();
			tradeTable.put(changedTrade.getId(), changedTrade); //simplified logic, update without comparing
		}
		Iterator<Integer> keyIterator = tradeTable.keySet().iterator();
		while (keyIterator.hasNext()){
			Integer id = keyIterator.next();
			if(!changedMap.containsKey(id)){
				keyIterator.remove();
			}
		}
	}
	
	public void save(Trade trade){
		if (trade.getId()==null){
			trade.setId(nextId());
		}
		tradeTable.put(trade.getId(), trade);
	}
	
	public Map<Integer, Trade> queryAll(){
		TreeMap<Integer, Trade> result = new TreeMap<Integer, Trade>();
		for (Map.Entry<Integer, Trade> entry : tradeTable.entrySet()){
			result.put(entry.getKey(), new Trade(entry.getValue()));
		}
		return result;
	}
	
	private Integer nextId(){
		return sequence++;
	}
}
