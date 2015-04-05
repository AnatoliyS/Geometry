import java.io.*;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import Utils.*;
import Utils.Exceptions.*;

public class ConvexHullAlgo extends Algorithm {

  private final int TRIVIAL_COUNT = 3;

  public ConvexHullAlgo(String _name, ArrayList<String> _deps) {
    super(_name, _deps);
  }

  // TODO: Add merging
  public Object merge(DACNode l, DACNode r) {
    //Debug.log("Merging ConvexHull for type=" + left.getClass());
    /*
    // Dummy realisation
    String name = getName();
    ConvexHull left  = (ConvexHull)l.getDataResult(name);
    ConvexHull right = (ConvexHull)r.getDataResult(name);

    ArrayList<Point> lpoints = left.getPoints();
    ArrayList<Point> rpoints = right.getPoints();

    ArrayList<Point> points = new ArrayList<Point>();
    for(Point p: lpoints)
      points.add(p);
    for(Point p: rpoints)
      points.add(p);
    
    ConvexHull result = new ConvexHull(points);
    return result;
    */
    return null;
  }

  // TODO: Add rendering
  public void render(Object result, Graphics g) {
    Debug.log("Rendering ConvexHull result...");
  }
  // TODO: Add realisation
  public boolean isTrivialCase(int count) {
    return (count <= TRIVIAL_COUNT);
  }

  // TODO: Add realisation
  public Object doTrivialCase(ArrayList<Point> points) {
    return new ConvexHull(points);
  }


}
