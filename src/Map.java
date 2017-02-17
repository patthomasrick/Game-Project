/** 
 * Provide the map for hang gliding. THe map scrolls with the progression of time.
 * The map also needs to scroll based on the speed of the hang glider. Map 
 * generation makes the map get more difficult the longer the player has been 
 * flying.
 * 
 * The map objects (stalagtite and stalagmites) are chosen based on "every other 
 * time", so that they alternate.
 * 
 * @author Patrick Thomas
 * @version 2/3/17
 */

import java.awt.Color; // imported to define custom colors
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom; // allows for random int in a range

/** 
 * Creates a map object that stores and controls game obstacles.
 */
public class Map
{
	// variables
	private double scroll_speed; // how fast the map scrolls (placeholder)
	private double scroll_factor = 1.0; // how fast the map moves in accord to screen size
	
	// applet size
	private int a_width;
	private int a_height;
	
	// floor and ceiling
	private int ceiling;
	private int floor;
	
	// constants
	private final double W_FACTOR = 800.0;
	private final double H_FACTOR = 600.0;
	private final double PCT_CEILING = 1.0/12.0;
	private double PCT_FLOOR = 10.0/12.0;
	
	// hardcode spawning
	private int distance_between_spawns = 150;
	private int distance_until_spawn = 0;
	private boolean next_spawn_is_ceiling = true;
	
	// harcode floor/ceiling generation
	private final int fc_max_dist = 100;
	private final int fc_min_dist = 40;
	private final int fc_max_height = 60;
	private final int fc_min_height = 10;
	
	private int next_spawn_floor_chunk, next_spawn_ceiling_chunk; 
	
	private CaveObstacle.Chunk last_floor_chunk, last_ceiling_chunk;
	
	/** Defined colors of theme */
	public static Color bg_color_1 = new Color(23, 37, 87); // dark blue
	public static Color bg_color_2 = new Color(48, 62, 115); // darker blue
	public static Color fg_color_1 = new Color(170, 135, 57); // sandy yellow
	public static Color fg_color_2 = new Color(128, 95, 21); // dark sandy yellow
	
	// arrays for game obstacles
	private ArrayList<CaveObstacle> obstacles;
	private ArrayList<CaveObstacle.Chunk> map_floor, map_ceiling;
//	
//	// create timer for events
//	private Events.EventTimer etimer = new Events.EventTimer();
	
	/**
	 * Constructor for the map.
	 * 
	 * Scroll speed will be designed to change based on the speed that the hand glider
	 * is travelling at. Physics will be used to calculate speeds and such, and the
	 * (somewhat unconserved) kinetic energy will be used to calculate the speed after
	 * dives and such. The x-component of the speed is then used as the scroll speed.
	 * 
	 * @param screensize_x	The width of the applet
	 * @param screensize_y	The height of the applet
	 * @param scroll_speed	The intial speed of the hang glider
	 */
	public Map(int screensize_x, int screensize_y, double scroll_speed)
	{
		// set scroll speed to an initial value
		this.scroll_speed = scroll_speed;
		
		// set screensize to be what is specified
		this.a_width = screensize_x;
		this.a_height = screensize_y;
		
		// y-values of the ceiling and floor
		// updates this to change to a percent of the applet size.
		this.ceiling = 	(int) (this.a_height*this.PCT_CEILING);
		this.floor 	 = 	(int) (this.a_height*this.PCT_FLOOR);
		
		// creates an array for the points of ceiling 
		this.obstacles = new ArrayList<CaveObstacle>();
		// init arrays for chunks
		this.map_floor = new ArrayList<CaveObstacle.Chunk>();
		this.map_ceiling = new ArrayList<CaveObstacle.Chunk>();
		
		// create preliminary chunks
		// random numbers for brevity
		int f_w, f_l, f_r;
		f_w = (int) (ThreadLocalRandom.current().nextInt(this.fc_min_dist, this.fc_max_dist));
		f_l = (int) (ThreadLocalRandom.current().nextInt(this.fc_min_height, this.fc_max_height));
		f_r = (int) (ThreadLocalRandom.current().nextInt(this.fc_min_height, this.fc_max_height));
		
		// create floor points
		Geometry.Point a1, a2, b1, b2;
		a1 = new Geometry.Point(this.a_width + 200, this.floor);
		a2 = new Geometry.Point(this.a_width + 200, this.floor + f_l);
		b1 = new Geometry.Point(this.a_width + f_w + 200, this.floor);
		b2 = new Geometry.Point(this.a_width + f_w + 200, this.floor + f_r);
		
		// create chunk
		CaveObstacle.Chunk floor_chunk = new CaveObstacle.Chunk(
				a1,
				a2,
				b1,
				b2,
				Map.fg_color_2);	// color
		
		// repeat process for ceiling chunk
		// random numbers for brevity
		int c_w, c_l, c_r;
		c_w = (int) (ThreadLocalRandom.current().nextInt(this.fc_min_dist, this.fc_max_dist));
		c_l = (int) (ThreadLocalRandom.current().nextInt(this.fc_min_height, this.fc_max_height));
		c_r = (int) (ThreadLocalRandom.current().nextInt(this.fc_min_height, this.fc_max_height));
		
		// create ceiling points
		a1 = new Geometry.Point(this.a_width + 200, this.ceiling);
		a2 = new Geometry.Point(this.a_width + 200, this.ceiling - c_l);
		b1 = new Geometry.Point(this.a_width + c_w + 200, this.ceiling);
		b2 = new Geometry.Point(this.a_width + c_w + 200, this.ceiling - c_r);
		
		// create chunk
		CaveObstacle.Chunk ceiling_chunk = new CaveObstacle.Chunk(
				a1,
				a2,
				b1,
				b2,
				Map.fg_color_1);	// color
		
		// add chunks to arraylists
		this.map_floor.add(floor_chunk);
		this.map_ceiling.add(ceiling_chunk);
		
		// set the new chunks as the newly created chunks
		this.last_ceiling_chunk = ceiling_chunk;
		this.last_floor_chunk = floor_chunk;
	} // end map constructor

	
	/**
	 * This is to update the map with time. The map is updated per tick, along with screen
	 * refreshes. Thus, the rate that the map is updated is tied in with FPS. This is
	 * supposed to be ran every update of the screen/when the rest of the game ticks.
	 * 
	 * Every tick, the map's objects are scrolled in relation with the hang glider.
	 * 
	 * This is also designed to hook in with the events class, so timed events can happen
	 * per amount of ticks or check a condition every set amount of ticks.
	 * 
	 * @param a_width	The width of the applet
	 * @param a_height	The height of the applet
	 * @param hg		The TestGlider object
	 */
	public void tick(int a_width, int a_height, TestGlider hg, double scroll_speed)
	{
		// set scroll speed to this
		this.scroll_speed = scroll_speed;
		// renew applet size. useful in case of screen size changes
		// change current screen objects to update immediately to changes
		if (!(this.a_width == a_width) || !(this.a_height == a_height))
		{
			// call iterator and loop through all objects in the iterator
			for (Iterator<CaveObstacle> iter = obstacles.iterator(); iter.hasNext();)
			{
				// get the next item in the iterator
				CaveObstacle go = iter.next();
				
				double[] factors = {
						((float) a_width)/((float) this.a_width), 
						((float) a_height)/((float) this.a_height)};
				
				// call the method that performs the resizing of the GO
				go.resize(factors[0], factors[1]);
			} // end iteration
			
			// also update distance between spawns and scroll speed
			this.distance_between_spawns = (int) (150.0 * a_width/this.W_FACTOR);
			this.scroll_factor = a_width/this.W_FACTOR;
			
			// update values after changes are done
			this.a_width = a_width;
			this.a_height = a_height;
		} // end if width changed
		
		// updates ceiling and floor to change to a percent of the applet size.
		this.ceiling = 	(int) (this.a_height*this.PCT_CEILING);
		this.floor 	 = 	(int) (this.a_height*this.PCT_FLOOR);
		
		// reset color. useful in case if the hg is actually colliding with an object
		hg.color = Color.GREEN;
		
		// call iterator and loop through all objects in the iterator
		for (Iterator<CaveObstacle> iter = obstacles.iterator(); iter.hasNext();)
		{
			// get the next item in the iterator
			CaveObstacle go = iter.next();
			
			// internally tick the game object
			go.tick(this.scroll_speed * this.scroll_factor);
			
			// test for collisions, if colliding, turn hg red
			if (go.collide_as_triangle(hg) == true)
				hg.color = Color.RED;
			
			// if the obstacle is off-screen, delete it
			if (go.x + go.w < 0)
			{
				iter.remove();
			} // end if off-screen
		} // end tick game objects

		// call iterator and loop through all objects in the iterator
		for (Iterator<CaveObstacle.Chunk> iter = map_ceiling.iterator(); iter.hasNext();)
		{
			// get the next item in the iterator
			CaveObstacle.Chunk chunk = iter.next();
			
			// internally tick the game object
			chunk.tick(1000/60, this.scroll_speed * this.scroll_factor * 0.5 );
			
			// test for collisions, if colliding, turn hg red
//			if (go.collide_as_triangle(hg) == true)
//				hg.color = Color.RED;
			
			// if the obstacle is off-screen, delete it
//			if (go.x + go.w < 0)
//			{
//				iter.remove();
//			} // end if off-screen
		} // end tick game objects
		
		// ---------- SPAWNING -------------
		// set to spawn obstacle every set distance
		if (this.distance_until_spawn <= 0)
		{
			// get applet screen factors
			double x_shift = this.a_width/this.W_FACTOR;
			double y_shift = this.a_height/this.H_FACTOR;
			
			// randomize values of the obstacle to be generated
			int x_rand = (int) (ThreadLocalRandom.current().nextInt(-40, 40 + 1) * x_shift);
			int w_rand = (int) (ThreadLocalRandom.current().nextInt(60, 230 + 1) * x_shift);
			int h_rand = (int) (ThreadLocalRandom.current().nextInt(80, 280 + 1) * y_shift);
			
			// init new obstacle
			CaveObstacle co;
			// spawning on ceilings
			if (this.next_spawn_is_ceiling == true)
			{
				co = new CaveObstacle(
						(double) (this.a_width + 50*x_shift + x_rand),
						(double) this.ceiling,
						h_rand,
						w_rand,
						Map.fg_color_1);
			} // end if time to spawn on ceiling
			// spawning on floors
			else
			{
				co = new CaveObstacle(
						(double) (this.a_width + 50*x_shift + x_rand),
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
		
		// nothing is supposed to be spawned, so just tick down distance
		else
		{
			// simulate the passage of cave
			this.distance_until_spawn -= (this.scroll_speed * this.scroll_factor);
		} // end else
		
		// spawning of "chunks" for floor and ceiling
		// --------------FLOOR CHUNKS-----------------
		if (this.next_spawn_floor_chunk <= 0)
		{
			// spawn new chunk
			// random numbers for brevity
			int f_l = (int) (ThreadLocalRandom.current().nextInt(this.fc_min_height, this.fc_max_height));
			int f_w = (int) (ThreadLocalRandom.current().nextInt(this.fc_min_dist, this.fc_max_dist));
			
			CaveObstacle.Chunk floor_chunk = new CaveObstacle.Chunk(
					this.last_floor_chunk,			// create chunk from last chunk
					f_l,							//
					f_w);
			this.last_floor_chunk = floor_chunk;	// set the new last chunk to the right one
			this.map_floor.add(floor_chunk);		// add new chunk to the arraylist
			
			// reset distance until floor/ceiling point
			this.next_spawn_floor_chunk = ThreadLocalRandom.current().nextInt(
					this.fc_min_dist, 
					this.fc_max_dist);
		} // end if time to spawn chunk
		else
		{
			this.next_spawn_floor_chunk -= (this.scroll_speed * this.scroll_factor);
		} // end else, not time to spawn new chunk
		
		// --------------CEILING CHUNKS-----------------
		if ((this.last_ceiling_chunk.b1.x <= this.a_width+100) && (this.next_spawn_ceiling_chunk <= 0))
		{
			// spawn new chunk
			// random numbers for brevity
			int f_l = (int) (ThreadLocalRandom.current().nextInt(this.fc_min_height, this.fc_max_height));
			
			CaveObstacle.Chunk ceiling_chunk = new CaveObstacle.Chunk(
					this.last_ceiling_chunk,			// create chunk from last chunk
					f_l,							//
					(int) (this.a_width - this.last_ceiling_chunk.pos.x));
			this.last_ceiling_chunk = ceiling_chunk;	// set the new last chunk to the right one
			this.map_ceiling.add(ceiling_chunk);		// add new chunk to the arraylist
			
			// reset distance until floor/ceiling point
			this.next_spawn_ceiling_chunk = ThreadLocalRandom.current().nextInt(
					this.fc_min_dist, 
					this.fc_max_dist);
		} // end if time to spawn chunk
		else
		{
			this.next_spawn_ceiling_chunk -= (this.scroll_speed * this.scroll_factor);
		} // end else, not time to spawn new chunk
	} // end tick
	
	/**
	 * Draw the map's objects (obstacles and coins) onto the given graphics object.
	 * 
	 * @param g		Graphics object to draw to applet
	 */
	public void draw(Graphics g)
	{	
		// draw stalagtites/mites
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
		} // end if any obstacles exist

		// draw floor
		if (this.map_floor.size() > 0)
		{
			// iterate through all of the obstacles
			Iterator<CaveObstacle.Chunk> chunk_iter = this.map_floor.iterator();
			while(chunk_iter.hasNext())
			{
				// get obstacle
				CaveObstacle.Chunk chunk = chunk_iter.next();
				// use built-in draw method
				chunk.draw(g);
			} // end while draw obstacles
		} // end if any obstacles exist

		// draw ceiling
		if (this.map_ceiling.size() > 0)
		{
			// iterate through all of the obstacles
			Iterator<CaveObstacle.Chunk> chunk_iter = this.map_ceiling.iterator();
			while(chunk_iter.hasNext())
			{
				// get obstacle
				CaveObstacle.Chunk chunk = chunk_iter.next();
				// use built-in draw method
				chunk.draw(g);
			} // end while draw obstacles
		} // end if any obstacles exist
		
		// draw floor and ceiling
		g.setColor(Map.fg_color_1);
		g.fillRect(0, 0, this.a_width, ceiling);
        g.setColor(Map.fg_color_2);
		g.fillRect(0, this.floor, this.a_width, floor);
	} // end draw
} // end class
