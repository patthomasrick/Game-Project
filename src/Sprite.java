/**
Authors:	Patrick Thomas
Date:		2/4/17
Purpose: 	Provides a simple sprite system.
*/

import java.awt.Graphics;
import java.awt.Color;

public class Sprite extends Rect
{
	boolean alive = true;
	
	public Sprite(double x, double y, int h, int w)
	// constructs Sprite based off of rect
	{
		super(x, y, h, w);
	} // end constructor
	
	public void draw(Graphics g, Color color)
	{// get obstacle
		if (this.alive = true)
		{
			g.setColor(color);
			g.fillRect((int) this.x, (int) this.y, this.w, this.h);
		} // end if alive
	} // end draw
	
	public void kill()
	// sets the sprite to be dead.
	{
		this.alive = false;
	}
} // end Sprite