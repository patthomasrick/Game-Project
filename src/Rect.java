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
	
	public boolean collide_point(double x, double y)
	/*
	 * 2/4/17
	 * 
	 * Tests a point inside the rect.
	 * @returns boolean
	 */
	{
		// test x-values
		if ((this.x < x) && (x < this.x + this.w))
		{
			// test y-valuess
			if ((this.y < y) && (y < this.y + this.h))
				return true;
			else
				return false;
		} // end if x is overlapping
		else return false;
	}
	
	public boolean collide_rect(Rect r)
	/*
	 * Tests to see if this rect and another rect, r, collide.
	 * 
	 * Has to test if any one of the second rect's corners (all 4) are inside of the
	 * current rectangle.
	 */
	{
		// unpack corners
		int[][] r_corners = this.get_corners();
		
		int[] r_c1 = r_corners[0];
		int[] r_c2 = r_corners[1];
		
		// use built in methods to test every point of the opposing rectangle
		if (this.collide_point(r_c1[0], r_c1[1]) || 
			this.collide_point(r_c1[0], r_c2[1]) || 
			this.collide_point(r_c2[0], r_c1[1]) ||
			this.collide_point(r_c2[0], r_c2[1]))
			return true;
		else 
			return false;
	} // end collide_rect
	
} // end rect
