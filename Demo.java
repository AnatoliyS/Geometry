import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.io.File;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Locale;

import DCEL.*;
import Utils.*;
import Utils.Exceptions.*;

public class Demo extends JPanel {
  private static DACTree tree;
  private static ArrayList<Point> points;
  private static final int window_width = 700;
  private static final int window_height = 500;

  /**
   * Hardcode for building DACTree instance.
   * In future tree will be construct according to checkbox values.
   * ArrayList of points must be not null!
   */
  private static void hardcodeForConstructionTree()
          throws AlgorithmDependenciesException, UnknownAlgorithmException{
    Debug.log("hardcodeForConstructionTree started.");
    // DACTree
    ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();

    // Add ConvexHullAlgo
    ArrayList<String> chDependencies =
            new ArrayList<String>(Arrays.asList(new String[] {}));
    ConvexHullAlgo chAlgo = new ConvexHullAlgo(AlgorithmName.CONVEX_HULL, chDependencies);
    algorithms.add(chAlgo);

    // Add MinimumAreaPolygonAlgo
    ArrayList<String> mapDependencies =
            new ArrayList<String>(Arrays.asList(new String[] {}));
    MinimumAreaPolygonAlgo mapAlgo =
            new MinimumAreaPolygonAlgo(AlgorithmName.MINIMUM_AREA_POLYGON, mapDependencies);
    algorithms.add(mapAlgo);

    // TODO: Add another algorithms here

    // Create DAC tree instance
    tree = new DACTree(points, algorithms);

    // Out tree
    Debug.log(tree.toString());

    Debug.log("hardcodeForConstructionTree finished.");
  }

  @Override  
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING, 
      RenderingHints.VALUE_ANTIALIAS_ON
    );
    AffineTransform oldAT = g2.getTransform();

    // 1. Flip Y axis and translate coords to go from
    // screen coords system to cartesian.
    // Now we draw in cartesian system
    g2.setTransform(DrawHelper.invertYAxisAffineTransform());

    // Draw coordinate axes
    DrawHelper.drawCoordinateAxes(g2);

    /**
     * Draw algorithms results according to checkbox values
     */
    try {
      // TODO: Draw VisualData according to checkbox values

      // Draw convex hull
      ConvexHull convHull =
              (ConvexHull)tree.getAlgorithmResult(AlgorithmName.CONVEX_HULL);
      convHull.render(g2);

      // Draw MinimumAreaPolygon
      MinimumAreaPolygon polygon =
              (MinimumAreaPolygon)tree.getAlgorithmResult(AlgorithmName.MINIMUM_AREA_POLYGON);
      polygon.render(g2);
    } catch(NoDataException e) {
      Debug.log(e.getMessage());
    }
    
    // 2. Flip back to screen cords system to draw text 
    // Now we draw in Screen-coordinate system
    g2.setTransform(oldAT);

    // Draw input points and labels 
    DrawHelper.drawPoints(g2, points, Color.black, true);
    g2.dispose();
  }

  /**
   * Loads points from file into points[]
   * And do some preprocessing
   */
  private static void load_and_preprocess_data(String file_name) {
    File points_file = new File(file_name);
    try {   
      Scanner sc = new Scanner(points_file);
      sc.useLocale(Locale.ENGLISH);

      // Read points
      int n = sc.nextInt();
      points = new ArrayList<Point>();
      for(int i = 0; i < n; i++) {
        double x = sc.nextDouble();
        double y = sc.nextDouble();
        points.add(new Point(x,y));
      }
 
      // Sort points with custom comparator
      Collections.sort(points, new PointComparator());

      sc.close();
    } catch(Exception e) {
      System.out.println("IO error:" + e.toString());    
    }
  }

  public static void doSomething()
          throws NoDataException, AlgorithmDependenciesException, UnknownAlgorithmException {
    Debug.log("Something strange started.");
    hardcodeForConstructionTree();

    // Process ConvexHullAlgo
    tree.processAlgorithm(AlgorithmName.CONVEX_HULL);

    // Process MinimumAreaPolygon
    tree.processAlgorithm(AlgorithmName.MINIMUM_AREA_POLYGON);

    Debug.log(tree.toString());
    Debug.log("Something strange finished.");
  }

  private static void doGeometry()
          throws NoIntersectionException {
    // Check intersect
    Point A = new Point(0, 0);
    Point B = new Point(0.1, 0.1);
    Point C = new Point(0, 1);
    Point D = new Point(1, 0);

    // Lines' intersection
    Line AB = new Line(A, B);
    Line CD = new Line(C, D);

    Point P = Geometry.intersect(AB, CD);
    Debug.log("Result of lines' intersection " + AB + " " + CD + " is " + P);

    /*
    // Segments' intersection
    Segment segmAB = new Segment(A, B);
    Segment segmCD = new Segment(C, D);

    Point segmP = Geometry.intersect(segmAB, segmCD);
    Debug.log("Result of segments' intersection " + segmAB + " " + segmCD + " is " + segmP);

    // Check onSegment
    Segment segm = new Segment(A, B);
    Point X = new Point(0.5, 0.5);
    Point Y = new Point(5, 5);
    Point Z = new Point(0.5, 0.25);

    boolean fOnSegmentX = segm.onSegment(X);
    boolean fOnSegmentY = segm.onSegment(Y);
    boolean fOnSegmentZ = segm.onSegment(Z);

    Debug.log("Result of onSegment for segment=" + segm + " and point=" + X + " is " + fOnSegmentX);
    Debug.log("Result of onSegment for segment=" + segm + " and point=" + Y + " is " + fOnSegmentY);
    Debug.log("Result of onSegment for segment=" + segm + " and point=" + Z + " is " + fOnSegmentZ);
    */
  }

  private static void doGetIntersection() {
    // Check intersect
    Point A = new Point(0, 0);
    Point B = new Point(0.1, 0.1);
    Point C = new Point(0, 1);
    Point D = new Point(1, 0);
    Point E = new Point(1, 1);
    Point F = new Point(1, 2);

    // Lines' intersection
    Line AB = new Line(A, B);
    Line CD = new Line(C, D);

    Line AE = new Line(A, E);
    Line CF = new Line(C, F);

    // NO_INTERSECTION
    IntersectionResult interResult_No = Geometry.getIntersection(AB, CF);
    Debug.log("Result of lines' intersection " + AB + " " + CF + " is " + interResult_No.toString());

    // POINT_INTERSECTION
    IntersectionResult interResult_Point = Geometry.getIntersection(AB, CD);
    Debug.log("Result of lines' intersection " + AB + " " + CD + " is " + interResult_Point.toString());

    // INFINITY_INTERSECTION
    IntersectionResult interResult_Infinity = Geometry.getIntersection(AB, AE);
    Debug.log("Result of lines' intersection " + AB + " " + AE + " is " + interResult_Infinity.toString());

    // Check onSegment
    Segment segm = new Segment(A, E);
    Point X = new Point(0.5, 0.5);
    Point Y = new Point(5, 5);
    Point Z = new Point(0.5, 0.25);

    boolean fOnSegmentX = segm.onSegment(X);
    boolean fOnSegmentY = segm.onSegment(Y);
    boolean fOnSegmentZ = segm.onSegment(Z);

    Debug.log("Result of onSegment for segment=" + segm + " and point=" + X + " is " + fOnSegmentX);
    Debug.log("Result of onSegment for segment=" + segm + " and point=" + Y + " is " + fOnSegmentY);
    Debug.log("Result of onSegment for segment=" + segm + " and point=" + Z + " is " + fOnSegmentZ);

    // Segments' intersection

    // NO_INTERSECTION
    Segment segmA = new Segment(new Point(4, 4), new Point(3, 3));
    Segment segmB = new Segment(new Point(2, 2), new Point(1, 1));
    IntersectionResult inter_AB = Geometry.getIntersection(segmA, segmB);
    Debug.log("Result of segments' intersection " + segmA + " " + segmB + " is " + inter_AB);

    // POINT_INTERSECTION
    segmA = new Segment(new Point(4, 4), new Point(2, 2));
    segmB = new Segment(new Point(2, 2), new Point(1, 1));
    inter_AB = Geometry.getIntersection(segmA, segmB);
    Debug.log("Result of segments' intersection " + segmA + " " + segmB + " is " + inter_AB);

    // INFINITY_INTERSECTION
    segmA = new Segment(new Point(4, 4), new Point(2, 2));
    segmB = new Segment(new Point(3, 3), new Point(1, 1));
    inter_AB = Geometry.getIntersection(segmA, segmB);
    Debug.log("Result of segments' intersection " + segmA + " " + segmB + " is " + inter_AB);
  }

  private static void doMinimumAreaPolygonAlgorithm()
          throws NoDataException, AlgorithmDependenciesException, UnknownAlgorithmException {
    Debug.log("doMinimumAreaPolygonAlgorithm started.");
    ArrayList<Algorithm> listAlgo = new ArrayList<Algorithm>();

    // Dependecies for ConvexHullAlgo
    ArrayList<String> mapAlgoDeps = new ArrayList<String>(Arrays.asList(new String[] {}));
    MinimumAreaPolygonAlgo mapAlgo = new MinimumAreaPolygonAlgo(AlgorithmName.MINIMUM_AREA_POLYGON, mapAlgoDeps);
    listAlgo.add(mapAlgo);

    ArrayList<Point> points = new ArrayList<Point>();
    points.add(new Point(1, 1));
    points.add(new Point(2, 2));
    points.add(new Point(3, 3));
    points.add(new Point(4, 1));
    points.add(new Point(5, 2));
    points.add(new Point(6, 3));

    DACTree tree = new DACTree(points, listAlgo);
    //Debug.log(tree.toString());

    tree.processAlgorithm(AlgorithmName.MINIMUM_AREA_POLYGON);
    Debug.log(tree.toString());

    Debug.log("doMinimumAreaPolygonAlgorithm finished.");
  }

  public static void main(String []args) {
    try {
      load_and_preprocess_data(args[0]);

      hardcodeForConstructionTree();
      // Some function for adding DAC tree
      doSomething();

      // Check some geometry function
      //doGeometry();

      // Check get instersection function
      doGetIntersection();

      // Test function for checking MinimumAreaPolygonAlgo
      //ArrayList<Point> pts = new ArrayList<Point>(Arrays.asList(points));
      //doMinimumAreaPolygonAlgorithm();

      // Display graphics
      JFrame frame = new JFrame();
      frame.getContentPane().add(new Demo());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(window_width, window_height);
      frame.setVisible(true);
    } catch (Exception ex) {
    //catch (NoDataException | AlgorithmDependenciesException | UnknownAlgorithmException | NoIntersectionException ex) {
      //Debug.log(ex.getMessage() + ": " + ex.getMessage());
      ex.printStackTrace();
    }

  }

}
