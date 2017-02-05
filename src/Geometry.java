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
			return a.v.x * b.v.y + a.v.y * b.v.x;
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
			// get vectors as detailed
			Vector ab, bc, ca, ap, bp, cp;
			
			ab = new Vector(this.a, this.b);
			bc = new Vector(this.b, this.c);
			ca = new Vector(this.c, this.a);

			ap = new Vector(this.a, p);
			bp = new Vector(this.b, p);
			cp = new Vector(this.c, p);
			
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
} // end clas geometry
