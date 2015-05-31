import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;

import Utils.Polygon;
import Utils.Point;
import Utils.ByPassType;
import Utils.Debug;
import Utils.DrawHelper;

public class MinimumAreaPolygon implements VisualData {
  private Polygon polygon;

  public MinimumAreaPolygon(Polygon polygon) {
    this.polygon = polygon;
  }

  public MinimumAreaPolygon(ArrayList<Point> points) {
    this.polygon = new Polygon(points);
  }

  public MinimumAreaPolygon(ArrayList<Point> points, ByPassType byPassType) {
    this.polygon = new Polygon(points, byPassType);
  }

  public Polygon getPolygon() {
    return polygon;
  }

  public ArrayList<Point> getPoints() {
    return polygon.getPoints();
  }

  public boolean isEmpty() {
    return polygon.isEmpty();
  }

  // TODO: Add realisation
  public void render(Graphics2D g) {
    Debug.log("Rendering MinimumAreaPolygon...");
    ArrayList<Point> points = polygon.getPoints();
    DrawHelper.drawFilledPolygon(g, points, AlgorithmColor.MINIMUM_AREA_POLYGON.getColor());
  }

  @Override
  public String toString() {
    return polygon.toString();
  }
}