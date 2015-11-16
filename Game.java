// Game.java

package escape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;
import javax.swing.Timer;


public class Game extends JPanel implements ActionListener, MessageDisplay {
	private int width;
	private int height;
	
	private Timer timer;
	private Level level;
	
	private boolean showingMessage;  // Whether a message is on-screen
	private boolean awaitingMessageResponse;  // Whether an on-screen message is awaiting user input
	private MessageBox curMessage;  // Reference to the currently displayed message (null if none)
	int messageTimer;  // Used to limit the time for which messages are displayed
	private boolean paused;
	
	// Message parameters
	private final int messageLength = 80;  // How long is it shown for?
	private final int messageWidth = 500;
	private final int messageHeight = 130;
	private final int messageOffsetY = 420;
	
	public Game(int width, int height) {
		this.width = width;
		this.height = height;
		
		addKeyListener(new TAdapter());
		MAdapter mouseAdapter = new MAdapter();
		addMouseMotionListener(mouseAdapter);
		addMouseListener(mouseAdapter);
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		
		Messages.registerMessageDisplay(this);
		showingMessage = false;
		awaitingMessageResponse = false;
		curMessage = null;
		messageTimer = 0;
		
		level = new Level(width, height);
		
		paused = false;
		
		timer = new Timer(33, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		level.draw(g);
		
		if (showingMessage) {
			curMessage.draw(g);
		}
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
	
	public void update() {
		if ((!awaitingMessageResponse) && (!paused)) {
			level.update();
		}
	}
	
	public void mainloop() {
		update();
		repaint();
		
		messageTimer++;
		if (messageTimer >= messageLength) {
			// If the message has been up for its specified time and is not awaiting input, get rid of it
			if (!awaitingMessageResponse) {
				showingMessage = false;
				curMessage = null;
			}
			
			// Reset the timer
			messageTimer = 0;
		}
	}
	
	public void showSimpleMessage(String message) {
		showingMessage = true;
		curMessage = new MessageBox(message, new Vector(messageWidth, messageHeight), new Vector(width, height));
		curMessage.setY(messageOffsetY);
		messageTimer = 0;
	}
	
	public void showInterruptingMessage(String message) {
		showingMessage = true;
		awaitingMessageResponse = true;
		curMessage = new MessageBox(message + "\\n \\nPress space to continue...", new Vector(messageWidth, messageHeight), new Vector(width, height));
		curMessage.setY(messageOffsetY);
		messageTimer = 0;
	}
	
	public void askYesNo(String question) {
		//TODO: Code this later
		Messages.handleYesNoReturn(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		mainloop();
	}
	
	private class MAdapter extends MouseAdapter implements MouseMotionListener {
		public void mouseMoved(MouseEvent e) {
			Mouse.setScreenX(e.getX());
			Mouse.setScreenY(e.getY());
			level.handleMouseMotion(e);
		}
		
		public void mouseDragged(MouseEvent e) {
			// We want the game to continue receiving mouse motion updates whilst buttons are being
			// held, hence 'dragging' events are caught and forwarded as motion events.
			mouseMoved(e);
		}
		
		public void mouseClicked(MouseEvent e) {			
			level.handleMouseClick(e);
		}
		
		public void mousePressed(MouseEvent e) {
			int mouseButton = e.getButton();
			if (mouseButton == MouseEvent.BUTTON1) Mouse.setLeftButton(true);
			else if (mouseButton == MouseEvent.BUTTON3) Mouse.setRightButton(true);
		}
		
		public void mouseReleased(MouseEvent e) {
			int mouseButton = e.getButton();
			if (mouseButton == MouseEvent.BUTTON1) Mouse.setLeftButton(false);
			else if (mouseButton == MouseEvent.BUTTON3) Mouse.setRightButton(false);
		}
	}
	
	private class TAdapter extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			level.handleKeyRelease(e);
			Keyboard.handleKeyRelease(e);
		}
		
		public void keyPressed(KeyEvent e) {
			// If the user has pressed space and a message awaiting response, let it know that it need no longer await input
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (showingMessage && awaitingMessageResponse) {
					awaitingMessageResponse = false;
					messageTimer = messageLength;  // Make sure the message disappears immediately
					Messages.endCurrentInterrupt();
					return;  // Don't let other objects handle the event or pressing space to
						// close a message will also perform actions in-game
				}
			}
			
			level.handleKeyPress(e);
			Keyboard.handleKeyPress(e);
		}
	}
}