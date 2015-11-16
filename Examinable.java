// Examinable.java

package escape;


class Examinable extends GameObject {
	private String description;
	
	public Examinable(int texIndex, String description, boolean isSolid, boolean isSeeThrough) {
		super(texIndex);
		
		this.description = description;
		this.solid = isSolid;
		this.seeThrough = isSeeThrough;
	}
	
	public void handleActionKeyPress() {
		Messages.sendSimpleMessage(description);
	}
}