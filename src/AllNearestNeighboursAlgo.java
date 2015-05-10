import java.lang.Object;
import java.lang.String;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import Utils.*;
import DCEL.*;
import Utils.Exceptions.*;

public class AllNearestNeighboursAlgo extends Algorithm {
  private final int TRIVIAL_COUNT = 3;

  public AllNearestNeighboursAlgo(String _name, ArrayList<String> _deps) {
        super(_name, _deps);
    }

  public Object merge(DACNode l, DACNode r) throws NoDataException, AlgorithmRuntimeException {
    String name = getName();

    // Deep copy of children nodes' neighbours
    AllNearestNeighbours leftNeighbours  = new AllNearestNeighbours((AllNearestNeighbours)l.getDataResult(name));
    AllNearestNeighbours rightNeighbours = new AllNearestNeighbours((AllNearestNeighbours)r.getDataResult(name));

    HashMap<Point, Point> allNeighbours = new HashMap<>();
    for (Map.Entry<Point, Point> entry : leftNeighbours.getNeighbours().entrySet()) {
      Point key = entry.getKey();
      Point value = entry.getValue();
      allNeighbours.put(key, value);
    }
    for (Map.Entry<Point, Point> entry : rightNeighbours.getNeighbours().entrySet()) {
      Point key = entry.getKey();
      Point value = entry.getValue();
      allNeighbours.put(key, value);
    }

    VoronoiDiagram left  = new VoronoiDiagram((VoronoiDiagram)l.getDataResult(AlgorithmName.VORONOI_DIAGRAM));
    VoronoiDiagram right = new VoronoiDiagram((VoronoiDiagram)r.getDataResult(AlgorithmName.VORONOI_DIAGRAM));

    Debug.log("Copy finished");

    ArrayList<HalfEdge> allEdges = new ArrayList<HalfEdge>();
    allEdges.addAll(left.getEdges());
    allEdges.addAll(right.getEdges());

    ArrayList<Vertex> allVertexes = new ArrayList<Vertex>();
    allVertexes.addAll(left.getVertexes());
    allVertexes.addAll(right.getVertexes());

    ArrayList<Face> allFaces = new ArrayList<Face>();
    allFaces.addAll(left.getFaces());
    allFaces.addAll(right.getFaces());

    HashMap<Point, Face> allFS = new HashMap<>();
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

    // Convex hulls of children nodes
    ConvexHull left_hull = (ConvexHull)l.getDataResult(AlgorithmName.CONVEX_HULL);
    ConvexHull right_hull = (ConvexHull)r.getDataResult(AlgorithmName.CONVEX_HULL);

    ArrayList<Point> lpoints = left_hull.getPoints();
    ArrayList<Point> rpoints = right_hull.getPoints();

    // Get support lines from convex hulls
    Pair<Integer, Integer> lcs = ConvexHullAlgo.lowestCommonSupport(
      lpoints,
      rpoints
    );
    Pair<Integer, Integer> ucs = ConvexHullAlgo.uppestCommonSupport(
      lpoints,
      rpoints
    );

    // Calculate bisector
    Point A = lpoints.get(ucs.first);
    Point B = rpoints.get(ucs.second);
    Debug.log("A = " + A.toString());
    Debug.log("B = " + B.toString());

    // Find support segment and its' bisector
    Segment AB = new Segment(A, B);
    Line bisector = AB.getPerpendicularBisector();

    // Find new inf vertex
    // Intersect bisector with bound
    Segment[] bounds = VoronoiDiagram.bounds;
    double minX = VoronoiDiagram.minX;
    double minY = VoronoiDiagram.minY;

    ArrayList<Point> intersection_points = new ArrayList<>();
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

    Debug.log("INF intersection_point" + p.toString());

    Vertex current_vertex = new Vertex(p.getX(), p.getY(), true);
    allVertexes.add(current_vertex);

    Vector ray_direction = (bisector.getDirection().getY() < 0) ?
      bisector.getDirection() : new Vector(-bisector.getDirection().getX(), -bisector.getDirection().getY());
    Point ray_point = new Point(current_vertex.getX(), current_vertex.getY());
    Ray current_ray = new Ray(ray_point, ray_direction);
    Debug.log("FIRST RAY = {" + ray_point.toString() + ", " + ray_direction.toString() + "}");

    Face faceA = left.getFaceAroundPoint(A);
    Face faceB = right.getFaceAroundPoint(B);

    ArrayList<HalfEdge> edgesA, edgesB, inf_edgesA, inf_edgesB;

    try {
      edgesA = DCEL.getAllEdgesAroundFace(faceA);
      edgesB = DCEL.getAllEdgesAroundFace(faceB);
    } catch (FaceTraversingException e) {
      throw new VoronoiBuildingException(e.getMessage());
    }

    inf_edgesA = DCEL.getAllInfiniteEdges(edgesA);
    inf_edgesB = DCEL.getAllInfiniteEdges(edgesB);

    assert(inf_edgesA.size() == 1);
    assert(inf_edgesB.size() == 1);

    HalfEdgeBuilder current_edge_builder = new HalfEdgeBuilder("first_edge");
    HalfEdgeBuilder current_edge_twin_builder = new HalfEdgeBuilder("first_edge_twin");

    current_edge_builder
      .setTwinEdge(current_edge_twin_builder.getHalfEdgeReference())
      .setLeftIncidentFace(faceA);
    current_edge_twin_builder
      .setOrigin(current_vertex)
      .setLeftIncidentFace(faceB);

    // Prepare new inf edges
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
      Collections.addAll(allEdges,
        left_new_inf_edge, right_new_inf_edge, right_new_inf_edge_twin, left_new_inf_edge_twin);
    } catch (HalfEdgeIsNotValidException e) {
      throw new VoronoiBuildingException(e.getMessage());
    }

    int current_left_edge_index = 0;
    int current_right_edge_index = 0;
    HalfEdgeBuilder next_edge_builder;
    HalfEdgeBuilder next_edge_twin_builder;
    int edge_number = 1;
    int step = 1;
    Point lcsA = lpoints.get(lcs.first);
    Point lcsB = rpoints.get(lcs.second);

    // Chain building. Invariant:
    // Right now we consider point A and B. And we are in FaceA, FaceB and
    // all edges from FaceA are in edges_A and all edges from FaceB are in edges_B
    // Right now we are in current_vertex vertex and want to build chain_edge inside
    // FaceA or FaceB.
    // So we find intersection of current_ray with segments in edges_A and edges_B
    while(A != lcsA || B != lcsB) {
      Debug.log("----------------");
      Debug.log("Step" + step + " of building chain");

      double dist = Geometry.getDistance(A, B);
      if(Geometry.getDistance(A, allNeighbours.get(A)) > dist) allNeighbours.put(A, B);
      if(Geometry.getDistance(B, allNeighbours.get(B)) > dist) allNeighbours.put(B, A);

      step++;
      boolean found_left_intersection = false;
      boolean found_right_intersection = false;

      // Find intersection between PolyA and ray. We traverse it clockwise only
      Point intersection_with_left_polygon = new Point(minX, minY);
      HalfEdge left_edge = new HalfEdge();
      int iterations = 0;
      while(iterations < edgesA.size()) {
        iterations++;
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

          if (Geometry.equalZero(intersection_with_left_polygon.getY() - current_vertex.getY()) &&
            Geometry.equalZero(intersection_with_left_polygon.getX() - current_vertex.getX())
          ) {
            current_left_edge_index = (current_left_edge_index + 1) % edgesA.size();
            continue;
          }

          found_left_intersection = true;
          Debug.log("Left intersected edge" + left_edge);
          break;
        } catch(NoIntersectionException e) {
          current_left_edge_index = (current_left_edge_index + 1) % edgesA.size();
        }
      }

      if (!found_left_intersection) {
        intersection_with_left_polygon = new Point(minX, minY);
      }

      Debug.log("Intersection with LEFT POLY = {" + intersection_with_left_polygon.toString() + "}");

      iterations = 0;
      Point intersection_with_right_polygon = new Point(minX, minY);
      HalfEdge right_edge = new HalfEdge();
      while(iterations < edgesB.size()) {
        iterations++;
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

          if (Geometry.equalZero(intersection_with_right_polygon.getY() - current_vertex.getY()) &&
            Geometry.equalZero(intersection_with_right_polygon.getX() - current_vertex.getX())
          ) {
            current_right_edge_index = (current_right_edge_index - 1 + edgesB.size()) % edgesB.size();
            continue;
          }

          found_right_intersection = true;
          Debug.log("Right intersected edge" + right_edge);
          break;
        } catch(NoIntersectionException e) {
          current_right_edge_index = (current_right_edge_index - 1 + edgesB.size()) % edgesB.size();
        }
      }

      if (!found_right_intersection) {
        intersection_with_right_polygon = new Point(minX, minY);
      }

      Debug.log("Intersection with RIGHT POLY = {" + intersection_with_right_polygon.toString() + "}");

      if (!found_left_intersection && !found_right_intersection){
        Debug.log("POINTS!\n");
        for (Map.Entry<Point, Face> entry : allFS.entrySet()) {
          Debug.log(entry.getKey() + "\n");
        }
        Debug.log("END POINTS!\n");
        //break;
        throw new VoronoiBuildingException("No intersection for both left and right!");
      }

      Debug.log("ok..");
      Vertex next_vertex;
      next_edge_builder = new HalfEdgeBuilder("chain_edge_" + edge_number);
      next_edge_twin_builder = new HalfEdgeBuilder("chain_edge_" + edge_number + "_twin");
      edge_number++;

      if (intersection_with_right_polygon.getY() > intersection_with_left_polygon.getY()) {
        next_vertex = new Vertex(
          intersection_with_right_polygon.getX(),
          intersection_with_right_polygon.getY()
        );

        // Exit from right polygon
        faceB = right_edge.getTwinEdge().getLeftIncidentFace();
        B = faceB.getPoint();
        Debug.log("Cur B point = " + B );
        Debug.log("Edge which we intersected = " + right_edge);

        try{
          edgesB = DCEL.getAllEdgesAroundFace(faceB);
        } catch (FaceTraversingException e) {
          throw new VoronoiBuildingException(e.getMessage());
        }

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

        // This is new faceB
        next_edge_twin_builder
          .setLeftIncidentFace(faceB);

        current_right_edge_index = 0;
        try {
          allEdges.add(current_edge_builder.getHalfEdge());
          allEdges.add(current_edge_twin_builder.getHalfEdge());
          Debug.log("Added edge! = " + current_edge_builder.getHalfEdge() + " and its twin = " + current_edge_twin_builder.getHalfEdge());
          Debug.log("And its face= " + current_edge_twin_builder.getHalfEdge().getLeftIncidentFace());
        } catch (HalfEdgeIsNotValidException e) {
          throw new VoronoiBuildingException(e.getMessage());
        }
      } else {
        next_vertex = new Vertex(
          intersection_with_left_polygon.getX(),
          intersection_with_left_polygon.getY()
        );

        faceA = left_edge.getTwinEdge().getLeftIncidentFace();
        A = faceA.getPoint();
        Debug.log("Cur A point = " + A.toString());
        try{
          edgesA = DCEL.getAllEdgesAroundFace(faceA);
        } catch (FaceTraversingException e) {
          throw new VoronoiBuildingException(e.getMessage());
        }

        Debug.log("Edge which we intersected = " + left_edge);
        Debug.log("New face = " + faceA);

        HalfEdge fake = new HalfEdge(left_edge.getTwinEdge());
        left_edge.getTwinEdge().getPreviousEdge().setNextEdge(fake);

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

        next_edge_builder
          .setNextEdge(left_edge.getTwinEdge())
          .setLeftIncidentFace(faceA)
          .setTwinEdge(next_edge_twin_builder.getHalfEdgeReference());

        current_left_edge_index = 0;

        try {
          allEdges.add(current_edge_builder.getHalfEdge());
          allEdges.add(current_edge_twin_builder.getHalfEdge());
          Debug.log("Added edge! = " + current_edge_builder.getHalfEdge() + " and its twin = " + current_edge_twin_builder.getHalfEdge());
          //Debug.log("And its face= " + current_edge_twin_builder.getHalfEdge().getLeftIncidentFace());
        } catch (HalfEdgeIsNotValidException e) {
          throw new VoronoiBuildingException(e.getMessage());
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

    double dist = Geometry.getDistance(A, B);
    if(Geometry.getDistance(A, allNeighbours.get(A)) > dist) allNeighbours.put(A, B);
    if(Geometry.getDistance(B, allNeighbours.get(B)) > dist) allNeighbours.put(B, A);

    AllNearestNeighbours result  = new AllNearestNeighbours(allNeighbours);
    Debug.log("merge finised. Doing check! \n");
    return result;
  }

  public boolean isTrivialCase(int count) {
        return (count <= TRIVIAL_COUNT);
    }

  public Object doTrivialCase(ArrayList<Point> points) {
    switch (points.size()) {
      case 1:
        return new AllNearestNeighbours(points.get(0));
      case 2:
        return new AllNearestNeighbours(points.get(0), points.get(1));
      case 3:
        return new AllNearestNeighbours(
          points.get(0),
          points.get(1),
          points.get(2)
        );
    }
    return null;
  }
}