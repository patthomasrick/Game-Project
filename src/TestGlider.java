import java.awt.Color;

/**
 * A class that performs as a hang glider. This version has no special movement
 * actions, and behaves the same as a sprite.
 * @author Patrick Thomas
 */
public class TestGlider extends Sprite
{
	double r = 0;//radius
	double vx = 0;//x-axis velocity
	double vy = 0;//y-axis velocity
	double a = 0; //angle
	double v = 0; //velocity
	public TestGlider(double x, double y, int h, int w, Color color)
	{
		// initialize Sprite
		super(x, y, h, w, color);
		r = ((18 * Math.sqrt(2 * this.y)) / Math.PI);
		a = Math.atan(vy / vx);
		v = Math.sqrt(this.y / 5);
		vy = v * Math.sin(a);
		vx = v * Math.cos(a);
	}
	public double tick(int mouse_x, int mouse_y)
	{
		double dx = mouse_x - this.x;
		double dy = mouse_y - this.y;
		this.a = Math.atan2(dy, dx);
		this.v = Math.sqrt(y / 5);
		vy = this.v * Math.sin(this.a);
		vx = this.v * Math.cos(this.a);
		this.y += this.vy;
		return vx;
	}
}
