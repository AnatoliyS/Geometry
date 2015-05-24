import DCEL.*;
import Utils.*;
import Utils.Point;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Hashtable;

/*
Delaunay Triangulation is dual to Voronoi Diagram
*/
public class DelaunayTriangulation implements VisualData {

  private Hashtable<Point, ArrayList<Point>> connectedPoints;

  // for less than 3 non-collinear points the triangulation is not valid,
  // but it may become valid after merging with other points
  private boolean isValid;

  public boolean isValid() {
    return isValid;
  }

  public Hashtable<Point, ArrayList<Point>> getGraph() {
    return connectedPoints;
  }

  public DelaunayTriangulation(VoronoiDiagram voronoiDiagram) {
    ArrayList<Face> faces = voronoiDiagram.getFaces();
    if (faces.size() < 3) {
      isValid = false;
    }

    Point vertex1 = faces.get(0).getPoint();
    Point vertex2 = faces.get(1).getPoint();
    boolean existsNotCollinear = false;
    for(Face face : faces) {
      Point point = face.getPoint();
      if (point != null && !vertex1.equals(point) && !vertex2.equals(point)) {
        if (!Geometry.arePointsCollinear(vertex1, vertex2, point)) {
          existsNotCollinear = true;
          break;
        }
      }
    }
    isValid = (false == existsNotCollinear) ? false : true;

    connectedPoints = new Hashtable<>();
    ArrayList<HalfEdge> edges = voronoiDiagram.getEdges();
    for(HalfEdge edge : edges) {
      Point point_source = edge.getLeftIncidentFace().getPoint();
      Point point_destination = edge.getTwinEdge().getLeftIncidentFace().getPoint();

      if (null == point_source || null == point_destination) {
        continue;
      }

      if (!connectedPoints.containsKey(point_source)) {
        connectedPoints.put(point_source, new ArrayList<Point>());
      }
      connectedPoints.get(point_source).add(point_destination);
    }
  }

  public DelaunayTriangulation(ArrayList<Point> points) {
    //we build triangulation for few points in trivial case;
    assert (points.size() <= 3);
//    if points are collinear, there are no triangles and triangulation is not valid
    if (points.size() < 3 || Geometry.arePointsCollinear(points.get(0), points.get(1), points.get(2))) {
      isValid = false;
    } else {
      isValid = true;
    }

    connectedPoints = new Hashtable<>();
    for (Point point : points) {
      if (!connectedPoints.containsKey(point)) {
        connectedPoints.put(point, new ArrayList<Point>());
      }
      for (Point addedPoint : points) {
        if (!addedPoint.equals(point)) {
          connectedPoints.get(point).add(addedPoint);
        }
      }
    }
  }

  @Override
  public void render(Graphics2D g) {
    Debug.log("Rendering Delaunay triangulation...");
    g.setColor(AlgorithmColor.DELAUNAY_TRIANGULATION.getColor());
    for (Point point : connectedPoints.keySet()) {
      for (Point incidentPoint : connectedPoints.get(point) ) {
        g.draw(new Line2D.Double(point.getX(),
            point.getY(),
            incidentPoint.getX(),
            incidentPoint.getY()));
      }
    }

  }

  @Override
  public String toString() {
    return new String("TODO");
  }
}
