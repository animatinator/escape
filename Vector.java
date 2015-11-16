// Vector.java

package escape;

class Vector {
	public int x;
	public int y;
	
	public Vector() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(int[] pos) {
		this.x = pos[0];
		this.y = pos[1];
	}
	
	public Vector negate() {
		return new Vector(-x, -y);
	}
}