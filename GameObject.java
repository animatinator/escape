// GameObject.java

package escape;

import java.lang.CloneNotSupportedException;


public abstract class GameObject implements Cloneable {
	protected boolean solid;  // Can the player walk through it?
	protected boolean seeThrough;  // Can the player see through it?
	// (Used for restricting the range of rooms the player can see to those they have already explored)
	protected int texIndex;
	
	public GameObject(int texIndex) {
		// By default, an object is non-solid and can be seen through
		solid = false;
		seeThrough = true;
		this.texIndex = texIndex;
	}
	
	public boolean isSolid() {return solid;}
	public void setSolid(boolean newbool) {solid = newbool;}
	public boolean isSeeThrough() {return seeThrough;}
	public void setSeeThrough(boolean newVal) {seeThrough = newVal;}
	public int getTexIndex() {return texIndex;}
	public void setTexIndex(int newTexIndex) {texIndex = newTexIndex;}
	
	public GameObject clone() {
		GameObject cloned;
		try {
			cloned = (GameObject) super.clone();
		} catch (CloneNotSupportedException e) {cloned = null;}
		
		return cloned;
	}
	
	public abstract void handleActionKeyPress();
}