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
	public static Button no_button = new Button(0,0,0,0,Color.white,"");
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
	
	/**
	 * Tick the menu. Returns the button that was clicked in the frame.
	 * @param x			x position of the mouse
	 * @param y			y position of the mouse
	 * @param clicked	boolean if the mouse is clicked
	 * @return			Button, clicked
	 */
	public Menu.Button tick(double x, double y, boolean clicked)
	{
		Menu.Button clickedbutton = no_button;
		
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
	
	/**
	 * Button class to be in menus.
	 * @author Isaac Payne
	 *
	 */
	public static class Button extends Geometry.Rect 
	{
		//Variables
		Color c, darker_c, current_c;
		String s;
		public static Font button_font = new Font("Dialog", Font.BOLD, 48);
		
		/**
		 * Constructor for button
		 * @param x		x position of button
		 * @param y		y position of button
		 * @param h		height of button
		 * @param w		width of button
		 * @param c		color of button
		 * @param s		text on button
		 */
		public Button(double x,double y,int h, int w, Color c, String s)
		{
			super(x,y,h,w);
			this.c = c;
			this.darker_c = new Color(51,48,93);
			this.current_c = c;
			this.s = s;
		} //end Button
		
		/**
		 * Renders the button and text.
		 * @param g		graphics object to draw to
		 */
		public void draw(Graphics g)
		{
			// draw button
			g.setColor(this.current_c);
			g.fillRect((int)x, (int)y, w, h);
			
			// draw text
			g.setFont(button_font);
			int w1 = (g.getFontMetrics().stringWidth(this.s))/2;
			int h1 = (g.getFontMetrics().getHeight())/4;
			g.setColor(Color.BLACK);
			g.drawString(this.s, (int)((this.x+(this.w/2))-w1+2), (int)((this.y+(this.h/2))+h1+2));
			g.setColor(Color.WHITE);
			g.drawString(this.s, (int)((this.x+(this.w/2))-w1), (int)((this.y+(this.h/2))+h1));
		} //end draw
		
		/**
		 * Ticks the button per frame. Checks every frame if the button has been clicked, and returns true
		 * if it has been clicked.
		 * @param x			X position of mouse
		 * @param y			Y position of mouse
		 * @param clicked	If the mouse is clicked
		 * @return			boolean if the mouse was clicked on the button
		 */
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
