// FlashEffect.java

package escape;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


public class FlashEffect extends Effect {
	private Vector pos;
	private Image flashImage;
	private int imageWidth, imageHeight;
	private double alpha;
	private Vector drawPos;
	
	public FlashEffect(Vector pos, String imageName, int duration) {
		super(duration);
		this.pos = pos;
		
		ImageIcon ii = new ImageIcon(this.getClass().getResource("img/" + imageName));
		flashImage = ii.getImage();
		flashImage.getWidth(null); // Prod
		imageWidth = flashImage.getWidth(null);
		imageHeight = flashImage.getHeight(null);
		alpha = 1.0;
		
		timer = 0;
		running = true;
		
		calculateDrawPos();
	}
	
	private void calculateDrawPos() {
		drawPos = new Vector(pos.x - (imageWidth / 2), pos.y - (imageHeight / 2));
	}
	
	private void calculateAlpha() {
		alpha = 1.0 - (double) (1.0 - (timer / duration));
		if (alpha < 0.0) alpha = 0.0;
		if (alpha > 1.0) alpha = 1.0;
	}
	
	public void draw(Vector offset, Graphics2D g2d) {
		Vector transformedDrawPos = new Vector(drawPos.x + offset.x, drawPos.y + offset.y);
		AffineTransform t = new AffineTransform();
		t.translate((double) transformedDrawPos.x, (double) transformedDrawPos.y);
			// And draw
		g2d.drawImage(flashImage, t, null);
	}
	
	public void update() {
		timer++;
		calculateAlpha();
		
		if (timer >= duration) running = false;
	}
}