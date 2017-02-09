import java.awt.Color;

/**
 * A class that performs as a hang glider. THis version has no special movement
 * actions, and behaves the same as a sprite.
 * @author Patrick Thomas
 */
public class TestGlider extends Sprite
{
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
} // end TestGlider
