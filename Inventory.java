// Inventory.java

package escape;

import java.util.ArrayList;


class Inventory {
	private static final int INVENTORY_SIZE = 36;
	
	private Item[] items;
	
	public Inventory() {
		items = new Item[INVENTORY_SIZE];
		
		for (int i = 0; i < INVENTORY_SIZE; i++) {
			items[i] = null;
		}
	}
	
	public int getSize() {return INVENTORY_SIZE;}
	
	public void addItem(int id) {
		Item newItem = ItemManager.getItem(id);
		
		for (int i = 0; i < INVENTORY_SIZE; i++) {
			if (items[i] == null) {
				items[i] = newItem;
				break;
			}
		}
	}
	
	public Item getItemByIndex(int index) {
		if ((index >= 0) && (index < INVENTORY_SIZE)) {
			return items[index];
		}
		
		else return null;
	}
	
	public Item getItemByID(int id) {
		Item curItem;
		
		// Linear search. Sorry :P
		for (int i = 0; i < INVENTORY_SIZE; i++) {
			curItem = items[i];
			
			if (curItem != null) {
				if (curItem.getID() == id) {
					return curItem;
				}
			}
		}
		
		return null;
	}
	
	public boolean hasItemWithID(int id) {
		return (getItemByID(id) != null);
	}
}