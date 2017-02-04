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
import java.util.concurrent.ThreadLocalRandom; // allows for random int in a range

public class Map
{
	// variables
	double scroll_speed; // how fast the map scrolls (placeholder)
	
	// applet size
	int a_width;
	int a_height;
	
	// floor and ceiling
	int ceiling;
	int floor;
	
	// hardcode spawning
	int distance_between_spawns = 150;
	int distance_until_spawn = 0;
	boolean next_spawn_is_ceiling = true;
	
	// define some theme colors
	static Color bg_color_1 = new Color(23, 37, 87); // dark blue
	static Color bg_color_2 = new Color(48, 62, 115); // darker blue
	static Color fg_color_1 = new Color(170, 135, 57); // sandy yellow
	static Color fg_color_2 = new Color(128, 95, 21); // dark sandy yellow
	
	// arrays for game obstacles
	ArrayList<CaveObstacle> obstacles;
	
	// create timer for events
	Events.EventTimer etimer = new Events.EventTimer();
	
	public Map(int screensize_x, int screensize_y, double scroll_speed)
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
		// set scroll speed to a constant for now
		this.scroll_speed = scroll_speed;
		
		// set screensize to be what is specified
		this.a_width = screensize_x;
		this.a_height = screensize_y;
		
		// y-values of the ceiling and floor
		// updates this to change to a percent of the applet size.
		this.ceiling = 	this.a_height/12;
		this.floor = 	this.a_height*11/12;
		
		// creates an array for the points of ceiling 
		this.obstacles = new ArrayList<CaveObstacle>();
	} // end map constructor

	public void tick(int a_width, int a_height)
	/*
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
		// renew applet size
		this.a_width = a_width;
		this.a_height = a_height;
		
		// TICK GAME OBJECTS
		
		/*
		 * Iterate through the ArrayList. Use iterators.
		 *  
		 * For every object in the ArrayList/iterator, run the individual object's
		 * tick. 
		 */
		
		// recall iterator
		Iterator<CaveObstacle> obst_iter = obstacles.iterator();
		while(obst_iter.hasNext())
		{
			
			// get the next item in the iterator
			CaveObstacle go = obst_iter.next();
			// internal tick
			go.tick(this.scroll_speed);
			/*
			// remove object if it moves off of screen
			ArrayList<Integer> cull_values = new ArrayList<Integer>();
			if (go.x + go.w < 0)
			{
				// kill the sprite
				go.kill();
				
				// remove object from obstacles
				int index = this.obstacles.indexOf(go);
				cull_values.add(index);
				//this.obstacles.remove(index);
			} // end if off of screen
			
				if (cull_values.size() > 0)
				{
					Iterator<Integer> cull_iter = cull_values.iterator();
					while(cull_iter.hasNext())
					{
						int index = cull_iter.next();
						this.obstacles.remove(index);
					} // end culling
				} // end if things to cull
			}
		*/
		} // end tick game objects
		
		// ---------- SPAWNING ----------------
		if (this.distance_until_spawn <= 0)
		{
			// get applet screen factors
			double x_shift = this.a_width/800.0;
			double y_shift = this.a_height/600.0;
			
			// randomize values
			int x_rand = (int) (ThreadLocalRandom.current().nextInt(-50, 50 + 1) * x_shift);
			int w_rand = (int) (ThreadLocalRandom.current().nextInt(60, 250 + 1) * x_shift);
			int h_rand = (int) (ThreadLocalRandom.current().nextInt(80, 300 + 1) * y_shift);
			// init new obstacle
			CaveObstacle co;
			if (this.next_spawn_is_ceiling == true)
			{
				co = new CaveObstacle(
						(double) (this.a_width + x_rand),
						(double) this.ceiling,
						h_rand,
						w_rand,
						Map.fg_color_1);
			} // end if time to spawn on ceiling
			else
			{
				co = new CaveObstacle(
						(double) (this.a_width + x_rand),
						(double) this.floor,
						-h_rand,
						w_rand,
						Map.fg_color_2);
			} // end if time to spawn on floor
			
			// add new obj to obstacles
			this.obstacles.add(co);
			
			// switch spawn plane for next time
			this.next_spawn_is_ceiling = !this.next_spawn_is_ceiling;
			
			// reset spawn timer
			this.distance_until_spawn = this.distance_between_spawns;
		} // end if spawn
		else
		{
			// simulate the passage of cave
			this.distance_until_spawn -= this.scroll_speed;
		} // end else
	} // end tick
	
	public void draw(Graphics g)
	/*
	 * 2/3/17
	 * 
	 * Draw the map's objects (obstacles and coins) onto the given graphics object.
	 */
	{		
		// first see if there are any objects in the arraylist
		if (this.obstacles.size() > 0)
		{
			// iterate through all of the obstacles
			Iterator<CaveObstacle> obst_iter = obstacles.iterator();
			while(obst_iter.hasNext())
			{
				// get obstacle
				CaveObstacle co = obst_iter.next();
				// use built-in draw method
				co.draw(g);
			} // end while draw obstacles
		} // end if any obstacles
		
		// draw floor and ceiling
		g.setColor(Map.fg_color_1);
		g.fillRect(0, 0, this.a_width, ceiling);
        g.setColor(Map.fg_color_2);
		g.fillRect(0, this.floor, this.a_width, 50);
	} // end draw
} // end class
