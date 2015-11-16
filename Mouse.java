// Mouse.java

package escape;


class Mouse {
	private static int screenX;
	private static int screenY;
	private static int worldX;
	private static int worldY;
	
	private static boolean leftButton;
	private static boolean rightButton;
	
	public static int getScreenX() {return screenX;}
	public static int getScreenY() {return screenY;}
	public static int getWorldX() {return worldX;}
	public static int getWorldY() {return worldY;}
	public static void setScreenX(int newX) {screenX = newX;}
	public static void setScreenY(int newY) {screenY = newY;}
	public static void setWorldX(int newX) {worldX = newX;}
	public static void setWorldY(int newY) {worldY = newY;}
	
	public static boolean leftButtonHeld() {return leftButton;}
	public static boolean rightButtonHeld() {return rightButton;}
	public static void setLeftButton(boolean status) {leftButton = status;}
	public static void setRightButton(boolean status) {rightButton = status;}
}