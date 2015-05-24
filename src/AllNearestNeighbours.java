import java.util.Map;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;
import Utils.*;

public class AllNearestNeighbours implements VisualData{
  private HashMap<Point, Point> neighbours;

  public AllNearestNeighbours() {
    neighbours = new HashMap<>();
  }

  public AllNearestNeighbours(HashMap<Point, Point> n) {
    neighbours = n;
  }

  /**
   * Copy constructor
   * @param AllNearestNeighbours Base AllNearestNeighbours
   * @return AllNearestNeighbours Copy of base AllNearestNeighbours
   */
  public AllNearestNeighbours(AllNearestNeighbours other) {
    neighbours = new HashMap<>();

    for (Map.Entry<Point, Point> entry : other.getNeighbours().entrySet()) {
      Point key = entry.getKey();
      Point value = entry.getValue();
      neighbours.put(key, value);
    }
  }

  /**
   * Constructor for 1 point
   * @param Point
   * @return AllNearestNeighbours for a set of one point
   */
  public AllNearestNeighbours(Point a) {
    neighbours = new HashMap<>();
  }

  /**
   * Constructor for 2 points
   * @param Point
   * @param Point
   * @return AllNearestNeighbours for a set of two points
   */
  public AllNearestNeighbours(Point a, Point b) {
    neighbours = new HashMap<>();
    neighbours.put(a, b);
    neighbours.put(b, a);
  }

  /**
   * Constructor for 3 points
   * @param Point
   * @param Point
   * @param Point
   * @return AllNearestNeighbours for a set of three points
   */
  public AllNearestNeighbours(Point a, Point b, Point c) {
    neighbours = new HashMap<>();

    double AB = Geometry.getDistance(a, b);
    double AC = Geometry.getDistance(a, c);
    double BC = Geometry.getDistance(b, c);

    if (AB < AC) { neighbours.put(a, b); }
    else { neighbours.put(a, c); }

    if (AB < BC) { neighbours.put(b, a); }
    else { neighbours.put(b, c); }

    if (BC < AC) { neighbours.put(c, b); }
    else { neighbours.put(c, a); }
  }

  public HashMap<Point, Point> getNeighbours() {
        return neighbours;
    }

  public void render(Graphics2D g) {
    Debug.log("Rendering All Nearest Neighbours...");

    for (Point p : neighbours.keySet()) {
      double x1 = p.getX();
      double y1 = p.getY();

      Point p1 = neighbours.get(p);
      double x2 = p1.getX();
      double y2 = p1.getY();

      g.setColor(AlgorithmColor.ALL_NEAREST_NEIGHBOURS.getColor());
      g.draw(new Line2D.Double(x1, y1, x2, y2));

      g.setColor(AlgorithmColor.ALL_NEAREST_NEIGHBOURS.getColor());
      g.fill(new Ellipse2D.Double(x1 - 4.0, y1 - 4.0, 8.0, 8.0));
      g.fill(new Ellipse2D.Double(x2 - 4.0, y2 - 4.0, 8.0, 8.0));
    }
  }

  @Override
  public String toString() {
    String s = "{ All Nearest Neighbours \nPoint-Point-pairs = ["+ neighbours.toString() +"] }";
    return s;
  }
}