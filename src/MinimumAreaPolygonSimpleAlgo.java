import Utils.*;
import Utils.Exceptions.NoDataException;
import Utils.Point;
import Utils.Polygon;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MinimumAreaPolygonSimpleAlgo {

  public MinimumAreaPolygonSimpleAlgo() {
  }

  public Polygon getMinimumAreaPolygon(ArrayList<Point> points) {
    Debug.log("\nMinimumAreaPolygonSimpleAlgo::getMinimumAreaPolygon started");
    Debug.log("Points=" + points);

    int countPoints = points.size();
    Permutation permutation = new Permutation(countPoints);

    ArrayList<Point> order = new ArrayList<>(countPoints);
    double area = Double.POSITIVE_INFINITY;
    do {
      ArrayList<Point> permPoints = permutePoints(points, permutation);
      if (Geometry.isSimplePolygon(permPoints)) {
        double permArea = Geometry.polygonArea(permPoints);
        Debug.log("\nPermPoints=" + permPoints);
        Debug.log("PermArea=" + permArea);
        if (area > permArea) {
          area = permArea;
          order = permPoints;
        }
      }
    }
    while (permutation.nextPermutation());

    Debug.log("MinimalOrder=" + order);
    Debug.log("Area=" + area);

    Polygon polygon = new Polygon(order, ByPassType.CLOCKWISE);
    Debug.log("Result polygon=" + polygon);
    Debug.log("MinimumAreaPolygonSimpleAlgo::getMinimumAreaPolygon finished");
    return polygon;
  }

  private ArrayList<Point> permutePoints(ArrayList<Point> points,
                            Permutation permutation) {
    int countPoints = points.size();
    Point[] permPoints = new Point[countPoints];
    ArrayList<Integer> order = permutation.getOrder();

    for (int i = 0; i < countPoints; i++) {
      int index = order.get(i);
      Point p = points.get(index);
      permPoints[i] = p;
    }

    ArrayList<Point> result = new ArrayList<>(countPoints);
    for (Point p : permPoints) {
      result.add(p);
    }
    return result;
  }

  // TODO: Add rendering
  public void render(Object result, Graphics g) {
    Debug.log("Rendering MinimumAreaPolygonAlgo result...");
  }
}
