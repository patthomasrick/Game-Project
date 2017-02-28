import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
Authors:	Patrick Thomas
Date:		2/4/17
Purpose: 	Stalagtite/mites. Stuff you hit. Extended from sprites.
*/

public class CaveObstacle extends Sprite
/*
 * 2/4/17
 * 
 * Extends sprite. Provides methods to tick a custom sprite that also has specialized
 * collision functions (e.g. triangles). Can draw triangles and store a color.
 */
{
	public CaveObstacle(double x, double y, int h, int w, Color color)
	{
		// init sprite
		super(x, y, h, w, color);
	} // end constructor
	
	public void draw(Graphics g)
	/*
	 * 2/4/17
	 * 
	 * Overwrites the default rectangle that Sprite draws. This draws a triangle, and the
	 * position of the tip of the triangle depends on the value of this.l.
	 */
	{
		// get the points of the triangle
		int[] x_values = {
				(int) this.x, 
				(int) (this.x + this.w), 
				(int) (this.x + this.w/2.0)};
		int[] y_values = {
				(int) this.y, 
				(int) this.y, 
				(int) (this.y + this.h)};
		
		if (g instanceof Graphics2D)
		{
			// draws based on color and points
			g.setColor(this.color);
			g.fillPolygon(x_values, y_values, 3);
		} // end if graphics2d
		else
		{
			// draws based on color and points
			g.setColor(this.color);
			g.fillPolygon(x_values, y_values, 3);
		} // end if grahpcsi
	} // end draw
	
	public void tick(double hg_speed)
	/*
	 * 2/4/17
	 * 
	 * Ticks the stalagtite based on the hang glider's speed. To be called en masse when
	 * ticking the map in general.
	 * 
	 * Will eventually host collision tests
	 */
	{
		// move obstacle
		this.push(-hg_speed, 0);	// method provided through Rect
	} // end tick
	
	/**
	 * Collides the triangle with a rectangle. Uses the collide point method with every single
	 * point of a rectangle.
	 * @param r		Geometry.Rect, used for its 4 corners
	 * @return		boolean, true if colliding, false if not
	 */
	public boolean collide_as_triangle(Geometry.Rect r)
	{
		// uses the Geometry class to help
		// define points of triangle
		Geometry.Point a = new Geometry.Point(this.x, this.y);
		Geometry.Point b = new Geometry.Point(this.x + this.w, this.y);
		Geometry.Point c = new Geometry.Point(this.x + this.w/2.0, this.y + this.h);
		
		// define triangle
		Geometry.Triangle tri = new Geometry.Triangle(a, b, c);
		
		// get points of rect
		Geometry.Point r1 = new Geometry.Point(r.x, r.y);
		Geometry.Point r2 = new Geometry.Point(r.x + r.w, r.y);
		Geometry.Point r3 = new Geometry.Point(r.x, r.y + r.h);
		Geometry.Point r4 = new Geometry.Point(r.x + r.w, r.y + r.h);
		
		// collide points with triangle
		boolean iscolliding = (
				(tri.contains_point(r1)) ||
				(tri.contains_point(r2)) ||
				(tri.contains_point(r3)) ||
				(tri.contains_point(r4))
				);
		
		return iscolliding;
	} // end collide as triangle
	
	/**
	 * Resize the object by a factor
	 * @param factor	double
	 */
	public void resize(double x_factor, double y_factor)
	{
		this.x *= x_factor;
		this.y *= y_factor;
		this.h = (int) (this.h * y_factor);
		this.w = (int) (this.w * x_factor);
	} // end resize
	
	
	public static class Chunk
	{
		// geometric containers
		public Geometry.Point pos;
		public Sprite bounding_rect, inscribed_rect;
		public Geometry.Triangle hat;
		public Geometry.Point a1, a2, b1, b2;
		
		// color to render shapes as
		private Color color;
		
		/**
		 * Constructor. 
		 * @param a1	Point	top left corner
		 * @param a2	Point	top right corner
		 * @param b1	Point 	bottom left corner
		 * @param b2	Point 	bottom right corner
		 */
		public Chunk(
				Geometry.Point a1, 
				Geometry.Point a2, 
				Geometry.Point b1, 
				Geometry.Point b2, 
				Color color)
		{
			// define points in class
			this.a1 = new Geometry.Point(a1);
			this.a2 = new Geometry.Point(a2);
			this.b1 = new Geometry.Point(b1);
			this.b2 = new Geometry.Point(b2);
			
			// determine where the base is
			if (a1.y - b1.y <= 0.01) // floating point equality testing
			{
				// a1 and b1 are base, are above a2 and b2
				this.pos = new Geometry.Point(a1.x, a1.y);	// set top left point
				
				// find if a2 or b2 is lower (eg more extreme)
				if (a2.y > b2.y)
				{
					this.bounding_rect = new Sprite(
							this.pos.x,					// x pos
							this.pos.y,					// y pos
							(int) (a2.y - this.pos.y),	// height
							(int) (b1.x - this.pos.x),  // width
							color);						// color
					this.inscribed_rect = new Sprite(
							this.pos.x,					// x pos
							this.pos.y,					// y pos
							(int) (b2.y - this.pos.y),	// height
							(int) (b1.x - this.pos.x),  // width
							color);						// color
					this.hat = new Geometry.Triangle(
							this.pos.x, 				// ax
							b2.y, 						// ay
							this.pos.x, 				// bx
							a2.y, 						// by
							b2.x, 						// cx
							b2.y);						// cy
				} // end if a2 is lower (closer to the ground)
				else
				{
					this.bounding_rect = new Sprite(
							this.pos.x,					// x pos
							this.pos.y,					// y pos
							(int) (b2.y - this.pos.y),	// height
							(int) (b1.x - this.pos.x),  // width
							color);						// color
					this.inscribed_rect = new Sprite(
							this.pos.x,					// x pos
							this.pos.y,					// y pos
							(int) (a2.y - this.pos.y),	// height
							(int) (b1.x - this.pos.x),  // width
							color);						// color
					this.hat = new Geometry.Triangle(
							this.pos.x, 				// ax
							a2.y, 						// ay
							b1.x, 						// bx
							a2.y, 						// by
							b2.x, 						// cx
							b2.y);						// cy
				} // end if b2 is lower
			} // end if top points are level
			else
			{
				// a2 and b2 are base, are below a1 and b1
				
				// find if a2 or b2 is lower (eg more extreme)
				if (a1.y < b1.y)
				{
					// a1 is higher than b1
					this.pos = new Geometry.Point(a1.x, a1.y);	// set top left point
					
					this.bounding_rect = new Sprite(
							this.pos.x,					// x pos
							this.pos.y,					// y pos
							(int) (b2.y - this.pos.y),	// height
							(int) (b2.x - this.pos.x),  // width
							color);						// color
					this.inscribed_rect = new Sprite(
							this.pos.x,					// x pos
							b1.y,						// y pos
							(int) (b2.y - b1.y),		// height
							(int) (b2.x - this.pos.x),  // width
							color);						// color
					this.hat = new Geometry.Triangle(
							this.pos.x, 				// ax
							this.pos.y, 				// ay
							this.pos.x, 				// bx
							b1.y, 						// by
							b1.x, 						// cx
							b1.y);						// cy
				} // end if a2 is lower (closer to the ground)
				else
				{
					// b1 is higher than a1
					this.pos = new Geometry.Point(a1.x, b1.y);	// set top left point

					this.bounding_rect = new Sprite(
							this.pos.x,					// x pos
							this.pos.y,					// y pos
							(int) (b2.y - this.pos.y),	// height
							(int) (b2.x - this.pos.x),  // width
							color);						// color
					this.inscribed_rect = new Sprite(
							a1.x,						// x pos
							a1.y,						// y pos
							(int) (b2.y - a2.y),		// height
							(int) (b2.x - this.pos.x),  // width
							color);						// color
					this.hat = new Geometry.Triangle(
							a1.x, 						// ax
							a1.y, 						// ay
							b1.x, 						// bx
							a1.y, 						// by
							b1.x, 						// cx
							b1.y);						// cy
				} // end if b2 is lower
			} // end else bottom points are level
		} // end Constructor

		/**
		 * Constructor for a chunk. Meant to extend a range of chunks given another chunk and a new
		 * width and leg height.
		 * @param chunk		Chunk	Chunk to extend to the right
		 * @param r_height	int		Height of right leg
		 * @param w			int		Width of base
		 */
		public Chunk(Chunk chunk, int r_height, int w)
		{
			// init chunk from values of other chunk and some new ones
			this(
					chunk.b1,
					chunk.b2,
					new Geometry.Point((chunk.b1.x + w), chunk.b1.y),
					new Geometry.Point((chunk.b1.x + w), chunk.b1.y + r_height),
					chunk.color);
		} // end constructor given another chunk
		
		public void tick(int milliseconds, double scrollspeed)
		{			
			this.a1.x -= scrollspeed;
			this.a2.x -= scrollspeed;
			this.b1.x -= scrollspeed;
			this.b2.x -= scrollspeed;
			
			this.pos.x -= scrollspeed;
			
			this.hat.a.x -= scrollspeed;
			this.hat.b.x -= scrollspeed;
			this.hat.c.x -= scrollspeed;
			
			this.bounding_rect.move(-scrollspeed, 0);
			this.inscribed_rect.move(-scrollspeed, 0);
		} // end tick
		
		public void draw(Graphics g)
		{
			// draw rectangle of trapezoid
			this.inscribed_rect.draw(g);
			
			// draw triangle (hat)
			// get the points of the triangle
			int[] x_values = {
					(int) this.a1.x, 
					(int) this.a2.x, 
					(int) this.b2.x,
					(int) this.b1.x};
			int[] y_values = {
					(int) this.a1.y, 
					(int) this.a2.y, 
					(int) this.b2.y,
					(int) this.b1.y};
			
			g.fillPolygon(x_values, y_values, 4);
		} // end draw
		
		public void resize(double x_factor, double y_factor)
		{
			// shift position
			this.pos.x *= x_factor;
			this.pos.y *= y_factor;
			
			// shift points
			this.a1.x *= x_factor;
			this.a2.x *= x_factor;
			this.b1.x *= x_factor;
			this.b2.x *= x_factor;
			this.a1.y *= y_factor;
			this.a2.y *= y_factor;
			this.b1.y *= y_factor;
			this.b2.y *= y_factor;
			
			// shift rects
			this.bounding_rect.x *= x_factor;
			this.bounding_rect.y *= y_factor;
			this.bounding_rect.w *= x_factor;
			this.bounding_rect.h *= y_factor;
			
			this.inscribed_rect.x *= x_factor;
			this.inscribed_rect.y *= y_factor;
			this.inscribed_rect.w *= x_factor;
			this.inscribed_rect.h *= y_factor;
			
			// shift hat
			this.hat.a.x *= x_factor;
			this.hat.b.x *= x_factor;
			this.hat.c.x *= x_factor;
			this.hat.a.y *= y_factor;
			this.hat.b.y *= y_factor;
			this.hat.c.y *= y_factor;
		} // end resize
		
		/**
		 * Collides the triangle with a rectangle. Uses the collide point method with every single
		 * point of a rectangle.
		 * @param r		Geometry.Rect, used for its 4 corners
		 * @return		boolean, true if colliding, false if not
		 */
		public boolean collide_hat_with_rect(Geometry.Rect r)
		{
			// uses the Geometry class to help
			
			// get points of rect
			Geometry.Point r1 = new Geometry.Point(r.x, r.y);
			Geometry.Point r2 = new Geometry.Point(r.x + r.w, r.y);
			Geometry.Point r3 = new Geometry.Point(r.x, r.y + r.h);
			Geometry.Point r4 = new Geometry.Point(r.x + r.w, r.y + r.h);
			
			// collide points with triangle
			boolean iscolliding = (
					(hat.contains_point(r1)) ||
					(hat.contains_point(r2)) ||
					(hat.contains_point(r3)) ||
					(hat.contains_point(r4))
					);
			
			return iscolliding;
		} // end collide as triangle
	} // end Chunk
} // end GameObstacle
