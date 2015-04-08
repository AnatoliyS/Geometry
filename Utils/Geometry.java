package Utils;

import Utils.Exceptions.*;

public class Geometry {
  /**
   * Checks if value is = 0 with allowable error which is set
   * in Constants
   * @param double value
   * @return boolean True if value is = 0 with EPS error
   */
  public static boolean equal_zero(double value) {
    return Math.abs(value) <= Constants.EPS;
  }

  /**
   * Cross product of vectors
   * http://en.wikipedia.org/wiki/Cross_product
   * @param Vector
   * @param Vector
   * @return double scalar product
   */
  public static double cross_product(Vector a, Vector b) {
    return a.getX()*b.getY() - a.getY()*b.getX();
  }
 
  /**
   * Scalar product of vectors
   * @param Vector
   * @param Vector
   * @return double scalar product
   */ 
  public static double scalar_product(Vector a, Vector b) {
    return a.getX()*b.getX() + a.getY()*b.getY();
  }

  /**
   * Get Point of intersection of two Lines.
   * @param Line
   * @param Line
   * @thrwos NoIntersectionException if lines do not cross
   * @return Point Point of intersection
   */
  public static Point intersect(Line a, Line b) throws NoIntersectionException {
    double dir_product = cross_product(b.direction, a.direction);
    if (equal_zero(dir_product)) {
      throw new NoIntersectionException();
    } else {
      double product = cross_product(a.direction, new Vector(a.first, b.first));
      double t = product / dir_product;
      double x = b.first.getX() + t * b.direction.getX();
      double y = b.first.getY() + t * b.direction.getY();
      return new Point(x, y);
    }
  }
  
  /**
   * Get Point of intersection of Line and Segment.
   * @param Line
   * @param Segment
   * @thrwos NoIntersectionException if they do not cross
   * @return Point Point of intersection
   */
  public static Point intersect(Line a, Segment b) throws NoIntersectionException {
    double dir_product = cross_product(b.direction, a.direction);
    if (equal_zero(dir_product)) {
      throw new NoIntersectionException();
    } else {
      double product = cross_product(a.direction, new Vector(a.first, b.first));
      double t = product / dir_product;

      // Check that point lies on segment
      if (t < 0 || t > 1) {
        throw new NoIntersectionException();
      } else {
        double x = b.first.getX() + t * b.direction.getX();
        double y = b.first.getY() + t * b.direction.getY();
        return new Point(x, y);
      }
    }
  }

  /**
   * Get left perpendicular vector of length 1 to given vector.
   * By left we mean that shortest rotataion from Vector d to
   * given vector will be to the left (cross_product of them is greater 0)
   * @param Vector
   * @return Vector Left perpendicular normalized vector
   */
  public static Vector getLeftNormalizedPerpendicular(Vector d) {
    if (equal_zero(d.getY())) {
      return new Vector(0.0, 1.0);
    } else {
      double dx_squared = d.getX() * d.getX();
      double dy_squared = d.getY() * d.getY();
      // To get right perpendicular one needs to write - sign before sqrt
      double x = Math.sqrt(1.0 / (1.0 + dx_squared / dy_squared));
      double y = - x * d.getX() / d.getY();
      return new Vector(x, y);
    }
  }

}
