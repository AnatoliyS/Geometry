import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import Utils.*;

public class MinimumAreaPolygon {
    
  private ArrayList<Point> points;

  public MinimumAreaPolygon(ArrayList<Point> pts) {
    points = new ArrayList<Point>(pts);  
  }

  public ArrayList<Point> getPoints() {
    return points;
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
