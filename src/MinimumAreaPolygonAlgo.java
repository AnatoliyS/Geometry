import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.awt.Graphics;

import Utils.Point;
import Utils.Vector;
import Utils.Polygon;
import Utils.Debug;
import Utils.Geometry;
import Utils.ByPassType;
import Utils.Exceptions.*;

public class MinimumAreaPolygonAlgo extends Algorithm {
  private final int TRIVIAL_COUNT = 3;

  public MinimumAreaPolygonAlgo(String _name, ArrayList<String> _deps) {
    super(_name, _deps);
  }

  public boolean isTrivialCase(int count) {
    return (count <= TRIVIAL_COUNT);
  }

  public Object doTrivialCase(ArrayList<Point> points) {
    int countPoints = points.size();
    ArrayList<Point> polygonPoints = new ArrayList<>(points);
    if (countPoints == 3) {
      Vector a = new Vector(points.get(0), points.get(1));
      Vector b = new Vector(points.get(1), points.get(2));
      double product = Geometry.crossProduct(a, b);
      if (product > 0.0) {
        polygonPoints = new ArrayList<>();
        polygonPoints.add(points.get(2));
        polygonPoints.add(points.get(1));
        polygonPoints.add(points.get(0));
      }
    }
    MinimumAreaPolygon minPolygon = new MinimumAreaPolygon(polygonPoints, ByPassType.CLOCKWISE);
    return minPolygon;
  }

  public Object merge(DACNode lNode, DACNode rNode) throws NoDataException {
    MinimumAreaPolygon lMinimumAreaPolygon =
            (MinimumAreaPolygon)lNode.getDataResult(AlgorithmName.MINIMUM_AREA_POLYGON);
    MinimumAreaPolygon rMinimumAreaPolygon =
            (MinimumAreaPolygon)rNode.getDataResult(AlgorithmName.MINIMUM_AREA_POLYGON);

    Debug.log("lPolygon : " + lMinimumAreaPolygon);
    Debug.log("rPolygon : " + rMinimumAreaPolygon);

    Polygon lPolygon = lMinimumAreaPolygon.getPolygon();
    Polygon rPolygon = rMinimumAreaPolygon.getPolygon();

    Polygon minPolygon1 = Geometry.getMinimalAreaIntermediatePolygonResult(lPolygon, rPolygon);
    Polygon minPolygon2 = Geometry.getMinimalAreaIntermediatePolygonResult(rPolygon, lPolygon);

    double area1 = Geometry.polygonArea(minPolygon1);
    double area2 = Geometry.polygonArea(minPolygon2);

    Polygon polygon = (area1 < area2) ? minPolygon1 : minPolygon2;

    if (!Geometry.isSimplePolygon(polygon)) {
      Debug.log("\n------------------------------------------------------------------------------------------");
      Debug.log("polygonLeft=" + lMinimumAreaPolygon);
      Debug.log("polygonRight=" + rMinimumAreaPolygon);
      Debug.log("area1=" + area1 + " minPolygon1=" + minPolygon1);
      Debug.log("area2=" + area2 + " minPolygon2=" + minPolygon2);
      Debug.log("polygon=" + polygon);
      Debug.log("------------------------------------------------------------------------------------------");
    }

    MinimumAreaPolygon minPolygon = new MinimumAreaPolygon(polygon);
    return minPolygon;
  }

  // TODO: Add rendering
  public void render(Object result, Graphics g) {
    Debug.log("Rendering MinimumAreaPolygonAlgo result...");
  }
}