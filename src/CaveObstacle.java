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
		private Geometry.Point pos;
		public Sprite bounding_rect, inscribed_rect;
		public Geometry.Triangle hat;
		
		private Color color;
		
		public Chunk(
				double x, 
				double y, 
				int w, 
				int l_height, 
				int r_height, 
				boolean pointing_up, 
				Color color)
		{
			// set color
			this.color = color;
			
			// get the greatest height to build the bounding rect
			int max_height, min_height;
			boolean left_side;
			if (l_height > r_height)
			{
				max_height = l_height;
				min_height = r_height;
				left_side = true;
			} // end if 
			else;
			{
				min_height = l_height;
				max_height = r_height;
				left_side = false;
			} // end else
			
			// get the factor to mult height by for pointing
			byte h_mult;
			if (pointing_up)
				h_mult = 1;
			else;
				h_mult = -1;
			
			this.pos = new Geometry.Point(x, y);
			this.bounding_rect = new Sprite(pos.x, pos.y, max_height*h_mult, w, this.color);
			
			// find inscribed rect
			if (pointing_up)
			{
				this.inscribed_rect = new Sprite(
						pos.x - (max_height - min_height),
						pos.y,
						min_height,
						w,
						this.color
				);
			} // end if pointing up
			else
			{
				this.inscribed_rect = new Sprite(
						pos.x,
						pos.y,
						min_height,
						w,
						this.color
				);
			} // end else
			
			Geometry.Point a, b, c;
			// get base
			if (pointing_up)
			{
				a = new Geometry.Point(
						this.inscribed_rect.x, 
						this.inscribed_rect.y);
				b = new Geometry.Point(
						this.inscribed_rect.x + this.inscribed_rect.w, 
						this.inscribed_rect.y);
				if (left_side)
				{
					c = new Geometry.Point(
							this.inscribed_rect.x,
							this.bounding_rect.y);
				} // end if left side is tallest
				else
				{
					c = new Geometry.Point(
							this.inscribed_rect.x + this.inscribed_rect.w,
							this.bounding_rect.y);
				} // right side is taller
			} // end if
			else
			{
				a = new Geometry.Point(
						this.inscribed_rect.x, 
						this.inscribed_rect.y + this.inscribed_rect.h);
				b = new Geometry.Point(
						this.inscribed_rect.x + this.inscribed_rect.w, 
						this.inscribed_rect.y + this.inscribed_rect.h);
				if (left_side)
				{
					c = new Geometry.Point(
							this.inscribed_rect.x,
							this.bounding_rect.y + this.bounding_rect.h);
				} // end if left side is tallest
				else
				{
					c = new Geometry.Point(
							this.inscribed_rect.x + this.inscribed_rect.w,
							this.bounding_rect.y + this.bounding_rect.h);
				} // right side is taller
			} // end else
			
			// define the hat
			this.hat = new Geometry.Triangle(a, b, c);
		} // end Constructor
		
		public void draw(Graphics g)
		{
			// draw rectangle of trapezoid
			this.inscribed_rect.draw(g);
			
			// draw triangle (hat)
			// get the points of the triangle
			int[] x_values = {
					(int) this.hat.a.x, 
					(int) this.hat.b.x, 
					(int) this.hat.c.x};
			int[] y_values = {
					(int) this.hat.a.y, 
					(int) this.hat.b.y, 
					(int) this.hat.c.y};
			
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
			} // end if graphics
		} // end draw
	} // end Chunk
} // end GameObstacle
