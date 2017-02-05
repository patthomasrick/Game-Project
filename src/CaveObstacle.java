import java.awt.Color;
import java.awt.Graphics;

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
	// left half and right half for collisions
	Rect left_half;
	Rect right_half;
	
	public CaveObstacle(double x, double y, int h, int w, Color color)
	{
		// init sprite
		super(x, y, h, w, color);
		
		// split halves
		left_half = new Rect(this.x, this.y, this.h, this.w/2);
		right_half = new Rect(this.x + this.w/2.0, this.y, this.h, this.w/2);
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
		
		// draws based on color and points
		g.setColor(this.color);
		g.fillPolygon(x_values, y_values, 3);;
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
	
	public boolean collide_as_triangle(Rect r)
	/*
	 * 2/4/17
	 * 
	 * Collides with a rect (around the hang glider preferably) as an isosceles triangle.
	 */
	{
		// use the Geometry class to help
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
} // end GameObstacle
