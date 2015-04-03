package Utils;

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

  /*public static Point intersect(Segment a, Segment b) {
    return new Point();
  }

  public static Point intersect(Line a, Line b) {
    return new Point();
  }*/

}
