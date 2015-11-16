// Main.java

package escape;

import javax.swing.JFrame;


public class Main extends JFrame {
	private int width;
	private int height;
	
	public Main() {
		width = 800;
		height = 600;
		
		add(new Game(width, height));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width, height);
		setTitle("Escape");
		setLocationRelativeTo(null);
		setLocation(1500, 100);  // Temporary; used for displaying the game on second screen
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Main();
	}
}