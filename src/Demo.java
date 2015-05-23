import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.io.File;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Locale;

import Utils.*;
import Utils.Exceptions.*;

public class Demo extends JPanel {
  private static DACTree tree;
  private static ArrayList<Point> points;
  private static final int window_width = 1200;
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

    // Add AllNearestNeighboursAlgo
    ArrayList<String> ann_dependencies =
      new ArrayList<String>(Arrays.asList(new String[] {AlgorithmName.CONVEX_HULL, AlgorithmName.VORONOI_DIAGRAM}));
    AllNearestNeighboursAlgo ann_algo =
      new AllNearestNeighboursAlgo(AlgorithmName.ALL_NEAREST_NEIGHBOURS, ann_dependencies);
    algorithms.add(ann_algo);

    // Add DelaunayTriangulationAlgo
    ArrayList<String> delaunayDependencies =
        new ArrayList<>(Arrays.asList(new String[] {AlgorithmName.VORONOI_DIAGRAM}));
    DelaunayTriangulationAlgo delaunayTriangulationAlgo =
        new DelaunayTriangulationAlgo(AlgorithmName.DELAUNAY_TRIANGULATION, delaunayDependencies);
    algorithms.add(delaunayTriangulationAlgo);

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
      polygon.render(g2);
     
      // Draw Voronoi Diagram 
      VoronoiDiagram voronoi = (VoronoiDiagram)tree.getAlgorithmResult(AlgorithmName.VORONOI_DIAGRAM);
      voronoi.render(g2);

      // Draw AllNearestNeighbours
      AllNearestNeighbours allNN = (AllNearestNeighbours)tree.getAlgorithmResult(AlgorithmName.ALL_NEAREST_NEIGHBOURS);
      allNN.render(g2);

      //Draw Delaunay Triangulation
      DelaunayTriangulation delaunay =
          (DelaunayTriangulation)tree.getAlgorithmResult(AlgorithmName.DELAUNAY_TRIANGULATION);
      delaunay.render(g2);

      //Draw Spanning Tree
      SpanningTreeKruskal spanningTree =
          (SpanningTreeKruskal)tree.getAlgorithmResult(AlgorithmName.SPANNING_TREE);
      spanningTree.render(g2);

    } catch(NoDataException e) {
      Debug.log(e.getMessage());
    }
    
    // 2. Flip back to screen cords system to draw text 
    // Now we draw in Screen-coordinate system
    g2.setTransform(oldAT);

    // Draw input points and labels
    boolean labels = false;
    //boolean labels = true;
    DrawHelper.drawPoints(g2, points, Color.black, labels);
    g2.dispose();
  }

  /**
   * Loads points from file into points[]
   * And do some preprocessing
   */
  public static void loadAndPreprocessData(String file_name) {
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

      sc.close();
    } catch(Exception e) {
      System.out.println("IO error:" + e.toString());    
    }
  }

  public static void doMinimalAreaPolygonAlgo()
          throws NoDataException, AlgorithmDependenciesException,
          UnknownAlgorithmException, AlgorithmRuntimeException {
    Debug.log("Demo::doMinimalAreaPolygonAlgo started.");
    hardcodeForConstructionTree();

    // Process ConvexHullAlgo
    tree.processAlgorithm(AlgorithmName.CONVEX_HULL);

    // Process MinimumAreaPolygonAlgo
    tree.processAlgorithm(AlgorithmName.MINIMUM_AREA_POLYGON);
    Debug.log(tree.toString());

    Debug.log("Demo::doMinimalAreaPolygonAlgo finished.");
  }

  public static double getMinimumAreaPolygonAlgoMistakeCoefficient()
          throws NoDataException, AlgorithmDependenciesException,
          UnknownAlgorithmException, AlgorithmRuntimeException {
    Debug.log("\nDemo::getMinimumAreaPolygonAlgoMistakeCoefficient started");
    hardcodeForConstructionTree();

    // Process ConvexHullAlgo
    //tree.processAlgorithm(AlgorithmName.CONVEX_HULL);

    // Process MinimumAreaPolygonAlgo
    tree.processAlgorithm(AlgorithmName.MINIMUM_AREA_POLYGON);

    //Debug.log(tree.toString());

    Debug.log("\nCheck polygon");
    Polygon polygonSimple = calcPermutationPolygon();
    double areaSimple = Geometry.polygonArea(polygonSimple);
    Debug.log("areaSimple=" + areaSimple + "\nSimple=" + polygonSimple);

    MinimumAreaPolygon polygonAlgo =
            (MinimumAreaPolygon)tree.getAlgorithmResult(AlgorithmName.MINIMUM_AREA_POLYGON);
    double areaAlgo = Geometry.polygonArea(polygonAlgo.getPolygon());
    Debug.log("\nareaAlgo=" + areaAlgo + "\nAlgo=" + polygonAlgo);

    if (Math.abs(areaAlgo - areaSimple) > Constants.EPS) {
      Debug.log("DIFFERENT POLYGONS FOUND");
    }

    double mistakeCoef = areaAlgo / areaSimple;
    Debug.log("MISTAKE COEFFICIENT=" + mistakeCoef);

    //System.out.println("MISTAKE COEFFICIENT=" + mistakeCoef);

    Debug.log("\nDemo::getMinimumAreaPolygonAlgoMistakeCoefficient finished");
    return mistakeCoef;
  }

  public static Polygon calcPermutationPolygon() throws NoDataException{
    MinimumAreaPolygonSimpleAlgo algo = new MinimumAreaPolygonSimpleAlgo();
    Polygon polygon = algo.getMinimumAreaPolygon(points);
    return polygon;
  }

  public static void doSomething()
          throws NoDataException, AlgorithmDependenciesException, UnknownAlgorithmException, AlgorithmRuntimeException {
    Debug.log("Something strange started.");

    // Process ConvexHullAlgo
    tree.processAlgorithm(AlgorithmName.CONVEX_HULL);

    // Process MinimumAreaPolygon
    tree.processAlgorithm(AlgorithmName.MINIMUM_AREA_POLYGON);

    // Process VoronoiDiagram 
    tree.processAlgorithm(AlgorithmName.VORONOI_DIAGRAM);

    // Process AllNearestNeighbours
    tree.processAlgorithm(AlgorithmName.ALL_NEAREST_NEIGHBOURS);

    // Process DelaunayTriangulation
    tree.processAlgorithm(AlgorithmName.DELAUNAY_TRIANGULATION);

    // Create Minimum Spanning tree based on delaunay triangulation and cache it in the tree
    DelaunayTriangulation delaunayTriangulation =
        (DelaunayTriangulation)tree.getAlgorithmResult(AlgorithmName.DELAUNAY_TRIANGULATION);
    SpanningTreeKruskal spanningTree = new SpanningTreeKruskal();
    spanningTree.calculate(delaunayTriangulation);
    tree.addAlgorithmResultToRoot(spanningTree, AlgorithmName.SPANNING_TREE);

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
    } catch (NoDataException | AlgorithmDependenciesException |
            UnknownAlgorithmException | AlgorithmRuntimeException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

}
