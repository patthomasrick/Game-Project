/*
Authors:	Isaac Payne
Date:		2/9/17
Purpose: 	Create menu for game
Version:	2/28/17
*/
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class Menu 
{
	ArrayList<Button> buttons;
	public static Button noButton = new Button(0,0,0,0,Color.white,"");
	public static Color bg_color = new Color(30,30,37);
	public static Color button_color = new Color(43,42,62);
	
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
		}// end iterator
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
			} // end if button clicked
		} // end iterator
		
		return clickedbutton;
	} //end tick
	
	public static class Button extends Geometry.Rect 
	{
		//Variables
		Color c, darker_c, current_c;
		String s;
		public static Font button_font = new Font("Dialog", Font.BOLD, 48);
		
		public Button(double x,double y,int h, int w, Color c, String s)
		{
			super(x,y,h,w);
			this.c = c;
			this.darker_c = new Color(51,48,93);
			this.current_c = c;
			this.s = s;
		} //end Button
		
		public void draw(Graphics g)
		{
			g.setColor(this.current_c);
			g.fillRect((int)x, (int)y, w, h);
			
			g.setFont(button_font);
			int w1 = (g.getFontMetrics().stringWidth(this.s))/2;
			int h1 = (g.getFontMetrics().getHeight())/4;
			g.setColor(Color.BLACK);
			g.drawString(this.s, (int)((this.x+(this.w/2))-w1+2), (int)((this.y+(this.h/2))+h1+2));
			g.setColor(Color.WHITE);
			g.drawString(this.s, (int)((this.x+(this.w/2))-w1), (int)((this.y+(this.h/2))+h1));
		} //end draw
		
		public boolean tick(double x,double y, boolean clicked)
		{
			if (this.collide_point(x,y))
			{
				this.current_c = this.darker_c;
				
				if (clicked == true)
				{
					return true;
				} // end if clicked 
				else return false;
			} // end if point colliding
			
			else
			{
				this.current_c = c;
				return false;
			} // end not colliding
		} //end tick
	} //end Button
} //end menu
