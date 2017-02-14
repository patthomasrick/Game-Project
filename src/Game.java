/*
Authors:	Isaac Payne, Chris Martin, Patrick Thomas
Date:		1/26/17
Purpose: 	play hang gliding
*/

/**
 * @author Patrick Thomas
 * @author Isaac Payne
 * @author Chris Martin
 * @version 2/5/17
 */

// import statements
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Date;

import javax.swing.Timer;

/**
 * @author Patrick Thomas
 * 
 * Applet to contain game. Map is generated by Map class. Implements all event listeners,
 * however, mouse is the only control method used.
 */
public class Game extends Applet
implements MouseListener, ActionListener, ItemListener, KeyListener, MouseMotionListener
{
	/** to fix warning... */
	private static final long serialVersionUID = 1L;

	/** double buffering variables */
	private Image dbImage;
	private Graphics dbg;
	
	/** timing */
	Date date = new Date();
	long lasttime = date.getTime();
	int timesince = 1000/60;
	
	public Timer timer;
	private int aWidth = 800, aHeight = 600;
	
	/** Creates map. Stores GameObstacles and handles the ticking of game
	 * objects */
	public Map m;
	
	/** Creates hang glider. This is the player that the user controls. */
	public TestGlider hg;
	private double scrollspeed;
	
	// mouse events
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	// mouse moved events
	public void mouseMoved(MouseEvent e)
	// track mouse movements
	{
		scrollspeed = hg.tick(e.getX(), e.getY());
	}
	public void mouseDragged(MouseEvent e) {}
	
	// applet events
	public void actionPerformed(java.awt.event.ActionEvent e) {}
	public void itemStateChanged(java.awt.event.ItemEvent e) {}
	
	//keyboard events
	public void keyReleased(java.awt.event.KeyEvent e) {}
	public void keyPressed(java.awt.event.KeyEvent e) {}
	public void keyTyped(java.awt.event.KeyEvent e) {}
	
	
	/**Initialize the applet.
	 * Add listeners for user input, configure window, start the timer,
	 * and create the map and hang glider.
	 */
	public void init()
	{
		// add mouse and key listeners
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		// focus window
		setFocusable(true);
		
		// configure window
		setSize(aWidth, aHeight);
		setBackground(Color.WHITE);
		
		/** Start timer */
		timer = new Timer(1000/60, new MyTimer());
		timer.start();
		
		/** Create map and hang glider */
		hg = new TestGlider(100.0, 300.0, 30, 30, Color.GREEN);
		m = new Map(aWidth, aHeight, 2.5);
	} // end initialization
	
	
	/**Draw the objects in the game to the screen.
	 * 
	 * @param g		Graphics object to draw to
	 */
	public void paint(Graphics g)
	{
		// cast graphics object to graphics 2d
		Graphics2D g2 = (Graphics2D) g;
		// turn on antialiasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// draw bg
		g2.setColor(Map.bg_color_1);
		g2.fillRect(0, 0, aWidth, aHeight);
		
		// draw map and its objects
		m.draw(g2);
		
		// draw hangglider
		hg.draw(g2);
	} // end draw

	
	/**Changes the variables associated with this applet's size to what it actually is.
	 * Essentially a setter.
	 */
	public void update_applet_size()
	{
		this.aWidth = this.getWidth();
		this.aHeight = this.getHeight();
	} // end update applet size

	
	/** Timer for timed events. */
	private class MyTimer implements ActionListener
	{
		/** Timed events (per frame). 
		 * @param a			ActionEvent
		 */
		public void actionPerformed(ActionEvent a)
		{
			// get number of milliseconds that have passed since last call
			timesince = (int) (date.getTime() - lasttime);
			lasttime = date.getTime();
			
			// update applet size
			update_applet_size();
			// tick map and map objects
			m.tick(aWidth, aHeight, hg, scrollspeed);
			
			// update screen
			repaint();
		} // end actionPerformed
	} // end class MyTimer

	
	/**Double buffering code.
	 * This method was obtained from the internet and cited as common knowledge.
	 * 
	 * @param g			Graphics object to draw to.
	 */
	public void update(Graphics g)
	{
		/** Graphics object the size of screen to store screen. */
		dbImage = createImage(this.getSize().width, this.getSize().height);
		dbg = dbImage.getGraphics();

		// initialize buffer
		if (dbImage == null) {} 
		
		// clear screen in background
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, aWidth, aHeight);

		// draw elements in background
		dbg.setColor(getForeground());
		paint(dbg);

		// draw image on the screen
		g.drawImage(dbImage, 0, 0, this);
		Toolkit.getDefaultToolkit().sync(); // fixes lag on Ubuntu
	} // end update
} // end Game
