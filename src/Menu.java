/*
Authors:	Isaac Payne
Date:		2/9/17
Purpose: 	Create menu for game
Version:	2/14/17
*/
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class Menu 
{
	ArrayList<Button> buttons;
	
	public Menu()
	{
		this.buttons = new ArrayList<Button>();
	} //Menu
	
	public void add_button(Button b)
	{
		buttons.add(b);
	} //end add_button
	
	public void draw(Graphics g)
	{
		for(Iterator<Button> iter = buttons.iterator(); iter.hasNext();)
		{
			Button b = iter.next();
			b.draw(g);
		}
	} //end draw
	
	public void tick(double x, double y)
	{
		for(Iterator<Button> iter = buttons.iterator(); iter.hasNext();)
		{
			Button b = iter.next();
			b.tick(x,y);
		}
	} //end tick
	
	public static class Button extends Geometry.Rect 
	{
		//Variables
		Color c, darker_c, current_c;
		String s;
		
		public Button(double x,double y,int h,int w, Color c, String s)
		{
			super(x,y,h,w);
			this.c = c;
			this.darker_c = c.darker();
			this.current_c = c;
			this.s = s;
		} //end Button
		
		public void draw(Graphics g)
		{
			g.setColor(this.current_c);
			g.fillRect((int)x, (int)y, w, h);
		} //end draw
		
		public void tick(double x,double y)
		{
			if (this.collide_point(x,y))
			{
				this.current_c = this.darker_c;
			}
			else this.current_c = c;
		} //end tick
		
	} //end Button
	
} //end menu
