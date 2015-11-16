// Enemy.java

package escape;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import java.util.ArrayList;
import java.lang.Math;


public abstract class Enemy implements CollisionActor {
	private static ArrayList<Enemy> enemies;
	
	protected Vector position;
	protected double speed;
	protected double walkingSpeed;
	protected double runningSpeed;
	protected double xMovement;
	protected double yMovement;
	protected double rotAngle;
	
	protected double attackStrength;
	protected double attackRange;
	public double health;
	
	protected int AITimer;
	protected int baseMoveDelay;
	protected int maxMoveDelay;
	protected int curMoveDelay;
	protected int state;
	
	protected boolean alive;
	
	protected Image enemyImage;
	
	static {
		enemies = new ArrayList<Enemy>();
	}
	
	public Enemy(Vector pos, String imageLocation) {
		position = pos;
		xMovement = 0.0;
		yMovement = 0.0;
		state = Constants.AI_STATE_CALM;
		
		ImageIcon ii = new ImageIcon(this.getClass().getResource("img/"+imageLocation));
		enemyImage = ii.getImage();
		
		alive = true;
	}
	
	public static ArrayList<Enemy> getEnemies() {return enemies;}
	
	public Vector getPos() {return position;}
	public void setPos(Vector newPos) {position = newPos;}
	public Vector getCentrePos() {
		Vector dim = getDimensions();
		return new Vector(position.x + (dim.x / 2), position.y + (dim.y / 2));
	}
	public double getSpeed() {return speed;}
	public Vector getDirection() {
		int x, y;
		if (xMovement == 0.0) x = 0;
		else x = ((xMovement > 0) ? 1 : -1);
		if (yMovement == 0.0) y = 0;
		else y = ((yMovement > 0) ? 1 : -1);
		
		return new Vector(x, y);
	}
	public Vector getDimensions() {return new Vector(32, 32);}  // TODO: Replace this temporary code
	public double getAngle() {return rotAngle;}
	
	public void hit(double attackStrength) {
		health -= attackStrength;
		
		if (health <= 0.0) {
			health = 0.0;
			alive = false;
		}
		
		Messages.sendSimpleMessage("Enemy hit! Health: "+health);  // TODO: Remove
	}
	
	public double getDistanceToPlayer() {
		Vector playerPos = Player.getPlayer().getPos();
		return Math.sqrt(Math.pow((playerPos.x - position.x), 2) + Math.pow((playerPos.y - position.y), 2));
	}
	
	public boolean playerInRoom() {
		return Collisions.inSameRoom(Player.getPlayer(), this);
	}
	
	public void attack() {
		// Create a ray representing the attack using this enemy's position and angle
		double adjustedAngle = rotAngle - (Math.PI * 0.5);  // Adjusted so 0 radians points right
			// Making a normal vector for the attack direction
		double attackRayX = Math.cos(adjustedAngle);
		double attackRayY = Math.sin(adjustedAngle);
		Ray attackRay = new Ray(position, attackRayX, attackRayY, attackRange);
		
		// Attack the player using the ray and this enemy's attack strength
		Collisions.attackPlayer(attackRay, attackStrength);
	}
	
	public void refreshTimer() {
		AITimer = 0;
		curMoveDelay = baseMoveDelay + (int) (Math.random() * ((maxMoveDelay - baseMoveDelay) + 1));
	}
	
	public abstract void doAI(); // Sets direction, state, speed and suchlike according to the subclass's AI algorithm
	
	public static void updateEnemies() {
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();
		}
	}
	
	public void update() {
		if (AITimer >= curMoveDelay) {
			doAI();
			refreshTimer();
		}
		
		position.x += xMovement * speed;
		position.y += yMovement * speed;
		
		AITimer += 1;
	}
	
	public static void drawEnemies(Vector offset, Graphics g) {
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(offset, g);
		}
	}
	
	public void draw(Vector offset, Graphics g) {
		// TODO: Write proper code here with magical images 'n' shit
		Graphics2D g2d = (Graphics2D) g;
		
		// Set up a transformation to translate the image to the right position and rotate it about its centre by the current angle
		AffineTransform t = new AffineTransform();
		t.translate((double) (position.x + offset.x), (double) (position.y + offset.y));
		int imageCentreX = enemyImage.getWidth(null) / 2;
		int imageCentreY = enemyImage.getHeight(null) / 2;
		t.rotate(rotAngle, imageCentreX, imageCentreY);
		// And draw
		g2d.drawImage(enemyImage, t, null);
	}
	
	public static void createEnemy(int id, Vector pos) {
		if (id == Constants.ENEMY_ZOMBIE) {
			Enemy newEnemy = new Zombie(pos);
			enemies.add(newEnemy);
		}
		else if (id == Constants.ENEMY_CHASER) {
			Enemy newEnemy = new Chaser(pos);
			enemies.add(newEnemy);
		}
		else {
			// No enemy type with this ID exists
			Messages.sendSimpleMessage("No enemy with id " + id + " exists. This enemy has not been created.");
		}
	}
}