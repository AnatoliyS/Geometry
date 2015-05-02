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
  private static final int window_width = 1100;
  private static final int window_height = 700;

  /**
   * Hardcode for building DACTree instance.
   * In future tree will be construct according to checkbox values.
   * ArrayList of points must be not null!
   */
  private static void hardcodeForConstructionTree()
          throws AlgorithmDependenciesException, UnknownAlgorithmException {
    Debug.log("hardcodeForConstructionTree started.");
    // DACTree
    ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();

    // Add ConvexHullAlgo
    ArrayList<String> ch_dependencies =
      new ArrayList<String>(Arrays.asList(new String[] {}));
    ConvexHullAlgo ch_algo = new ConvexHullAlgo(AlgorithmName.CONVEX_HULL, ch_dependencies);
    algorithms.add(ch_algo);

    // Add MinimumAreaPolygonAlgo
    ArrayList<String> map_dependencies =
      new ArrayList<String>(Arrays.asList(new String[] {}));
    MinimumAreaPolygonAlgo map_algo =
      new MinimumAreaPolygonAlgo(AlgorithmName.MINIMUM_AREA_POLYGON, map_dependencies);
    algorithms.add(map_algo);

    // Add VoronoiDigramAlgo 
    ArrayList<String> voronoi_dependencies = 
      new ArrayList<String>(Arrays.asList(new String[] {AlgorithmName.CONVEX_HULL}));
    VoronoiDiagramAlgo voronoi_algo = 
      new VoronoiDiagramAlgo(AlgorithmName.VORONOI_DIAGRAM, voronoi_dependencies);
    algorithms.add(voronoi_algo);
    
    // TODO: Add another algorithms here

    // Create DAC tree instance
    tree = new DACTree(points, algorithms);

    // Out DAC tree
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
      //polygon.render(g2);
     
      // Draw Voronoi Diagram 
      VoronoiDiagram voronoi = (VoronoiDiagram)tree.getAlgorithmResult(AlgorithmName.VORONOI_DIAGRAM);
      voronoi.render(g2);
    } catch(NoDataException e) {
      Debug.log(e.getMessage());
    }
    
    // 2. Flip back to screen cords system to draw text 
    // Now we draw in Screen-coordinate system
    g2.setTransform(oldAT);

    // Draw input points and labels 
    DrawHelper.drawPoints(g2, points, Color.black, false);
    g2.dispose();
  }

  /**
   * Loads points from file into points[]
   * And do some preprocessing
   */
  private static void loadAndPreprocessData(String file_name) {
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
        points.add(new Point(x, y));
      }
 
      // Sort points with custom comparator
      Collections.sort(points, new PointComparator());
     
      // TODO: MODIFY CONVEX HULL TO ACCEPT POINTS WITH SAME X
      // Modify points with same X 
      for(int i = 1; i < n; i++) {
        if (points.get(i).getX() == points.get(i-1).getX()) {
          points.get(i-1).setX(points.get(i-1).getX() - 5*Constants.EPS);
        }
      }
      Collections.sort(points, new PointComparator());

      sc.close();
    } catch(Exception e) {
      System.out.println("IO error:" + e.toString());    
    }
  }

  public static void doSomething()
          throws NoDataException, AlgorithmDependenciesException, UnknownAlgorithmException {
    Debug.log("Something strange started.");

    // Process ConvexHullAlgo
    tree.processAlgorithm(AlgorithmName.CONVEX_HULL);

    // Process MinimumAreaPolygon
    tree.processAlgorithm(AlgorithmName.MINIMUM_AREA_POLYGON);

    // Process VoronoiDiagram 
    tree.processAlgorithm(AlgorithmName.VORONOI_DIAGRAM);
    
    //Debug.log(tree.toString());
    Debug.log("Something strange finished.");
  }

  public static void main(String []args) {
    try {
      loadAndPreprocessData(args[0]);

      // Construct DAC tree
      hardcodeForConstructionTree();

      // Some function for checking algorithms
      doSomething();

      // Display graphics
      JFrame frame = new JFrame();
      frame.getContentPane().add(new Demo());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(window_width, window_height);
      frame.setVisible(true);
    } catch (NoDataException | AlgorithmDependenciesException | UnknownAlgorithmException ex) {
      ex.printStackTrace();
    }

  }

}
