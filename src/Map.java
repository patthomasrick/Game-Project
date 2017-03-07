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
// import java.awt.Graphics2D;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom; // allows for random int in a range

/** 
 * Creates a map object that stores and controls game obstacles.
 */


public class Map
{
	// create decimal format
	DecimalFormat fmt = new DecimalFormat ("0.00");
	
	
	
	// variables
	private double scroll_speed; // how fast the map scrolls (placeholder)
	private double scroll_factor = 1.0; // how fast the map moves in accord to screen x size
	private double dist_travelled = 0;
	
	public Font dist_font;
	
	
	
	// applet size
	private int a_width;	// the current size of the applet
	private int a_height;	
	
	
	
	// floor and ceiling
	private int ceiling;	// the current y positions of the ceiling and floor
	private int floor;
	private int bg_ceiling;	// the current y positions of the ceiling and floor
	private int bg_floor;
	
	
	
	// constants
	
	// how large the window was originally designed to be
	private final double[] SCREEN_FACTOR = {800.0, 600.0};
	public double[] factors = {1.0, 1.0}; 
	
	// the percent of the screen that is the ceiling or floor
	private final double[] PCT_MAP = {1.0/12.0, 10.0/12.0};
	
	
	
	// hardcode spawning
	private final int[] SPIKE_X_RANGE = {-40, 40};
	private final int[] SPIKE_W_RANGE = {60, 300};
	private final int[] SPIKE_H_RANGE = {80, 230};
	private int DIST_BETWEEN_SPIKES = 120;
	
	private int dist_until_spike_spawn = 0;
	private boolean next_spawn_is_ceiling = true;		// this toggles after every spawn
	
	// harcode floor/ceiling generation
	private final int[] CHUNK_SPAWN_OFFSET = {40, 100};
	private final int[] CHUNK_SPAWN_HEIGHT = {10, 60};
	
	private CaveObstacle.Chunk last_floor_chunk, last_ceiling_chunk;

	// arrays for game obstacles
	private ArrayList<CaveObstacle> obstacles;
	private ArrayList<CaveObstacle.Chunk> map_floor, map_ceiling;
	
	
	
	// hardcode spawning OF BACKGROUND OBJECTS
	private final int[] BG_SPIKE_X_RANGE = {-35, 35};
	private final int[] BG_SPIKE_W_RANGE = {50, 240};
	private final int[] BG_SPIKE_H_RANGE = {65, 200};
	private int BG_DIST_BETWEEN_SPIKES = 100;
	
	private int bg_dist_until_spike_spawn = 0;
	private boolean bg_next_spawn_is_ceiling = true;		// this toggles after every spawn
	
	// harcode floor/ceiling generation
	private final int[] BG_CHUNK_SPAWN_OFFSET = {40, 100};
	private final int[] BG_CHUNK_SPAWN_HEIGHT = {10, 60};
	
	private CaveObstacle.Chunk bg_last_floor_chunk, bg_last_ceiling_chunk;

	// arrays for game obstacles
	private ArrayList<CaveObstacle> bg_obstacles;
	private ArrayList<CaveObstacle.Chunk> bg_map_floor, bg_map_ceiling;
	
	
	
	/** Defined colors of theme */
	public static final Color BG_COLOR_L = new Color(48, 62, 115); 	// dark blue
	public static final Color BG_COLOR_D = new Color(23, 37, 87); 	// darker blue
	public static final Color FG_COLOR_L = new Color(170, 135, 57); // sandy yellow
	public static final Color FG_COLOR_D = new Color(128, 95, 21); 	// dark sandy yellow
	
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
		// init font
		this.dist_font = new Font("Dialog", Font.PLAIN, 20);
		
		// set scroll speed to an initial value
		this.scroll_speed = scroll_speed;
		
		// set screensize to be what is specified
		this.a_width = screensize_x;
		this.a_height = screensize_y;
		
		// y-values of the ceiling and floor
		// updates this to change to a percent of the applet size.
		this.ceiling = 	(int) (a_height*PCT_MAP[0]);
		this.floor 	 = 	(int) (a_height*PCT_MAP[1]);
		
		this.bg_ceiling = 	(int) (a_height*(PCT_MAP[0] + 0.1));
		this.bg_floor 	= 	(int) (a_height*(PCT_MAP[1] - 0.1));
		
		// creates an array for the points of ceiling 
		this.obstacles = new ArrayList<CaveObstacle>();
		
		this.bg_obstacles = new ArrayList<CaveObstacle>();
		
		// init arrays for chunks
		this.map_floor = new ArrayList<CaveObstacle.Chunk>();
		this.map_ceiling = new ArrayList<CaveObstacle.Chunk>();
		
		this.bg_map_floor = new ArrayList<CaveObstacle.Chunk>();
		this.bg_map_ceiling = new ArrayList<CaveObstacle.Chunk>();
		
		// --------------------------------------
		// -------- CREATE FIRST CHUNKS ---------
		// --------------------------------------
		
		// -------- FLOOR CHUNKS ----------------
		int f_w, f_l, f_r;
		f_w = (int) (ThreadLocalRandom.current().nextInt(CHUNK_SPAWN_OFFSET[0], CHUNK_SPAWN_OFFSET[1]));
		f_l = (int) (ThreadLocalRandom.current().nextInt(CHUNK_SPAWN_HEIGHT[0], CHUNK_SPAWN_HEIGHT[1]));
		f_r = (int) (ThreadLocalRandom.current().nextInt(CHUNK_SPAWN_HEIGHT[0], CHUNK_SPAWN_HEIGHT[1]));
		
		// create floor points
		int x2, y1, y2, y3;
		x2 = a_width + f_w + 200;
		y1 = this.floor;
		y2 = this.floor - f_l;
		y3 = this.floor - f_r;

		Geometry.Point a1, a2, b1, b2;
		a1 = new Geometry.Point(0, y1);
		a2 = new Geometry.Point(0, y2);
		b1 = new Geometry.Point(x2, y1);
		b2 = new Geometry.Point(x2, y3);
		
		// create chunk
		CaveObstacle.Chunk floor_chunk = new CaveObstacle.Chunk(a2, a1, b2, b1, Map.FG_COLOR_D);
		
		// -------- CEILING CHUNKS ----------------
		
		int c_w, c_l, c_r;
		c_w = (int) (ThreadLocalRandom.current().nextInt(CHUNK_SPAWN_OFFSET[0], CHUNK_SPAWN_OFFSET[1]));
		c_l = (int) (ThreadLocalRandom.current().nextInt(CHUNK_SPAWN_HEIGHT[0], CHUNK_SPAWN_HEIGHT[1]));
		c_r = (int) (ThreadLocalRandom.current().nextInt(CHUNK_SPAWN_HEIGHT[0], CHUNK_SPAWN_HEIGHT[1]));
		
		// create floor points
		x2 = a_width + c_w + 200;
		y1 = this.ceiling;
		y2 = this.ceiling + c_l;
		y3 = this.ceiling + c_r;
		
		// now in Geometry.Point form
		a1 = new Geometry.Point(0, y1);
		a2 = new Geometry.Point(0, y2);
		b1 = new Geometry.Point(x2, y1);
		b2 = new Geometry.Point(x2, y3);
		
		// create chunk
		CaveObstacle.Chunk ceiling_chunk = new CaveObstacle.Chunk(a1, a2, b1, b2, Map.FG_COLOR_L);
		
		// add chunks to arraylists
		this.map_floor.add(floor_chunk);
		this.map_ceiling.add(ceiling_chunk);
		
		// set the new chunks as the newly created chunks
		this.last_ceiling_chunk = ceiling_chunk;
		this.last_floor_chunk = floor_chunk;
		
		// --------------------------------------
		// -------- BG CREATE FIRST CHUNKS -------
		// --------------------------------------
		
		// -------- BG FLOOR CHUNKS ----------------
		f_w = (int) (ThreadLocalRandom.current().nextInt(BG_CHUNK_SPAWN_OFFSET[0], BG_CHUNK_SPAWN_OFFSET[1]));
		f_l = (int) (ThreadLocalRandom.current().nextInt(BG_CHUNK_SPAWN_HEIGHT[0], BG_CHUNK_SPAWN_HEIGHT[1]));
		f_r = (int) (ThreadLocalRandom.current().nextInt(BG_CHUNK_SPAWN_HEIGHT[0], BG_CHUNK_SPAWN_HEIGHT[1]));
		
		// create floor points
		x2 = a_width + f_w + 200;
		y1 = this.bg_floor;
		y2 = this.bg_floor - f_l;
		y3 = this.bg_floor - f_r;

		a1 = new Geometry.Point(0, y1);
		a2 = new Geometry.Point(0, y2);
		b1 = new Geometry.Point(x2, y1);
		b2 = new Geometry.Point(x2, y3);
		
		// create chunk
		CaveObstacle.Chunk bg_floor_chunk = new CaveObstacle.Chunk(a2, a1, b2, b1, Map.BG_COLOR_L);
		
		// -------- CEILING CHUNKS ----------------
		c_w = (int) (ThreadLocalRandom.current().nextInt(BG_CHUNK_SPAWN_OFFSET[0], BG_CHUNK_SPAWN_OFFSET[1]));
		c_l = (int) (ThreadLocalRandom.current().nextInt(BG_CHUNK_SPAWN_HEIGHT[0], BG_CHUNK_SPAWN_HEIGHT[1]));
		c_r = (int) (ThreadLocalRandom.current().nextInt(BG_CHUNK_SPAWN_HEIGHT[0], BG_CHUNK_SPAWN_HEIGHT[1]));
		
		// create floor points
		x2 = a_width + c_w + 200;
		y1 = this.bg_ceiling;
		y2 = this.bg_ceiling + c_l;
		y3 = this.bg_ceiling + c_r;
		
		// now in Geometry.Point form
		a1 = new Geometry.Point(0, y1);
		a2 = new Geometry.Point(0, y2);
		b1 = new Geometry.Point(x2, y1);
		b2 = new Geometry.Point(x2, y3);
		
		// create chunk
		CaveObstacle.Chunk bg_ceiling_chunk = new CaveObstacle.Chunk(a1, a2, b1, b2, Map.BG_COLOR_L);
		
		// add chunks to arraylists
		this.bg_map_floor.add(bg_floor_chunk);
		this.bg_map_ceiling.add(bg_ceiling_chunk);
		
		// set the new chunks as the newly created chunks
		this.bg_last_ceiling_chunk = bg_ceiling_chunk;
		this.bg_last_floor_chunk = bg_floor_chunk;
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

		// get the factors that the screen has changed by
		this.factors[0] = ((float) a_width)/((float) this.a_width);
		this.factors[1] = ((float) a_height)/((float) this.a_height);
		
		// keep track of distance travelled for high scores
		this.dist_travelled += this.scroll_speed;
		
		// --------------------------------------
		// ----------- RESIZE APPLET ------------
		// --------------------------------------
		/*
		 * Renew applet size. useful in case of screen size changes.
		 * Change current screen objects to update immediately to changes.
		 */
		if (!(this.a_width == a_width) || !(this.a_height == a_height))
		{	
			// updates ceiling and floor to change to a percent of the applet size.
			this.ceiling = 	(int) (a_height*PCT_MAP[0]);
			this.floor 	 = 	(int) (a_height*PCT_MAP[1]);

			this.bg_ceiling = 	(int) (a_height*(PCT_MAP[0] + 0.1));
			this.bg_floor 	= 	(int) (a_height*(PCT_MAP[1] - 0.1));
			
			
			
			
			// Loop through all of the CaveObstacles
			for (Iterator<CaveObstacle> iter = obstacles.iterator(); iter.hasNext();)
			{
				CaveObstacle go = iter.next();
				// resize the obstacle
				go.resize(factors[0], factors[1]);
			} // end iteration
			
			// Loop through all chunks
			for (Iterator<CaveObstacle.Chunk> iter = map_floor.iterator(); iter.hasNext();)
			{
				CaveObstacle.Chunk chunk = iter.next();
				chunk.resize(factors[0], factors[1]);
			} // end for loop
			
			// Loop through all chunks
			for (Iterator<CaveObstacle.Chunk> iter = map_ceiling.iterator(); iter.hasNext();)
			{
				CaveObstacle.Chunk chunk = iter.next();
				chunk.resize(factors[0], factors[1]);
			} // end for loop

			
			
			
			// Loop through all of the CaveObstacles
			for (Iterator<CaveObstacle> iter = bg_obstacles.iterator(); iter.hasNext();)
			{
				CaveObstacle go = iter.next();
				// resize the obstacle
				go.resize(factors[0], factors[1]);
			} // end iteration
			
			// Loop through all chunks
			for (Iterator<CaveObstacle.Chunk> iter = bg_map_floor.iterator(); iter.hasNext();)
			{
				CaveObstacle.Chunk chunk = iter.next();
				chunk.resize(factors[0], factors[1]);
			} // end for loop
			
			// Loop through all chunks
			for (Iterator<CaveObstacle.Chunk> iter = bg_map_ceiling.iterator(); iter.hasNext();)
			{
				CaveObstacle.Chunk chunk = iter.next();
				chunk.resize(factors[0], factors[1]);
			} // end for loop
			
			
			
			
			// also update distance between spawns and scroll speed
			DIST_BETWEEN_SPIKES = (int) (150.0 * a_width/SCREEN_FACTOR[0]);
			BG_DIST_BETWEEN_SPIKES = (int) (100.0 * a_width/SCREEN_FACTOR[0]);
			this.scroll_factor = a_width/SCREEN_FACTOR[0];
			
			// update values after changes are done
			this.a_width = a_width;
			this.a_height = a_height;
		} // end if width changed
		
		
		// reset color incase of previous collisions
		hg.color = Color.GREEN;
		
		
		
		
		
		
		
		
		
		
		
		// --------------------------------------
		// -------- TICK GAME OBSTACLES ---------
		// --------------------------------------
		for (Iterator<CaveObstacle> iter = obstacles.iterator(); iter.hasNext();)
		{
			CaveObstacle go = iter.next();
			
			// internally tick the game object
			go.tick(this.scroll_speed * this.scroll_factor);
			
			// test for collisions, if colliding, turn hg red
			if (go.collide_as_triangle(hg) == true)
				hg.color = Color.RED;
			
			// delete off-screen obstacles
			if (go.x + go.w < 0)
			{
				iter.remove();
			} // end if off-screen
		} // end tick game objects

		// --------------------------------------
		// -------- TICK CEILING CHUNKS ---------
		// --------------------------------------
		for (Iterator<CaveObstacle.Chunk> iter = map_ceiling.iterator(); iter.hasNext();)
		{
			CaveObstacle.Chunk chunk = iter.next();
			
			// internally tick the game object
			chunk.tick(1000/60, this.scroll_speed * this.scroll_factor);
			
			// test for collisions, if colliding, turn hg red
			if (chunk.collide_hat_with_rect(hg) == true || chunk.inscribed_rect.collide_rect(hg) == true)
				hg.color = Color.RED;
			
			if (chunk.b1.y <= 0)
			{
				iter.remove();
			} // end gc
			
		} // end tick game objects

		// --------------------------------------
		// -------- TICK FLOOR CHUNKS ---------
		// --------------------------------------
		for (Iterator<CaveObstacle.Chunk> iter = map_floor.iterator(); iter.hasNext();)
		{
			CaveObstacle.Chunk chunk = iter.next();
			
			// internally tick the game object
			chunk.tick(1000/60, this.scroll_speed * this.scroll_factor);
			
			// test for collisions, if colliding, turn hg red
			if (chunk.collide_hat_with_rect(hg) == true || chunk.inscribed_rect.collide_rect(hg) == true)
				hg.color = Color.RED;
			
			if (chunk.b1.y <= 0)
				iter.remove();
		} // end tick game objects

		// --------------------------------------
		// -------- COLLIDE FLOOR & CEILING -----
		// --------------------------------------
		if (hg.y < ceiling || hg.y+hg.h > floor)
			hg.color = Color.RED;
		
		// ########################### BACKGROUND #########################
		
		// --------------------------------------
		// -------- TICK GAME OBSTACLES ---------
		// --------------------------------------
		for (Iterator<CaveObstacle> iter = bg_obstacles.iterator(); iter.hasNext();)
		{
			CaveObstacle go = iter.next();
			
			// internally tick the game object
			go.tick(this.scroll_speed * 0.7);
			
			// delete off-screen obstacles
			if (go.x + go.w < 0)
			{
				iter.remove();
			} // end if off-screen
		} // end tick game objects

		// --------------------------------------
		// -------- TICK CEILING CHUNKS ---------
		// --------------------------------------
		for (Iterator<CaveObstacle.Chunk> iter = bg_map_ceiling.iterator(); iter.hasNext();)
		{
			CaveObstacle.Chunk chunk = iter.next();
			
			// internally tick the game object
			chunk.tick(1000/60, this.scroll_speed * 0.7);
			
			if (chunk.b1.y <= 0)
			{
				iter.remove();
			} // end gc
			
		} // end tick game objects

		// --------------------------------------
		// -------- TICK FLOOR CHUNKS ---------
		// --------------------------------------
		for (Iterator<CaveObstacle.Chunk> iter = bg_map_floor.iterator(); iter.hasNext();)
		{
			CaveObstacle.Chunk chunk = iter.next();
			
			// internally tick the game object
			chunk.tick(1000/60, this.scroll_speed * 0.7);
			
			if (chunk.b1.y <= 0)
				iter.remove();
		} // end tick game objects
		
		
		
		
		
		
		
		
		// --------------------------------------
		// -------- SPAWN GAME OBSTACLES --------
		// --------------------------------------
		if (this.dist_until_spike_spawn <= 0)
		{
			// get applet screen factors
			double x_shift = this.a_width/ SCREEN_FACTOR[0];
			double y_shift = this.a_height/SCREEN_FACTOR[1];
			
			// randomize values of the obstacle to be generated
			int random_x = (int) (ThreadLocalRandom.current().nextInt(
					SPIKE_X_RANGE[0], SPIKE_X_RANGE[1]) * x_shift);
			int random_w = (int) (ThreadLocalRandom.current().nextInt(
					SPIKE_W_RANGE[0], SPIKE_W_RANGE[1]) * x_shift);
			int random_h = (int) (ThreadLocalRandom.current().nextInt(
					SPIKE_H_RANGE[0], SPIKE_H_RANGE[1]) * y_shift);
			
			
			// init new obstacle
			CaveObstacle co;
			Color new_color;
			int new_y;
			
			// spawn on ceiling
			if (next_spawn_is_ceiling == true)
			{
				new_y = this.ceiling;
				new_color = Map.FG_COLOR_L;
			} // end if time to spawn on ceiling
			// otherwise spawn on floor
			
			else
			{
				new_y = this.floor;
				random_h = -random_h;
				new_color = Map.FG_COLOR_D;
			} // end if time to spawn on floor
			
			// create obstacle
			co = new CaveObstacle(
					(double) (this.a_width + 50*x_shift + random_x),
					(double) new_y,
					random_h,
					random_w,
					new_color);
			obstacles.add(co);	// add new obj to obstacles
			
			// switch spawn plane for next time
			next_spawn_is_ceiling = !next_spawn_is_ceiling;
			
			// reset spawn timer
			dist_until_spike_spawn = DIST_BETWEEN_SPIKES;
		} // end if spawn
		
		// nothing is supposed to be spawned, so just tick down distance
		else
		{
			this.dist_until_spike_spawn -= (this.scroll_speed * this.scroll_factor);
		} // end else
		
		// --------------------------------------
		// -------- SPAWN FLOOR CHUNKS ----------
		// --------------------------------------
		if (last_floor_chunk.b1.x <= this.a_width + (100 * factors[0]))
		{
			// spawn new chunk
			// random numbers for brevity
			int f_w, f_l;
			f_w = (int) (ThreadLocalRandom.current().nextInt(
					CHUNK_SPAWN_OFFSET[0], CHUNK_SPAWN_OFFSET[1]) * factors[0]) ;
			f_l = (int) (ThreadLocalRandom.current().nextInt(
					CHUNK_SPAWN_HEIGHT[0], CHUNK_SPAWN_HEIGHT[1]) * factors[1]);
			
			Geometry.Point c1, c2;
			c1 = new Geometry.Point(last_floor_chunk.b1.x + f_w, floor - f_l);
			c2 = new Geometry.Point(last_floor_chunk.b2.x + f_w, floor); 
			
			CaveObstacle.Chunk floor_chunk = new CaveObstacle.Chunk(
					last_floor_chunk.b1, // create chunk from last chunk
					last_floor_chunk.b2,
					c1,
					c2,
					Map.FG_COLOR_D);
			
			last_floor_chunk = floor_chunk;	// set the new last chunk to the right one
			map_floor.add(floor_chunk);		// add new chunk to the arraylist
		} // end if time to spawn chunk

		// --------------------------------------
		// -------- SPAWN CEILING CHUNKS --------
		// --------------------------------------
		if (this.last_ceiling_chunk.b1.x <= this.a_width + (100 * factors[0]))
		{
			// spawn new chunk
			// random numbers for brevity
			int c_w, c_l;
			c_w = (int) (ThreadLocalRandom.current().nextInt(
					CHUNK_SPAWN_OFFSET[0], CHUNK_SPAWN_OFFSET[1]) * factors[0]);
			c_l = (int) (ThreadLocalRandom.current().nextInt(
					CHUNK_SPAWN_HEIGHT[0], CHUNK_SPAWN_HEIGHT[1]) * factors[1]);
			
			CaveObstacle.Chunk ceiling_chunk = new CaveObstacle.Chunk(
					this.last_ceiling_chunk,	// create chunk from last chunk
					c_l,						// ceiling left side height
					c_w);						// ceiling width
			this.map_ceiling.add(ceiling_chunk);		// add new chunk to the arraylist
			this.last_ceiling_chunk = ceiling_chunk;	// set the new last chunk to the right one
		} // end if time to spawn chunk
		
		// KILL THE HANG GLIDER IF HE HITS ANYTHING
		
		if (hg.color == Color.RED)
		{
			hg.kill();
		} // end game if collision
		
		
		
		// #################### BACKGROUND ##################################
		
		// --------------------------------------
		// -------- SPAWN GAME OBSTACLES --------
		// --------------------------------------
		if (this.bg_dist_until_spike_spawn <= 0)
		{
			// get applet screen factors
			double x_shift = this.a_width/ SCREEN_FACTOR[0];
			double y_shift = this.a_height/SCREEN_FACTOR[1];
			
			// randomize values of the obstacle to be generated
			int random_x = (int) (ThreadLocalRandom.current().nextInt(
					BG_SPIKE_X_RANGE[0], BG_SPIKE_X_RANGE[1]) * x_shift);
			int random_w = (int) (ThreadLocalRandom.current().nextInt(
					BG_SPIKE_W_RANGE[0], BG_SPIKE_W_RANGE[1]) * x_shift);
			int random_h = (int) (ThreadLocalRandom.current().nextInt(
					BG_SPIKE_H_RANGE[0], BG_SPIKE_H_RANGE[1]) * y_shift);
			
			
			// init new obstacle
			CaveObstacle co;
			int new_y;
			
			// spawn on ceiling
			if (bg_next_spawn_is_ceiling == true)
			{
				new_y = this.bg_ceiling;
			} // end if time to spawn on ceiling
			// otherwise spawn on floor
			
			else
			{
				new_y = this.bg_floor;
				random_h = -random_h;
			} // end if time to spawn on floor
			
			// create obstacle
			co = new CaveObstacle(
					(double) (this.a_width + 50*x_shift + random_x),
					(double) new_y,
					random_h,
					random_w,
					Map.BG_COLOR_L);
			bg_obstacles.add(co);	// add new obj to obstacles
			
			// switch spawn plane for next time
			bg_next_spawn_is_ceiling = !bg_next_spawn_is_ceiling;
			
			// reset spawn timer
			bg_dist_until_spike_spawn = BG_DIST_BETWEEN_SPIKES;
		} // end if spawn
		
		// nothing is supposed to be spawned, so just tick down distance
		else
		{
			this.bg_dist_until_spike_spawn -= (this.scroll_speed * this.scroll_factor * 0.7);
		} // end else
		
		// --------------------------------------
		// -------- SPAWN FLOOR CHUNKS ----------
		// --------------------------------------
		if (bg_last_floor_chunk.b1.x <= this.a_width + (100 * factors[0]))
		{
			// spawn new chunk
			// random numbers for brevity
			int f_w, f_l;
			f_w = (int) (ThreadLocalRandom.current().nextInt(
					BG_CHUNK_SPAWN_OFFSET[0], BG_CHUNK_SPAWN_OFFSET[1]) * factors[0]) ;
			f_l = (int) (ThreadLocalRandom.current().nextInt(
					BG_CHUNK_SPAWN_HEIGHT[0], BG_CHUNK_SPAWN_HEIGHT[1]) * factors[1]);
			
			Geometry.Point c1, c2;
			c1 = new Geometry.Point(bg_last_floor_chunk.b1.x + f_w, bg_floor - f_l);
			c2 = new Geometry.Point(bg_last_floor_chunk.b2.x + f_w, bg_floor); 
			
			CaveObstacle.Chunk floor_chunk = new CaveObstacle.Chunk(
					bg_last_floor_chunk.b1, // create chunk from last chunk
					bg_last_floor_chunk.b2,
					c1,
					c2,
					Map.BG_COLOR_L);
			
			bg_last_floor_chunk = floor_chunk;	// set the new last chunk to the right one
			bg_map_floor.add(floor_chunk);		// add new chunk to the arraylist
		} // end if time to spawn chunk

		// --------------------------------------
		// -------- SPAWN CEILING CHUNKS --------
		// --------------------------------------
		if (this.bg_last_ceiling_chunk.b1.x <= this.a_width + (100 * factors[0]))
		{
			// spawn new chunk
			// random numbers for brevity
			int c_w, c_l;
			c_w = (int) (ThreadLocalRandom.current().nextInt(
					BG_CHUNK_SPAWN_OFFSET[0], BG_CHUNK_SPAWN_OFFSET[1]) * factors[0]);
			c_l = (int) (ThreadLocalRandom.current().nextInt(
					BG_CHUNK_SPAWN_HEIGHT[0], BG_CHUNK_SPAWN_HEIGHT[1]) * factors[1]);
			
			CaveObstacle.Chunk ceiling_chunk = new CaveObstacle.Chunk(
					this.bg_last_ceiling_chunk,	// create chunk from last chunk
					c_l,						// ceiling left side height
					c_w);						// ceiling width
			this.bg_map_ceiling.add(ceiling_chunk);		// add new chunk to the arraylist
			this.bg_last_ceiling_chunk = ceiling_chunk;	// set the new last chunk to the right one
		} // end if time to spawn chunk
		
		// KILL THE HANG GLIDER IF HE HITS ANYTHING
		
		if (hg.color == Color.RED)
		{
			hg.kill();
		} // end game if collision
	} // end tick
	
	/**
	 * Draw the map's objects (obstacles and coins) onto the given graphics object.
	 * 
	 * @param g		Graphics object to draw to applet
	 */
	public void draw(Graphics g)
	{	
		// ############# BACKGROUND ############################################

		// --------------------------------------
		// ------------ DRAW SPIKES -------------
		// --------------------------------------
		if (this.bg_obstacles.size() > 0)
		{
			// iterate through all of the obstacles
			Iterator<CaveObstacle> obst_iter = bg_obstacles.iterator();
			while(obst_iter.hasNext())
			{
				// get obstacle
				CaveObstacle co = obst_iter.next();
				// use built-in draw method
				co.draw(g);
			} // end while draw obstacles
		} // end if any obstacles exist


		// --------------------------------------
		// --------- DRAW FLOOR CHUNKS ----------
		// --------------------------------------
		if (this.bg_map_floor.size() > 0)
		{
			// iterate through all of the obstacles
			Iterator<CaveObstacle.Chunk> chunk_iter = this.bg_map_floor.iterator();
			while(chunk_iter.hasNext())
			{
				// get obstacle
				CaveObstacle.Chunk chunk = chunk_iter.next();
				// use built-in draw method
				chunk.draw(g);
			} // end while draw obstacles
		} // end if any obstacles exist


		// --------------------------------------
		// -------- DRAW CEILING CHUNKS ---------
		// --------------------------------------
		if (this.bg_map_ceiling.size() > 0)
		{
			// iterate through all of the obstacles
			Iterator<CaveObstacle.Chunk> chunk_iter = this.bg_map_ceiling.iterator();
			while(chunk_iter.hasNext())
			{
				// get obstacle
				CaveObstacle.Chunk chunk = chunk_iter.next();
				// use built-in draw method
				chunk.draw(g);
			} // end while draw obstacles
		} // end if any obstacles exist
		

		// --------------------------------------
		// ------------ DRAW BOXES --------------
		// --------------------------------------
		g.setColor(Map.BG_COLOR_L);
		g.fillRect(0, 0, this.a_width, bg_ceiling);
        g.setColor(Map.BG_COLOR_L);
		g.fillRect(0, this.bg_floor, this.a_width, 1000);
				
				
				
		// --------------------------------------
		// ------------ DRAW SPIKES -------------
		// --------------------------------------
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


		// --------------------------------------
		// --------- DRAW FLOOR CHUNKS ----------
		// --------------------------------------
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


		// --------------------------------------
		// -------- DRAW CEILING CHUNKS ---------
		// --------------------------------------
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
		

		// --------------------------------------
		// ------------ DRAW BOXES --------------
		// --------------------------------------
		g.setColor(Map.FG_COLOR_L);
		g.fillRect(0, 0, this.a_width, ceiling);
        g.setColor(Map.FG_COLOR_D);
		g.fillRect(0, this.floor, this.a_width, 1000);


		// --------------------------------------
		// ------------ DRAW HUD ----------------
		// --------------------------------------
		this.dist_font = new Font("Dialog", Font.PLAIN, (int) (20*Math.sqrt(this.factors[1])));
		
		// distance
		
		// convert distance to string
		double d_dist = this.dist_travelled / 10 / 1000 * 0.62137119;
		String s_dist = fmt.format(d_dist);
		
		g.setFont(this.dist_font);
		g.setColor(Color.BLACK);
		g.drawString("Distance: " + s_dist + " mi",
				(int) (21*this.factors[0]),
				this.floor + (int) (21*this.factors[1]));
		g.setColor(Color.WHITE);
		g.drawString("Distance: " + s_dist + " mi",
				(int) (20*this.factors[0]),
				this.floor + (int) (20*this.factors[1]));
		
		// speed
		
		// convert speed to string
		double d_speed = this.scroll_speed * 1000.0 / 60.0 / 10.0 * 2.23693629;
		String s_speed = fmt.format(d_speed);
		
		g.setColor(Color.BLACK);
		g.drawString("Speed: " + s_speed + " mi/h",
				(int) (21*this.factors[0]),
				this.floor + (int) (41*this.factors[1]));
		g.setColor(Color.WHITE);
		g.drawString("Speed: " + s_speed + " mi/h",
				(int) (20*this.factors[0]),
				this.floor + (int) (40*this.factors[1]));
	} // end draw
} // end class
