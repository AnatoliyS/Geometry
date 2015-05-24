import java.awt.*;
import java.util.*;
import java.io.File;
import javax.swing.*;

import Utils.*;
import Utils.Exceptions.*;
import Utils.Point;
import Utils.Polygon;

public class Demo implements MenuHandler{
  private static DACTree tree;
  private static ArrayList<Point> points;
  private static final int window_width = 1200;
  private static final int window_height = 700;
  private static Menu menu = new Menu();
  private static AlgorithmPanel algorithmPanel = new AlgorithmPanel();
  private static JFrame frame = new JFrame();

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

    // Add SpanningTree
    ArrayList<String> spanningDependencies =
        new ArrayList<>(Arrays.asList(new String[] {AlgorithmName.DELAUNAY_TRIANGULATION}));
    SpanningTreeKruskalAlgo spanningTreeKruskal =
        new SpanningTreeKruskalAlgo(AlgorithmName.SPANNING_TREE, spanningDependencies);
    algorithms.add(spanningTreeKruskal);

    // TODO: Add another algorithms here

    // Create DAC tree instance
    tree = new DACTree(points, algorithms);

    // Out DAC tree
    Debug.log(tree.toString());

    Debug.log("hardcodeForConstructionTree finished.");
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

    // Process MinimumSpanningTree
    tree.processAlgorithm(AlgorithmName.SPANNING_TREE);

    //Debug.log(tree.toString());
    Debug.log("Something strange finished.");
  }

  private void validateInput(MenuConfiguration menuConfiguration) {
    if (!menuConfiguration.isCorrectInputDisplayed) {
      try {
        if (menuConfiguration.isInputStringAFile) {
          points = FileReader.loadData(menuConfiguration.inputFile);
        } else {
          if (menuConfiguration.inputString.isEmpty()) {
            menuConfiguration.inputString = "25"; // if nothing is set, just draw something
          }
          points = GraphGenerator.generateData(new Integer(menuConfiguration.inputString));
        }

        // Sort points with custom comparator
        Collections.sort(points, new PointComparator());

        // Construct DAC tree
        hardcodeForConstructionTree();
      } catch (AlgorithmDependenciesException |
          UnknownAlgorithmException ex) {
        ex.printStackTrace();
        System.exit(1);
      }
    }
  }

  private void savePointsIfNeeded(boolean shouldSavePoints, String file) {
    if (shouldSavePoints) {
      if (file.isEmpty()) {
        file = "graph_default.txt";
      }
      FileWriter.savePoints(file, points);
    }
  }

  @Override
  public void onMenuChange() {
    validateInput(menu.getMenuConfiguration());
    savePointsIfNeeded(menu.getMenuConfiguration().shouldSavePoints, menu.getMenuConfiguration().inputFile);
    ArrayList<String> algorithmsNames = menu.getAlgorithmsToBeDrawn();
    ArrayList<VisualData> algorithms = new ArrayList<>();
    try {
      for(String algorithmName :  algorithmsNames) {
        switch (algorithmName) {
          // Convex Hull
          case AlgorithmName.CONVEX_HULL : {
            tree.processAlgorithm(AlgorithmName.CONVEX_HULL);
            ConvexHull convHull =
                (ConvexHull)tree.getAlgorithmResult(AlgorithmName.CONVEX_HULL);
            algorithms.add(convHull);
            break;
          }
          // MinimumAreaPolygon
          case AlgorithmName.MINIMUM_AREA_POLYGON : {
            tree.processAlgorithm(AlgorithmName.MINIMUM_AREA_POLYGON);
            MinimumAreaPolygon polygon =
                (MinimumAreaPolygon)tree.getAlgorithmResult(AlgorithmName.MINIMUM_AREA_POLYGON);
            algorithms.add(polygon);
            break;
          }
          // Voronoi Diagram
          case AlgorithmName.VORONOI_DIAGRAM : {
            tree.processAlgorithm(AlgorithmName.VORONOI_DIAGRAM);
            VoronoiDiagram voronoi = (VoronoiDiagram)tree.getAlgorithmResult(AlgorithmName.VORONOI_DIAGRAM);
            algorithms.add(voronoi);
            break;
          }
          //All Nearest Neighbours
          case AlgorithmName.ALL_NEAREST_NEIGHBOURS : {
            tree.processAlgorithm(AlgorithmName.ALL_NEAREST_NEIGHBOURS);
            AllNearestNeighbours allNearestNeighbours =
                (AllNearestNeighbours)tree.getAlgorithmResult(AlgorithmName.ALL_NEAREST_NEIGHBOURS);
            algorithms.add(allNearestNeighbours);
            break;
          }
          //Draw Delaunay Triangulation
          case AlgorithmName.DELAUNAY_TRIANGULATION : {
            tree.processAlgorithm(AlgorithmName.DELAUNAY_TRIANGULATION);
            DelaunayTriangulation delaunayTriangulation =
                (DelaunayTriangulation)tree.getAlgorithmResult(AlgorithmName.DELAUNAY_TRIANGULATION);
            algorithms.add(delaunayTriangulation);
            break;
          }
          //Draw Spanning Tree
          case AlgorithmName.SPANNING_TREE : {
            tree.processAlgorithm(AlgorithmName.SPANNING_TREE);
            SpanningTreeKruskal spanningTreeKruskal =
                (SpanningTreeKruskal)tree.getAlgorithmResult(AlgorithmName.SPANNING_TREE);
            algorithms.add(spanningTreeKruskal);
            break;
          }
          default :
            break;
        }
      }
    } catch (NoDataException |
        UnknownAlgorithmException | AlgorithmRuntimeException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    algorithmPanel.setPointsToBeDrawn(points);
    algorithmPanel.setObjectsToBeDrawn(algorithms);
    frame.repaint();
  }

  public static void main(String []args) {
    try {
      loadAndPreprocessData(args[0]);

      // Construct DAC tree
      hardcodeForConstructionTree();

      // Some function for checking algorithms
      doSomething();

      // Display graphics
      menu.attachToHandler(new Demo());
      //frame.getContentPane().add(menu);
      frame.getContentPane().add(menu.menuPanel, BorderLayout.EAST);
      frame.getContentPane().add(menu.menuPanel_1, BorderLayout.SOUTH);
      frame.getContentPane().add(algorithmPanel);
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
