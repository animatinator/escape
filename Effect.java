// Effect.java

package escape;

import java.awt.Graphics2D;


public abstract class Effect {
	protected boolean running;
	protected int duration;
	protected int timer;
	
	public Effect(int duration) {
		timer = 0;
		this.duration = duration;
	}
	
	public abstract void draw(Vector offset, Graphics2D g2d);
	public abstract void update();
	public boolean isRunning() {return running;}
}