import Utils.Debug;
import Utils.Exceptions.AlgorithmRuntimeException;
import Utils.Exceptions.NoDataException;
import Utils.Geometry;
import Utils.Pair;
import Utils.Point;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

public class SpanningTreeKruskal implements VisualData{
  private Hashtable<Point, ArrayList<Point>> spanningTree;
  private Double weight;

  public SpanningTreeKruskal() {
    spanningTree = new Hashtable<>();
    weight = 0.0;
  }

  public SpanningTreeKruskal(Hashtable<Point, ArrayList<Point>> spanningTree, Double weight) {
    this.spanningTree = spanningTree;
    this.weight = weight;
  }

  Double getWeight() {
    return weight;
  }

  @Override
  public void render(Graphics2D g) {
    Debug.log("Rendering Delaunay triangulation...");
    g.setColor(AlgorithmColor.MINIMUM_SPANNING_TREE.getColor());
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
