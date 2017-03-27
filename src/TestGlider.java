/**
 * This is the hang glider and its controls. It is designed to fly somewhat realistically, increasing in speed as it falls
 * and decreasing as it rises. Its controls are somewhat unresponsive to simulate actual flight.
 * 
 * @author Chris Martin
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * The hang glider itself.
 * @author Chris Martin
 *
 */
public class TestGlider extends Sprite
{
	// enable/disable debug tools
	private final boolean DEBUG = false;
	
	double r = 0;	//radius
	double vx = 0;	//x-axis velocity
	double vy = 0;	//y-axis velocity
	double a = 0; 	//angle
	double v = 0; 	//velocity
	
	// points for trail
	private static final int[] _R_TRAIL_OFFSET = {2, 10};
	private static final int[] _L_TRAIL_OFFSET = {4, 5};
	
	private _TrailPointPair[] trail_points = new _TrailPointPair[_TrailPoint.MAX_FRAME_LIFE];
	
	private _TrailPoint hg_point_spawn_l;
	private _TrailPoint hg_point_spawn_r;
	
	// for rendering of the image
	BufferedImage img = null;
	AffineTransform at = new AffineTransform();
	
	// simulated mouse code
	Simulated_mouse sm;
	double t = 0.0;
  
	/**
	 * Constructor for a hang glider. Needs bounding rect size and image.
	 * @param x		double
	 * @param y		double
	 * @param h		int
	 * @param w		int
	 * @param img	BufferedImage
	 */
	public TestGlider(double x, double y, int h, int w, BufferedImage img)
	{
		// initialize Sprite
		super(x, y, h, w, Color.WHITE);
		
		// get image
		this.img = img;
		
		// initialize trail points
		this.hg_point_spawn_l = new _TrailPoint(this.x + TestGlider._L_TRAIL_OFFSET[0], this.y + TestGlider._L_TRAIL_OFFSET[1]);
		this.hg_point_spawn_r = new _TrailPoint(this.x + TestGlider._R_TRAIL_OFFSET[0], this.y + TestGlider._R_TRAIL_OFFSET[1]);
		
		for (int i = 0; i < _TrailPoint.MAX_FRAME_LIFE; i++)
		{
			this.trail_points[i] = new _TrailPointPair(hg_point_spawn_l, hg_point_spawn_r);
		} // end for loop
		
		// initialize all variables to current state
		// r = ((18 * Math.sqrt(2 * this.y)) / Math.PI);
		a = 0;
		v = Math.sqrt(this.y / 5);
		vy = v * Math.sin(a);
		vx = v * Math.cos(a);
		sm = new Simulated_mouse(this);
	} // end constructor
	
	/**
	 * Constructor, to copy hang glider. Makes code more readable in places where the hang glider is restarted.
	 * Results in a slightly more memory intensive code, however.
	 * @param tg		Another TestGlider
	 */
	public TestGlider(TestGlider tg)
	{
		// call sprite constructor
		super(tg.x, tg.y, tg.h, tg.w, tg.color);
		
		// initialize trail points
		this.hg_point_spawn_l = new _TrailPoint(this.x + TestGlider._L_TRAIL_OFFSET[0], this.y + TestGlider._L_TRAIL_OFFSET[1]);
		this.hg_point_spawn_r = new _TrailPoint(this.x + TestGlider._R_TRAIL_OFFSET[0], this.y + TestGlider._R_TRAIL_OFFSET[1]);
		
		for (int i = 0; i < _TrailPoint.MAX_FRAME_LIFE; i++)
		{
			this.trail_points[i] = new _TrailPointPair(hg_point_spawn_l, hg_point_spawn_r);
		} // end for loop
		
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
	
	/**
	 * The per-frame tick of the hang glider.
	 * @param mouse_y	Y position of the mouse
	 * @return			X component of the velocity, double
	 */
	public double tick(int mouse_y)
	{
		
		// MOVEMENT
		
		sm.chase_mouse(mouse_y);
		double dx = 100;
		double dy = sm.simmouse_y - this.y;
		this.a = Math.atan2(dy, dx);
		t = t + (1 / 60);
		
		// velocity is based on the current height of the hang glider
		this.v = Math.sqrt(y / 3);
		
		// break velocity into components with trig
		vy = this.v * Math.sin(this.a);
		vx = this.v * Math.cos(this.a);
		
		// increment the glider's speed
		this.y += this.vy;
		
		
		// TRAILPOINT
		
		// initialize trail points
		this.hg_point_spawn_l = new _TrailPoint(this.x + TestGlider._L_TRAIL_OFFSET[0], this.y + TestGlider._L_TRAIL_OFFSET[1]);
		this.hg_point_spawn_r = new _TrailPoint(this.x + TestGlider._R_TRAIL_OFFSET[0], this.y + TestGlider._R_TRAIL_OFFSET[1]);		
		
		for (int i = _TrailPoint.MAX_FRAME_LIFE-1; i > 0; i--)
		{
			this.trail_points[i-1].l.x -= v;
			this.trail_points[i-1].r.x -= v;
			
			this.trail_points[i] = this.trail_points[i-1];
			
			this.trail_points[i].l.frame_life -= 1;
			this.trail_points[i].r.frame_life -= 1;
		} // end for loop;
		
		this.trail_points[0] = new _TrailPointPair(this.hg_point_spawn_l, this.hg_point_spawn_r);
		
		// return the speed that the screen should scroll at
		return vx;
		
	} // end tick

	/**
	 * Draw the sprite as an actual hang glider with trails. The glider is rotated to face its direction of
	 * movement.
	 * @param g		Graphics object to draw to
	 */
	@Override
	public void draw(Graphics g)
	{// get obstacle
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
	    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC) ;
	    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY) ;

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
		
		// draw trail points
		for (int i = 1; i < _TrailPoint.MAX_FRAME_LIFE-1; i++)
		{
			// create color based on alpha
			Color draw_color = new Color(255, 255, 255, 
					(int) (200 * ((float) this.trail_points[i].l.frame_life/ (float) _TrailPoint.MAX_FRAME_LIFE)));
			
			g.setColor(draw_color);

			// get points and draw line
			int x1, y1, x2, y2;
			x1 = (int) this.trail_points[i].l.x;
			x2 = (int) this.trail_points[i-1].l.x;
			y1 = (int) this.trail_points[i].l.y;
			y2 = (int) this.trail_points[i-1].l.y;
			((Graphics2D) g).draw(new Line2D.Double(x1, y1, x2-1, y2));

			// get points and draw line
			x1 = (int) this.trail_points[i].r.x;
			x2 = (int) this.trail_points[i-1].r.x;
			y1 = (int) this.trail_points[i].r.y;
			y2 = (int) this.trail_points[i-1].r.y;
			((Graphics2D) g).draw(new Line2D.Double(x1, y1, x2-1, y2));
		} // end for loop
	} // end draw
	
	/**
	 * A private class to keep track of the points that the hang glider leaves behind. These are used
	 * to draw the trails.
	 * @author Patrick Thomas
	 *
	 */
	private static class _TrailPoint extends Geometry.Point
	{
		// duration
		public static final int MAX_FRAME_LIFE = 12;
		public int frame_life = MAX_FRAME_LIFE;
		
		/**
		 * Constructor. Needs point.
		 * @param x		double
		 * @param y		double
		 */
		public _TrailPoint(double x, double y)
		{
			super(x, y);
		} // end constructor
		
		/**
		 * Copy constructor
		 * @param tp	_TrailPoint
		 */
		public _TrailPoint(_TrailPoint tp)
		{
			super(tp.x, tp.y);
		} // end constructor
	} // end class trail point
	
	/**
	 * A class to keep track of pairs of points. Makes the array operations simpler.
	 * @author Patrick Thomas
	 *
	 */
	private static class _TrailPointPair
	{
		/** The left trail point */
		public _TrailPoint l;
		/** The right trail point */
		public _TrailPoint r;
		
		/**
		 * Constructor. Takes two points.
		 * @param l		the left trail point, correlates to hang glider
		 * @param r		the right point
		 */
		public _TrailPointPair(_TrailPoint l, _TrailPoint r)
		{
			this.l = new _TrailPoint(l);
			this.r = new _TrailPoint(r);
		} // end constructor
	} // end class

	
	/**
	 * A "simulated" mouse to have a slight input delay and limit, so the game is harder.
	 * @author Chris Martin
	 *
	 */
	public class Simulated_mouse
	{
		double simmouse_x = 0;//simulated mouse x
		double simmouse_y = 0;//simulated mouse y
		double simmouse_ay = 0;//simulated mouse y acceleration
		double simmouse_vy = 0;//simulated mouse y velocity
		double simmouse_maxa = 0;//simulated mouse max acceleration
		
		/**
		 * Constructor. Needs its own hang glider.
		 * @param hg
		 */
		public Simulated_mouse(TestGlider hg)
		{
			simmouse_x = hg.x + 100;
			simmouse_y = hg.y;
		} // end constructor
		
		/**
		 * Chase a given mouse y.
		 * @param mouse_y	int, height of mouse
		 * @return			the height of the simulated mouse
		 */
		public double chase_mouse(int mouse_y)
		{
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
			if (simmouse_y < mouse_y)
			{
				// mouse sim mouse
				simmouse_y = simmouse_y + 5;
				
				// correct if mouse is level/below mouse now
				if (simmouse_y > mouse_y)
					simmouse_vy = 0;
			} // end if sim y is higher than mouse y
			else if (simmouse_y > mouse_y)
			{
				// move sim mouse
				simmouse_y = simmouse_y - 5;
				
				if (simmouse_y < mouse_y)
					simmouse_vy = 0;
			} // end if sim y is lower than mouse y
			
			if (mouse_y <= .01)
			{
				simmouse_y = 0;
			} // end if
			
			if ((simmouse_y == 0) && (t == 2))
			{
				simmouse_y = simmouse_y - 10;
			} // end if
			
			return(simmouse_y);
		} // end chase mouse
	} // end Simulated_mouse
} // end TestGlider
