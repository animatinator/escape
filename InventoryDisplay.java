// InventoryDisplay.java

package escape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Image;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.lang.Math;


public class InventoryDisplay {
	private static final int screenWidth = 800;  // TODO: Get this from elsewhere, ideally
	private static final Vector offset = new Vector(10, 10);  // Used to specify the topleft corner of the main inventory
	// and the topright corner of the key items inventory
	private static int tileSize = 32;  // The side length of an inventory tile
	private static int columnHeight = 16;  // The height (in tiles) of an inventory column
	private Inventory playerInv;  // The inventory to display
	private Vector leftSideDimensions;
	private Vector rightSideDimensions;
	
	public InventoryDisplay(Inventory inv) {
		playerInv = inv;
		leftSideDimensions = new Vector();
		rightSideDimensions = new Vector();
	}
	
	private void drawItems(Graphics2D g2d, ArrayList<Item> items, boolean rightAligned) {
		int row = 0;
		int col = 0;
		Item current;
		Image curTexture;
		
		for (int i = 0; i < items.size(); i++) {
			current = items.get(i);
			curTexture = Textures.getTextureByID(current.getTexIndex());
			
			AffineTransform translate = new AffineTransform();  // AffineTransform for moving the image to the correct location
			
			if (rightAligned) {  // Align to the right-hand side and grow left
				// TODO: ABSOLUTELY REMOVE THIS MAGICAL LITERAL (800) BY SOME MEANS; it shall be the death of us all!!!
				translate.translate(screenWidth - (((col + 1) * tileSize) + offset.x), (row * tileSize) + offset.y);
			}
			
			else {  // Align to the left-hand side and grow right (muuuuch easier)
				translate.translate((col * tileSize) + offset.x, (row * tileSize) + offset.y);
			}
			
			g2d.drawImage(curTexture, translate, null);
			
			// If we're not on the last element yet...
			if (i < (items.size() - 1)) {
				// Move down a row
				row += 1;
				
				// If we have passed the maximum number of rows per column, zero the row counter and start a new column
				if (row == columnHeight) {
					row = 0;
					col += 1;
				}
			}
		}
		
		// Record the dimensions of the side of the inventory which has just been drawn
		Vector dimensions = new Vector((col + 1) * tileSize, (row + 1) * tileSize);
		
		if (rightAligned) rightSideDimensions = dimensions;
		else leftSideDimensions = dimensions;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		ArrayList<Item> generalItems = new ArrayList<Item>();
		ArrayList<Item> keyItems = new ArrayList<Item>();
		
		Item current;
		
		for (int i = 0; i < playerInv.getSize(); i++) {
			current = playerInv.getItemByIndex(i);
			
			if (current != null) {
				if (current.getType() == Constants.ID_KEY) {
					keyItems.add(current);
				}
				
				else generalItems.add(current);
			}
		}
		
		drawItems(g2d, generalItems, false);
		drawItems(g2d, keyItems, true);
	}
	
	public boolean posInInventory(Vector pos) {
		// Try the left side
		if ((pos.x > offset.x) && (pos.y > offset.y)  // Downright of topleft corner
			&& (pos.x < (offset.x + leftSideDimensions.x))&& (pos.y < (offset.y + leftSideDimensions.y))) {  // Upleft of bottomright corner
			return true;
		}
		
		// Try the right side
		else if ((pos.x > (screenWidth - (offset.x + rightSideDimensions.x))) && (pos.y > (offset.y))  // Downright of topleft corner
			&& (pos.x < (screenWidth - offset.x)) && (pos.y < (offset.y + rightSideDimensions.y))) {  // Upleft of bottomright corner
			return true;
		}
		
		else return false;  // Nope
	}
	
	private Item getSelectedItem(Vector mousePos) {
		if (posInInventory(mousePos)) {
			Vector transformedPos;  // The position is transformed so that the inventory starts at (0, 0) and progresses right
			boolean isOnRight = false;  // Specifies which side of the inventory the click is on
			
			// Cheap hack to work out which side of the inventory the mouse is on
			// Should work because if one side of the inventory crosses the centre of the screen,
			// the game has far deeper issues than mouse clicks not being detected properly...
			if (mousePos.x > (screenWidth / 2)) {  // Right-hand side
				transformedPos = new Vector((screenWidth - mousePos.x) - offset.x, mousePos.y - offset.y);
				isOnRight = true;
			}
			else {
				transformedPos = new Vector(mousePos.x - offset.x, mousePos.y - offset.y);
			}
			
			int col = transformedPos.x / tileSize;
			int row = transformedPos.y / tileSize;
			int itemIndex = (col * columnHeight) + row;
			
			// Getting the selected item begins here
			Item selectedItem = null;
			Item current;
			
			int j = 0;  // Index used for counting the number of items of the correct type which have been seen
			
			for (int i = 0; i < playerInv.getSize(); i++) {
				current = playerInv.getItemByIndex(i);
				
				// If there is an item at this index
				if (current != null) {
					// If its type matches the types of the items on the selected side of the inventory
					if ((current.getType() == Constants.ID_KEY && isOnRight) || (current.getType() != Constants.ID_KEY && !isOnRight)) {
						// If we are at the itemIndex-th item of the right type, this is the selected item
						if (j == itemIndex) {
							selectedItem = current;
							break;
						}
						// Otherwise, increment j
						else{
							j += 1;
						}
					}
				}
			}
			
			return selectedItem;
		}
		
		else return null;
	}
	
	public void handleMouseMotion(MouseEvent e) {
		Vector mousePos = new Vector(e.getX(), e.getY());
		Item selectedItem = getSelectedItem(mousePos);
		if (selectedItem != null) selectedItem.handleMouseOver();
	}
	
	public void handleMouseClick(MouseEvent e) {
		int mouseButton = e.getButton();
		if (mouseButton == MouseEvent.BUTTON1) {  // Left-click
			Vector mousePos = new Vector(e.getX(), e.getY());
			Item selectedItem = getSelectedItem(mousePos);
			if (selectedItem != null) {
				selectedItem.handleMouseClick();
				Mouse.setLeftButton(false);  // Stop the level from receiving this click
			}
		}
	}
}