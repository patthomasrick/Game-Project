/**
 * This provides the tools to have animations. Designed to closely work with Sprites.
 * 
 * @author Patrick Thomas
 * @version 2/5/17
 */

/**
 * A container for animation frames and their durations.
 * 
 * @author Patrick Thomas
 */

import java.awt.Graphics;
import java.awt.Image;
import java.applet.*;
import java.util.ArrayList;

public class Animation
{	
	ArrayList<Frame> frames = new ArrayList<Frame>();
	ArrayList<Integer> durations = new ArrayList<Integer>();
	
	int current_frame;
	int current_time;
	
	Image current_image;
	
	/**
	 * Constructor for Animation
	 */
	public Animation()
	{
		current_frame = 0;
	} // end constructor
	
	/**
	 * Add a frame to the animation.
	 * @param f			Frame
	 * @param duration	How long frame runs (milliseconds)
	 */
	public void add_frame(Frame f)
	{
		frames.add(f);
		durations.add(f.duration);
	} // end add frame
	
	/**
	 * Progresses the animation based on the time that has passed.
	 * @param milliseconds		int, milliseconds elapsed since animation was last ticked
	 */
	public void tick(int milliseconds)
	{
		// increment the current time
		this.current_time += milliseconds;
		
		// loop, animate while there is still time to pass
		while (durations.get(this.current_frame) < this.current_time)
		{
			// decrement time for every frame that has passed
			this.current_time -= this.durations.get(this.current_frame);
			// this only happens when a frame has passed, so increment frame
			this.current_frame += 1;
			// set the current image to the corresponding frame
			this.current_image = this.frames.get(this.current_frame).image;
		} // end while ticking time
	} // end tick()
	
	/**
	 * One frame of an animation
	 * @author Patrick Thomas
	 */
	public static class Frame
	{
		Image image;
		int duration;
		
		/**
		 * Constructor for a frame of an animation. Takes a path to an image and a duration as
		 * input. Duration is in milliseconds.
		 */
		public Frame(Image i, int duration)
		{
			this.image = i;
			this.duration = duration;
		} // end frame constructor
		
		public void draw(Graphics g, int x, int y, Applet a)
		{
			g.drawImage(this.image, x, y, a);
		}
	} // end frame
} // end 
