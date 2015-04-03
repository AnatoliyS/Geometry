package Utils;

import Utils.Exceptions.*;

public class Line {
  protected Point first;
  protected Point second;
  protected Vector direction;

  public static final int POINT_ON_LINE = 0;
  public static final int POINT_ABOVE_LINE = 1;
  public static final int POINT_BELOW_LINE = 2;
  public static final int POINT_HAS_NO_X_PROJECTION = 3;
  
  public Line(Point a, Point b) {
    first = a;
    second = b;
    direction = new Vector(a, b);
  }

  /**
   * Checks point vertical alignment relative to line.
   * Find x-projection of point p on line and check Y of it.
   */
  public int checkPointVerticalAlignment(Point p) {
    Vector to_point = new Vector(first, p);
    if (Geometry.equal_zero(Geometry.cross_product(direction, to_point))) {
      return POINT_ON_LINE;
    } else {
      if (Geometry.equal_zero(direction.getX())) {
        return POINT_HAS_NO_X_PROJECTION;
      } else {
        double projection_y = first.getY() +
          (p.getX() - first.getX()) * direction.getY() / direction.getX();
        return (projection_y < p.getY()) ? POINT_ABOVE_LINE : POINT_BELOW_LINE;
      }
    }
  }

  public Point intersect(Line other) throws NoIntersectionException {
    return null;
  }
  
  public Point intersect(Segment other) throws NoIntersectionException {
    return null;
  }

}
