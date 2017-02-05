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
	
	// double buffering...
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
	public void mouseMoved(MouseEvent e)
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
	
	// initialize applet
	public void init()
	{
		// ADD KEY AND MOUSE LISTENERS
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		// focus window
		setFocusable(true);
		
		setSize(aWidth, aHeight);					// sets size of window
		setBackground(Color.white);					// sets bg color
		// direction = (int) (360 * Math.random());	// intializes initial direction
		timer = new Timer(1000/60, new MyTimer());	// class associated with the timer and the length
													// of time between 
		// start the timer
		timer.start();
		
		// create map
		m = new Map(aWidth, aHeight, 2.5);
		
		hg = new TestGlider(100.0, 300.0, 30, 30, Color.GREEN);
	} // end initialization
	
	public void paint(Graphics g)
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
	{
		this.aWidth = this.getWidth();
		this.aHeight = this.getHeight();
	} // end update applet size
	
	private class MyTimer implements ActionListener
	{
		public void actionPerformed(ActionEvent a)
		{
			// tick map and map objects
			
			m.tick(aWidth, aHeight, hg);
			
			// update screen
			repaint();
		} // end actionPerformed
	} // end class MyTimer
	
	public void update(Graphics g)	// method to double buffer
	{
		// this method was obtained from the internet and cited as common knowledge
		
		// double buffering surfaces
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
}
