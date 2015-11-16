// Upgrade.java

package escape;


public abstract class Upgrade extends Item {
	Upgrade(int id, int texIndex, String name, String description) {
		super(id, Constants.ID_UPGRADE, texIndex, name, description);
	}
	
	public abstract void performUpgradeFunction();  // Performs this upgrade's upgrading function
	
	public void handleMouseClick() {
		performUpgradeFunction();
		Messages.sendSimpleMessage("Used the " + getName() + ".");
	}
}