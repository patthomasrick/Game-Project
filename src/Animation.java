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

import java.util.ArrayList;

public class Animation
{
	ArrayList<Frame> frames = new ArrayList<Frame>();
	ArrayList<Integer> durations = new ArrayList<Integer>();
	
	int current_frame;
	
	int current_time;
	
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
	public void add_frame(Frame f, int duration)
	{
		frames.add(f);
		durations.add(duration);
	} // end add frame
	
	/**
	 * One frame of an animation
	 * @author Patrick Thomas
	 */
	public static class Frame
	{
		
	}
} // end 
