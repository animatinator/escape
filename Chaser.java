// Chaser.java

package escape;

import java.lang.Math;


public class Chaser extends Enemy {
	private double viewAngleTolerance;
	private double cosAngle, sinAngle;
	
	public Chaser(Vector pos) {
		super(pos, Constants.CHASER_IMAGEFILE);
		baseMoveDelay = 0;
		maxMoveDelay = 0;
		curMoveDelay = baseMoveDelay;
		
		walkingSpeed = 5.0;
		runningSpeed = 5.0;
		
		attackStrength = 6.0;
		attackRange = 30.0;
		health = 20.0;
		
		viewAngleTolerance = Math.PI * 0.4;
		cosAngle = 0.0;
		sinAngle = 0.0;
	}
	
	private void updateAngle() {
		Vector playerPos = Player.getPlayer().getPos();
		double playerDist = getDistanceToPlayer();
		double dx = (double) (playerPos.x - position.x);
		double dy = (double) (playerPos.y - position.y);
		rotAngle = Math.atan2(dy, dx);
		rotAngle += Math.PI * 0.5;
		cosAngle = (dx / playerDist);
		sinAngle = (dy / playerDist);
	}
	
	private boolean playerLooking() {
		double playerAngle = Player.getPlayer().getAngle() % (Math.PI * 2.0);
		if (playerAngle <= 0.0) playerAngle += (Math.PI * 2.0);
		
		// If the player were directly facing the enemy, their angle would be the opposite of the enemy's angle
		double idealPlayerAngle = (rotAngle + Math.PI) % (Math.PI * 2.0);
		
		// If the player's angle is within the viewAngleTolerance of the idealPlayerAngle, they are looking at this enemy
		double angleDiff = Math.abs(idealPlayerAngle - playerAngle);
		if (angleDiff < viewAngleTolerance) return true;
		else return false;
	}
	
	public void doAI() {
		// Always face the player when they are in the room
		boolean playerInRoom = playerInRoom();
		if (playerInRoom) updateAngle();
		
		if (state == Constants.AI_STATE_ATTACKING) {
			// If the player is not in the room or is looking at this enemy, stop attacking
			if (!playerInRoom || playerLooking()) {
				state = Constants.AI_STATE_CALM;
				xMovement = 0.0;
				yMovement = 0.0;
				return;
			}
			
			// If the player is near enough, attack
			if (getDistanceToPlayer() < attackRange) {
				// TODO: Set the angle so it's facing the player at this stage
				attack();
			}
			
			// When attacking, run at the player
			xMovement = cosAngle;
			yMovement = sinAngle;
			speed = runningSpeed;
		}
		else if (state == Constants.AI_STATE_CALM) {
			if (playerInRoom) {
				// If the player is not looking, start attacking
				if (!playerLooking()) state = Constants.AI_STATE_ATTACKING;
			}
		}
	}
}