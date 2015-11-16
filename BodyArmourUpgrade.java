// BodyArmourUpgrade.java

package escape;


public class BodyArmourUpgrade extends Upgrade {
	public BodyArmourUpgrade(int id, int texIndex, String name, String description) {
		super(id, texIndex, name, description);
	}
	
	public void performUpgradeFunction() {
		Player.getPlayer().setArmour(Constants.BODYARMOUR_UPGRADE_VALUE);
	}
}