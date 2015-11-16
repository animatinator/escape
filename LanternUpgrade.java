// LanternUpgrade.java

package escape;


public class LanternUpgrade extends Upgrade {
	public LanternUpgrade(int id, int texIndex, String name, String description) {
		super(id, texIndex, name, description);
	}
	
	public void performUpgradeFunction() {
		Player.getPlayer().setHasLantern(true);
	}
}