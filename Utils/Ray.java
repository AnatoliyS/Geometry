package Utils;

public class Ray {
  protected Point start;
  protected Vector direction;
  
  public Ray(Point a, Point b) {
    start = a;
    direction = new Vector(a, b);
  }
  
  public Ray(Point a, Vector dir) {
    start = a;
    direction = dir;
  }

  public Point getStartPoint() {
    return start;
  }

}
