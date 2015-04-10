import java.util.Arrays;
import java.util.Set;
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
  
  /*private static final double maxX = 100000.0;
  private static final double minX = -100000.0;
  private static final double maxY = 100000.0;
  private static final double minY = -100000.0;*/
  
  private static final double maxX = 600.0;
  private static final double minX = 0.0;
  private static final double maxY = 400.0;
  private static final double minY = 0.0;

  // Constructor for 1 point
  public VoronoiDiagram(Point a) {
    edge = new ArrayList<HalfEdge>();
    vertex = new ArrayList<Vertex>();
    face = new ArrayList<Face>();
    face_searcher = new HashMap<Point, Face>();
    
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
    face_searcher = new HashMap<Point, Face>();
    
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
    f_inf.setInfinite(true);
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
    edge = new ArrayList<HalfEdge>();
    vertex = new ArrayList<Vertex>();
    face = new ArrayList<Face>();
    face_searcher = new HashMap<Point, Face>();
    
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

    Segment AB = new Segment(a, b);
    Segment BC = new Segment(b, c);
    Segment CA = new Segment(c, a);
    Line AB_bisector = AB.getPerpendicularBisector();
    Line BC_bisector = BC.getPerpendicularBisector();
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

    Ray AB_ray = new Ray(bisector_intersection, AB_middle);
    Ray BC_ray = new Ray(bisector_intersection, BC_middle);
    Ray CA_ray = new Ray(bisector_intersection, CA_middle);

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
    
    Vertex center = new Vertex(bisector_intersection.getX(), bisector_intersection.getY(), false);
    Vertex v1 = new Vertex(p1.getX(), p1.getY(), true);
    Vertex v2 = new Vertex(p2.getX(), p2.getY(), true);
    Vertex v3 = new Vertex(p3.getX(), p3.getY(), true);
    vertex.add(center);
    vertex.add(v1);
    vertex.add(v2);
    vertex.add(v3);

    Face f1 = new Face();
    Face f2 = new Face();
    Face f3 = new Face();
    Face f_inf = new Face();
    f_inf.setInfinite(true);
    face.add(f1);
    face.add(f2);
    face.add(f3);
    face.add(f_inf);
    face_searcher.put(a, f1);
    face_searcher.put(b, f2);
    face_searcher.put(c, f3);

    // TODO:
    ArrayList<HalfEdgeBuilder> builder = new ArrayList<HalfEdgeBuilder>();
    for (int i = 0; i < 13; i++) {
      Integer name = new Integer(i+1);
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
        .setNextEdge(builder.get(2).getHalfEdgeReference())
        .setPreviousEdge(builder.get(9).getHalfEdgeReference())
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
    g.setColor(Color.red);
    Debug.log("rendering voronoi...");
    for (HalfEdge e : edge) {
      if (!e.isInfinite()) {
        Vertex v1 = e.getOrigin();
        Vertex v2 = e.getNextEdge().getOrigin();
        g.draw(new Line2D.Double(v1.getX(), v1.getY(), v2.getX(), v2.getY()));
      }
    }
    
    for (Point v : face_searcher.keySet()) {
      g.fill(new Ellipse2D.Double(v.getX()-4.0, v.getY()-4.0, 8.0, 8.0));
    }
  }

  @Override
  public String toString() {
    String s = "";
    // TODO: add code here
    return s;
  }

}
