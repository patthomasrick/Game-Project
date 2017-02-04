/**
Authors:	Patrick Thomas
Date:		2/3/17
Purpose: 	Provide the map for hang gliding. THe map scrolls with the progression of time.
			The map also needs to scroll based on the speed of the hang glider. Map 
			generation makes the map get more difficult the longer the player has been 
			flying.
			
			The map objects (stalagtite and stalagmites) are chosen based on "every other 
			time", so that they alternate.
*/

import java.awt.Color; // imported to define custom colors
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class Map
{
	// variables
	int scroll_speed; // how fast the map scrolls (placeholder)
	
	// floor and ceiling
	int ceiling;
	int floor;
	
	// min and max space between obstacles (x)
	int min_x_space = 50;
	int max_x_space = 150;
	
	// current tick
	int current_tick = 0;
	int total_ticks = 0;
	int ticks_til_obj_spawn = 30;
	
	// define some theme colors
	static Color bg_color_1 = new Color(23, 37, 87); // dark blue
	static Color bg_color_2 = new Color(48, 62, 115); // darker blue
	static Color fg_color_1 = new Color(170, 135, 57); // sandy yellow
	static Color fg_color_2 = new Color(128, 95, 21); // dark sandy yellow
	
	// arrays for game objs
	ArrayList<CaveObstacle> obstacles;
	// iterator for obstacles
	Iterator<CaveObstacle> obst_iter = obstacles.iterator();
	
	// create timer for events
	Events.EventTimer etimer = new Events.EventTimer();
	
	public Map(int screensize_x, int screensize_y)
	// constructor for map
	{
		// variables
		/*
		 * Scroll speed will be designed to change based on the speed that the hand glider
		 * is travelling at. Physics will be used to calculate speeds and such, and the
		 * (somewhat unconserved) kinetic energy will be used to calculate the speed after
		 * dives and such. The x-component of the speed is then used as the scroll speed.
		 * 
		 * Hang glider needs to be implemented.
		 */
		this.scroll_speed = 3;
		
		// floor and ceiling
		// update this to change to a percent of the applet size.
		this.ceiling = 50;
		this.floor = 550;
		
		// creates an array for the points of ceiling 
		this.obstacles = new ArrayList<CaveObstacle>();
		
		this.current_tick = 0;
	} // end map constructor

	public void tick()
	/**
	 * This is to update the map with time. The map is updated per tick, along with screen
	 * refreshes. Thus, the rate that the map is updated is tied in with FPS. This is
	 * supposed to be ran every update of the screen/when the rest of the game ticks.
	 * 
	 * Every tick, the map's objects are scrolled in relation with the hang glider.
	 * 
	 * This is also designed to hook in with the events class, so timed events can happen
	 * per amount of ticks or check a condition every set amount of ticks.
	 */
	{
		// TICK GAME OBJECTS
		if (((this.current_tick%1 == 0)))
		{
			/*
			 * Iterate through the ArrayList. Use iterators.
			 *  
			 * For every object in the ArrayList/iterator, run the individual object's
			 * tick. 
			 */
			while(this.obst_iter.hasNext())
			{
				// get the next item in the iterator
				CaveObstacle go = this.obst_iter.next();
				
				go.tick(this.scroll_speed);
				if (go.rect_x + go.rect_w < 0)
				{
					go.rect_x = 800;
				} // reset rect to beginning of screen
			} // end tick ceiling g.os
			
			// increment tick once all is done
		} // end if every third tick
		
		// increment ticks
		this.total_ticks += 1; // effectively distance
		this.current_tick += 1; // for timing game spawns
		
		// timed tick
		if (this.current_tick >= 50)
		{
			// reset tick
			this.current_tick = 0;
			
			// spawn stuff
			this.create_game_object();
		} // end if game tick
	} // end tick
	
	public void draw(Graphics g)
	// 2/3/17
	// draw the map's objects
	{
		// 	PLACEHOLDER UNTIL GETTING APPLET SIZE IS DONE
		int aWidth = 800;
		
		// set color and draw objects
        g.setColor(Map.fg_color_1);
		while(this.obst_iter.hasNext())
		{
			CaveObstacle co = this.obst_iter.next();
			g.setColor(co.color);
			g.fillPolygon(co.polygon_x(), co.polygon_y(), 3);
		} // draw obstacles
		
		// draw floor and ceiling
		g.setColor(Map.fg_color_1);
		g.fillRect(0, 0, aWidth, ceiling); // rect for ceiling (placeholder)

        g.setColor(Map.fg_color_2);
		g.fillRect(0, this.floor, aWidth, 50); // rect for floor (placeholder)
	}
	
	public void create_game_object()
	{
		// UNUSED
	} // end create tick game object

	
	public static class CaveObstacle
	// class for a stalagtite/mite
	{
		// these are all isosceles trianges (2 sides equal)
		int base, side;
		
		Color color;
		
		// bounding rect
		int rect_x, rect_y;
		int rect_l, rect_w;
		
		int[] tri_a = new int[2];
		int[] tri_b = new int[2];
		int[] tri_c = new int[2];
		
		boolean ceiling_or_floor;
		
		public CaveObstacle(int x, boolean ceiling, Color color)
		{
			this.ceiling_or_floor = ceiling;
			
			this.color = color;
			
			// placeholder lengths
			this.base = (int) (Math.random()*60)+30;
			this.side = (int) (Math.random()*130)+100;
			
			// x is between two set numbers
			this.rect_x = (int)(Math.random() * 51) + 100;
			// y will always be a set number for simplicity
			this.rect_y = x;
			
			// set other rect dimentions
			this.rect_w = this.base;
			this.rect_l = (int) Math.sqrt(Math.pow(this.side, 2) - Math.pow(this.base/2, 2));
			
			// three points of triangle
			this.tri_a[0] = this.rect_x;
			this.tri_a[1] = this.rect_y;
			
			this.tri_b[0] = this.rect_x + this.base;
			this.tri_b[1] = this.rect_y;
			
			this.tri_c[0] = this.rect_x + this.rect_w/2;
			
			if (this.ceiling_or_floor == true)
			{
				this.tri_c[1] = this.rect_y + this.rect_l;
			}
			else
			{
				this.tri_c[1] = this.rect_y - this.rect_l;
			}
		} // end constructor for stalagtite
		
		public void tick(int scroll_speed)
		{
			// move triangle
			this.rect_x -= scroll_speed;
			
			// three points of triangle
			this.tri_a[0] = this.rect_x;
			// this.tri_a[1] = this.rect_y;
			
			this.tri_b[0] = this.rect_x + this.base;
			// this.tri_b[1] = this.rect_y;
			
			this.tri_c[0] = this.rect_x + this.rect_w/2;
			/*
			if (this.ceiling_or_floor == true)
			{
				this.tri_c[1] = this.rect_y + this.rect_l;
			}
			else
			{
				this.tri_c[1] = this.rect_y - this.rect_l;
			}
			*/
		} // end tick
		
		public int[] polygon_x()
		{
			// collect triangle point x values
			int[] tri_x = {this.tri_a[0], this.tri_b[0], this.tri_c[0]};
			return tri_x;
		} // end polygon x
		
		public int[] polygon_y()
		{
			// collect triangle point y values
			int[] tri_y = {this.tri_a[1], this.tri_b[1], this.tri_c[1]};
			return tri_y;
		} // end polygon y
	} // end gameobject
} // end class
