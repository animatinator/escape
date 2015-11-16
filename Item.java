// Item.java

package escape;


public class Item {
	private int id;
	private int type;
	private int texIndex;
	private String name;
	private String description;
	
	public Item(int id, int type, int texIndex, String name, String description) {
		this.id = id;
		this.type = type;  // Weapon, supply, key?
		this.texIndex = texIndex;
		this.name = name;
		this.description = description;
	}
	
	public int getID() {return id;}
	
	public int getType() {return type;}
	
	public int getTexIndex() {return texIndex;}
	public void setTexIndex(int newIndex) {texIndex = newIndex;}
	
	public String getName() {return name;}
	public String getDescription() {return description;}
	
	public void handleMouseOver() {
		Messages.sendSimpleMessage(name + " - " + description);
	}
	
	public void handleMouseClick() {
		// This should be inherited and manipulated the dubious ends of whichever item subclass is being clicked
	}
}