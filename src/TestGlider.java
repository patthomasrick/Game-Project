import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * A class that performs as a hang glider.
 * @author Christopher Martin
 */


// hang glider picture credits: CC BY-SA 3.0, https://commons.wikimedia.org/w/index.php?curid=531761

public class TestGlider extends Sprite
{
	// enable/disable debug tools
	private final boolean DEBUG = true;
	
	double r = 0;//radius
	double vx = 0;//x-axis velocity
	double vy = 0;//y-axis velocity
	double a = 0; //angle
	double v = 0; //velocity
	
	BufferedImage img = null;
	AffineTransform at = new AffineTransform();
	//double mouse_y = 0;
	//double mouse_x = 0;
//	double mouse_acceleration= 2.0;
//	double mouse_speed = 0;
//	double simmouse_y = 0; //simulated mouse y
//	double simmouse_maxa = 0; //maximum acceleration for simulated mouse
	Simulated_mouse sm;
	
	public TestGlider(double x, double y, int h, int w, BufferedImage img)
	{
		// initialize Sprite
		super(x, y, h, w, Color.WHITE);
		
		this.img = img;
		
		// initialize all variables to current state
		// r = ((18 * Math.sqrt(2 * this.y)) / Math.PI);
		a = 0;
		v = Math.sqrt(this.y / 5);
		vy = v * Math.sin(a);
		vx = v * Math.cos(a);
		sm = new Simulated_mouse(this);
	} // end constructor
	
	public TestGlider(TestGlider tg)
	{
		// call sprite constructor
		super(tg.x, tg.y, tg.h, tg.w, tg.color);
		
		// copy all of the values from the other hang glider
		this.x = tg.x;
		this.y = tg.y;
		this.w = tg.w;
		this.h = tg.h;
		this.img = tg.img;
		this.a = tg.a;
		this.v = tg.v;
		this.vy = tg.vy;
		this.vx = tg.vx;
		this.sm = new Simulated_mouse(this);
	} // end copy constructor
	
	public double tick(int mouse_y)
	{
		sm.chase_mouse(mouse_y);
		double dx = 100;
		double dy = sm.simmouse_y - this.y;
		this.a = Math.atan2(dy, dx);
		
		// velocity is based on the current height of the hang glider
		this.v = Math.sqrt(y / 5);
		
		// break velocity into components with trig
		vy = this.v * Math.sin(this.a);
		vx = this.v * Math.cos(this.a);
		
		// increment the glider's speed
		this.y += this.vy;
		
		// return the speed that the screen should scroll at
		return vx;
	}

	/**
	 * Draw the sprite as a simple rectangle to the screen.
	 * 
	 * @param g		Graphics object to draw to
	 */
	public void draw(Graphics g)
	{// get obstacle
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
	    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC) ;
	    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY) ;

		if (this.alive == true)
		{
			// draw the collision rectangle if debugging is enabled in file
			if (DEBUG) super.draw(g);
			
			// refresh at
			at = new AffineTransform();
			
			// 3. move to hang glider
			at.translate(((int) this.x) + this.w/2, ((int) this.y) + this.h/2);			
			// 2. rotate
			at.rotate(this.a/1.5);
			// 1. center image for rotation
			at.translate(-24, -8);
			
			((Graphics2D) g).drawImage(img,
	                at,
	                null);
			
		} // end if alive
	} // end draw

	
	
	/*
	 * if the user wants to move down:
	 * 		move the point down
	 * 			if the point is below the mouse:
	 * 				stop moving
	 * else if the user wants to move up:
	 * 		move the point up
	 * 			if the point is above the mouse;
	 * 				stop moving
	 */
	public class Simulated_mouse
	{
		double simmouse_x = 0;//simulated mouse x
		double simmouse_y = 0;//simulated mouse y
		double simmouse_ay = 0;//simulated mouse y acceleration
		double simmouse_vy = 0;//simulated mouse y velocity
		double simmouse_maxa = 0;//simulated mouse max acceleration
		
		public Simulated_mouse(TestGlider hg)
		{
			simmouse_x = hg.x + 100;
			simmouse_y = hg.y;
		}
		
		public double chase_mouse(int mouse_y)
		{
			if (simmouse_y < mouse_y)
			{
				simmouse_y = simmouse_y + 5;
				
				if (simmouse_y > mouse_y)
				{
					simmouse_vy = 0;
				}
			}
			else if (simmouse_y > mouse_y)
			{
				simmouse_y = simmouse_y - 5;
				
				if (simmouse_y < mouse_y)
				{
					simmouse_vy = 0;
				}
			}
			if (mouse_y <= .01)
			{
				simmouse_y = 0;
			}
			return(simmouse_y);
		}
	}
}
