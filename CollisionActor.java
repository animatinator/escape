// CollisionActor.java

package escape;


public interface CollisionActor {
	public Vector getDirection();
	public Vector getPos();
	public void setPos(Vector newPos);
	public Vector getCentrePos();
	public Vector getDimensions();
}