// MessageBox.java

package escape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;

import java.util.ArrayList;


public class MessageBox {
	private String message;
	private Vector pos;
	private Vector dimensions;
	private Vector screenDimensions;
	
	private final int textLineSpacing = 10;
	private final int textSideSpacing = 10;
	
	private ArrayList<String> lines;
	
	public MessageBox(String message, Vector pos, Vector dimensions, Vector screenDimensions) {
		this.message = message;
		this.pos = pos;
		this.dimensions = dimensions;
		this.screenDimensions = screenDimensions;
		lines = null;
	}
	
	public MessageBox(String message, Vector dimensions, Vector screenDimensions) {
		this.message = message;
		this.pos = new Vector();
		this.dimensions = dimensions;
		this.screenDimensions = screenDimensions;
		centre();
		lines = null;
	}
	
	public void setPos(Vector newPos) {pos = newPos;}
	public void setX(int newX) {pos.x = newX;}
	public void setY(int newY) {pos.y = newY;}
	public int getX() {return pos.x;}
	public int getY() {return pos.y;}
	
	public void setDimensions(Vector newDimensions) {dimensions = newDimensions;}
	public void setWidth(int newWidth) {dimensions.x = newWidth;}
	public void setHeight(int newHeight) {dimensions.y = newHeight;}
	public int getWidth() {return dimensions.x;}
	public int getHeight() {return dimensions.y;}
	
	public void centreX() {
		// Centre on the x-axis
		pos.x = (screenDimensions.x / 2) - (dimensions.x / 2);
	}
	
	public void centreY() {
		// Centre on the y-axis
		pos.y = (screenDimensions.y / 2) - (dimensions.y / 2);
	}
	
	public void centre() {
		// Centre on both axes
		centreX();
		centreY();
	}
	
	private void initLines(Graphics g) {  // Word-wraps the messages and stores the created lines thereof in 'lines'
		lines = new ArrayList<String>();
		
		// Work out the maximum line width
		int maxLineWidth = dimensions.x - (2 * textSideSpacing);
		
		// Determine the height of a line of text
		FontMetrics fm = g.getFontMetrics();
		int fontHeight = fm.getHeight();
		
		int curLineWidth = 0;  // Total width of the line so far (in pixels)
		String curLine = new String("");  // The line currently being written
		String curWord = new String("");  // The word currently being written
		int curCharWidth = 0;  // The width of the current character
		
		for (int i = 0; i < message.length(); i++) {
			curCharWidth = fm.charWidth(message.charAt(i));
			curLineWidth += curCharWidth;  // Add the current character's width to the total
			
			// If this character is a backslash and the next is an 'n', newline it
			if (Character.toString(message.charAt(i)).equals("\\")) {
				if ((i < message.length() - 1) && (Character.toString(message.charAt(i + 1)).equals("n"))) {
					i += 2;
					curLine = curLine.concat(curWord);
					curWord = new String("");
					lines.add(curLine);
					curLineWidth = 0;
					curLine = new String("");
				}
			}
			
			// If this character takes us past the maximum line length, start a new line
			// and add the current one to the list
			if (curLineWidth > maxLineWidth) {
				curLineWidth = curCharWidth;
				lines.add(curLine);
				curLine = new String("");
			}
			
			// If the character is a space, add this word to the end of the current line
			// (with a space attached) and start a new word
			if (Character.toString(message.charAt(i)).equals(" ")) {
				curLine = curLine.concat(curWord.concat(" "));
				curWord = new String("");
			}
			// Otherwise, just add the current character to the word
			else curWord = curWord.concat(Character.toString(message.charAt(i)));
		}
		
		// Add this last word to the line and add the line to the lines list
		curLine = curLine.concat(curWord);
		lines.add(curLine);
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(pos.x, pos.y, dimensions.x, dimensions.y);
		
		if (lines == null) initLines(g2d);
		
		int textPosX = pos.x + textSideSpacing;
		int textPosY = pos.y + textLineSpacing;
		
		FontMetrics fm = g.getFontMetrics();
		int fontHeight = fm.getHeight();
		
		for (int i = 0; i < lines.size(); i++) {
			textPosY += fontHeight;
			
			g2d.setPaint(Color.WHITE);
			g.drawString(lines.get(i), textPosX, textPosY);
			
			textPosY += textLineSpacing;
		}
	}
}