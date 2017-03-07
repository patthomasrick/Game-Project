/*
Authors:	Isaac Payne
Date:		2/9/17
Purpose: 	Create menu for game
Version:	2/28/17
*/
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class Menu 
{
	ArrayList<Button> buttons;
	public static Button noButton = new Button(0,0,0,0,Color.white,"");
	
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
	
	
	public Menu.Button tick(double x, double y, boolean clicked)
	{
		Menu.Button clickedbutton = noButton;
		
		for(Iterator<Button> iter = buttons.iterator(); iter.hasNext();)
		{
			Button b = iter.next();
			boolean hasbeenclicked = b.tick(x,y,clicked);
			
			if(hasbeenclicked == true)
			{
				clickedbutton = b;
			}
		}
		
		return clickedbutton;
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
		
		public boolean tick(double x,double y, boolean clicked)
		{
			if (this.collide_point(x,y))
			{
				this.current_c = this.darker_c;
				
				if(clicked == true)
				{
					return true;
				}
				else return false;
			}
			
			else
			{
				this.current_c = c;
				return false;
			}
			
		} //end tick
		
	} //end Button
	
} //end menu
