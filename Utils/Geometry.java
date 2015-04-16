package Utils;

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
   * Get IntersectionResult instance of intersection of two Lines
   * @param Line
   * @param Line
   * @return IntersectionResult
   */
  public static IntersectionResult getIntersection(Line a, Line b) {
    double dir_product = crossProduct(b.direction, a.direction);
    double product = crossProduct(a.direction, new Vector(a.first, b.first));

    // Variables for return result
    IntersectionType type;
    Pair<Point, Point> result;

    // If lines are parallel
    if (equalZero(dir_product)) {
      // If lines are equal
      if (equalZero(product)) {
        // INFINITY_INTERSECTION
        type = IntersectionType.INFINITY_INTERSECTION;
        double diffX = a.first.getX() - b.first.getX();
        if (equalZero(diffX)) {
          result = new Pair<Point, Point>(a.first, b.second);
        } else {
          result = new Pair<Point, Point>(a.first, b.first);
        }
      } else {
        // NO_INTERSECTION
        type = IntersectionType.NO_INTERSECTION;
        result = new Pair<Point, Point>(null, null);
      }
    } else {
      // POINT_INTERSECTION
      double t = product / dir_product;
      double x = b.first.getX() + t * b.direction.getX();
      double y = b.first.getY() + t * b.direction.getY();

      type = IntersectionType.POINT_INTERSECTION;
      Point p = new Point(x, y);
      result = new Pair<Point, Point>(p, null);
    }

    IntersectionResult interResult = new IntersectionResult(type, result);
    return interResult;
  }

  /**
   * Get IntersectionResult of intersection of Segment and Segment
   * @param Segment
   * @param Segment
   * @return IntersectionResult
   */
  public static IntersectionResult getIntersection(Segment a, Segment b) {
    Line lineA = new Line(a.getFirst(), a.getSecond());
    Line lineB = new Line(b.getFirst(), b.getSecond());

    // Intersection for according lines
    IntersectionResult lineIntersectionResult = getIntersection(lineA, lineB);
    IntersectionType lineIntersectionType = lineIntersectionResult.getType();

    // Variables for answer for segments' intersection
    IntersectionType type;
    Pair<Point, Point> result;

    // NO_INTERSECTION for lines
    if (lineIntersectionType == IntersectionType.NO_INTERSECTION) {
      type = IntersectionType.NO_INTERSECTION;
      result = new Pair<Point, Point>(null, null);
    } else {
      Pair<Point, Point> lineResult = lineIntersectionResult.getResult();

      // POINT_INTERSECTION for lines
      if (lineIntersectionType == IntersectionType.POINT_INTERSECTION) {
        Point p = lineResult.first;
        // Each segment contain point p
        if (a.onSegment(p) && b.onSegment(p)) {
          type = IntersectionType.POINT_INTERSECTION;
          result = new Pair<Point, Point>(p, null);
        } else {
          type = IntersectionType.NO_INTERSECTION;
          result = new Pair<Point, Point>(null, null);
        }
      } else {
        // INFINITY_INTERSECTION for lines

        // Get intersection of segments on line
        IntersectionResult interSegmentOnLine = getSegmentIntersectionOnLine(a, b);
        type = interSegmentOnLine.getType();
        result = interSegmentOnLine.getResult();
      }
    }

    IntersectionResult interResult = new IntersectionResult(type, result);
    return interResult;
  }

  /**
   * Helper function for getting intersection of two segments
   * which laying on the same line!!!
   * @param Segment a           = first segment
   * @param Segment b           = second segment
   * @return IntersectionResult = Instance of IntersectionResult which contain
   * intersection type and according to type result
   */
  private static IntersectionResult getSegmentIntersectionOnLine(Segment a, Segment b) {
    Point A0 = a.first;
    Point A1 = a.second;
    Point B0 = b.first;
    Point B1 = b.second;

    // TODO: Rewrite this code using swap function for wraping objects

    // Left point of segment a must have less x coordinate than right point
    if (A0.getX() > A1.getX()) {
      Point temp = A0;
      A0 = A1;
      A1 = temp;
    }

    // Left point of segment b must have less x coordinate than right point
    if (B0.getX() > B1.getX()) {
      Point temp = B0;
      B0 = B1;
      B1 = temp;
    }

    // Segment a must have less x coordinate than segment b
    if (A0.getX() > B0.getX()) {
      // swap A0 B0
      Point temp = A0;
      A0 = B0;
      B0 = temp;
      // swap A1 B1
      temp = A1;
      A1 = B1;
      B1 = temp;
    }

    Point A;
    Point B;
    a = new Segment(A0, A1);
    b = new Segment(B0, B1);

    IntersectionType type;
    Pair<Point, Point> result;

    // Segments haven't intersection on line
    if (!a.onSegment(B0)) {
      // NO_INTERSECTION
      type = IntersectionType.NO_INTERSECTION;
      result = new Pair<Point, Point>(null, null);
    } else {
      // Left point of result will be left point of segment b
      A = B0;

      // Define right point of result
      if (b.onSegment(A1)) {
        B = A1;
      } else {
        B = B1;
      }

      double diffX = A.getX() - B.getX();
      if (equalZero(diffX)) {
        // POINT_INTERSECTION
        type = IntersectionType.POINT_INTERSECTION;
        result = new Pair<Point, Point>(A, null);
      } else {
        // INFINITY_INTERSECTION
        type = IntersectionType.INFINITY_INTERSECTION;
        result = new Pair<Point, Point>(A, B);
      }
    }

    IntersectionResult interResult = new IntersectionResult(type, result);
    return interResult;
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

}
