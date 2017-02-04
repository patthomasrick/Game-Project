/**
Authors:	Patrick Thomas
Date:		2/4/17
Purpose: 	Provides a simple sprite system.
*/

import java.awt.Graphics;
import java.awt.Color;

public class Sprite extends Rect
{
	public Sprite(double x, double y, int h, int w)
	// constructs Sprite based off of rect
	{
		super(x, y, h, w);
	} // end constructor
	
	public void draw(Graphics g, Color color)
	{// get obstacle
		g.setColor(color);
		g.fillRect((int) this.x, (int) this.y, this.w, this.h);
	} // end draw
} // end Sprite
