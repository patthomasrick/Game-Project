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
	// init variables
	Color color;
	
	public CaveObstacle(double x, double y, int h, int w, Color color)
	{
		// init sprite
		super(x, y, h, w);
		
		// init this
		this.color = color;
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
	 * Will eventually host collision functions
	 */
	{
		// move obstacle
		this.push(-hg_speed, 0);	// method provided through Rect
	} // end tick
} // end GameObstacle