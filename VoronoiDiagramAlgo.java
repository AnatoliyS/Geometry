import java.io.*;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.Color;
import Utils.*;
import DCEL.*;
import Utils.Exceptions.*;

public class VoronoiDiagramAlgo extends Algorithm {

  private final int TRIVIAL_COUNT = 3;

  public VoronoiDiagramAlgo(String _name, ArrayList<String> _deps) {
    super(_name, _deps);
  }

  // TODO: add realization
  public Object merge(DACNode l, DACNode r) throws NoDataException {
    String name = getName();
    
    VoronoiDiagram left  = new VoronoiDiagram((VoronoiDiagram)l.getDataResult(name));
    VoronoiDiagram right = new VoronoiDiagram((VoronoiDiagram)r.getDataResult(name));
    Debug.log("Copy finished");

    // Step 0. Create resulting Voronoi by adding everything from children 
    // into one collections. Later they will be modified
    ArrayList<HalfEdge> allEdges = new ArrayList<HalfEdge>();
    allEdges.addAll(left.getEdges());
    allEdges.addAll(right.getEdges());

    ArrayList<Vertex> allVertexes = new ArrayList<Vertex>();
    allVertexes.addAll(left.getVertexes());
    allVertexes.addAll(right.getVertexes());

    ArrayList<Face> allFaces = new ArrayList<Face>();
    allFaces.addAll(left.getFaces());
    allFaces.addAll(right.getFaces());
    
    HashMap<Point, Face> allFS = new HashMap<Point, Face>();
    for (Map.Entry<Point, Face> entry : left.getFS().entrySet()) {
      Point key = entry.getKey();
      Face value = entry.getValue();
      allFS.put(key, value);
    }
    for (Map.Entry<Point, Face> entry : right.getFS().entrySet()) {
      Point key = entry.getKey();
      Face value = entry.getValue();
      allFS.put(key, value);
    }
   
    ConvexHull left_hull = (ConvexHull)l.getDataResult(AlgorithmName.CONVEX_HULL);
    ConvexHull right_hull = (ConvexHull)r.getDataResult(AlgorithmName.CONVEX_HULL);
     
    ArrayList<Point> lpoints = left_hull.getPoints();
    ArrayList<Point> rpoints = right_hull.getPoints();

    // Step 1. Get support lines from convex hulls
    Pair<Integer, Integer> lcs = ConvexHullAlgo.lowestCommonSupport(
      lpoints,
      rpoints
    );
    Pair<Integer, Integer> ucs = ConvexHullAlgo.uppestCommonSupport(
      lpoints,
      rpoints
    );

    // Step 2. Calc bisector
    int current_A_index = ucs.first;
    int current_B_index = ucs.second;
    Point A = lpoints.get(ucs.first);
    Point B = rpoints.get(ucs.second);
    Debug.log("A = " + A.toString());
    Debug.log("B = " + B.toString());
    
    Segment AB = new Segment(A, B);
    Line bisector = AB.getPerpendicularBisector();

    // Step 3. Find new inf vertex
    double maxX = VoronoiDiagram.maxX;    
    double minX = VoronoiDiagram.minX;    
    double maxY = VoronoiDiagram.maxY;    
    double minY = VoronoiDiagram.minY;    

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

    ArrayList<Point> intersection_points = new ArrayList<Point>();
    for (Segment s : bounds) {
      try {
        Point p = Geometry.intersect(bisector, s);
        intersection_points.add(p);
      } catch (NoIntersectionException e) {
        // Do nothing
      }
    }

    intersection_points = Geometry.excludeSamePoints(intersection_points);

    assert(intersection_points.size() == 2);

    Point p1 = intersection_points.get(0);
    Point p2 = intersection_points.get(1);
    
    Point p = (p1.getY() > p2.getY()) ? p1 : p2;

    //Debug.log("INF intersection_point" + p.toString());

    Vertex current_vertex = new Vertex(p.getX(), p.getY(), true);
    allVertexes.add(current_vertex);

    Vector ray_direction = (bisector.getDirection().getY() < 0) ? 
      bisector.getDirection() : new Vector(-bisector.getDirection().getX(), -bisector.getDirection().getY());
    Point ray_point = new Point(current_vertex.getX(), current_vertex.getY());
    Ray current_ray = new Ray(ray_point, ray_direction);
    Debug.log("FIRST RAY = {" + ray_point.toString() + ", " + ray_direction.toString() + "}");

    Face faceA = new Face(left.getFaceAroundPoint(A));
    Face faceB = new Face(right.getFaceAroundPoint(B));
    
    ArrayList<HalfEdge> edgesA = getAllEdgesAroundFace(faceA);
    Debug.log("faceA = " + faceA.toString());
    ArrayList<HalfEdge> edgesB = getAllEdgesAroundFace(faceB);

    ArrayList<HalfEdge> inf_edgesA = getAllInfiniteEdges(edgesA);
    ArrayList<HalfEdge> inf_edgesB = getAllInfiniteEdges(edgesB);

    assert(inf_edgesA.size() == 1);
    assert(inf_edgesB.size() == 1);

    HalfEdgeBuilder current_edge_builder = new HalfEdgeBuilder("frist_edge");
    HalfEdgeBuilder current_edge_twin_builder = new HalfEdgeBuilder("first_edge_twin");
    current_edge_builder
      .setTwinEdge(current_edge_twin_builder.getHalfEdgeReference())
      .setLeftIncidentFace(faceA);
    current_edge_twin_builder
      .setOrigin(current_vertex)
      .setLeftIncidentFace(faceB);
    
    // Step 4. Prepare new inf edges
    HalfEdge left_inf_edge = inf_edgesA.get(0);
    HalfEdge right_inf_edge = inf_edgesB.get(0);
    HalfEdgeBuilder left_new_inf_edge_builder = new HalfEdgeBuilder("Le_inf_1");
    HalfEdgeBuilder right_new_inf_edge_builder = new HalfEdgeBuilder("Re_inf_1");
    HalfEdgeBuilder left_new_inf_edge_twin_builder = new HalfEdgeBuilder("Le_inf_1(twin)");
    HalfEdgeBuilder right_new_inf_edge_twin_builder = new HalfEdgeBuilder("Re_inf_1(twin)");

    left_new_inf_edge_builder
      .setOrigin(current_vertex)
      .setNextEdge(left_inf_edge.getNextEdge())
      .setPreviousEdge(current_edge_builder.getHalfEdgeReference())
      .setTwinEdge(left_new_inf_edge_twin_builder.getHalfEdgeReference())
      .setLeftIncidentFace(faceA);
    left_new_inf_edge_twin_builder
      .setOrigin(left_inf_edge.getNextEdge().getOrigin()) 
      .setPreviousEdge(left_inf_edge.getTwinEdge().getPreviousEdge())
      .setLeftIncidentFace(left_inf_edge.getTwinEdge().getLeftIncidentFace());


    right_new_inf_edge_builder
      .setOrigin(right_inf_edge.getOrigin())
      .setNextEdge(current_edge_twin_builder.getHalfEdgeReference())
      .setPreviousEdge(right_inf_edge.getPreviousEdge())
      .setTwinEdge(right_new_inf_edge_twin_builder.getHalfEdgeReference())
      .setLeftIncidentFace(faceB);
    right_new_inf_edge_twin_builder
      .setOrigin(current_vertex) 
      .setPreviousEdge(left_new_inf_edge_twin_builder.getHalfEdgeReference())
      .setNextEdge(right_inf_edge.getTwinEdge().getNextEdge())
      .setLeftIncidentFace(right_inf_edge.getTwinEdge().getLeftIncidentFace());

    // Safely initialize new edges
    try {
      HalfEdge left_new_inf_edge = left_new_inf_edge_builder.getHalfEdge();
      HalfEdge right_new_inf_edge = right_new_inf_edge_builder.getHalfEdge();
      HalfEdge right_new_inf_edge_twin = right_new_inf_edge_twin_builder.getHalfEdge();
      HalfEdge left_new_inf_edge_twin = left_new_inf_edge_twin_builder.getHalfEdge();
  //    Collections.addAll(allEdges, 
   //       left_new_inf_edge, right_new_inf_edge, right_new_inf_edge_twin, left_new_inf_edge_twin);
    } catch (Exception e) {
      Debug.log("Something was not initialized in DCEL");
    }
   
    // Remove old inf edges from new Voronoi diagram and from edges lists 
    //edgesA.remove(left_inf_edge);
    //edgesB.remove(right_inf_edge);
    /*edgesA.remove(left_inf_edge.getTwinEdge());
    edgesA.remove(left_inf_edge.getTwinEdge());

    allEdges.remove(left_inf_edge);
    allEdges.remove(right_inf_edge);
    allEdges.remove(left_inf_edge.getTwinEdge());
    allEdges.remove(left_inf_edge.getTwinEdge());
*/
    int current_left_edge_index = 0; 
    int current_right_edge_index = 0;
    HalfEdgeBuilder next_edge_builder;
    HalfEdgeBuilder next_edge_twin_builder;
    int edge_number = 2;
    int step = 1;
    // Invariant:
    // Right now we consider point A and B. And we are in FaceA, FaceB and
    // all edges from FaceA are in PolyA and all edges from FaceB are in PolyB
    // Right now we are in current_vertex vertex and want to build chain_edge inside
    // FaceA or FaceB.
    // So we find intersection of current_ray with segments in PolyA and PolyB 
    while(current_A_index != lcs.first || current_B_index != lcs.second) {
      if(step == 3) break;
      Debug.log("----------------");
      Debug.log("Step" + step + " of building chain");
      Debug.log("EdgesA = {");
      for (HalfEdge e : edgesA) {
        Debug.log(e.toString());
      }
      Debug.log("}");
      Debug.log("EdgesB= {");
      for (HalfEdge e : edgesB) {
        Debug.log(e.toString());
      }
      Debug.log("}");

      Debug.log("Cur A index" + current_A_index+ "/ " + lcs.first );
      Debug.log("Cur B index" + current_B_index+ "/ " + lcs.second );

      step++;
      // Find intersection between PolyA and ray. We traverse it clockwise only
      Point intersection_with_left_polygon = new Point(maxX, maxY);
      HalfEdge left_edge = new HalfEdge();
      while(true) {
        try {
          left_edge = edgesA.get(current_left_edge_index);
          if (left_edge.isInfinite()) {
            current_left_edge_index = (current_left_edge_index + 1) % edgesA.size();  
            continue;
          }
          Point first = new Point(left_edge.getOrigin().getX(), left_edge.getOrigin().getY());
          Point second = new Point(left_edge.getNextEdge().getOrigin().getX(), left_edge.getNextEdge().getOrigin().getY());
          Segment current_edge_segment = new Segment(first, second);
          intersection_with_left_polygon = Geometry.intersect(current_ray, current_edge_segment);
          break;
        } catch(NoIntersectionException e) {
          current_left_edge_index = (current_left_edge_index + 1) % edgesA.size();  
        } 
      }
      if (intersection_with_left_polygon.getY() == current_vertex.getY() ) {
        intersection_with_left_polygon.setY(-minY);
      }
      Debug.log("Intersection with LEFT POLY = {" + intersection_with_left_polygon.toString() + "}");

      Point intersection_with_right_polygon = new Point(maxX, maxY);
      HalfEdge right_edge = new HalfEdge();
      while(true) {
        try {
          right_edge = edgesB.get(current_right_edge_index);
          if (right_edge.isInfinite()) {
            current_right_edge_index = (current_right_edge_index - 1 + edgesB.size()) % edgesB.size();  
            continue;
          }
          Point first = new Point(right_edge.getOrigin().getX(), right_edge.getOrigin().getY());
          Point second = new Point(right_edge.getNextEdge().getOrigin().getX(), right_edge.getNextEdge().getOrigin().getY());
          Segment current_edge_segment = new Segment(first, second);
          intersection_with_right_polygon = Geometry.intersect(current_ray, current_edge_segment);
          break;
        } catch(NoIntersectionException e) {
          current_right_edge_index = (current_right_edge_index - 1 + edgesB.size()) % edgesB.size();  
        } 
      }
      if (intersection_with_right_polygon.getY() == current_vertex.getY() ) {
        intersection_with_right_polygon.setY(-minY);
      }
      Debug.log("Intersection with RIGHT POLY = {" + intersection_with_right_polygon.toString() + "}");
     
      Debug.log("ok.."); 
      Vertex next_vertex;
      next_edge_builder = new HalfEdgeBuilder("chain_edge_" + edge_number);
      next_edge_twin_builder = new HalfEdgeBuilder("chain_edge_" + edge_number + "_twin");
      

      if (intersection_with_right_polygon.getY() > intersection_with_left_polygon.getY()) {
        next_vertex = new Vertex(
          intersection_with_right_polygon.getX(), 
          intersection_with_right_polygon.getY()
        );
        // Exit from right polygon
        faceB = right_edge.getTwinEdge().getLeftIncidentFace();
        edgesB = getAllEdgesAroundFace(faceB);
        Debug.log("Edge which we intersected = " + right_edge);
        Debug.log("New face = " + faceB);
        
        // Close right polygon
        current_edge_twin_builder
          .setNextEdge(right_edge);
        right_edge
          .setOrigin(next_vertex);

        // Setup next edge
        next_edge_twin_builder
          .setPreviousEdge(right_edge.getTwinEdge())
          .setOrigin(next_vertex);
       
        current_edge_builder 
          .setOrigin(next_vertex);
        next_edge_builder
          .setNextEdge(current_edge_builder.getHalfEdgeReference())
          .setTwinEdge(next_edge_twin_builder.getHalfEdgeReference())
          .setLeftIncidentFace(faceA);
        
        current_B_index = (current_B_index - 1 + rpoints.size()) % rpoints.size();
        B = rpoints.get(current_B_index);
        Debug.log("Cur B point = " + B );
        
        next_edge_twin_builder
          .setLeftIncidentFace(faceB);

        current_right_edge_index = 0;
        try {
          allEdges.add(current_edge_builder.getHalfEdge());
          allEdges.add(current_edge_twin_builder.getHalfEdge());
          Debug.log("Added edge!");
        } catch(VoronoiHalfEdgeIsNotValidException e) {
          Debug.log(e.getMessage());
        }
      } else {
        next_vertex = new Vertex(
          intersection_with_left_polygon.getX(), 
          intersection_with_left_polygon.getY()
        );
        // Exit from left polygon
        // Close left polygon
        faceA = left_edge.getTwinEdge().getLeftIncidentFace();
        edgesA = getAllEdgesAroundFace(faceA);
        Debug.log("Edge which we intersected = " + left_edge);
        Debug.log("New face = " + faceA);
        //left_edge
        //  .setNextEdge(current_edge_builder.getHalfEdgeReference());
        current_edge_builder
          .setPreviousEdge(left_edge)
          .setOrigin(next_vertex);

        left_edge.getTwinEdge()
          .setOrigin(next_vertex);

        // Setup next edge
        next_edge_twin_builder
          .setPreviousEdge(current_edge_twin_builder.getHalfEdgeReference())
          .setOrigin(next_vertex)
          .setLeftIncidentFace(faceB);
       
        current_A_index = (current_A_index + 1) % lpoints.size();
        A = lpoints.get(current_A_index);
        Debug.log("Cur A point = " + A.toString());
        
        next_edge_builder
          .setNextEdge(left_edge.getTwinEdge())
          .setLeftIncidentFace(faceA)
          .setTwinEdge(next_edge_twin_builder.getHalfEdgeReference());

        current_left_edge_index = 0;

        try {
          allEdges.add(current_edge_builder.getHalfEdge());
          allEdges.add(current_edge_twin_builder.getHalfEdge());
          Debug.log("Added edge!");
        } catch(VoronoiHalfEdgeIsNotValidException e) {
          Debug.log(e.getMessage());
        }
      }
      
      current_vertex = next_vertex;
      current_edge_builder = next_edge_builder;
      current_edge_twin_builder = next_edge_twin_builder;

      AB = new Segment(A, B);
      bisector = AB.getPerpendicularBisector();

      allVertexes.add(current_vertex);

      ray_direction = (bisector.getDirection().getY() < 0) ? 
        bisector.getDirection() : new Vector(-bisector.getDirection().getX(), -bisector.getDirection().getY());
      ray_point = new Point(current_vertex.getX(), current_vertex.getY());
      current_ray = new Ray(ray_point, ray_direction);
      Debug.log("RAY = {" + ray_point.toString() + ", " + ray_direction.toString() + "}");

    } //while 


    VoronoiDiagram result  = new VoronoiDiagram(allEdges, allVertexes, allFaces, allFS);
    Debug.log("merge finised");
    return result;
  }

  private ArrayList<HalfEdge> getAllEdgesAroundFace(Face f) {
    ArrayList<HalfEdge> edges = new ArrayList<HalfEdge>();
    HalfEdge start = f.getIncidentEdge();
    HalfEdge temp = start;
    do {
      edges.add(temp);
      temp = temp.getNextEdge();
      //Debug.log(temp.toString());
    } while(temp != start);
    return edges; 
  }
  
  private ArrayList<HalfEdge> getAllInfiniteEdges(ArrayList<HalfEdge> l) {
    ArrayList<HalfEdge> edges = new ArrayList<HalfEdge>();
    for (HalfEdge e : l) {
      if (e.isInfinite()) {
        edges.add(e);
      }
    }
    return edges; 
  }

  public boolean isTrivialCase(int count) {
    return (count <= TRIVIAL_COUNT);
  }

  public Object doTrivialCase(ArrayList<Point> points) {
    switch (points.size()) {
      case 1:
        return new VoronoiDiagram(points.get(0));
      case 2:
        return new VoronoiDiagram(points.get(0), points.get(1));
      case 3:
        return new VoronoiDiagram(
          points.get(0),
          points.get(1),
          points.get(2)
        );
    }
    return null;
  }

}
