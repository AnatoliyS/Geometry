import java.util.Arrays;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashSet;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;
import Utils.*;
import Utils.Exceptions.*;
import DCEL.*;

public class VoronoiDiagram extends DCEL implements VisualData{
  // Max coordinates of diagram
  public static final double maxX = 1000000.0;
  public static final double minX = -1000000.0;
  public static final double maxY = 1000000.0;
  public static final double minY = -1000000.0;
/*public static final double maxX = 700.0;
  public static final double minX = -100.0;
  public static final double maxY = 500.0;
  public static final double minY = -200.0;*/

  private static final Point top_right = new Point(maxX, maxY);
  private static final Point top_left = new Point(minX, maxY);
  private static final Point bottom_right = new Point(maxX, minY);
  private static final Point bottom_left = new Point(minX, minY);

  // Bounding segemnts of Voronoi Diagram
  public static final Segment[] bounds = new Segment[] {
    new Segment(top_left, top_right),
    new Segment(top_right, bottom_right),
    new Segment(bottom_right, bottom_left),
    new Segment(bottom_left, top_left) 
  };
  
  public VoronoiDiagram(
    ArrayList<HalfEdge> e, 
    ArrayList<Vertex> v, 
    ArrayList<Face> f,
    HashMap<Point, Face> fs
  ) {
    super(e, v, f, fs);
  }
  
  /**
   * Copy constructor
   * @param VoronoiDiagram other diagram
   * @return VoronoiDiagram Copy of other diagram (deep).
   */
  public VoronoiDiagram(VoronoiDiagram other) {
    super(other); // deep copy of DCEL 
  }

  /** 
   * Constructor for 1 point
   * @param Point
   * @return VoronoiDiagram of one point
   */
  public VoronoiDiagram(Point a) {
    super();
    Face f = new Face();
    f.setPoint(a);
    face_searcher.put(a, f);
    face.add(f);
  }

  /** 
   * Constructor for 2 points
   * @param Point 
   * @param Point
   * @return VoronoiDiagram of two points
   */
  public VoronoiDiagram(Point a, Point b) {
    super(); // initialize empty DCEL

    // Find support segment and its' bisector
    Segment support_segment = new Segment(a, b);
    Line perp_bisector = support_segment.getPerpendicularBisector();

    // Find intersection of bisector with bounds
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

    // We always expect to have 2 intersection points
    assert(intersection_points.size() == 2);
    
    Point p1 = intersection_points.get(0);
    Point p2 = intersection_points.get(1);

    Debug.log("intersection_point" + p1.toString());
    Debug.log("intersection_point" + p2.toString());

    // "sort" points to have p1.y > p2.y
    if (p1.getY() < p2.getY()) {
      Point temp = new Point(p1.getX(), p1.getY());
      p1.setX(p2.getX());
      p1.setY(p2.getY());
      p2 = temp;
    }
    
    // 2 infinte vertexes of Voronoi diagram
    Vertex v1 = new Vertex(p1.getX(), p1.getY(), true);
    Vertex v2 = new Vertex(p2.getX(), p2.getY(), true);
    vertex.add(v1);
    vertex.add(v2);

    // 3 faces
    Face f1 = new Face();
    Face f2 = new Face();
    Face f_inf = new Face();
    f_inf.setInfinite(true);
    face.add(f1);
    face.add(f2);
    face.add(f_inf);
    f1.setPoint(a);
    f2.setPoint(b);
    face_searcher.put(a, f1);
    face_searcher.put(b, f2);

    // Build half edges
    HalfEdgeBuilder e_builder = new HalfEdgeBuilder("e");
    HalfEdgeBuilder e_twin_builder = new HalfEdgeBuilder("e_twin");
    HalfEdgeBuilder e_inf_builder = new HalfEdgeBuilder("e_inf");
    HalfEdgeBuilder e_inf_twin_builder = new HalfEdgeBuilder("e_inf_twin");
    HalfEdgeBuilder e_twin_inf_builder = new HalfEdgeBuilder("e_twin_inf");
    HalfEdgeBuilder e_twin_inf_twin_builder = new HalfEdgeBuilder("e_twin_inf_twin");
    
    try {
      e_builder
        .setOrigin(v2)
        .setNextEdge(e_inf_builder.getHalfEdgeReference())
        .setPreviousEdge(e_inf_builder.getHalfEdgeReference())
        .setTwinEdge(e_twin_builder.getHalfEdgeReference())
        .setLeftIncidentFace(f1);
      e_inf_builder
        .setOrigin(v1)
        .setTwinEdge(e_inf_twin_builder.getHalfEdgeReference())
        .setLeftIncidentFace(f1);
      e_inf_twin_builder
        .setOrigin(v2)
        .setLeftIncidentFace(f_inf)
        .setNextEdge(e_twin_inf_twin_builder.getHalfEdgeReference())
        .setPreviousEdge(e_twin_inf_twin_builder.getHalfEdgeReference());

      e_twin_builder
        .setOrigin(v1)
        .setNextEdge(e_twin_inf_builder.getHalfEdgeReference())
        .setPreviousEdge(e_twin_inf_builder.getHalfEdgeReference())
        .setLeftIncidentFace(f2);
      e_twin_inf_builder
        .setOrigin(v2)
        .setTwinEdge(e_twin_inf_twin_builder.getHalfEdgeReference())
        .setLeftIncidentFace(f2);
      e_twin_inf_twin_builder
        .setOrigin(v1)
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
  
  /** 
   * Constructor for 3 points
   * @param Point 
   * @param Point
   * @param Point
   * @return VoronoiDiagram of three points
   */
  public VoronoiDiagram(Point a, Point b, Point c) {
    super(); // initialize empty DCEL structure
    
    // Three support segments and their bisectors
    Segment AB = new Segment(a, b);
    Segment BC = new Segment(b, c);
    Segment CA = new Segment(c, a);

    Line AB_bisector = AB.getPerpendicularBisector();
    Line BC_bisector = BC.getPerpendicularBisector();
    
    // Find point of bisector intersection
    Point bisector_intersection;
    try {
      bisector_intersection = Geometry.intersect(AB_bisector, BC_bisector);
    } catch(NoIntersectionException e) {
      Debug.log(e.getMessage());
      return;
    }

    Point AB_middle = Geometry.getMiddlePoint(AB);
    Point BC_middle = Geometry.getMiddlePoint(BC);
    Point CA_middle = Geometry.getMiddlePoint(CA);

    // Rays from the center of triangle
    Ray AB_ray = new Ray(bisector_intersection, AB_middle);
    Ray BC_ray = new Ray(bisector_intersection, BC_middle);
    Ray CA_ray = new Ray(bisector_intersection, CA_middle);

    // Check that (a,b,c) listed in clockwise order.
    // Use sign in other case
    double sign = 1.0;
    Line l = new Line(a, c);
    if (l.checkPointVerticalAlignment(b) == Line.POINT_BELOW_LINE) {
     sign = -1.0;
    }

    // If triangle (a,b,c) has angle > Pi/2 than we need to change direction of some ray
    if (sign*Geometry.crossProduct(AB.getDirection(), new Vector(a, bisector_intersection)) > 0) {
      AB_ray = new Ray(AB_middle, bisector_intersection);
    }
    if (sign*Geometry.crossProduct(BC.getDirection(), new Vector(b, bisector_intersection)) > 0) {
      BC_ray = new Ray(BC_middle, bisector_intersection);
    }
    if (sign*Geometry.crossProduct(CA.getDirection(), new Vector(c, bisector_intersection)) > 0) {
      CA_ray = new Ray(CA_middle, bisector_intersection);
    }

    // Intersect all rays with all bounding segments
    ArrayList<Point> AB_ray_intersection_points = new ArrayList<Point>();
    ArrayList<Point> BC_ray_intersection_points = new ArrayList<Point>();
    ArrayList<Point> CA_ray_intersection_points = new ArrayList<Point>();
    for (Segment s : bounds) {
      try {
        Point p = Geometry.intersect(AB_ray, s);
        AB_ray_intersection_points.add(p);
      } catch (NoIntersectionException e) {
        // Do nothing
      }
      
      try {
        Point p = Geometry.intersect(BC_ray, s);
        BC_ray_intersection_points.add(p);
      } catch (NoIntersectionException e) {
        // Do nothing
      }
      
      try {
        Point p = Geometry.intersect(CA_ray, s);
        CA_ray_intersection_points.add(p);
      } catch (NoIntersectionException e) {
        // Do nothing
      }
    }

    // Check we have only 1 intersetion for each ray
    AB_ray_intersection_points = Geometry.excludeSamePoints(AB_ray_intersection_points);
    assert(AB_ray_intersection_points.size() == 1);
    BC_ray_intersection_points = Geometry.excludeSamePoints(BC_ray_intersection_points);
    assert(BC_ray_intersection_points.size() == 1);
    CA_ray_intersection_points = Geometry.excludeSamePoints(CA_ray_intersection_points);
    assert(CA_ray_intersection_points.size() == 1);
    
    Point p1 = AB_ray_intersection_points.get(0);
    Point p2 = BC_ray_intersection_points.get(0);
    Point p3 = CA_ray_intersection_points.get(0);

    Debug.log("intersection_point" + p1.toString());
    Debug.log("intersection_point" + p2.toString());
    Debug.log("intersection_point" + p3.toString());
   
    // Initialize vertexes 
    Vertex center = new Vertex(bisector_intersection.getX(), bisector_intersection.getY(), false);
    Vertex v1;
    Vertex v2 = new Vertex(p2.getX(), p2.getY(), true);
    Vertex v3;

    // If he have counter-clockwise order, swap vertexes
    if (sign  < 0)
      Debug.log("Invert points!");
    if (sign < 0) {
      v1 = new Vertex(p3.getX(), p3.getY(), true);
      v3 = new Vertex(p1.getX(), p1.getY(), true);
    } else {
      v1 = new Vertex(p1.getX(), p1.getY(), true);
      v3 = new Vertex(p3.getX(), p3.getY(), true);
    }

    Debug.log("v1 = " + v1);
    Debug.log("v2 = " + v2);
    Debug.log("v3 = " + v3);
    
    vertex.add(center);
    vertex.add(v1);
    vertex.add(v2);
    vertex.add(v3);

    // Initialize faces
    Face f1 = new Face();
    Face f2 = new Face();
    Face f3 = new Face();
    Face f_inf = new Face();
    f_inf.setInfinite(true);
    
    f1.setPoint(a);
    face_searcher.put(a, f1);
    if (sign < 0) {
      f2.setPoint(c);
      f3.setPoint(b);
      face_searcher.put(c, f2);
      face_searcher.put(b, f3);
    } else {
      f2.setPoint(b);
      f3.setPoint(c);
      face_searcher.put(b, f2);
      face_searcher.put(c, f3);
    }

    face.add(f1);
    face.add(f2);
    face.add(f3);
    face.add(f_inf);
    
    // Initialize half-edges
    ArrayList<HalfEdgeBuilder> builder = new ArrayList<HalfEdgeBuilder>();
    for (int i = 0; i < 13; i++) {
      Integer name = new Integer(i);
      builder.add(new HalfEdgeBuilder(name.toString()));
    }
    try {

      builder.get(1)
        .setOrigin(center)
        .setNextEdge(builder.get(7).getHalfEdgeReference())
        .setPreviousEdge(builder.get(3).getHalfEdgeReference())
        .setTwinEdge(builder.get(2).getHalfEdgeReference())
        .setLeftIncidentFace(f1);
      builder.get(2)
        .setOrigin(v1)
        .setNextEdge(builder.get(6).getHalfEdgeReference())
        .setPreviousEdge(builder.get(9).getHalfEdgeReference())
        .setLeftIncidentFace(f2);
      
      builder.get(3)
        .setOrigin(v3)
        .setNextEdge(builder.get(1).getHalfEdgeReference())
        .setPreviousEdge(builder.get(7).getHalfEdgeReference())
        .setTwinEdge(builder.get(4).getHalfEdgeReference())
        .setLeftIncidentFace(f1);
      builder.get(4)
        .setOrigin(center)
        .setNextEdge(builder.get(11).getHalfEdgeReference())
        .setPreviousEdge(builder.get(5).getHalfEdgeReference())
        .setTwinEdge(builder.get(3).getHalfEdgeReference())
        .setLeftIncidentFace(f3);
      
      builder.get(5)
        .setOrigin(v2)
        .setNextEdge(builder.get(4).getHalfEdgeReference())
        .setPreviousEdge(builder.get(11).getHalfEdgeReference())
        .setTwinEdge(builder.get(6).getHalfEdgeReference())
        .setLeftIncidentFace(f3);
      builder.get(6)
        .setOrigin(center)
        .setNextEdge(builder.get(9).getHalfEdgeReference())
        .setPreviousEdge(builder.get(2).getHalfEdgeReference())
        .setTwinEdge(builder.get(5).getHalfEdgeReference())
        .setLeftIncidentFace(f2);
      
      builder.get(7)
        .setOrigin(v1)
        .setNextEdge(builder.get(3).getHalfEdgeReference())
        .setPreviousEdge(builder.get(1).getHalfEdgeReference())
        .setTwinEdge(builder.get(8).getHalfEdgeReference())
        .setLeftIncidentFace(f1);
      builder.get(8)
        .setOrigin(v3)
        .setNextEdge(builder.get(10).getHalfEdgeReference())
        .setPreviousEdge(builder.get(12).getHalfEdgeReference())
        .setTwinEdge(builder.get(7).getHalfEdgeReference())
        .setLeftIncidentFace(f_inf);
      
      builder.get(9)
        .setOrigin(v2)
        .setNextEdge(builder.get(2).getHalfEdgeReference())
        .setPreviousEdge(builder.get(6).getHalfEdgeReference())
        .setTwinEdge(builder.get(10).getHalfEdgeReference())
        .setLeftIncidentFace(f2);
      builder.get(10)
        .setOrigin(v1)
        .setNextEdge(builder.get(12).getHalfEdgeReference())
        .setPreviousEdge(builder.get(8).getHalfEdgeReference())
        .setTwinEdge(builder.get(9).getHalfEdgeReference())
        .setLeftIncidentFace(f_inf);
      
      builder.get(11)
        .setOrigin(v3)
        .setNextEdge(builder.get(5).getHalfEdgeReference())
        .setPreviousEdge(builder.get(4).getHalfEdgeReference())
        .setTwinEdge(builder.get(12).getHalfEdgeReference())
        .setLeftIncidentFace(f3);
      builder.get(12)
        .setOrigin(v2)
        .setNextEdge(builder.get(8).getHalfEdgeReference())
        .setPreviousEdge(builder.get(10).getHalfEdgeReference())
        .setTwinEdge(builder.get(11).getHalfEdgeReference())
        .setLeftIncidentFace(f_inf);
      
      ArrayList<HalfEdge> half_edges = new ArrayList<HalfEdge>();
      for (int i = 1; i <= 12; i++) {
        HalfEdge e = builder.get(i).getHalfEdge();
        edge.add(e); 
      }
    } catch (VoronoiHalfEdgeIsNotValidException e) {
      Debug.log(e.getMessage());
    }

    // CAN BE REMOVED 
    // Check we have correct DCEL structure
    // and delete unused elements (all to right from L diagram
    // and all to left from R)
    checkAndClean();
  }

  public void render(Graphics2D g) {
    Debug.log("Rendering voronoi...");
    for (HalfEdge e : edge) {
      if (!e.isInfinite()) {
        g.setColor(Color.red);
      } else {
        g.setColor(Color.blue);
      }
      Vertex v1 = e.getOrigin();
      Vertex v2 = e.getNextEdge().getOrigin();
      double x1 = v1.getX();
      double y1 = v1.getY();
      double x2 = v2.getX();
      double y2 = v2.getY();
      g.draw(new Line2D.Double(x1, y1, x2, y2));
    }
    
    for (Point v : face_searcher.keySet()) {
      g.fill(new Ellipse2D.Double(v.getX()-4.0, v.getY()-4.0, 8.0, 8.0));
    }
  }

  @Override
  public String toString() {
    String s = "{ Voronoi Diagram \nPoints = [";
    for (Point v : face_searcher.keySet()) {
      s += "(" + v.getX() + ", " + v.getY() + "), ";
    }
    s += "] Vertexes = [";
    for (Vertex v : vertex) {
      s += "(" + v.getX() + ", " + v.getY() + "), ";
    }
    s += "], Edges = [" + edge.size() + "], Faces = [" + face.size();
    s += "], Point-Face-pairs = ["+ face_searcher.toString() +"]";
    s += "}";
    return s;
  }

}
