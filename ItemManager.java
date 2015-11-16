// ItemManager.java

package escape;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.InputStream;


public class ItemManager {
	// Stores all available items indexed by id
	private static Item[] availableItems;
	
	static {
		availableItems = new Item[255];
	}
	
	private static String getXMLTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
		
		if (nValue != null) return nValue.getNodeValue();
		else return "";
	}
	
	public static void loadItems() {
		// Get the items file as a datastream
		InputStream inStream = new ItemManager().getClass().getResourceAsStream("lvl/items.xml");
		
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
		
		// Get the list of 'item' nodes
		NodeList nList = doc.getElementsByTagName("item");
		Node curNode;
		Element curElement;
		
		// Load the nodes into items
		for (int i = 0; i < nList.getLength(); i++) {
			curNode = nList.item(i);
			curElement = (Element) curNode;
			
			String type = getXMLTagValue("type", curElement);
			
			Item newItem = null;
			int id = Integer.parseInt(curElement.getAttribute("id"));
			
			int itemType = 0;
			if (type.equals("key")) itemType = Constants.ID_KEY;
			else if (type.equals("upgrade")) itemType = Constants.ID_UPGRADE;
			else if (type.equals("weapon")) itemType = Constants.ID_WEAPON;
			else if (type.equals("supply")) itemType = Constants.ID_SUPPLY;
			
			// TODO: Add handling for all types of items (will require other item types to have subclasses made for them first)
			if ((itemType == Constants.ID_KEY) || (itemType == Constants.ID_UPGRADE)) {  // Keys and upgrades (treated the same way)
				itemType = Constants.ID_KEY;
				String name = getXMLTagValue("name", curElement);
				String description = getXMLTagValue("description", curElement);
				int texIndex = Integer.parseInt(getXMLTagValue("texIndex", curElement));
				newItem = new Item(id, itemType, texIndex, name, description);
			}
			
			availableItems[id] = newItem;
		}
		
		// Create weapons
			// Sword
		Weapon sword = new Weapon(Constants.ID_WEAPON_SWORD, Constants.TEX_INDEX_SWORD, Constants.SWORD_NAME, Constants.SWORD_DESCRIPTION, Constants.SWORD_DAMAGE, Constants.SWORD_RANGE, Constants.SWORD_REPEATDELAY, Constants.SWORD_BASEAMMO);
		availableItems[Constants.ID_WEAPON_SWORD] = sword;
			// Pistol
		Weapon pistol = new Weapon(Constants.ID_WEAPON_PISTOL, Constants.TEX_INDEX_PISTOL, Constants.PISTOL_NAME, Constants.PISTOL_DESCRIPTION, Constants.PISTOL_DAMAGE, Constants.PISTOL_RANGE, Constants.PISTOL_REPEATDELAY, Constants.PISTOL_BASEAMMO);
		availableItems[Constants.ID_WEAPON_PISTOL] = pistol;
			// Shotgun
		Weapon shotgun = new Weapon(Constants.ID_WEAPON_SHOTGUN, Constants.TEX_INDEX_SHOTGUN, Constants.SHOTGUN_NAME, Constants.SHOTGUN_DESCRIPTION, Constants.SHOTGUN_DAMAGE, Constants.SHOTGUN_RANGE, Constants.SHOTGUN_REPEATDELAY, Constants.SHOTGUN_BASEAMMO);
		availableItems[Constants.ID_WEAPON_SHOTGUN] = shotgun;
			// Machine gun
		Weapon machineGun = new Weapon(Constants.ID_WEAPON_MACHINEGUN, Constants.TEX_INDEX_MACHINEGUN, Constants.MACHINEGUN_NAME, Constants.MACHINEGUN_DESCRIPTION, Constants.MACHINEGUN_DAMAGE, Constants.MACHINEGUN_RANGE, Constants.MACHINEGUN_REPEATDELAY, Constants.MACHINEGUN_BASEAMMO);
		availableItems[Constants.ID_WEAPON_MACHINEGUN] = machineGun;
			// Rocket launcher
		Weapon rocketLauncher = new Weapon(Constants.ID_WEAPON_ROCKETLAUNCHER, Constants.TEX_INDEX_ROCKETLAUNCHER, Constants.ROCKETLAUNCHER_NAME, Constants.ROCKETLAUNCHER_DESCRIPTION, Constants.ROCKETLAUNCHER_DAMAGE, Constants.ROCKETLAUNCHER_RANGE, Constants.ROCKETLAUNCHER_REPEATDELAY, Constants.ROCKETLAUNCHER_BASEAMMO);
		availableItems[Constants.ID_WEAPON_ROCKETLAUNCHER] = rocketLauncher;
		
		// Create upgrades
			// Lantern
		Upgrade lantern = new LanternUpgrade(Constants.ID_UPGRADE_LANTERN, Constants.TEX_INDEX_LANTERN, Constants.LANTERN_NAME, Constants.LANTERN_DESCRIPTION);
		availableItems[Constants.ID_UPGRADE_LANTERN] = lantern;
			// Body armour
		Upgrade bodyArmour = new BodyArmourUpgrade(Constants.ID_UPGRADE_BODYARMOUR, Constants.TEX_INDEX_BODYARMOUR, Constants.BODYARMOUR_NAME, Constants.BODYARMOUR_DESCRIPTION);
		availableItems[Constants.ID_UPGRADE_BODYARMOUR] = bodyArmour;
			// Running shoes
		Upgrade runningShoes = new RunningShoesUpgrade(Constants.ID_UPGRADE_RUNNINGSHOES, Constants.TEX_INDEX_RUNNINGSHOES, Constants.RUNNINGSHOES_NAME, Constants.RUNNINGSHOES_DESCRIPTION);
		availableItems[Constants.ID_UPGRADE_RUNNINGSHOES] = runningShoes;
	}
	
	public static Item getItem(int id) {
		return availableItems[id];
	}
}