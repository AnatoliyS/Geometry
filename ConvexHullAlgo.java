import java.io.*;
import java.lang.Override;
import java.lang.String;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import Utils.*;

public class ConvexHullAlgo extends Algorithm {

  public ConvexHullAlgo(String _name, ArrayList<String> _deps) {
    super(_name, _deps);
  }

  //TODO: Add merging  
  public Object merge(Object l, Object r) {
    //Debug.log("Merging ConvexHull for type=" + left.getClass());
    // Dummy realisation
    ConvexHull left  = (ConvexHull)l;
    ConvexHull right = (ConvexHull)r;    

    ArrayList<Point> lpoints = left.getPoints();
    ArrayList<Point> rpoints = right.getPoints();

    ArrayList<Point> points = new ArrayList<Point>();
    for(Point p: lpoints)
      points.add(p);
    for(Point p: rpoints)
      points.add(p);
    
    ConvexHull result = new ConvexHull(points);
    return result;
  }

  //TODO: Add rendering  
  public void render(Object result, Graphics g) {
    Debug.log("Rendering ConvexHull result...");
  }

}
