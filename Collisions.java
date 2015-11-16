// Collisions.java

package escape;

import java.lang.Math;
import java.util.ArrayList;


public class Collisions {
	private static Level level;
	
	public static void setLevel(Level theLevel) {level = theLevel;}
	
	public static void handleCollisionsWithLevel(CollisionActor actor) {
		// Get the actor's grid co-ordinates
		Vector actorGridPos = level.getGridCoords(actor.getCentrePos());
		
		// Do collision detection and resolution
			// Get all grid positions this actor could be colliding with (all surrounding tiles)
		ArrayList<Vector> possibleCollisions = level.getSurroundingTiles(actorGridPos);
		
		for (int i = 0; i < possibleCollisions.size(); i++) {
			// For each one, get its associated LevelTile
			Vector gridCoords = possibleCollisions.get(i);
			LevelTile tile = level.getTile(gridCoords.x, gridCoords.y);
			
			// If the tile contains an object and said object is solid, do collisions resolutions
			if (tile.getObject() != null) {
				if (tile.getObject().isSolid()) {
					Collisions.doCollisionResolution(actor, tile);
				}
			}
		}
	}
	
	public static void doCollisionResolution(CollisionActor actor, CollisionObstacle obstacle) {
		// Get object dimensions
		Vector actorDimensions = actor.getDimensions();
		int actorWidth = actorDimensions.x;
		int actorHeight = actorDimensions.y;
		Vector obstacleDimensions = obstacle.getDimensions();
		int obstacleWidth = obstacleDimensions.x;
		int obstacleHeight = obstacleDimensions.y;
		
		// Get actor side positions
		Vector actorPos = actor.getPos();
		int actorLeft = actorPos.x;
		int actorRight = actorLeft + actorWidth;
		int actorTop = actorPos.y;
		int actorBottom = actorTop + actorHeight;
		
		// Get obstacle side positions
		Vector obstaclePos = obstacle.getPos();
		int obstacleLeft = obstaclePos.x;
		int obstacleRight = obstacleLeft + obstacleWidth;
		int obstacleTop = obstaclePos.y;
		int obstacleBottom = obstacleTop + obstacleHeight;
		
		// Booleans for testing each axis separately
		boolean xCollide = false;
		boolean yCollide = false;
		
		// Test on the x-axis
		if ((actorRight > obstacleLeft && actorLeft < obstacleRight)
			|| (actorLeft < obstacleRight && actorRight > obstacleLeft)) {
			xCollide = true;
		}
		
		// Test on the y-axis
		if ((actorBottom > obstacleTop && actorTop < obstacleBottom)
			|| (actorTop < obstacleBottom && actorBottom > obstacleTop)) {
			yCollide = true;
		}
		
		if (xCollide && yCollide) {  // If there is a genuine collision
			// Set up two vectors representing the player's position resolved on each possible axis
			Vector xResolved, yResolved;  // The resolution involving minimum displacement will be used
			xResolved = null;
			yResolved = null;
			
			Vector xResolveLeft = new Vector(obstacleLeft - actorWidth, actor.getPos().y);
			Vector xResolveRight = new Vector(obstacleRight, actor.getPos().y);
			Vector yResolveTop = new Vector(actor.getPos().x, obstacleTop - actorHeight);
			Vector yResolveBottom = new Vector(actor.getPos().x, obstacleBottom);
			
			// Resolve on the x-axis
			if (actor.getDirection().x == 1) {
				xResolved = xResolveLeft;
			}
			else if (actor.getDirection().x == -1) {
				xResolved = xResolveRight;
			}
			
			// Resolve on the y-axis
			if (actor.getDirection().y == 1) {
				yResolved = yResolveTop;
			}
			else if (actor.getDirection().y == -1) {
				yResolved = yResolveBottom;
			}
			
			if (xResolved == null) {
				if (yResolved == null) {
					// If there is no resolution, select the x and y resolutions based on which
					// directions (left versus right, top versus bottom) involve minimum displacement
					xResolved = selectMinimumDisplacementPosition(actorPos, xResolveLeft, xResolveRight);
					yResolved = selectMinimumDisplacementPosition(actorPos, yResolveTop, yResolveBottom);
					
					// Now use the one with minimum displacement
					actor.setPos(selectMinimumDisplacementPosition(actorPos, xResolved, yResolved));
				}
				
				else actor.setPos(yResolved);
			}
			else if (yResolved == null) actor.setPos(xResolved);
			else {
				// Use the option with minimum displacement
				actor.setPos(selectMinimumDisplacementPosition(actorPos, xResolved, yResolved));
			}
		}
	}
	
	public static Vector selectMinimumDisplacementPosition(Vector actorPos, Vector pos1, Vector pos2) {
		// Returns the position which requires the least displacement from actorPos
		// (Just using Manhattan distance)
		int d1 = Math.abs(actorPos.x - pos1.x) + Math.abs(actorPos.y - pos1.y);
		int d2 = Math.abs(actorPos.x - pos2.x) + Math.abs(actorPos.y - pos2.y);
		
		if (d1 < d2) return pos1;
		else return pos2;
	}
	
	public static boolean searchFor(Vector goal, Vector curPos, ArrayList<Vector> exploredAlready, int limit) {
		if (limit == 0) return false;  // This limit prevents stack overflows from endless recursion
		limit -= 1;
		
		if (level.isInGrid(curPos.x, curPos.y)) {
			// If the current position is in the exploredAlready list, return false
			Vector current = new Vector();
			for (int i = 0; i < exploredAlready.size(); i++) {
				current = exploredAlready.get(i);
				if ((current.x == curPos.x) && (current.y == curPos.y)) return false;
			}
			
			exploredAlready.add(curPos);  // This tile is now being explored
			
			// If the tile is not solid
			if (level.isTileSolid(curPos.x, curPos.y) == false) {
				// If this is the goal, return true
				if (goal.x == curPos.x && goal.y == curPos.y) {
					return true;
				}
				
				// Otherwise, search the surrounding tiles
				else {					
					// Search surrounding tiles
					return (searchFor(goal, new Vector(curPos.x, curPos.y - 1), exploredAlready, limit)
					|| searchFor(goal, new Vector(curPos.x + 1, curPos.y), exploredAlready, limit)
					|| searchFor(goal, new Vector(curPos.x, curPos.y + 1), exploredAlready, limit)
					|| searchFor(goal, new Vector(curPos.x - 1, curPos.y), exploredAlready, limit));
				}
			}
			
			else return false;
		}
		
		else return false;
	}
	
	public static boolean inSameRoom(CollisionActor act1, CollisionActor act2) {
		Vector pos1 = level.getGridCoords(act1.getPos());
		Vector pos2 = level.getGridCoords(act2.getPos());
		
		// Search for pos2 from pos1 (max depth of 20 chosen arbitrarily)
		return searchFor(pos2, pos1, new ArrayList<Vector>(), 20);
	}
	
	public static boolean doesRayIntersect(Ray ray, CollisionActor target) {
		// Get the ray values
		Vector rayStart = ray.getStartPos();
		double rayNormX = ray.getNormalX();
		double rayNormY = ray.getNormalY();
		double rayRange = ray.getRange();
		
		// Get the target's position and dimensions
		Vector targetPos = target.getPos();
		Vector targetDimensions = target.getDimensions();
		
		// Use these to calculate the positions of its four sides
		int leftSide = targetPos.x;
		int rightSide = leftSide + targetDimensions.x;
		int topSide = targetPos.y;
		int bottomSide = topSide + targetDimensions.y;
		
		// Determine the time values for the ray intersecting each edge
			// X-axis values
		double tLeft = (double) (leftSide - rayStart.x) / rayNormX;
		double tRight = (double) (rightSide - rayStart.x) / rayNormX;
			// Work out which is entry and which is exit (smallest is entry, largest is exit, ofc)
		double enterTimeX = Math.min(tLeft, tRight);
		double exitTimeX = Math.max(tLeft, tRight);
			// Y-axis values
		double tTop = (double) (topSide - rayStart.y) / rayNormY;
		double tBottom = (double) (bottomSide - rayStart.y) / rayNormY;
		double enterTimeY = Math.min(tTop, tBottom);
		double exitTimeY = Math.max(tTop, tBottom);
		
		// Work out the largest entry time and the smallest exit time
		// (The section of ray inside the object lies between these values)
		double maxEntryTime = Math.max(enterTimeX, enterTimeY);
		double minExitTime = Math.min(exitTimeX, exitTimeY);
		
		// If the maxEntryTime is larger than the minExitTime, the section of the ray contained
		// within the object is nonexistant (that is, the ray does not hit)
		if (maxEntryTime > minExitTime) return false;
		// Otherwise, if the point of entry is outwith the ray's range, this also counts as a miss
		else if (maxEntryTime > rayRange) return false;
		// Otherwise, the ray hits
		else return true;
	}
	
	public static void attackEnemies(Ray attackRay, double attackStrength) {
		ArrayList<Enemy> enemies = Enemy.getEnemies();
		ArrayList<Enemy> enemiesHit = new ArrayList<Enemy>();
		
		// Add each enemy which is hit by the ray to the list of enemies hit
		for (int i = 0; i < enemies.size(); i++) {
			if (doesRayIntersect(attackRay, enemies.get(i))) {
				enemiesHit.add(enemies.get(i));
			}
		}
		
		// If enemies were hit
		if (enemiesHit.size() > 0) {
			// Work out which enemy is the closest
			Enemy current;
			Enemy closest = null;
			double nearestDist = attackRay.getRange();
			
			for (int j = 0; j < enemiesHit.size(); j++) {
				current = enemiesHit.get(j);
				double currentDist = current.getDistanceToPlayer();
				
				if ((closest == null) || (currentDist < nearestDist)) {
					closest = current;
					nearestDist = currentDist;
				}
			}
			
			// Land the attack on the closest hit enemy
			closest.hit(attackStrength);
		}
	}
	
	public static void attackPlayer(Ray attackRay, double attackStrength) {
		Player player = Player.getPlayer();
		
		// If the attack ray hits the player, damage the player with the supplied attack strength
		if (doesRayIntersect(attackRay, player)) {
			player.hit(attackStrength);
		}
	}
}