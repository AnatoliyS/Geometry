import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import Utils.*;

public class ConvexHull implements VisualData {
    
  private ArrayList<Point> points;

  public ConvexHull(ArrayList<Point> pts) {
    points = new ArrayList<Point>(pts);
  }

  public ArrayList<Point> getPoints() {
    return points;
  }

  public void render(Graphics2D g) {
    Debug.log("rendering convex hull...");

    DrawHelper.drawFilledPolygon(g, points, AlgorithmColor.CONVEX_HULL.getColor());
  }

  @Override
  public String toString() {
    String s = "";
    for(Point p: points)
      s += p.toString() + ", ";
    s = "{" + s + "}";
    return s;  
  }

}
