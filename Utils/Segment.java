package Utils;

public class Segment {
  protected Point first;
  protected Point second;
  protected Vector direction;
  
  public Segment(Point a, Point b) {
    first = a;
    second = b;
    direction = new Vector(a, b);
  }

}
