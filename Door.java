// Door.java

package escape;


class Door extends GameObject {
	private boolean locked;
	private boolean open;
	private int key;
	private String lockedDescription;  // Displayed to the user after informing them that the door is locked
	private int direction;
	
	public Door(boolean isLocked, int key, String lockedDescription, int direction) {
		super(0);
		
		solid = true;
		seeThrough = false;
		locked = isLocked;
		open = false;
		this.key = key;
		this.lockedDescription = lockedDescription;
		this.direction = direction;
	}
	
	public int getDirection() {return direction;}
	public void setDirection(int newDirection) {
		if (newDirection == Constants.HORIZONTAL || newDirection == Constants.VERTICAL) {
			direction = newDirection;
		}
	}
	
	public int getTexIndex() {
		if (direction == Constants.VERTICAL) {
			if (open) return Constants.TEX_INDEX_DOOR_V_O;
			else return Constants.TEX_INDEX_DOOR_V_C;
		}
		else {
			if (open) return Constants.TEX_INDEX_DOOR_H_O;
			else return Constants.TEX_INDEX_DOOR_H_C;
		}
	}
	
	private void toggleDoor() {
		open = (!open);
		solid = (!solid);
	}
	
	public void handleActionKeyPress() {
		// If unlocked, toggle its openness
		if (!locked) {
			toggleDoor();
		}
		
		// Otherwise, if the player has the correct key, unlock it and open it
		// If the player does not have the key, display a message to that effect
		else {
			if (Player.getPlayer().hasItem(key)) {
				locked = false;
				toggleDoor();
				Messages.sendSimpleMessage("You unlocked it.");
			}
			else {
				Messages.sendInterruptingMessage("The door is locked. " + lockedDescription);
			}
		}
	}
}