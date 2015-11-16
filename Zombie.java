// Zombie.java

package escape;

import java.lang.Math;


public class Zombie extends Enemy {
	public Zombie(Vector pos) {
		super(pos, Constants.ZOMBIE_IMAGEFILE);
		baseMoveDelay = 20;
		maxMoveDelay = 60;
		curMoveDelay = baseMoveDelay;
		
		walkingSpeed = 1.0;
		runningSpeed = 3.0;
		
		attackStrength = 2.0;
		attackRange = 30.0;
		health = 10.0;
	}
	
	private void updateAngle() {
		if (xMovement == 0) {
			if (yMovement == 0) rotAngle = 0.0;
			else if (yMovement > 0) rotAngle = Math.PI;
			else if (yMovement < 0) rotAngle = 0.0;
		}
		
		else if (xMovement > 0) {
			if (yMovement == 0) rotAngle = Math.PI * 0.5;
			else if (yMovement > 0) rotAngle = Math.PI * 0.75;
			else if (yMovement < 0) rotAngle = Math.PI * 0.25;
		}
		
		else if (xMovement < 0) {
			if (yMovement == 0) rotAngle = Math.PI * 1.5;
			else if (yMovement > 0) rotAngle = Math.PI * 1.25;
			else if (yMovement < 0) rotAngle = Math.PI * 1.75;
		}
	}
	
	public void doAI() {
		if (state == Constants.AI_STATE_ATTACKING) {
			// If the player is near enough, attack
			if (getDistanceToPlayer() < attackRange) {
				// TODO: Set the angle so it's facing the player at this stage
				attack();
			}
			
			// When attacking, run in the general direction of the player
			speed = runningSpeed;
			Vector playerPos = Player.getPlayer().getPos();
			int playerDistX = playerPos.x - position.x;
			int playerDistY = playerPos.y - position.y;
			int absPlayerDistX = Math.abs(playerDistX);
			int absPlayerDistY = Math.abs(playerDistY);
			
			// If the x distance is most significant, run on that axis only
			if (absPlayerDistX > (absPlayerDistY * 3)) {
				xMovement = playerDistX / absPlayerDistX;
				yMovement = 0.0;
			}
			// Same for y
			else if (absPlayerDistY > (absPlayerDistX * 3)) {
				xMovement = 0.0;
				yMovement = playerDistY / absPlayerDistY;
			}
			// Otherwise, run diagonally
			else {
				xMovement = playerDistX / absPlayerDistX;
				yMovement = playerDistY / absPlayerDistY;
			}
			
			// If the player has left the room, stop chasing
			if (!playerInRoom()) {
				state = Constants.AI_STATE_CALM;
			}
		}
		else {
			// If the player is now in the room, give chase
			if (playerInRoom()) {
				state = Constants.AI_STATE_ATTACKING;
				doAI();
			}
			// Otherwise, walk randomly (like a boss)
			else {
				speed = walkingSpeed;
				xMovement = (float)(-1 + (int)(Math.random() * 3));
				yMovement = (float)(-1 + (int)(Math.random() * 3));
			}
		}
	}
	
	public void update() {
		super.update();
		updateAngle();
	}
}