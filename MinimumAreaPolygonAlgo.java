import java.io.*;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import Utils.*;
import Utils.Segment;
import Utils.Exceptions.*;

public class MinimumAreaPolygonAlgo extends Algorithm {

  private final int TRIVIAL_COUNT = 1;

  public MinimumAreaPolygonAlgo(String _name, ArrayList<String> _deps) {
    super(_name, _deps);
  }

  // TODO: Add realisation
  public boolean isTrivialCase(int count) {
    return (count <= TRIVIAL_COUNT);
  }

  // TODO: Add realisation
  public Object doTrivialCase(ArrayList<Point> points) {
    return new MinimumAreaPolygon(points);
  }

  // TODO: Add merging
  public Object merge(DACNode lNode, DACNode rNode) throws NoDataException {
    MinimumAreaPolygon lPolygon =
            (MinimumAreaPolygon)lNode.getDataResult(AlgorithmName.MINIMUM_AREA_POLYGON);
    MinimumAreaPolygon rPolygon =
            (MinimumAreaPolygon)rNode.getDataResult(AlgorithmName.MINIMUM_AREA_POLYGON);

    Debug.log("lPolygon : " + lPolygon);
    Debug.log("rPolygon : " + rPolygon);

    ArrayList<Point> lPoints = lPolygon.getPoints();
    ArrayList<Point> rPoints = rPolygon.getPoints();

    ArrayList<Point> points = new ArrayList<Point>(lPoints);
    points.addAll(rPoints);

    MinimumAreaPolygon polygon = new MinimumAreaPolygon(points);
    boolean fSimplePolygon = isSimplePolygon(polygon);

    return polygon;
  }

  // TODO: Add rendering
  public void render(Object result, Graphics g) {
    Debug.log("Rendering MinimumAreaPolygonAlgo result...");
  }

  /**
   * Helpers for merging MinimumAreaPolygon
   */
  private boolean isSimplePolygon(MinimumAreaPolygon polygon) {
    ArrayList<Point> points = polygon.getPoints();
    ArrayList<Segment> edges = new ArrayList<Segment>();
    int countPoints = points.size();
    Point currPoint;
    Point prevPoint = points.get(countPoints - 1);
    for (int i = 0; i < countPoints; i++) {
      currPoint = points.get(i);
      Segment segm = new Segment(prevPoint, currPoint);
      edges.add(segm);

      // Checking for intersect edges with each previous edges
      for (int j = 0; j < edges.size(); j++) {
        Segment edge = edges.get(j);
        try {
          //intersect(edge, segm);
        } catch (Exception ex) {

        }
      }

      prevPoint = currPoint;
    }

    Debug.log("Edges=" + edges);

    return true;
  }

}
