package Utils;

public class Point {
  private double x;
  private double y;

  public Point(double _x, double _y) {
    x = _x;
    y = _y; 
  }

  public Point(Point p) {
    this.x = p.x;
    this.y = p.y;
  }

  public double getX() {
    return x;
  }
  public double getY() {
    return y;
  }
  public void setX(double _x) {
    x = _x;
  }
  public void setY(double _y) {
    y = _y;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if ((obj == null) || (obj.getClass() != this.getClass())) {
      return false;
    }

    Point p = (Point)obj;
    return Geometry.equalZero(p.x - this.x) && Geometry.equalZero(p.y - this.y);
  }

  @Override
  public String toString() {
    String s = new String();
    s = "[" + x + ", " + y + "]";
    return s; 
  }

}
