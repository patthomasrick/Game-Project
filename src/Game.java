/**
Authors:	Isaac Payne, Chris Martin, Patrick Thomas
Date:		01/26/17
Purpose: 	play hang gliding
*/

// import statements
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.Timer;

public class Game extends Applet
implements MouseListener, ActionListener, ItemListener, KeyListener, MouseMotionListener
{
	// to fix warning...
	private static final long serialVersionUID = 1L;
	
	// double buffering variables
	private Image dbImage;
	private Graphics dbg;
	
	// variables
	public Timer timer;
	private int aWidth = 800, aHeight = 600;
	
	// create Map
	public Map m;
	
	// create test hang glider
	public TestGlider hg;

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
		hg.move(125, e.getY()-15);
	}
	public void mouseDragged(MouseEvent e) {}
	
	// applet events
	public void actionPerformed(java.awt.event.ActionEvent p1) {}
	public void itemStateChanged(java.awt.event.ItemEvent p1) {}
	
	//keyboard events
	public void keyReleased(java.awt.event.KeyEvent p1) {}
	public void keyPressed(java.awt.event.KeyEvent p1) {}
	public void keyTyped(java.awt.event.KeyEvent p1) {}
	
	public void init()
	/*
	 * 1/26/17
	 * 
	 * Initialize the applet.
	 * Add listeners for user input, configure window, start the timer,
	 * and create the map and hang glider.
	 */
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
		
		// start timer
		timer = new Timer(1000/60, new MyTimer());
		timer.start();
		
		// create map and hang glider
		m = new Map(aWidth, aHeight, 2.5);
		hg = new TestGlider(100.0, 300.0, 30, 30, Color.GREEN);
	} // end initialization
	
	public void paint(Graphics g)
	/*
	 * 1/23/17
	 * 
	 * Draw the objects in the game to the screen.
	 */
	{
		// draw bg
		g.setColor(Map.bg_color_1);
		g.fillRect(0, 0, aWidth, aHeight);
		
		// draw map and its objects
		m.draw(g);
		
		// draw hangglider
		hg.draw(g);
	} // end draw
	
	public void update_applet_size()
	/*
	 * 2/4/17
	 * 
	 * Changes the variables associated with this applet's size to what it actually is.
	 * Essentially a setter.
	 */
	{
		this.aWidth = this.getWidth();
		this.aHeight = this.getHeight();
	} // end update applet size
	
	private class MyTimer implements ActionListener
	/*
	 * 1/23/17
	 * 
	 * Timed events (per frame).
	 */
	{
		public void actionPerformed(ActionEvent a)
		/*
		 * 1/23/17
		 * 
		 * Ran per action performed, basically every 1/60th of a second.
		 */
		{
			// tick map and map objects
			m.tick(aWidth, aHeight, hg);
			
			// update screen
			repaint();
		} // end actionPerformed
	} // end class MyTimer
	
	public void update(Graphics g)	// method to double buffer
	/*
	 * 1/23/17
	 * 
	 * Double buffering code.
	 * This method was obtained from the internet and cited as common knowledge.
	 */
	{
		// create
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
