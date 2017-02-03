/**
Authors:	Patrick Thomas
Date:		2/3/17
Purpose: 	Provide the map for hang gliding. THe map scrolls with the progression of time. The map also needs
			to scroll based on the speed of the hang glider. Map generation makes the map get more difficult the longer
			the player has been flying.
			
			The map objects (stalagtite and stalagmites) are chosen based on "every other time", so that they alternate.
*/

import java.awt.Color; // imported to define custom colors
import java.awt.Graphics;

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
	
	// define some theme colors
	static Color bg_color_1 = new Color(23, 37, 87); // dark blue
	static Color bg_color_2 = new Color(48, 62, 115); // darker blue
	static Color fg_color_1 = new Color(170, 135, 57); // sandy yellow
	static Color fg_color_2 = new Color(128, 95, 21); // dark sandy yellow
	
	// arrays for game objs
	CaveObstacle[] ceiling_objs;
	CaveObstacle[] floor_objs;
	
	// create timer for events
	Events.EventTimer etimer = new Events.EventTimer();
	
	// constructor for map
	public Map(int screensize_x, int screensize_y)
	{
		// variables
		this.scroll_speed = 3;
		
		// floor and ceiling
		this.ceiling = 50;
		this.floor = 550;
		
		// creates an array for the points of ceiling 
		this.ceiling_objs = new CaveObstacle[10];
		this.floor_objs = new CaveObstacle[10];
		
		// create initial game objects
		for (int i = 0; i < 10; i++)
		{
			// -----------------------
			// CEILING
			// -----------------------
			
			// create first obj
			if (i == 0)
			{
				CaveObstacle co = new CaveObstacle(this.ceiling, true);
				this.ceiling_objs[i] = co;
			} // end if first run
			
			// create all subsequent objs
			else
			{
				CaveObstacle co = new CaveObstacle(this.ceiling, true);
				co.rect_x += this.ceiling_objs[i-1].rect_x;
				
				this.ceiling_objs[i] = co;
			} // end else

			// -----------------------
			// FLOOR
			// -----------------------
			
			// create first obj
			if (i == 0)
			{
				CaveObstacle co = new CaveObstacle(this.floor, false);
				this.floor_objs[i] = co;
			} // end if first run
			
			// create all subsequent objs
			else
			{
				CaveObstacle co = new CaveObstacle(this.floor, false);
				co.rect_x += this.floor_objs[i-1].rect_x;
				
				this.floor_objs[i] = co;
			} // end else
		} // end for loop
		
		this.current_tick = 0;
	} // end map constructor

	public void tick()
	{
		// TICK GAME OBJECTS
		if (this.current_tick%1 == 0)
		{
			for (CaveObstacle go: this.ceiling_objs)
			{
				go.tick(this.scroll_speed);
				if (go.rect_x + go.rect_w < 0)
				{
					go.rect_x = 800;
				} // reset rect to beginning of screen
			} // end tick ceiling g.os
			
			for (CaveObstacle go: this.floor_objs)
			{
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
	
	// 2/3/17
	// draw the map's objects
	public void draw(Graphics g)
	{
		// 	PLACEHOLDER UNTIL GETTING APPLET SIZE IS DONE
		int aWidth = 800;
		
		// set color and draw objects
        g.setColor(Map.fg_color_1);
		for (CaveObstacle co: this.ceiling_objs)
		{
			g.fillPolygon(co.polygon_x(), co.polygon_y(), 3);
		} // end tick ceiling g.os
		g.fillRect(0, 0, aWidth, ceiling); // rect for ceiling (placeholder)

        g.setColor(Map.fg_color_2);
		for (Map.CaveObstacle co: this.floor_objs)
		{
			g.fillPolygon(co.polygon_x(), co.polygon_y(), 3);
		} // end tick ceiling g.os
		g.fillRect(0, this.floor, aWidth, 50); // rect for floor (placeholder)
	}
	
	public void create_game_object()
	{
		// UNUSED
	} // end create tick game object

	
	// class for a stalagtite/mite
	public static class CaveObstacle
	{
		// these are all isosceles trianges (2 sides equal)
		int base, side;
		
		// bounding rect
		int rect_x, rect_y;
		int rect_l, rect_w;
		
		int[] tri_a = new int[2];
		int[] tri_b = new int[2];
		int[] tri_c = new int[2];
		
		boolean ceiling_or_floor;
		
		public CaveObstacle(int x, boolean ceiling)
		{
			this.ceiling_or_floor = ceiling;
			
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
