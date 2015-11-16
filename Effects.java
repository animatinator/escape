// Effects.java

package escape;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Effects {
	private static ArrayList<Effect> effects;
	
	static {
		effects = new ArrayList<Effect>();
	}
	
	public static void createFlashEffect(Vector pos, String imageName, int duration) {
		effects.add(new FlashEffect(pos, imageName, duration));
	}
	
	public static void createLineEffect(Vector startPos, Vector endPos, int duration) {
		effects.add(new LineEffect(startPos, endPos, duration));
	}
	
	public static void draw(Vector offset, Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		Effect current;
		
		for (int i = 0; i < effects.size(); i++) {
			current = effects.get(i);
			
			if (current.isRunning()) {
				current.draw(offset, g2d);
			}
		}
	}
	
	public static void update() {
		Effect current;
		
		for (int i = 0; i < effects.size(); i++) {
			current = effects.get(i);
			
			if (current.isRunning()) {
				current.update();
			}
		}
	}
}