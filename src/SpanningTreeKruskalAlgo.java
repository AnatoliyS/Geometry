import Utils.Debug;
import Utils.Geometry;
import Utils.Pair;
import Utils.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

public class SpanningTreeKruskalAlgo extends Algorithm {

  public SpanningTreeKruskalAlgo(String _name, ArrayList<String> _deps) {
    super(_name, _deps);
  }

  @Override
  public Object merge(DACNode left, DACNode right) {
    Debug.log("got to merge() in Spanning tree. wrong logic");
    return null;
  }

  @Override
  public boolean isTrivialCase(int count) {
    Debug.log("got to isTrivialCase() in Spanning tree. wrong logic");
    return true;
  }

  @Override
  public Object doTrivialCase(ArrayList<Point> points) {
    Debug.log("got to doTrivialCase() in Spanning tree. wrong logic");
    return null;
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

  public SpanningTreeKruskal calculate(DelaunayTriangulation delaunayTriangulation) {
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
    Hashtable<Point, ArrayList<Point>> spanningTree = new Hashtable<>();
    Double weight = new Double(0);
    for (Object _edge : weightedEdges) {
      Pair<Double, Pair<Point, Point>> edge = (Pair<Double, Pair<Point, Point>>) _edge;
      Point a = edge.second.first;
      Point b = edge.second.second;
      if (!dsu.findLeader(a).equals(dsu.findLeader(b))) {
        dsu.union(a, b);
        if (!spanningTree.containsKey(a)) {
          spanningTree.put(a, new ArrayList<Point>());
        }
        spanningTree.get(a).add(b);
        weight += edge.first;
      }
    }
    return new SpanningTreeKruskal(spanningTree, weight);
  }

}
