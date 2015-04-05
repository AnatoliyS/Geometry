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

  public Line getPerpendicularBisector() {
    Point start = new Point((first.getX() + second.getX())/2.0, (first.getY() + second.getY())/2.0);
    Vector n = Geometry.getNormalizedPerpendicular(direction);
    Point end = new Point(start.getX() + n.getX(), start.getY() + n.getY());
    Line perpendicular_bisector = new Line(start, end);
    return perpendicular_bisector;
  }

}
