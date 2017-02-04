/**
Authors:	Patrick Thomas
Date:		2/4/17
Purpose: 	Provides a simple rectangle system.
*/

public class Rect
/*
 * A class for rectangles. Provides mathematical tools to transform them and such.
 * 
 * Position is stored in doubles to allow for fine movements.
 */
{
	// variables for position and size
	double x, y;
	int w, h;
	
	public Rect(double x, double y, int h, int w)
	// default constructor for rect
	{
		this.x = x;
		this.y = y;
		this.h = h;
		this.w = w;
	} // end constructor
	
	public void move(double x, double y)
	/*
	 * Move the rectangle to the point given. Basically a teleport to position.
	 */
	{
		this.x = x;
		this.y = y;
	} // end move
	
	public void push(double x, double y)
	/*
	 * Like move, but works by adding the values to the current position. Good
	 * for smaller movements that are often or periodical in nature.
	 */
	{
		this.x += x;
		this.y += y;
	} // end nudge
	
	public int[][] get_corners()
	/*
	 * Returns the points as ints. For drawing or operations that do not support
	 * doubles.
	 * 
	 * Tries to return the top-left point and the bottom-right point.
	 */
	{
		int[][] corners = 
			{
				{
					(int) this.x, 
					(int) this.y
				},
				{
					(int) (this.x + this.w), 
					(int) (this.y + this.h)
				}
			}; // end define corners
		
		return corners;
	} // end get_corners
	
	public boolean collide_rect(Rect r)
	/*
	 * Tests to see if this rect and another rect, r, collide.
	 * 
	 * Has to test if any one of the second rect's corners (all 4) are inside of the
	 * current rectangle.
	 */
	{
		boolean is_colliding = false;
		
		// unpack corners
		int[][] r1_corners = this.get_corners();
		int[][] r2_corners = r.get_corners();
		
		int[] r1_c1 = r1_corners[0];
		int[] r1_c2 = r1_corners[1];
		int[] r2_c1 = r2_corners[0];
		int[] r2_c2 = r2_corners[1];
		
		// test x-values
		// if r2's left side is within r1 or right is within
		if (((r1_c1[0] < r2_c1[0]) && (r2_c1[0] < r1_c2[0])) ||
			((r1_c1[0] < r2_c2[0]) && (r2_c2[0] < r1_c2[0])))
		{
			// test y-values
			// if r2's top side is within r1 or bottom is within
			if (((r1_c1[1] < r2_c1[1]) && (r2_c1[1] < r1_c2[1])) ||
				((r1_c1[1] < r2_c2[1]) && (r2_c2[1] < r1_c2[1])))
			{
				is_colliding = true;
			} // end if y is overlapping
		} // end if x is overlapping
		
		return is_colliding;
	} // end collide_rect
	
} // end rect
