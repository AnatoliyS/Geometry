package Utils;
import java.util.ArrayList;
import Utils.Exceptions.*;

public class Geometry {
  /**
   * Checks if value is = 0 with allowable error which is set
   * in Constants
   * @param double value
   * @return boolean True if value is = 0 with EPS error
   */
  public static boolean equalZero(double value) {
    return Math.abs(value) <= Constants.EPS;
  }

  /**
   * Cross product of vectors
   * http://en.wikipedia.org/wiki/Cross_product
   * @param Vector
   * @param Vector
   * @return double cross product
   */
  public static double crossProduct(Vector a, Vector b) {
    return a.getX()*b.getY() - a.getY()*b.getX();
  }
 
  /**
   * Scalar product of vectors
   * @param Vector
   * @param Vector
   * @return double scalar product
   */ 
  public static double scalarProduct(Vector a, Vector b) {
    return a.getX()*b.getX() + a.getY()*b.getY();
  }

  /**
   * Get Point of intersection of two Lines.
   * @param Line
   * @param Line
   * @throws NoIntersectionException if lines do not cross
   * @return Point Point of intersection
   */
  public static Point intersect(Line a, Line b) throws NoIntersectionException {
    double dir_product = crossProduct(b.direction, a.direction);
    // If lines are parallel
    if (equalZero(dir_product)) {
      throw new NoIntersectionException();
    } else {
      double product = crossProduct(a.direction, new Vector(a.first, b.first));
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
   * @throws NoIntersectionException if they do not cross
   * @return Point Point of intersection
   */
  public static Point intersect(Line a, Segment b) throws NoIntersectionException {
    double dir_product = crossProduct(b.direction, a.direction);
    // If line and segment are parallel
    if (equalZero(dir_product)) {
      throw new NoIntersectionException();
    } else {
      double product = crossProduct(a.direction, new Vector(a.first, b.first));
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
   * Get Point of intersection of Ray and Segment.
   * @param Ray
   * @param Segment
   * @throws NoIntersectionException if they do not cross
   * @return Point Point of intersection
   */
  public static Point intersect(Ray a, Segment b) throws NoIntersectionException {
    double dir_product = crossProduct(b.direction, a.direction);
    // If line and segment are parallel
    if (equalZero(dir_product)) {
      throw new NoIntersectionException();
    } else {
      double product = crossProduct(a.direction, new Vector(a.start, b.first));
      double t = product / dir_product;

      // Check that point lies on segment
      if (t < 0 || t > 1) {
        throw new NoIntersectionException();
      } else {
        double x = b.first.getX() + t * b.direction.getX();
        double y = b.first.getY() + t * b.direction.getY();
        Point p = new Point(x, y);
        
        // Check that point lies on ray
        // This is same check that parameter t for ray > 0
        if ((p.getX() - a.start.getX()) * a.direction.getX() < 0 ||
            (p.getY() - a.start.getY()) * a.direction.getY() < 0) {
          throw new NoIntersectionException();
        }
        
        return p;
      }
    }
  }
  
  /**
   * Get left perpendicular vector of length 1 to given vector.
   * By left we mean that shortest rotataion from Vector d to
   * given vector will be to the left (crossProduct of them is greater 0)
   * @param Vector
   * @return Vector Left perpendicular normalized vector
   */
  public static Vector getLeftNormalizedPerpendicular(Vector d) {
    if (equalZero(d.getY())) {
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

  /**
   * Exclude same (with EPS error) points from ArrayList.
   * @param ArrayList<Point> ArrayList which can store same (with EPS error)
   * points.
   * @return ArrayList<Point> ArralyList of unique points
   */
  public static ArrayList<Point> excludeSamePoints(ArrayList<Point> points) {
    ArrayList<Point> unique_points = new ArrayList<Point>();
    
    for (Point test_point : points) {
      boolean test_point_is_unique = true;
      
      for (Point unique_point : unique_points) {
        // Find difference in Points' coordinates
        double dx = test_point.getX() - unique_point.getX();
        double dy = test_point.getY() - unique_point.getY();

        if (equalZero(dx) && equalZero(dy)) {
          test_point_is_unique = false;
          break;
        }
      }

      if (test_point_is_unique) {
        unique_points.add(test_point);
      }
    }

    return unique_points;
  }

  public static Point getMiddlePoint(Segment s) {
    double x = (s.first.getX() + s.second.getX()) / 2.0;
    double y = (s.first.getY() + s.second.getY()) / 2.0;
    return new Point(x, y);
  }

}
