// MessageDisplay.java

package escape;


public interface MessageDisplay {
	public void showSimpleMessage(String message);
	public void showInterruptingMessage(String message);
	public void askYesNo(String question);
}