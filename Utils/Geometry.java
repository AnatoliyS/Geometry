package Utils;

import Utils.Exceptions.*;

public class Geometry {
  public static boolean equal_zero(double value) {
    return Math.abs(value) <= Constants.EPS;
  }

  public static double cross_product(Vector a, Vector b) {
    return a.getX()*b.getY() - a.getY()*b.getX();
  }
  
  public static double scalar_product(Vector a, Vector b) {
    return a.getX()*b.getX() + a.getY()*b.getY();
  }

  public static Point intersect(Line a, Line b) throws NoIntersectionException {
    double dir_product = Geometry.cross_product(b.direction, a.direction);
    if (Geometry.equal_zero(dir_product)) {
      throw new NoIntersectionException();
    } else {
      double t = Geometry.cross_product(a.direction, new Vector(a.first, b.first)) / dir_product;
      return new Point(b.first.getX() + t*b.direction.getX(), b.first.getY() + t*b.direction.getY());
    }
  }
  
  public static Point intersect(Line a, Segment b) throws NoIntersectionException {
    double dir_product = Geometry.cross_product(b.direction, a.direction);
    if (Geometry.equal_zero(dir_product)) {
      throw new NoIntersectionException();
    } else {
      double t = Geometry.cross_product(a.direction, new Vector(a.first, b.first)) / dir_product;
      if (t < 0 || t > 1) {
        throw new NoIntersectionException();
      } else {
        return new Point(b.first.getX() + t*b.direction.getX(), b.first.getY() + t*b.direction.getY());
      }
    }
  }

  public static Vector getNormalizedPerpendicular(Vector d) {
    if (Geometry.equal_zero(d.getY())) {
      return new Vector(0.0, 1.0);
    } else {
      double x = Math.sqrt(1.0 / (1.0 + d.getX()*d.getX() / (d.getY() * d.getY())));
      double y = - x * d.getX() / d.getY();
      return new Vector(x, y);
    }
  }

}
