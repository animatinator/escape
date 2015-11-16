// LevelTile.java

package escape;


class LevelTile implements CollisionObstacle {
	private GameObject object;
	private boolean explored;
	private int texIndex;
	private Vector pos;
	private Vector dimensions;
	
	public LevelTile(GameObject object, int texIndex, Vector pos, Vector dimensions) {
		this.object = object;
		this.texIndex = texIndex;
		this.explored = false;
		this.pos = pos;
		this.dimensions = dimensions;
	}
	
	public GameObject getObject() {return object;}
	public void setObject(GameObject newObject) {object = newObject;}
	public int getTexIndex() {return texIndex;}
	public void setTexIndex(int newTexIndex) {texIndex = newTexIndex;}
	public boolean isExplored() {return explored;}
	public void setExplored(boolean newVal) {explored = newVal;}
	public Vector getPos() {return pos;}
	public Vector getDimensions() {return dimensions;}
}