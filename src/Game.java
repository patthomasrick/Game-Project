
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
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;
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
	
	// hang glider image
	BufferedImage hg_img;
	
	/** timing */
	Date date = new Date();
	long lasttime = date.getTime();
	int timesince = 1000/60;
	
	public Timer timer;
	private int aWidth = 800, aHeight = 600;
	
	/** Creates map. Stores GameObstacles and handles the ticking of game
	 * objects */
	public Map m = new Map(aWidth, aHeight, 2.5);
	
	public Menu.Button b;
	
	/** Creates hang glider. This is the player that the user controls. */
	public TestGlider hg;
  
	public int mouse_x = 0;
	public int mouse_y = 0;
	public boolean clicked = false;
	public boolean pressed = false;
	private double scrollspeed;
	
	//Variables for menu
	private boolean gr = false; //game running
	private boolean in_mm = true; //in main menu
	private boolean in_pm = false; //in pause menu
	private boolean in_em = false; //in end menu
	private Menu mm = new Menu(); //create main menu
	private Menu pm = new Menu(); //create pause menu
	private Menu em = new Menu(); //create end menu
	private Menu.Button mm_start_b; //create main menu start button
	private Menu.Button mm_quit_b; //create main menu quit button
	private Menu.Button pm_resume_b; //create pause menu resume button
	private Menu.Button pm_reload_b; //create pause menu reload button
	private Menu.Button em_restart_b; //create end menu restart button
	private Menu.Button em_reload_b; //create end menu reload button
	private Sprite mb; //create menu background
	
	// mouse events
	public void mouseReleased(MouseEvent e) 
	{
		this.clicked = false;
	} // end mouseReleased
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) 
	{
		this.clicked = true;
	}// end mousePressed
	
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) // track mouse movements
	{
		mouse_x = e.getX();
		mouse_y = e.getY();
	} // end mouseMoved
	
	public void mouseDragged(MouseEvent e) {}
	
	// applet events
	public void actionPerformed(java.awt.event.ActionEvent e) {}
	public void itemStateChanged(java.awt.event.ItemEvent e) {}
	
	//keyboard events
	public void keyReleased(java.awt.event.KeyEvent e) {}
	public void keyPressed(java.awt.event.KeyEvent p1) 
	{
		//Allows space bar to initiate pause menu
		if (p1.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE && in_mm != true && in_em != true)
		{
			in_pm = true;
			gr = false;
			in_mm = false;
			in_em = false;
		} // end Spacebar
	} // end keyPressed
	
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
		
		// load image of hang glider
		try
		{
	        URL url = new URL(getCodeBase(), "Hang glider white scaled.png");
		    hg_img = ImageIO.read(url);
		} // end try
		catch (IOException e) {}
		hg = new TestGlider(100.0, 300.0, 30, 30, hg_img);
		
		// focus window
		setFocusable(true);
		
		// configure window
		setSize(aWidth, aHeight);
		setBackground(Color.WHITE);
		
		/** Start timer */
		timer = new Timer(1000/60, new MyTimer());
		timer.start();
		/** Create map and hang glider */
		hg = new TestGlider(100.0, 300.0, 30, 30, hg_img);
		m = new Map(aWidth, aHeight, 2.5);
		
		//Creates all buttons, backgrounds, and assigns buttons to their respective menus
		mm_start_b = new Menu.Button(
				aWidth*0.3, aHeight*0.4, 
				(int) (aHeight*0.15), 
				(int) (aWidth*0.4), 
				Menu.button_color, "Start");
		mm_quit_b = new Menu.Button(
				aWidth*0.3, aHeight*0.6, 
				(int) (aHeight*0.15), 
				(int) (aWidth*0.4), 
				Menu.button_color, "Quit");
		pm_resume_b = new Menu.Button(
				aWidth*0.3, aHeight*0.4, 
				(int) (aHeight*0.15), 
				(int) (aWidth*0.4), 
				Menu.button_color, "Restart");
		pm_reload_b = new Menu.Button(
				aWidth*0.3, aHeight*0.6, 
				(int) (aHeight*0.15), 
				(int) (aWidth*0.4), 
				Menu.button_color, "Main Menu");
		em_restart_b = new Menu.Button(
				aWidth*0.3, aHeight*0.4, 
				(int) (aHeight*0.15), 
				(int) (aWidth*0.4), 
				Menu.button_color, "Restart");
		em_reload_b = new Menu.Button(
				aWidth*0.3, aHeight*0.6, 
				(int) (aHeight*0.15), 
				(int) (aWidth*0.4), 
				Menu.button_color, "Main Menu");
		mm.add_button(mm_start_b);
		mm.add_button(mm_quit_b);
		pm.add_button(pm_resume_b);
		pm.add_button(pm_reload_b);
		em.add_button(em_restart_b);
		em.add_button(em_reload_b);
		mb = new Sprite(aWidth*0.2, aHeight*0.3, (int) (aHeight*0.55), (int) (aWidth*0.6), Menu.bg_color);
	} // end initialization
	
	
	/**Draw the objects in the game to the screen.
	 * @param g		Graphics object to draw to
	 */
	public void paint(Graphics g)
	{
		// cast graphics object to graphics 2d
		Graphics2D g2 = (Graphics2D) g;
		// turn on antialiasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// draw bg
		g2.setColor(Map.BG_COLOR_D);
		g2.fillRect(0, 0, aWidth, aHeight);
		
		// draw map and its objects
		m.draw(g2);
		
		// draw hangglider
		hg.draw(g2);
		
		// draw main menu
		if (in_mm == true)
		{
			mb.draw(g2);
			mm.draw(g2);
		}
		
		//draw end menu
		if (in_em == true)
		{
			mb.draw(g2);
			em.draw(g2);
		}
		
		//draw pause menu
		if (in_pm == true)
		{
			mb.draw(g2);
			pm.draw(g2);
		}
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
			if (gr == true && hg.alive == true)
			{
				hg.tick(mouse_y);
				scrollspeed = hg.v;
				m.tick(aWidth, aHeight, hg, scrollspeed);
			}
			
			//tick the main menu and set button actions
			else if (in_mm == true && in_pm == false && in_em == false)
			{
				Menu.Button clickedbutton = mm.tick(mouse_x, mouse_y, clicked);
				
				if(clickedbutton == mm_start_b)
				{
					clicked = false;
					in_mm = false;
					gr = true;
				}
				
				if(clickedbutton == mm_quit_b)
				{
					clicked = false;
					System.exit(0);
				}
			}//end main menu
			
			//tick pause menu and set button actions
			else if (in_pm == true && in_mm == false && in_em == false)
			{
				Menu.Button clickedbutton = pm.tick(mouse_x, mouse_y, clicked);
				
				if(clickedbutton == pm_resume_b)
				{
					clicked = false;
					in_pm = false;
					gr = true;
				}
				
				if(clickedbutton == pm_reload_b)
				{
					clicked = false;
					in_pm = false;
					in_mm = true;
					gr = false;
					in_em = false;
					hg.alive = true;
					hg = new TestGlider(100.0, 300.0, 30, 30, hg_img);
					m = new Map(aWidth, aHeight, 2.5);
				}
			}//end pause menu
			
			//tick end menu and set button actions
			else if (hg.alive == false)
			{
				in_em = true;
				Menu.Button clickedbutton = em.tick(mouse_x, mouse_y, clicked);
				
				if(clickedbutton == em_restart_b)
				{
					clicked = false;
					in_mm = false;
					gr = true;
					in_em = false;
					hg.alive = true;
					hg = new TestGlider(100.0, 300.0, 30, 30, hg_img);
					m = new Map(aWidth, aHeight, 2.5);
				}
				
				if(clickedbutton == em_reload_b)
				{
					clicked = false;
					in_mm = true;
					gr = false;
					in_em = false;
					hg.alive = true;
					hg = new TestGlider(100.0, 300.0, 30, 30, hg_img);
					m = new Map(aWidth, aHeight, 2.5);
				}
			}//end end menu
			
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
