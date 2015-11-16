// Container.java

package escape;


class Container extends GameObject {
	private boolean locked;
	private int key;
	private String lockedDescription;
	private int containedItem;
	
	public Container(int texIndex, boolean isLocked, int key, String lockedDescription, int containedItem) {
		super(texIndex);
		
		solid = true;
		locked = isLocked;
		this.key = key;
		this.lockedDescription = lockedDescription;
		this.containedItem = containedItem;
	}
	
	private void givePlayerItem() {
		// If the chest still contains an item, give it to the player and set the contained item to 0 (none)
		if (containedItem != 0) {
			Player.getPlayer().addItem(containedItem);
			containedItem = 0;
		}
		// Otherwise, notify the player of the chest's emptiness at the hands of their greed
		else {
			Messages.sendSimpleMessage("You've already robbed this poor chest, you oaf!");
		}
	}
	
	public void handleActionKeyPress() {
		// If the chest is not locked, give the cointained item to the player
		if (!locked) {
			givePlayerItem();
		}
		
		// Otherwise, if the chest is indeed locked, check for the key before opening
		else {
			if (Player.getPlayer().hasItem(key)) {
				Messages.sendSimpleMessage("You unlocked it.");
				locked = false;
				givePlayerItem();
			}
			else {
				Messages.sendInterruptingMessage("It's locked. " + lockedDescription);
			}
		}
	}
}