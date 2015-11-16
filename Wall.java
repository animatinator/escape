// Wall.java

package escape;


public class Wall extends GameObject {
	Wall() {
		super(0);
		
		// Walls are solid and you can't generally see through them
		this.solid = true;
		this.seeThrough = false;
		this.texIndex = Constants.TEX_INDEX_WALL;
	}
	
	public void handleActionKeyPress() {
		// Do absolutely nothing. This is a wall, fool.
	}
}