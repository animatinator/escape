// Ray.java

package escape;


public class Ray {
	private Vector startPos;
	private double normX, normY;
	private double range;
	
	public Ray(Vector pos, double normX, double normY, double range) {
		startPos = pos;
		this.normX = normX;
		this.normY = normY;
		this.range = range;
	}
	
	public Vector getStartPos() {return startPos;}
	public void setStartPos(Vector newStartPos) {startPos = newStartPos;}
	public double getNormalX() {return normX;}
	public void setNormalX(double newNormX) {normX = newNormX;}
	public double getNormalY() {return normY;}
	public void setNormalY(double newNormY) {normY = newNormY;}
	public double[] getNormal() {
		double[] norm = {normX, normY};
		return norm;
	}
	public double getRange() {return range;}
	public void setRange(double newRange) {range = newRange;}
}