// RunningShoesUpgrade.java

package escape;


public class RunningShoesUpgrade extends Upgrade {
	public RunningShoesUpgrade(int id, int texIndex, String name, String description) {
		super(id, texIndex, name, description);
	}
	
	public void performUpgradeFunction() {
		Player.getPlayer().setRunningSpeed(Constants.RUNNINGSHOES_UPGRADE_VALUE);
	}
}