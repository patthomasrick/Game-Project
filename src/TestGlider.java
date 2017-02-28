import java.awt.Color;

/**
 * A class that performs as a hang glider.
 * @author Christopher Martin
 */
public class TestGlider extends Sprite
{
	// double r = 0; 	// radius
	double vx = 0; 	// x-axis velocity
	double vy = 0; 	// y-axis velocity
	double a = 0; 	// angle
	double v = 0; 	// velocity
	
	
	public TestGlider(double x, double y, int h, int w, Color color)
	{
		// initialize Sprite
		super(x, y, h, w, color);
		
		// initialize all variables to current state
		// r = ((18 * Math.sqrt(2 * this.y)) / Math.PI);
		a = Math.atan(vy / vx);
		v = Math.sqrt(this.y / 5);
		vy = v * Math.sin(a);
		vx = v * Math.cos(a);
	} // end constructor
	
	
	public double tick(int mouse_x, int mouse_y)
	{
		// find the difference between the mouse and the position of the glider
		double dx = mouse_x - this.x;
		double dy = mouse_y - this.y;
		
		// angle is from the mouse position to the hang glider's corner
		this.a = Math.atan2(dy, dx);
		
		// velocity is based on the current height of the hang glider
		this.v = Math.sqrt(y / 5);
		
		// break velocity into components with trig
		vy = this.v * Math.sin(this.a);
		vx = this.v * Math.cos(this.a);
		
		// increment the glider's speed
		this.y += this.vy;
		
		// return the speed that the screen should scroll at
		return vx;
	} // end tick
} // end testglider
