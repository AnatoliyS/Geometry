import java.util.Arrays;
import java.util.HashMap;
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
  private HashMap<Point, Face> face_searcher;
  
  private static final double maxX = 100000.0;
  private static final double minX = -100000.0;
  private static final double maxY = 100000.0;
  private static final double minY = -100000.0;

  // Constructor for 1 point
  public VoronoiDiagram(Point a) {
    edge = new ArrayList<HalfEdge>();
    vertex = new ArrayList<Vertex>();
    face = new ArrayList<Face>();
    HashMap<Point, Face> face_searcher = new HashMap<Point, Face>();
    
    Face f = new Face();
    face_searcher.put(a, f);
    face.add(f);
    // TODO: check if this is enough for algortihm
  }

  // Constructor for 2 points
  public VoronoiDiagram(Point a, Point b) {
    edge = new ArrayList<HalfEdge>();
    vertex = new ArrayList<Vertex>();
    face = new ArrayList<Face>();
    HashMap<Point, Face> face_searcher = new HashMap<Point, Face>();
    
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
    
    // 2 infinte vertexes of Voronoi diagram
    Vertex v1 = new Vertex(p1.getX(), p1.getY(), true);
    Vertex v2 = new Vertex(p2.getX(), p2.getY(), true);
    vertex.add(v1);
    vertex.add(v2);

    // TODO: add DCEL initialization
    Face f1 = new Face();
    Face f2 = new Face();
    Face f_inf = new Face();
    face.add(f1);
    face.add(f2);
    face.add(f_inf);
    face_searcher.put(a, f1);
    face_searcher.put(b, f2);

    HalfEdgeBuilder e_builder = new HalfEdgeBuilder("e");
    HalfEdgeBuilder e_twin_builder = new HalfEdgeBuilder("e_twin");
    HalfEdgeBuilder e_inf_builder = new HalfEdgeBuilder("e_inf");
    HalfEdgeBuilder e_inf_twin_builder = new HalfEdgeBuilder("e_inf_twin");
    HalfEdgeBuilder e_twin_inf_builder = new HalfEdgeBuilder("e_twin_inf");
    HalfEdgeBuilder e_twin_inf_twin_builder = new HalfEdgeBuilder("e_twin_inf_twin");
    try {
      e_builder
        .setOrigin(v1)
        .setNextEdge(e_inf_builder.getHalfEdgeReference())
        .setPreviousEdge(e_inf_builder.getHalfEdgeReference())
        .setTwinEdge(e_twin_builder.getHalfEdgeReference())
        .setLeftIncidentFace(f1);
      e_inf_builder
        .setOrigin(v2)
        .setTwinEdge(e_inf_twin_builder.getHalfEdgeReference())
        .setLeftIncidentFace(f1);
      e_inf_twin_builder
        .setOrigin(v1)
        .setLeftIncidentFace(f_inf)
        .setNextEdge(e_twin_inf_twin_builder.getHalfEdgeReference())
        .setPreviousEdge(e_twin_inf_twin_builder.getHalfEdgeReference());

      e_twin_builder
        .setOrigin(v2)
        .setNextEdge(e_twin_inf_builder.getHalfEdgeReference())
        .setPreviousEdge(e_twin_inf_builder.getHalfEdgeReference())
        .setLeftIncidentFace(f2);
      e_twin_inf_builder
        .setOrigin(v1)
        .setTwinEdge(e_twin_inf_twin_builder.getHalfEdgeReference())
        .setLeftIncidentFace(f2);
      e_twin_inf_twin_builder
        .setOrigin(v2)
        .setLeftIncidentFace(f_inf);
      
      HalfEdge e = e_builder.getHalfEdge();
      HalfEdge e_twin = e_twin_builder.getHalfEdge();
      HalfEdge e_inf = e_inf_builder.getHalfEdge();
      HalfEdge e_inf_twin = e_inf_twin_builder.getHalfEdge();
      HalfEdge e_twin_inf = e_twin_inf_builder.getHalfEdge();
      HalfEdge e_twin_inf_twin = e_twin_inf_twin_builder.getHalfEdge();

      edge.add(e);  
      edge.add(e_twin);  
      edge.add(e_inf);  
      edge.add(e_inf_twin);  
      edge.add(e_twin_inf);  
      edge.add(e_twin_inf_twin);  

    } catch (VoronoiHalfEdgeIsNotValidException e) {
      Debug.log(e.getMessage());
    }
  }
  
  // TODO: constructor for 3 points
  public VoronoiDiagram(Point a, Point b, Point c) {
  }
 
  public Face getFaceAroundPoint(Point point) throws NoDataException {
    if (!face_searcher.containsKey(point)) {
      throw new NoDataException();
    } else {
      return face_searcher.get(point);
    }
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
