// Messages.java

package escape;


public class Messages {
	private static MessageDisplay display;  // The object which displays messages
	private static MessageSender returnObject;  // The object to which yes/no input should be returned
	private static boolean awaitingInput;  // True when a message requiring input is being displayed
	
	static {
		display = null;
		returnObject = null;
		awaitingInput = false;
	}
	
	public static void registerMessageDisplay(MessageDisplay newDisp) {display = newDisp;}
	
	public static void sendSimpleMessage(String message) {
		// Pop up a text-based message
		if (display != null) {
			if (!awaitingInput) {
				display.showSimpleMessage(message);
			}
		}
	}
	
	public static void sendInterruptingMessage(String message) {
		// Pop up a message which awaits action key input before vanishing
		if (display != null) {
			if (!awaitingInput) {
				display.showInterruptingMessage(message);
				awaitingInput = true;
			}
		}
	}
	
	public static void askYesNo(String question, MessageSender theReturnObject) {
		// Ask a yes/no question
		if (display != null) {
			if (!awaitingInput) {
				display.askYesNo(question);
				awaitingInput = true;
				returnObject = theReturnObject;
			}
		}
	}
	
	public static void handleYesNoReturn(boolean value) {
		// Called by the message display when a yes/no response has been provided
		returnObject.handleYesNoReturn(value);
		returnObject = null;
		awaitingInput = false;
	}
	
	public static void endCurrentInterrupt() {
		// Called by the message display when an interrupting message can stop being displayed
		awaitingInput = false;
	}
}