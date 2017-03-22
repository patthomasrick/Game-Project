/**
 * This provides the tools for advanced collisions (triangles and such). As well as rectangles
 * for sprites and other objects.
 * 
 * @author Patrick Thomas
 * @version 2/3/17
 */

/**
 * Really just a container for the objects contained in this class.
 * Provides 2D points, vectors, triangles, and rectangles.
 * 
 * Classes in here are meant more for calculations and less for animation or motion.
 * 
 * @author Patrick Thomas
 */
public class Geometry
{
	/**
	 * A class to contain two values, x and y, contained in a 2D space.
	 * 
	 * @author Patrick Thomas
	 */
	public static class Point
	{
		/** Coordinates of the point */
		double x, y;
		
		/**
		 * Constructor for Point. 
		 * 
		 * @param x		Position of point on x-axis
		 * @param y		Position of point on y-axis
		 */
		public Point(double x, double y)
		{
			this.x = x;
			this.y = y;
		} // end constructor
		
		public Point(Point p)
		{
			this.x = p.x;
			this.y = p.y;
		} // end duplicator
		
		public static Point add(Point p1, Point p2)
		{
			return new Point(p1.x + p2.x, p1.y + p2.y);
		} // end add method
	} // end class point
	
	/**
	 * A class to contain two points or a point and a magnitude.
	 * 
	 * @author Patrick Thomas
	 */
	public static class Vector
	{
		// values
		/** a and b are for the two-point defintion, v is to store an actual vector.*/
		Point a, b, v;
		
		/** Magnitude as a scalar (double) */
		double m;
		
		/**
		 * Constructor for vector. Uses two Points to create vector.
		 * 
		 * @param a		Point, tail of vector
		 * @param b		Point, head of vector
		 */
		public Vector(Point a, Point b)
		{
			this.a = a;
			this.b = b;
			
			// calculate actual direction vector
			this.v = new Point(b.x - a.x, b.y - a.y);
			
			// calculate magnitude
			this.m = Math.sqrt((this.v.x*this.v.x) + (this.v.y*this.v.y));
		} // end constructor
		
		/**
		 * Cross product of a 2D vector.
		 * Essentially returns only the magnitude, but in this 2d space, not more is needed.
		 * 
		 * @param a		Vector
		 * @param b		Vector
		 * @return		Cross product defined as (ax*by-bx*ay)
		 */
		public static double cross_product(Vector a, Vector b)
		{
			return a.v.x * b.v.y - a.v.y * b.v.x;
		} // end cross product
		
		public static Vector add(Vector a, Vector b)
		{
			return new Vector(Point.add(a.a, b.a), Point.add(b.a, b.b));
		} // end add vectors
	} // end class point
	
	/**
	 * Class to contain three points of a triangle. Provides manipulations for
	 * triangle, such as collision detection.
	 * 
	 * @author Patrick Thomas
	 */
	public static class Triangle
	{
		// three points of triangle
		Point a, b, c;
		
		/**
		 * Constructor of triangle. Needs 3 points.
		 * @param a		Point
		 * @param b		Point
		 * @param c		Point
		 */
		public Triangle(Point a, Point b, Point c)
		{
			this.a = a;
			this.b = b;
			this.c = c;
		} // end constructor
		
		/**
		 * Constructor of triangle. Needs 2 vectors, with the tails being equal.
		 * @param a		Vector
		 * @param b		Vector
		 * @throws InvalidVectors 
		 */
		public Triangle(Vector a, Vector b) throws InvalidVectors
		{
			if (a.a != b.a)
			{
				throw new Geometry.Triangle.InvalidVectors("Vector tails not equal");
			} // end if tails not equal
			else
			{
				this.a = a.a;
				this.b = a.b;
				this.c = b.b;
			} // end else
		} // end constructor
		
		/**
		 * Constructor for triangle. Creates triangle from points broken into components.
		 * @param a1	double of x-value
		 * @param a2	double of y-value
		 * @param b1	double of x-value
		 * @param b2	double of y-value
		 * @param c1	double of x-value
		 * @param c2	double of y-value
		 */
		public Triangle(double a1, double a2, double b1, double b2, double c1, double c2)
		// constructor if given points as doubles/ints
		{
			this.a = new Point(a1, a2);
			this.b = new Point(b1, b2);
			this.c = new Point(c1, c2);
		} // end constructor
		
		/**
		 * Tests to see if a point is inside of this triangle.
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
		 * 
		 * @param p		Point
		 * @return		boolean true if colliding, false if not
		 */
		public boolean contains_point(Point p)
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
		

		/**
		 * Collides the triangle with a rectangle. Uses the collide point method with every single
		 * point of a rectangle.
		 * @param r		Geometry.Rect, used for its 4 corners
		 * @return		boolean, true if colliding, false if not
		 */
		public boolean collide_rect(Geometry.Rect r)
		{			
			// get points of rect
			Geometry.Point r1 = new Geometry.Point(r.x, r.y);
			Geometry.Point r2 = new Geometry.Point(r.x + r.w, r.y);
			Geometry.Point r3 = new Geometry.Point(r.x, r.y + r.h);
			Geometry.Point r4 = new Geometry.Point(r.x + r.w, r.y + r.h);
			
			// collide points with triangle
			boolean iscolliding = (
					(this.contains_point(r1)) ||
					(this.contains_point(r2)) ||
					(this.contains_point(r3)) ||
					(this.contains_point(r4))
					);
			
			return iscolliding;
		} // end collide as triangle
		
		public static class InvalidVectors extends Exception
		{
			  /**
			 * 
			 */
			private static final long serialVersionUID = 2510996784592698720L;

			public InvalidVectors(String message)
			  {
			     super(message);
			  } // end constructor
		} // end custom exception
	} // end class triangle
	
	/**
	 * A class for rectangles. Provides mathematical tools to transform them and such.
	 * Position is stored in doubles to allow for fine movements.
	 * 
	 * @author Patrick Thomas
	 */
	public static class Rect
	{
		// variables for position and size
		double x, y;
		int w, h;
		
		/**
		 * Constructor. Takes position and size.
		 * @param x		double, x position
		 * @param y		double, y position
		 * @param h		int, height
		 * @param w		int, width
		 */
		public Rect(double x, double y, int h, int w)
		{
			this.x = x;
			this.y = y;
			this.h = h;
			this.w = w;
		} // end constructor

		/**
		 * Constructor. Takes two points
		 * @param a		Top-left corner of rectangle
		 * @param b		Bottom-right corner of rectangle
		 */
		public Rect(Point a, Point b)
		{
			this.x = a.x;
			this.y = a.y;
			this.w = (int) (b.x - a.x);
			this.h = (int) (b.y - a.y);
		} // end constructor
		
		/**
		 * Move the rectangle to the components of the point given. Basically a teleport 
		 * to the position.
		 * @param x		double x component
		 * @param y		double y component
		 */
		public void move(double x, double y)
		{
			this.x = x;
			this.y = y;
		} // end move

		/**
		 * Move the rectangle to the point given.
		 * @param p		Point
		 */
		public void move(Point p)
		{
			this.x = p.x;
			this.y = p.y;
		} // end move
		
		/**
		 * Moves rectangle by a delta. Adds values to current position to move.
		 * Essentially a move that is local, or relative to this's current position.
		 * @param x		double
		 * @param y		double
		 */
		public void push(double x, double y)
		{
			this.x += x;
			this.y += y;
		} // end nudge

		/**
		 * Moves rectangle by a vector. Adds values to current position to move.
		 * Essentially a move that is local, or relative to this's current position.
		 * @param v		Vector
		 */
		public void push(Vector v)
		{
			this.x += v.v.x;
			this.y += v.v.y;
		} // end nudge
		
		/**
		 * Returns the corners of the rect as a 2-deep array of ints.
		 * Gives top-left and bottom-right points.
		 * @return		2-deep array of ints (like {{x1, y1}, {x2, y2}})
		 */
		public int[][] get_corners()
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
		
		/**
		 * Tests to see if a point is inside of this rectangle.
		 * @param x		double, x position of point
		 * @param y		double, y position of point
		 * @return		boolean, true if colliding, false if not
		 */
		public boolean collide_point(double x, double y)
		{
			// test x-values
			if ((this.x < x) && (x < this.x + this.w))
			{
				// test y-values
				if ((this.y < y) && (y < this.y + this.h))
					return true;
				else
					return false;
			} // end if x is overlapping
			else return false;
		}

		/**
		 * Tests to see if a point is inside of this rectangle. Uses an actual point.
		 * @param p		Point
		 * @return		boolean, true if colliding, false if not
		 */
		public boolean collide_point(Point p)
		{
			// test x-values
			if ((this.x < p.x) && (p.x < this.x + this.w))
			{
				// test y-values
				if ((this.y < p.y) && (p.y < this.y + this.h))
					return true;
				else
					return false;
			} // end if x is overlapping
			else return false;
		}
		
		/**
		 * Tests to see if two rects are overlapping. Uses this rect and another rect.
		 * @param r		Rectangle
		 * @return		boolean, true if colliding, false if not
		 */
		public boolean collide_rect(Rect r)
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

		/**
		 * Tests to see if two rects are overlapping. Uses two rects.
		 * @param r		Rectangle
		 * @return		boolean, true if colliding, false if not
		 */
		public static boolean collide_rect(Rect r1, Rect r2)
		{
			// unpack corners
			int[][] r1_corners = r1.get_corners();
			int[] r1_c1 = r1_corners[0];
			int[] r1_c2 = r1_corners[1];
			
			// use built in methods to test every point of the opposing rectangle
			if (r2.collide_point(r1_c1[0], r1_c1[1]) || 
				r2.collide_point(r1_c1[0], r1_c2[1]) || 
				r2.collide_point(r1_c2[0], r1_c1[1]) ||
				r2.collide_point(r1_c2[0], r1_c2[1]))
				return true;
			else 
				return false;
		} // end collide_rect
	} // end rect
} // end clas geometry
