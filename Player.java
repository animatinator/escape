// Player.java

package escape;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

import java.util.ArrayList;

import java.lang.Math;


class Player implements CollisionActor {
	private static Player instance;
	private Vector pos;
	private Vector direction;  // Both components 1, 0 or -1 - used for collision resolution
	private int speed;
	private int runningSpeed;
	private boolean running;
	private double angle;
	
	private Vector dimensions;
	
	private final double baseHealth = 10.0;
	private double health;
	private double armour;  // Damage is divided by this
	
	private Inventory inventory;
	private Weapon curWeapon;
	
	private ArrayList<ArrayList<Image>> images;  // Images indexed by animation and then frame
	private int animFrame;  // Current animation frame
	private int animIndex;  // Which animation is playing?
	
	private boolean hasLantern;
	
	private boolean alive;
	
	private Player(Vector startPos) {
		pos = new Vector();
		direction = new Vector();
		pos.x = startPos.x;
		pos.y = startPos.y;
		speed = 3;
		runningSpeed = 6;
		running = false;
		angle = 0.0;
		
		dimensions = new Vector(40, 40);
		
		health = baseHealth;
		armour = 1.0;
		
		inventory = new Inventory();
		
		loadImages();
		animFrame = 0;
		animIndex = Constants.ANIMATION_STATIONARY;
		
		curWeapon = null;
		
		hasLantern = false;
		
		alive = true;
	}
	
	public static Player createPlayer(Vector startPos) {
		instance = new Player(startPos);
		
		return instance;
	}
	
	public static Player getPlayer() {return instance;}
	
	private ArrayList<Image> loadAnimationFromStrip(Image rawImages) {
		ArrayList<Image> frames = new ArrayList<Image>();
		
		// Take the strip's width as being each image's height (assuming square images)
		int size = rawImages.getWidth(null);
		int imgHeight = rawImages.getHeight(null);
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();  // Used for creating cropped images
		
		Image curImage;
		
		for (int i = 0; i < (imgHeight / size); i++) {
			int startY = i * size;
			
			// Get a cropped image and add it to the frames list
			curImage = toolkit.createImage(new FilteredImageSource(rawImages.getSource(), new CropImageFilter(0, startY, size, size)));
			frames.add(curImage);
			
			curImage.getWidth(null);  // Slightly odious hack - 'prods' the image so the foolish asynchronous loader actually loads it
		}
		
		return frames;
	}
	
	private void loadImages() {
		// Create the array of image arrays
		images = new ArrayList<ArrayList<Image>>();
		
		ImageIcon ii = new ImageIcon(this.getClass().getResource("img/character.png"));
		Image stationaryImage = ii.getImage();
		ArrayList<Image> stationary = new ArrayList<Image>();
		stationary.add(stationaryImage);
		images.add(stationary);
		
		ii = new ImageIcon(this.getClass().getResource("img/character_walk.png"));
		Image walkImagesRaw = ii.getImage();
		ArrayList<Image> walk = loadAnimationFromStrip(walkImagesRaw);
		images.add(walk);
	}
	
	public Vector getPos() {return pos;}
	public void setPos(Vector newPos) {pos = newPos;}
	public Vector getDirection() {return direction;}
	public double getAngle() {return angle;}
	public Inventory getInventory() {return inventory;}
	public Weapon getCurWeapon() {return curWeapon;}
	public void setCurWeapon(Weapon newWeapon) {curWeapon = newWeapon;}
	public boolean hasLantern() {return hasLantern;}
	public void setHasLantern(boolean hasLantern) {this.hasLantern = hasLantern;}
	public void setArmour(double newArmour) {armour = newArmour;}
	public void setRunningSpeed(int newRunningSpeed) {runningSpeed = newRunningSpeed;}
	public boolean isAlive() {return alive;}
	public double getBaseHealth() {return baseHealth;}
	public double getHealth() {return health;}
	
	public void addItem(int id) {
		// Add the item and notify the player of its addition
		inventory.addItem(id);
		Item newItem = inventory.getItemByID(id);
		Messages.sendInterruptingMessage("You got an item (" + newItem.getName() + ")! " + newItem.getDescription());
	}
	
	public boolean hasItem(int id) {
		return inventory.hasItemWithID(id);
	}
	
	public void hit(double damage) {
		health -= (damage / armour);
		
		if (health <= 0.0) {
			health = 0.0;
			alive = false;
		}
		
		Messages.sendSimpleMessage("OUCH! Health is now: "+health);  // TODO: Remove this
	}
	
	public Vector getDimensions() {
		return dimensions;
	}
	
	public Vector getCentrePos() {
		Vector dim = getDimensions();
		
		int x = pos.x + (dim.x / 2);
		int y = pos.y + (dim.y / 2);
		
		return new Vector(x, y);
	}
	
	private Image getCurrentImage() {
		// Return the current frame of the current animation
		return images.get(animIndex).get(animFrame % (images.get(animIndex)).size());
	}
	
	public void draw(Vector offset, Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// Draw the player image
			// Get the current image
		Image current = getCurrentImage();
			// Set up a transformation to translate the image to the right position and rotate it about its centre by the current angle
		AffineTransform t = new AffineTransform();
		t.translate((double) (pos.x + offset.x), (double) (pos.y + offset.y));
		int imageCentreX = current.getWidth(null) / 2;
		int imageCentreY = current.getHeight(null) / 2;
		t.rotate(angle, imageCentreX, imageCentreY);
			// And draw
		g2d.drawImage(current, t, null);
	}
	
	public void update() {
		int moveSpeed;
		
		getMovementInput();
		
		if (running) moveSpeed = runningSpeed;
		else moveSpeed = speed;
		
		pos.x += direction.x * moveSpeed;
		pos.y += direction.y * moveSpeed;
		
		// Calculate the angle to the mouse
		Vector centrePos = getCentrePos();
		double dx = (double) (Mouse.getWorldX() - centrePos.x);
		double dy = (double) (Mouse.getWorldY() - centrePos.y);
		angle = Math.atan2(dy, dx);
		angle += Math.PI * 0.5;
		
		// Work out which animation to use
		if ((direction.x == 0) && (direction.y == 0)) animIndex = Constants.ANIMATION_STATIONARY;
		else animIndex = Constants.ANIMATION_WALKING;
		
		// If the mouse is being clicked, fire the current weapon
		if (curWeapon != null) {
			if (Mouse.leftButtonHeld()) {
				curWeapon.fire();
			}
			// (Stop firing when the left mouse button is released)
			else curWeapon.stopFire();
		}
		
		animFrame += 1;
	}
	
	public void getMovementInput() {
		// Holding shift makes the player run
		if (Keyboard.SHIFT) running = true;
		else running = false;
		
		// WASD to move
		if (Keyboard.A) {
			if (Keyboard.D) direction.x = 0;
			else direction.x = -1;
		}
		else if (Keyboard.D) direction.x = 1;
		else direction.x = 0;
		
		if (Keyboard.W) {
			if (Keyboard.S) direction.y = 0;
			else direction.y = -1;
		}
		else if (Keyboard.S) direction.y = 1;
		else direction.y = 0;
	}
	
	public void handleKeyPress(KeyEvent e) {
		int key = e.getKeyCode();
		
		// Maybe use stuff
	}
	
	public void handleKeyRelease(KeyEvent e) {
		int key = e.getKeyCode();
		
		// Stuff would go here if it were necessary
	}
	
	public void handleMouseMotion(MouseEvent e) {
		// Maybe do stuff...
	}
}