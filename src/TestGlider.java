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
	//double mouse_y = 0;
	//double mouse_x = 0;
//	double mouse_acceleration= 2.0;
//	double mouse_speed = 0;
//	double simmouse_y = 0; //simulated mouse y
//	double simmouse_maxa = 0; //maximum acceleration for simulated mouse
	Simulated_mouse sm;
	public TestGlider(double x, double y, int h, int w, Color color)
	{
		// initialize Sprite
		super(x, y, h, w, color);
		r = ((18 * Math.sqrt(2 * this.y)) / Math.PI);
		a = Math.atan(vy / vx);
		v = Math.sqrt(this.y / 5);
		vy = v * Math.sin(a);
		vx = v * Math.cos(a);
		sm = new Simulated_mouse(this);
	}
	public double tick(int mouse_y)
	{
		sm.chase_mouse(mouse_y);
		double dx = 100;
		double dy = sm.simmouse_y - this.y;
		this.a = Math.atan2(dy, dx);
		this.v = Math.sqrt(y / 5);
		vy = this.v * Math.sin(this.a);
		vx = this.v * Math.cos(this.a);
		this.y += this.vy;
		return vx;
	}
	/*
	 * if the user wants to move down:
	 * 		move the point down
	 * 			if the point is below the mouse:
	 * 				stop moving
	 * else if the user wants to move up:
	 * 		move the point up
	 * 			if the point is above the mouse;
	 * 				stop moving
	 */
	public class Simulated_mouse
	{
		double simmouse_x = 0;//simulated mouse x
		double simmouse_y = 0;//simulated mouse y
		double simmouse_ay = 0;//simulated mouse y acceleration
		double simmouse_vy = 0;//simulated mouse y velocity
		double simmouse_maxa = 0;//simulated mouse max acceleration
		
		public Simulated_mouse(TestGlider hg)
		{
			simmouse_x = hg.x + 100;
			simmouse_y = hg.y;
		}
		
		public double chase_mouse(int mouse_y)
		{
			if (simmouse_y < mouse_y)
			{
				simmouse_y = simmouse_y + 5;
				
				if (simmouse_y > mouse_y)
				{
					simmouse_vy = 0;
				}
			}
			else if (simmouse_y > mouse_y)
			{
				simmouse_y = simmouse_y - 5;
				
				if (simmouse_y < mouse_y)
				{
					simmouse_vy = 0;
				}
			}
			if (mouse_y <= .01)
			{
				simmouse_y = 0;
			}
			return(simmouse_y);
		}
		
		
	}
}
