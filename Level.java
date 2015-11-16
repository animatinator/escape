// Level.java

package escape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.Composite;
import java.awt.AlphaComposite;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.io.InputStream;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Component;

import java.util.ArrayList;
import java.lang.Math;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class Level {
	private int width, height;
	private final int tileSize;
	private int gridWidth;
	private int gridHeight;
	
	private Player player;
	private Vector cameraOffset;
	private Vector mousePos;
	
	private InventoryDisplay inventoryDisplay;
	
	private LevelTile[][] layout;
	private GameObject[] objects;
	
	private Image darknessOverlay;
	private Image lightOverlay;
	
	private BufferedImage damageOverlay;
	private double damageOverlayAlpha;
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tileSize = 64;
		
		player = Player.createPlayer(new Vector());
		cameraOffset = new Vector();
		mousePos = new Vector();
		
		inventoryDisplay = new InventoryDisplay(player.getInventory());
		
		// Load the two images overlaid to convey darkness and lightness
		ImageIcon ii = new ImageIcon(this.getClass().getResource("img/darknessOverlay.png"));
		darknessOverlay = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource("img/lightOverlay.png"));
		lightOverlay = ii.getImage();
		
		// Load the damage overlay (blitted with varying alpha to indicate how damaged the player is)
		URL url = this.getClass().getResource("img/damageOverlay.png");
		damageOverlay = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		try {
			damageOverlay = ImageIO.read(url);
		} catch (IOException e) {System.out.println("Couldn't load damage overlay image...");}
		
		loadLevel();
		
		Collisions.setLevel(this);
	}
	
	public void updateDamageOverlay() {
		damageOverlayAlpha = 1.0 - (player.getHealth() / player.getBaseHealth());
		if (damageOverlayAlpha < 0.0) damageOverlayAlpha = 0.0;
		else if (damageOverlayAlpha > 1.0) damageOverlayAlpha = 1.0;
	}
	
	public boolean pointOnScreen(int x, int y) {
		// Returns true if a particular pixel co-ordinate (in world space) is on-screen
			// Transform to camera space
		int movedX = x - cameraOffset.x;
		int movedY = y - cameraOffset.y;
		
		return ((movedX >= 0 && movedX < width) && (movedY >= 0 && movedY < height));
	}
	
	public boolean pointOnScreen(Vector point) {
		return pointOnScreen(point.x, point.y);
	}
	
	public boolean tileOnScreen(Vector tile) {
		// Returns true if a particular tile (specified as a position vector) can be seen on-screen
			// Get its left, right, top and bottom positions
		int leftSide = tile.x * tileSize;
		int rightSide = leftSide + tileSize;
		int topSide = tile.y * tileSize;
		int bottomSide = topSide + tileSize;
		
			// Return true if one of its four corners is on-screen
		return (pointOnScreen(leftSide, topSide) || pointOnScreen(rightSide, topSide) || pointOnScreen(rightSide, bottomSide) || pointOnScreen(leftSide, bottomSide));
	}
	
	public void drawTiles(Graphics g, boolean clipOffScreen) {
		Graphics2D g2d = (Graphics2D) g;
		
		g.setColor(Color.WHITE);
		for (int row = 0; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				LevelTile tile = layout[row][col];
				
				// Only draw the tile if it has been 'explored' (player has been in this room)
				// Also only draw if the tile is on-screen or off-screen clipping is off
				if (tile.isExplored() && ((!clipOffScreen) || tileOnScreen(new Vector(col, row)))) {
					// Set up a transformation to draw the tile in the right place
					AffineTransform translate = new AffineTransform();
					translate.translate((col * tileSize) - cameraOffset.x, (row * tileSize) - cameraOffset.y);
					
					// Draw the tile's floor texture first
					Image texture = Textures.getTextureByID(tile.getTexIndex());
					g2d.drawImage(texture, translate, null);
					
					GameObject obj = tile.getObject();
					
					if (obj != null) {
						// Get the current object's texture and draw it
						Image objTexture = Textures.getTextureByID(obj.getTexIndex());
						g2d.drawImage(objTexture, translate, null);
					}
				}
			}
		}
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// Draw the level background
		drawTiles(g, true);
		
		// Draw enemies
		Enemy.drawEnemies(cameraOffset.negate(), g);
		
		// Draw the player
		player.draw(cameraOffset.negate(), g);
		
		// Draw effects
		Effects.draw(cameraOffset.negate(), g);
		
		// Draw the darkness vignette thingy
		if (player.hasLantern()) {
			g.drawImage(lightOverlay, 0, 0, null);
		}
		else g.drawImage(darknessOverlay, 0, 0, null);
		
		// Draw the damage overlay
		if (damageOverlayAlpha != 0.0) {
			// Save the current composite mode
			Composite oldComp = g2d.getComposite();
			// Create an AlphaComposite with the appropriate alpha and use it
			Composite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) damageOverlayAlpha);
			g2d.setComposite(alphaComp);
			// Draw the image
			g2d.drawImage(damageOverlay, 0, 0, null);
			// Restore the old composite
			g2d.setComposite(oldComp);
		}
		
		// Draw the inventory display
		inventoryDisplay.draw(g);
	}
	
	public void update() {
		player.update();  // Update the player
		updateDamageOverlay();
		
		ArrayList<Enemy> enemies = Enemy.getEnemies();
		
		// Update enemies which are on-screen
		Enemy curEnemy;
		for (int i = 0; i < enemies.size(); i++) {
			curEnemy = enemies.get(i);
			if (pointOnScreen(curEnemy.getCentrePos())) {
				curEnemy.update();
			}
		}
		
		// Update all running effects
		Effects.update();
		
		Vector playerGridPos = getGridCoords(player.getCentrePos());
		
		// Do collision detection and resolution
			// Player
		Collisions.handleCollisionsWithLevel(player);
		
			// Enemies
		for (int i = 0; i < enemies.size(); i++) {
			Collisions.handleCollisionsWithLevel(enemies.get(i));
		}
		
		// If the player has entered an unexplored area, set all the tiles contained within the area to explored
		if (layout[playerGridPos.y][playerGridPos.x].isExplored() == false) {
			explore(playerGridPos);
		}
		
		// Shift the camera and update the world coordinates for the mouse
		Vector playerCentre = player.getCentrePos();
		cameraOffset = new Vector(playerCentre.x - (width / 2), playerCentre.y - (height / 2));
		refreshMouseWorldCoords();
	}
	
	private void explore(Vector tile) {
		if (isInGrid(tile.x, tile.y)) {
			LevelTile curTile = layout[tile.y][tile.x];
			
			// If the selected tile is currently unexplored
			if (curTile.isExplored() == false) {
				// Set it to explored
				curTile.setExplored(true);
				
				GameObject obj = curTile.getObject();
				
				// If this tile's object is not see-through, stop the exploration recursion here
				if (obj != null) {
					if (obj.isSeeThrough() == false) {
						return;
					}
				}
				// Otherwise, explore the surrounding tiles
				ArrayList<Vector> surroundingTiles = getSurroundingTiles(tile);
				
				for (int i = 0; i < surroundingTiles.size(); i++) {
					explore(surroundingTiles.get(i));
				}
			}
		}
	}
	
	private static String getXMLTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
		
		if (nValue != null) return nValue.getNodeValue();
		else return "";
	}

	
	public void loadObjects() {
		// Get the objects file as a datastream
		InputStream inStream = this.getClass().getResourceAsStream("lvl/test_objects.xml");
		
		// Create the document builder
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println("Parser configuration exception!");
		}
		
		// Use the document builder to parse the XML and load the result into 'doc'
		Document doc = null;
		try {
			doc = dBuilder.parse(inStream);
		} catch (SAXException e) {
			System.out.println("SAX exception!");
		} catch (Exception e) {
			System.out.println("General exception of some sort in parsing the XML file - damned if I know... " + e.getMessage());
		}
		doc.getDocumentElement().normalize();
		
		// Get the list of 'object' nodes
		NodeList nList = doc.getElementsByTagName("object");
		Node curNode;
		Element curElement;
		
		// Load the nodes into objects
		for (int i = 0; i < nList.getLength(); i++) {
			curNode = nList.item(i);
			curElement = (Element) curNode;
			
			String type = getXMLTagValue("type", curElement);  // Item type - door/container/examinable
			
			GameObject newObject = null;  // The object to be added
			int id = Integer.parseInt(curElement.getAttribute("id"));  // The id of the object,
					// which determines its index in the array and is used when referencing it
			
			if (type.equals("door")) {  // Doors
				// Is it locked?
				String isLockedRaw = getXMLTagValue("locked", curElement);
				boolean isLocked = (isLockedRaw.equals("true"));
				// The item id of its key (0 => no key)
				int key = Integer.parseInt(getXMLTagValue("key", curElement));
				// Description to be shown when the player attempts to open it with no key
				String lockedDescription = getXMLTagValue("lockedDescription", curElement);
				newObject = new Door(isLocked, key, lockedDescription, Constants.HORIZONTAL);
			}
			
			else if (type.equals("container")) {  // Containers
				// Get the index of this container's texture
				int texIndex = Integer.parseInt(getXMLTagValue("texIndex", curElement));
				// 0 means use the generic container texture
				if (texIndex == 0) texIndex = Constants.TEX_INDEX_GENERICCONTAINER;
				// Is it locked? There's only one way to find out!
				String isLockedRaw = getXMLTagValue("locked", curElement);
				boolean isLocked = (isLockedRaw.equals("true"));
				// Wat key?
				int key = Integer.parseInt(getXMLTagValue("key", curElement));
				// I'm just repeating the comments from 'door' here...
				String lockedDescription = getXMLTagValue("lockedDescription", curElement);
				// Index of the item contained within
				int containedItem = Integer.parseInt(getXMLTagValue("containedItem", curElement));
				newObject = new Container(texIndex, isLocked, key, lockedDescription, containedItem);
			}
			
			else if (type.equals("examinable")) {  // Examinable object
				// Same old texIndex, as above
				int texIndex = Integer.parseInt(getXMLTagValue("texIndex", curElement));
				// Description to be displayed on action keypress
				String description = getXMLTagValue("description", curElement);
				// Is it solid? (In a physics sense, see GameObject class)
				String isSolidRaw = getXMLTagValue("solid", curElement);
				boolean isSolid = (isSolidRaw.equals("true"));
				String isSeeThroughRaw = getXMLTagValue("seeThrough", curElement);
				boolean isSeeThrough = (isSeeThroughRaw.equals("true"));
				newObject = new Examinable(texIndex, description, isSolid, isSeeThrough);
			}
			
			objects[id] = newObject;
		}
	}
	
	private void updateDoorRotations() {
		// Sort out door rotations so that they face the appropriate directions
		
		// For each tile in the grid, check if it holds a door
		GameObject curObject;
		Door curDoor;
		
		for (int row = 0; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				curObject = layout[row][col].getObject();
				
				// If this tile has an object
				if (curObject != null) {
					if (Door.class.isAssignableFrom(curObject.getClass())) {
						curDoor = (Door) curObject;  // If it's a door, doorify that shizz
						
						// Now check the tiles to the left and right of the door for an excuse to switch it to vertical
						// (Horizontal is default)
						GameObject obj;
						
						// Checkin' to the right
						if (isInGrid(col + 1, row)) {
							obj = layout[row][col + 1].getObject();
							
							if (obj != null) {
								if (obj.isSolid()) {
									// Solid object on one side, so the door should probably be vertical instead
									curDoor.setDirection(Constants.VERTICAL);
								}
							}
						}
						
						// Checkin' to the left
						if (isInGrid(col - 1, row)) {
							obj = layout[row][col - 1].getObject();
							
							if (obj != null) {
								if (obj.isSolid()) {
									curDoor.setDirection(Constants.VERTICAL);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void propagateFloorTextures() {
		// Tiles holding GameObjects cannot also be given floor textures in the layout file
		// Thus, this function is required to ensure these tiles share the same floor texture as
		// their neighbours.
		
		LevelTile curTile;
		
		for (int row = 0; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				curTile = layout[row][col];
				
				// If the current tile has been assigned the default textures
				if (curTile.getTexIndex() == Constants.TEX_INDEX_FLOOR_BASE) {					
					// Check each surrounding square in turn - if it has no object, it will have
					// been assigned a floor texture so copy dat shizz!
					// Trying right and down first as doors face left and up and this approach
					// leads to them copying the texture from the correct side
					if (isInGrid(col + 1, row) && layout[row][col + 1].getObject() == null) {
						curTile.setTexIndex(layout[row][col + 1].getTexIndex());
					}
					else if (isInGrid(col, row + 1) && layout[row + 1][col].getObject() == null) {
						curTile.setTexIndex(layout[row + 1][col].getTexIndex());
					}
					else if (isInGrid(col - 1, row) && layout[row][col - 1].getObject() == null) {
						curTile.setTexIndex(layout[row][col - 1].getTexIndex());
					}
					else if (isInGrid(col, row - 1) && layout[row - 1][col].getObject() == null) {
						curTile.setTexIndex(layout[row - 1][col].getTexIndex());
					}
				}
			}
		}
	}
	
	public void loadLevel() {
		// Load the level image
		URL url = this.getClass().getResource("lvl/test.png");
		BufferedImage levelImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		try {
			levelImage = ImageIO.read(url);
		} catch (IOException e) {System.out.println("Couldn't load level image...");}
		
		gridWidth = levelImage.getWidth(null);
		gridHeight = levelImage.getHeight(null);
		
		layout = new LevelTile[gridHeight][gridWidth];
		
		Textures.loadTextures();
		
		objects = new GameObject[255];
		loadObjects();
		
		ItemManager.loadItems();
		
		for (int row = 0; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				// Get the current pixel's colour and split it into RGB components
				int colour = levelImage.getRGB(col, row);
				int r = (colour >> 16) & 0xff;
				int g = (colour >> 8) & 0xff;
				int b = colour & 0xff;
				
				// Create a new level tile
				LevelTile current = new LevelTile(null, Constants.TEX_INDEX_FLOOR_BASE, new Vector(col * tileSize, row * tileSize), new Vector(tileSize, tileSize));
				
				// Work out what should be in this tile based on the colour
				if (r == 0 && g == 0 && b == 0) {
					// Empty space - the level tile needn't be added to
				}
				else if (r == 255 && g == 255 && b == 255) {
					// Pure white - add a wall
					GameObject wall = new Wall();
					current.setObject(wall);
				}
				else if (r == g && g == b) {
					// Greyscale - add a clone of the object associated with this id
					int id = r;
					current.setObject(objects[id].clone());
				}
				else if (r == 0 && g == 255 && b == 0) {
					// Pure green - this is a player start point
					player.setPos(new Vector(col * tileSize, row * tileSize));
				}
				else if (r != 0 && g == 0 && b == 0) {
					// Shades of red - specify enemies (ids stored in Constants.java)
					int id = r;
					Enemy.createEnemy(id, new Vector(col * tileSize, row * tileSize));
				}
				else if (r == 1 && g == 1) {
					// Red 1, green 1, blue 0-255 - this is a blank tile specifying the floor texture
					int texIndex = Constants.TEX_INDEX_FLOOR_BASE + b;
					current.setTexIndex(texIndex);
				}
				
				layout[row][col] = current;
			}
		}
		
		updateDoorRotations();
		propagateFloorTextures();
	}
	
	public LevelTile getTile(int x, int y) {
		if (isInGrid(x, y)) return layout[y][x];
		else return null;
	}
	
	public boolean isTileSolid(int x, int y) {
		if (isInGrid(x, y)) {
			GameObject obj = layout[y][x].getObject();
			if (obj != null) return (obj.isSolid());
			else return false;
		}
		else return false;
	}
	
	public Vector getGridCoords(Vector coords) {
		return new Vector((int) Math.floor(coords.x / tileSize), (int) Math.floor(coords.y / tileSize));
	}
	
	public boolean isInGrid(int x, int y) {
		return ((x >= 0) && (x < gridWidth) && (y >= 0) && (y < gridHeight));
	}
	
	public ArrayList<Vector> getSurroundingTiles(Vector coords) {
		ArrayList<Vector> possibles = new ArrayList<Vector>();
		
		if (isInGrid(coords.x, coords.y)) possibles.add(coords);
		if (isInGrid(coords.x - 1, coords.y)) possibles.add(new Vector(coords.x - 1, coords.y));
		if (isInGrid(coords.x + 1, coords.y)) possibles.add(new Vector(coords.x + 1, coords.y));
		if (isInGrid(coords.x, coords.y - 1)) possibles.add(new Vector(coords.x, coords.y - 1));
		if (isInGrid(coords.x, coords.y + 1)) possibles.add(new Vector(coords.x, coords.y + 1));
		if (isInGrid(coords.x - 1, coords.y - 1)) possibles.add(new Vector(coords.x - 1, coords.y - 1));
		if (isInGrid(coords.x + 1, coords.y - 1)) possibles.add(new Vector(coords.x + 1, coords.y - 1));
		if (isInGrid(coords.x - 1, coords.y + 1)) possibles.add(new Vector(coords.x - 1, coords.y + 1));
		if (isInGrid(coords.x + 1, coords.y + 1)) possibles.add(new Vector(coords.x + 1, coords.y + 1));
		
		return possibles;
	}
	
	public Vector getLookedAtTile(Vector coords, double angle) {
		// Determine whether the direction is looking generally right or left, and generally up or down
		boolean lookingUp = ((angle >= (-0.5 * Math.PI) && angle <= (0.5 * Math.PI)));
		boolean lookingRight = (angle >= 0.0 && angle <= Math.PI);
		
		Vector bestTile = new Vector();
		
		// Test up, right, down, left, upright, downright, downleft and upleft in sequence
		if (angle >= (-0.2 * Math.PI) && angle <= (0.2 * Math.PI)) bestTile = new Vector(coords.x, coords.y - 1);
		else if (angle >= (0.3 * Math.PI) && angle <= (0.7 * Math.PI)) bestTile = new Vector(coords.x + 1, coords.y);
		else if (angle >= (0.8 * Math.PI) && angle <= (1.2 * Math.PI)) bestTile = new Vector(coords.x, coords.y + 1);
		else if ((angle >= (1.3 * Math.PI) && angle <= (1.7 * Math.PI)) || (angle <= (-0.3 * Math.PI))) bestTile = new Vector(coords.x - 1, coords.y);
		else if (lookingUp && lookingRight) bestTile = new Vector(coords.x + 1, coords.y - 1);
		else if (!lookingUp && lookingRight) bestTile = new Vector(coords.x + 1, coords.y + 1);
		else if (!lookingUp && !lookingRight) bestTile = new Vector(coords.x - 1, coords.y + 1);
		else if (lookingUp && !lookingRight) bestTile = new Vector(coords.x - 1, coords.y - 1);
		
		// If the looked-at tile is in the grid, return it; otherwise just return the supplied grid space
		if (isInGrid(bestTile.x, bestTile.y)) return bestTile;
		else return new Vector();
	}
	
	public void refreshMouseWorldCoords() {
		// Translate the mouse position from camera space into world space
		Mouse.setWorldX(Mouse.getScreenX() + cameraOffset.x);
		Mouse.setWorldY(Mouse.getScreenY() + cameraOffset.y);
	}
	
	public void handleKeyPress(KeyEvent e) {
		player.handleKeyPress(e);
		
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_SPACE) {
			// Get the player's location and from that work out the tile they are looking at
			Vector playerGridPos = getGridCoords(player.getCentrePos());
			Vector viewedTile = getLookedAtTile(playerGridPos, player.getAngle());
			
			// If the tile they are looking at contains an object, send the action keypress to it
			LevelTile levelTile = layout[viewedTile.y][viewedTile.x];
			GameObject obj = levelTile.getObject();
			if (obj != null) {
				obj.handleActionKeyPress();
			}
		}
	}
	
	public void handleKeyRelease(KeyEvent e) {
		player.handleKeyRelease(e);
	}
	
	public void handleMouseMotion(MouseEvent e) {
		refreshMouseWorldCoords();
		player.handleMouseMotion(e);
		inventoryDisplay.handleMouseMotion(e);
	}
	
	public void handleMouseClick(MouseEvent e) {
		inventoryDisplay.handleMouseClick(e);
	}
}