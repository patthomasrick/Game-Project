import java.awt.Color;

/**
 * A class that performs as a hang glider. This version has no special movement
 * actions, and behaves the same as a sprite.
 * @author Patrick Thomas
 */
public class TestGlider extends Sprite
{
	// information about turning
	double angle = 0;
	final double max_ang_change = 10.0 / (1000.0/60.0); // in degrees per frame, is 10 deg/sec
	
	// energy variables
	final double max_pot_eng = 3.5;
	final double max_eng = 5;
	
	/** 
	 * Default constructor for a test glider.
	 * @param x			double, x pos on screen
	 * @param y			double, y pos on screen
	 * @param h			int, height of bounding rectangle
	 * @param w			int, width of bounding rectangle
	 * @param color		color of rectangle when drawn
	 */
	public TestGlider(double x, double y, int h, int w, Color color)
	{
		// initialize Sprite
		super(x, y, h, w, color);
	} // end constructor
	
	public double chase(int x, int y)
	{
		// get degrees that the mouse is at
		double current_angle = Math.atan2(x - this.x, y - this.y);
		current_angle = Math.toDegrees(current_angle);
		
		if (Math.abs(this.max_ang_change) < Math.abs(current_angle))
		{
			if (this.angle > current_angle)
			{
				this.angle -= this.max_ang_change;
			} // if going down
			else if (this.angle < current_angle)
			{
				this.angle += this.max_ang_change;
			} // if going up
		} // end if supposed to change
		
		return 1.5;
	} // end chase
} // end TestGlider
