// Keyboard.java

package escape;

import java.awt.event.KeyEvent;


class Keyboard {
	public static boolean SHIFT;
	public static boolean LEFT;
	public static boolean RIGHT;
	public static boolean UP;
	public static boolean DOWN;
	public static boolean W;
	public static boolean A;
	public static boolean S;
	public static boolean D;
	
	// Initialise all keys to false
	static {
		SHIFT = false;
		LEFT = false;
		RIGHT = false;
		UP = false;
		DOWN = false;
		W = false;
		A = false;
		S = false;
		D = false;
	}
	
	public static void handleKeyPress(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_W) {
			W = true;
		}
		if (key == KeyEvent.VK_A) {
			A = true;
		}
		if (key == KeyEvent.VK_S) {
			S = true;
		}
		if (key == KeyEvent.VK_D) {
			D = true;
		}
		if (key == KeyEvent.VK_SHIFT) {
			SHIFT = true;
		}
		if (key == KeyEvent.VK_UP) {
			UP = true;
		}
		if (key == KeyEvent.VK_DOWN) {
			DOWN = true;
		}
		if (key == KeyEvent.VK_LEFT) {
			LEFT = true;
		}
		if (key == KeyEvent.VK_RIGHT) {
			RIGHT = true;
		}
	}
	
	public static void handleKeyRelease(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_W) {
			W = false;
		}
		if (key == KeyEvent.VK_A) {
			A = false;
		}
		if (key == KeyEvent.VK_S) {
			S = false;
		}
		if (key == KeyEvent.VK_D) {
			D = false;
		}
		if (key == KeyEvent.VK_SHIFT) {
			SHIFT = false;
		}
		if (key == KeyEvent.VK_UP) {
			UP = false;
		}
		if (key == KeyEvent.VK_DOWN) {
			DOWN = false;
		}
		if (key == KeyEvent.VK_LEFT) {
			LEFT = false;
		}
		if (key == KeyEvent.VK_RIGHT) {
			RIGHT = false;
		}
	}
}