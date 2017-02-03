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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.Timer;

public class Game extends Applet
implements MouseListener, ActionListener, ItemListener, KeyListener
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
	public Map.CaveObstacle tri;

	// mouse events
	public void mouseReleased(java.awt.event.MouseEvent p1) {}
	public void mouseEntered(java.awt.event.MouseEvent p1) {}
	public void mouseClicked(java.awt.event.MouseEvent p1) {}
	public void mousePressed(java.awt.event.MouseEvent p1) {}
	public void mouseExited(java.awt.event.MouseEvent p1) {}
	
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
		m = new Map(aWidth, aHeight);
	} // end initialization
	
	public void paint(Graphics g)
	{
		// set color of ceiling objects
        g.setColor(Color.BLACK);
		// draw map objects	
		for (Map.CaveObstacle co: m.ceiling_objs)
		{
			g.fillPolygon(co.polygon_x(), co.polygon_y(), 3);
		} // end tick ceiling g.os
		for (Map.CaveObstacle co: m.floor_objs)
		{
			g.fillPolygon(co.polygon_x(), co.polygon_y(), 3);
		} // end tick ceiling g.os
		
		// draw ceiling and floor rect
		g.fillRect(0, 0, aWidth, m.ceiling);
		g.fillRect(0, m.floor, aWidth, 50);
	}
	
	private class MyTimer implements ActionListener
	{
		public void actionPerformed(ActionEvent a)
		{
			// tick map and map objects
			m.tick();
			
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
