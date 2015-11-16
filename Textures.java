// Textures.java

package escape;

import java.awt.Image;
import javax.swing.ImageIcon;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class Textures {
	private static Image[] textures;
	
	private static Image loadImage(String filename) {
		ImageIcon ii = new ImageIcon(new Textures().getClass().getResource("img/" + filename));
		Image theImage = ii.getImage();
		
		return theImage;
	}
	
	public static void loadTextures() {
		textures = new Image[512];
		
		// Get the textures file as a datastream
		InputStream inStream = new Textures().getClass().getResourceAsStream("lvl/textures.xml");
		
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
		
		// Get the list of 'texture' nodes
		NodeList nList = doc.getElementsByTagName("texture");
		Node curNode;
		Element curElement;
		
		// Load the images associated with the nodes into the texture array
		for (int i = 0; i < nList.getLength(); i++) {
			curNode = nList.item(i);
			curElement = (Element) curNode;
			
			int id = Integer.parseInt(curElement.getAttribute("id"));
			String fileName = curElement.getChildNodes().item(0).getNodeValue();
			Image newImage = loadImage(fileName);
			textures[id] = newImage;
		}
	}
	
	public static Image getTextureByID(int index) {
		return textures[index];
	}
}