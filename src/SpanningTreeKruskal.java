import Utils.Debug;
import Utils.Geometry;
import Utils.Pair;
import Utils.Point;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

public class SpanningTreeKruskal implements VisualData {
  private Hashtable<Point, ArrayList<Point>> spanningTree;
  private Double weight;

  public SpanningTreeKruskal() {
    spanningTree = new Hashtable<>();
    weight = 0.0;
  }

  Double getWeight() {
    return weight;
  }

  class EdgeComparator
      implements Comparator<Object> {
    @Override
    public int compare(Object _first, Object _second) {
      Pair<Double, Pair<Point, Point>> first = (Pair<Double, Pair<Point, Point>>)_first;
      Pair<Double, Pair<Point, Point>> second = (Pair<Double, Pair<Point, Point>>)_second;
      if (first.first > second.first) {
        return 1;
      } else if (first.first < second.first) {
        return -1;
      } else {
        return 0;
      }
    }
  }

  public Object calculate(DelaunayTriangulation delaunayTriangulation) {
    Hashtable<Point, ArrayList<Point>> graph = delaunayTriangulation.getGraph();
    ArrayList<Pair<Double, Pair<Point, Point>>> edges = new ArrayList<>();
    for(Point point: graph.keySet()) {
      for(Point incidentPoint : graph.get(point)) {
        edges.add(new Pair(Geometry.getDistance(point, incidentPoint),
            new Pair(point, incidentPoint)));
      }
    }
    Object[] weightedEdges = edges.toArray();
    Arrays.sort(weightedEdges, new EdgeComparator());

    DisjointSetUnion dsu = new DisjointSetUnion();
    for (Pair<Double, Pair<Point, Point>> edge : edges) {
      dsu.makeSet(edge.second.first);
      dsu.makeSet(edge.second.second);
    }
    for (Object _edge : weightedEdges) {
      Pair<Double, Pair<Point, Point>> edge = (Pair<Double, Pair<Point, Point>>) _edge;
      Point a = edge.second.first;
      Point b = edge.second.second;
      if (!dsu.find(a).equals(dsu.find(b))) {
        dsu.union(a, b);
        if (!spanningTree.containsKey(a)) {
          spanningTree.put(a, new ArrayList<Point>());
        }
        spanningTree.get(a).add(b);
        weight += edge.first;
      }
    }
    return spanningTree;
  }

  @Override
  public void render(Graphics2D g) {
    Debug.log("Rendering Delaunay triangulation...");
    g.setColor(Color.CYAN);
    for (Point point : spanningTree.keySet()) {
      for (Point incidentPoint : spanningTree.get(point) ) {
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
