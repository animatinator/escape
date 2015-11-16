// Weapon.java

package escape;

import java.lang.Math;


public class Weapon extends Item {
	private double damage;  // Damage done to enemies on hit
	private double range;  // Range over which the weapon will have effect (fists < pistol, for instance)
	private int repeatDelay;  // Time between shots whilst holding fire
	private int firingTimer;  // Incremented and used in conjunction with repeatDelay to space shots properly
	private int ammo;  // What it says on the tin. A value of -1 specifies infinite ammo
	
	Weapon(int id, int texIndex, String name, String description, double damage, double range, int repeatDelay, int baseAmmo) {
		super(id, Constants.ID_WEAPON, texIndex, name, description);
		this.damage = damage;
		this.range = range;
		this.repeatDelay = repeatDelay;
		firingTimer = 0;
		ammo = baseAmmo;
	}
	
	public double getDamage() {return damage;}
	public void setDamage(double newDamage) {damage = newDamage;}
	public double getRange() {return range;}
	public void setRange(double newRange) {range = newRange;}
	public int getRepeatDelay() {return repeatDelay;}
	public void setRepeatDelay(int newRepeatDelay) {repeatDelay = newRepeatDelay;}
	
	public void fire() {
		// If it's time to fire
		if (firingTimer == 0) {
			// If the weapon still has ammo (or ammo is infinite)
			if ((ammo > 0) || (ammo == -1)) {
				// Get the player's position
				Player thePlayer = Player.getPlayer();
				Vector playerPos = thePlayer.getCentrePos();
				
				// Get a normal for their direction
				double angle = thePlayer.getAngle() - (Math.PI * 0.5);
				double rayNormX = Math.cos(angle);
				double rayNormY = Math.sin(angle);
				
				// Create a ray representing the attack
				Ray attackRay = new Ray(playerPos, rayNormX, rayNormY, range);
				
				// Make the Collisions class attack enemies with the ray and this weapon's damage value
				Collisions.attackEnemies(attackRay, damage);
				
				// Show a gunfire flash
				Effects.createFlashEffect(playerPos, Constants.FLASH_MEDIUM, Constants.FLASH_DURATION_GUNFIRE);
				Effects.createLineEffect(playerPos, new Vector(playerPos.x + (int) (rayNormX * range), playerPos.y + (int) (rayNormY * range)), Constants.LINE_DURATION_GUNFIRE);
				
				ammo -= 1;
			}
		}
		
		// Increment the timer and loop it back to zero when it reaches the value of repeatDelay
		firingTimer = (firingTimer + 1) % repeatDelay;
	}
	
	public void stopFire() {
		// Resets the firing timer
		firingTimer = 0;
	}
	
	public void reload(int addedAmmo) {
		// If this weapon does not have infinite ammo, add the supplied ammount
		if (ammo != -1) {
			ammo += addedAmmo;
		}
	}
	
	public void handleMouseClick() {
		Player.getPlayer().setCurWeapon(this);
		Messages.sendSimpleMessage("Equipped the " + getName() + ".");
	}
}