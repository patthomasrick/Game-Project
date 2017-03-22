/**
 * Provides a simple sprite system. Provides simple tools to draw a sprite's rect,
 * store a color for a rect, and killing a sprite if it is no longer needed. Killing
 * a sprite doesn't delete it from memory or prevent it from still being used, it just
 * disables rendering of the sprite.
 * 
 * @author Patrick Thomas
 * @version 2/3/17
 */

import java.awt.Graphics;
import java.awt.Color;

/**
 * The Sprite class. Provides simple tools to draw a sprite's rect,
 * store a color for a rect, and killing a sprite if it is no longer needed. Killing
 * a sprite doesn't delete it from memory or prevent it from still being used, it just
 * disables rendering of the sprite.
 * 
 * Behaves similarily to a rectangle (Rectangle)
 * 
 * @author Patrick Thomas
 */
public class Sprite extends Geometry.Rect
{
	/** Whether or not to draw the rectangle or use it at all. */
	boolean alive = true;
	
	/** The color that the rect will be draw as */
	Color color;
	
	/**
	 * A constructor for a Sprite. Needs position and dimension for a 2D rectangle.
	 * Color is stored for the eventual drawing of a rectangle.
	 * 
	 * @param x			X coord of rectangle
	 * @param y			Y coord of rectangle
	 * @param h			Height of rectangle
	 * @param w			Width of rectangle
	 * @param color		Color of rectangle
	 */
	public Sprite(double x, double y, int h, int w, Color color)
	// constructs Sprite based off of rect
	{
		super(x, y, h, w);
		
		this.color = color;
	} // end constructor
	
	/**
	 * Draw the sprite as a simple rectangle to the screen.
	 * 
	 * @param g		Graphics object to draw to
	 */
	public void draw(Graphics g)
	{// get obstacle
		if (this.alive == true)
		{
			g.setColor(color);
			g.fillRect((int) this.x, (int) this.y, this.w, this.h);
		} // end if alive
	} // end draw
	
	public void resize(double x_factor, double y_factor)
	{
		this.x *= x_factor;
		this.w *= x_factor;
		this.y *= y_factor;
		this.h *= y_factor;
	}
	
	/**
	 * Disables the Sprite from being draw through calling the Sprite.draw() method.
	 */
	public void kill()
	// sets the sprite to be dead.
	{
		this.alive = false;
	}
} // end Sprite
