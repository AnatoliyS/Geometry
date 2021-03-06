package Utils;

public class Vector {
  protected double x;
  protected double y;

  public Vector(Point start, Point end) {
    x = end.getX() - start.getX();
    y = end.getY() - start.getY();
  }

  public Vector(double _x, double _y) {
    x = _x;
    y = _y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  @Override
  public String toString() {
    String s = new String();
    s = "[" + x + ", " + y + "]";
    return s; 
  }

}
