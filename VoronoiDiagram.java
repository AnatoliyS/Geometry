import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;
import Utils.*;
import Utils.Exceptions.*;
import DCEL.*;

public class VoronoiDiagram implements VisualData{
  private ArrayList<HalfEdge> edge;
  private ArrayList<Vertex> vertex;
  private ArrayList<Face> face;
  
  private static final double maxX = 100000.0;
  private static final double minX = -100000.0;
  private static final double maxY = 100000.0;
  private static final double minY = -100000.0;

  // Constructor for 1 point
  public VoronoiDiagram(Point a) {
    face.add(new Face());
    // TODO: check if this is enough for algortihm
  }

  // Constructor for 2 points
  public VoronoiDiagram(Point a, Point b) {
    Point top_right = new Point(maxX, maxY);
    Point top_left = new Point(minX, maxY);
    Point bottom_right = new Point(maxX, minY);
    Point bottom_left = new Point(minX, minY);

    Segment[] bounds = new Segment[] {
      new Segment(top_left, top_right),
      new Segment(top_right, bottom_right),
      new Segment(bottom_right, bottom_left),
      new Segment(bottom_left, top_left) 
    };

    Segment support_segment = new Segment(a, b);
    Line perp_bisector = support_segment.getPerpendicularBisector();

    ArrayList<Point> intersection_points = new ArrayList<Point>();
    for (Segment s : bounds) {
      try {
        Point p = Geometry.intersect(perp_bisector, s);
        intersection_points.add(p);
      } catch (NoIntersectionException e) {
        // Do nothing
      }
    }

    intersection_points = Geometry.excludeSamePoints(intersection_points);

    assert(intersection_points.size() == 2);
    
    Point p1 = intersection_points.get(0);
    Point p2 = intersection_points.get(1);

    Debug.log("intersection_point" + p1.toString());
    Debug.log("intersection_point" + p2.toString());
    
    Vertex v1 = new Vertex(p1.getX(), p1.getY());
    Vertex v2 = new Vertex(p2.getX(), p2.getY());

    // TODO: add DCEL initialization

  }
  
  // TODO: constructor for 3 points
  public VoronoiDiagram(Point a, Point b, Point c) {
  }
  
  // TODO: add render
  public void render(Graphics2D g) {
    Debug.log("rendering voronoi...");
  }

  @Override
  public String toString() {
    String s = "";
    // TODO: add code here
    return s;
  }

}
