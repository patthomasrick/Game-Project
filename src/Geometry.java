/**
Authors:	Patrick Thomas
Date:		2/4/17
Purpose: 	This provides the tools for advanced collisions (triangles and such).
*/


public class Geometry
/*
 * Classes in here are meant more for calculations and less for animation or motion.
 */
{
	public static class Point
	/*
	 * A class to contain two values, x and y, contained in a 2D space.
	 */
	{
		// values
		double x, y;
		
		public Point(double d, double e)
		// constructor
		{
			this.x = d;
			this.y = e;
		} // end constructor
	} // end class point
	

	public static class Vector
	/*
	 * A class to contain two points or a point and a magnitute
	 */
	{
		// values
		// two points definition
		// v is stored as a Point but is the x and y components of vector
		Point a, b, v;
		
		// magnitude
		double m;
		
		public Vector(Point a, Point b)
		// constructor, two points definition
		// a is tail, b is head
		{
			this.a = a;
			this.b = b;
			
			this.v = new Point(b.x - a.x, b.y - a.y);
			
			// calculate magnitude
			this.m = Math.sqrt((this.v.x*this.v.x) + (this.v.y*this.v.y));
		} // end constructor
		
		public static double cross_product(Vector a, Vector b)
		/*
		 * Gets the cross product of 2d vectors.
		 * 
		 * Essentially returns only the magnitude, but in this 2d space, not more is needed.
		 */
		{
			return a.v.x * b.v.y - a.v.y * b.v.x;
		} // end cross product
	} // end class point
	
	
	public static class Triangle
	{
		// three points of triangle
		Point a, b, c;
		
		public Triangle(Point a, Point b, Point c)
		// constructor if given points
		{
			this.a = a;
			this.b = b;
			this.c = c;
		} // end constructor
		
		public Triangle(double a1, double a2, double b1, double b2, double c1, double c2)
		// constructor if given points as doubles/ints
		{
			this.a = new Point(a1, a2);
			this.b = new Point(b1, b2);
			this.c = new Point(c1, c2);
		} // end constructor
		
		public boolean contains_point(Point p)
		/*
		 * A method to test if a point is inside of the triangle.
		 * 
		 * Method inspired from http://math.stackexchange.com/questions/51326/
		 * determining-if-an-arbitrary-point-lies-inside-a-triangle-defined-by-three-points
		 * 
		 * "This is a fairly well known algorithm. It all comes down to using the cross product. 
		 * Define the vectors AB, BC and CA and the vectors AP, BP and CP. Then P 
		 * is inside the triangle formed by A, B and C if and only if all of the cross 
		 * products AB×AP, BC×BP, and CA×CP point in the same direction relative 
		 * to the plane. That is, either all of them point out of the plane, or all of them 
		 * point into the plane."
		 */
		{
			// set point a on the triangle as the origin
			Point a_r = new Vector(a, a).v;
			Point b_r = new Vector(a, b).v;
			Point c_r = new Vector(a, c).v;
			Point p_r = new Vector(a, p).v;
					
			// get vectors as detailed
			Vector ab, bc, ca, ap, bp, cp;
			
			ab = new Vector(a_r, b_r);
			bc = new Vector(b_r, c_r);
			ca = new Vector(c_r, a_r);

			ap = new Vector(a_r, p_r);
			bp = new Vector(b_r, p_r);
			cp = new Vector(c_r, p_r);
			
			// get the cross products
			double cross1 = Vector.cross_product(ab, ap);
			double cross2 = Vector.cross_product(bc, bp);
			double cross3 = Vector.cross_product(ca, cp);
			
			if ((cross1 > 0) && (cross2 > 0) && (cross3 > 0))
				// all vectors point in the same direction condition 1
				return true;
			else if ((cross1 < 0) && (cross2 < 0) && (cross3 < 0))
				// all vectors point in the same direction condition 2
				return true;
			else
				// at least one vector is not in the same direction
				return false;
		}
	} // end class triangle
	
	public static class Rect
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
} // end clas geometry
