import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;
import Utils.*;

public class MinimumAreaPolygon implements VisualData {
    
  private ArrayList<Point> points;

  public MinimumAreaPolygon(ArrayList<Point> pts) {
    points = new ArrayList<Point>(pts);  
  }

  public ArrayList<Point> getPoints() {
    return points;
  }

  // TODO: Add realisation
  public void render(Graphics2D g) {
    Debug.log("Rendering MinimumAreaPolygon...");
    DrawHelper.drawFilledPolygon(g, points, Color.RED);
  }

  @Override
  public String toString() {
    String s = "";
    for(Point p : points)
      s += p.toString() + ", ";
    s = "Polygon={" + s + "}";
    return s;  
  }

}
