// LineEffect.java

package escape;

import java.awt.Graphics2D;
import java.awt.Color;


public class LineEffect extends Effect {
	private Vector startPoint;
	private Vector endPoint;
	private double alpha;
	
	public LineEffect(Vector pos1, Vector pos2, int duration) {
		super(duration);
		
		startPoint = pos1;
		endPoint = pos2;
		alpha = 1.0;
		
		running = true;
	}
	
	private void calculateAlpha() {
		alpha = 1.0 - ((double) timer / (double) duration);
		if (alpha < 0.0) alpha = 0.0;
		if (alpha > 1.0) alpha = 1.0;
	}
	
	public void draw(Vector offset, Graphics2D g2d) {
		g2d.setPaint(new Color(Constants.LINE_COLOUR_R, Constants.LINE_COLOUR_G, Constants.LINE_COLOUR_B, (float) alpha));
		
		int x1 = startPoint.x + offset.x;
		int x2 = endPoint.x + offset.x;
		int y1 = startPoint.y + offset.y;
		int y2 = endPoint.y + offset.y;
		
		g2d.drawLine(x1, y1, x2, y2);
	}
	
	public void update() {
		timer++;
		calculateAlpha();
		
		if (timer >= duration) running = false;
	}
}