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
  private static VoronoiDiagram v1;
  private static VoronoiDiagram v2;
  private static VoronoiDiagram v;
  private static ConvexHull c1;
  private static ConvexHull c2;
  private static ConvexHull c;

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
    
    c1.render(g2);
    c2.render(g2);
    v.render(g2);
    //v1.render(g2);
    //v2.render(g2);
    
    /*try {
      // Draw convex hull
    //  ConvexHull c = (ConvexHull)tree.getAlgorithmResult(AlgorithmName.CONVEX_HULL);
      //c.render(g2);
    } catch(NoDataException e) {
      Debug.log(e.getMessage());
    }*/
    
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

  public static void doSomething() throws NoDataException, AlgorithmDependenciesException, UnknownAlgorithmException {
    Debug.log("Something strange started.");
    // Dependecies for ConvexHullAlgo
    ArrayList<Algorithm> listAlgo = new ArrayList<Algorithm>();

    ArrayList<String> algoDeps1 = new ArrayList<String>(Arrays.asList(new String[] {}));
    ConvexHullAlgo algo1 = new ConvexHullAlgo("Algo1", algoDeps1);
    listAlgo.add(algo1);

    ArrayList<String> algoDeps2 = new ArrayList<String>(Arrays.asList(new String[] {}));
    ConvexHullAlgo algo2 = new ConvexHullAlgo("Algo2", algoDeps2);
    listAlgo.add(algo2);

    ArrayList<String> algoDeps3 = new ArrayList<String>(Arrays.asList(new String[] {}));
    ConvexHullAlgo algo3 = new ConvexHullAlgo("Algo3", algoDeps3);
    listAlgo.add(algo3);

    ArrayList<String> chDeps1 = new ArrayList<String>(Arrays.asList(new String[] {"Algo1", "Algo2", "Algo3"}));
    ConvexHullAlgo cha1 = new ConvexHullAlgo(AlgorithmName.CONVEX_HULL, chDeps1);
    listAlgo.add(cha1);


    ArrayList<String> cha2Deps = new ArrayList<String>(Arrays.asList(new String[] {"Algo1", "Algo2", "Algo3"}));
    ConvexHullAlgo cha2 = new ConvexHullAlgo("222", cha2Deps);
    listAlgo.add(cha2);

    ArrayList<String> cha3Deps = new ArrayList<String>(Arrays.asList(new String[] {"Algo1", "Algo2", "Algo3"}));
    ConvexHullAlgo cha3 = new ConvexHullAlgo("333", cha3Deps);
    listAlgo.add(cha3);
    
    ArrayList<String> voronoi_deps = new ArrayList<String>(Arrays.asList(new String[] {AlgorithmName.CONVEX_HULL}));
    VoronoiDiagramAlgo voronoi_algo = new VoronoiDiagramAlgo(AlgorithmName.VORONOI_DIAGRAM, voronoi_deps);
    listAlgo.add(voronoi_algo);

    // Out list
    for (Algorithm algo : listAlgo) {
      Debug.log("Algo in list =" + algo.getName());
    }

    ArrayList<Point> lpoints = new ArrayList<Point>();

    lpoints.add(new Point(0, 50));
    lpoints.add(new Point(100, 300));
    lpoints.add(new Point(190, 100));

    ArrayList<Point> rpoints = new ArrayList<Point>();
    rpoints.add(new Point(250, 0));
    rpoints.add(new Point(300, 300));
    rpoints.add(new Point(400, 30));

    points.clear();
    points.addAll(lpoints);
    points.addAll(rpoints);

    ConvexHull chLeft = new ConvexHull(lpoints);
    ConvexHull chRight = new ConvexHull(rpoints);
    c1 = chLeft;
    c2 = chRight;

    VoronoiDiagram vLeft = new VoronoiDiagram(lpoints.get(0), lpoints.get(1), lpoints.get(2));
    VoronoiDiagram vRight = new VoronoiDiagram(rpoints.get(0), rpoints.get(1), rpoints.get(2));
    v1 = vLeft;
    v2 = vRight;

    // Creation DACTree
    
    // DACNode Left entry
    DACNode nodeLeft = new DACNode();
    nodeLeft.setDataResult(AlgorithmName.CONVEX_HULL, chLeft);
    nodeLeft.setDataResult(AlgorithmName.VORONOI_DIAGRAM, vLeft);
    //nodeLeft.outputDescription();
    
    // DACNode Right entry
    DACNode nodeRight = new DACNode();
    nodeRight.setDataResult(AlgorithmName.CONVEX_HULL, chRight);
    nodeRight.setDataResult(AlgorithmName.VORONOI_DIAGRAM, vRight);
    //nodeRight.outputDescription();

    v = (VoronoiDiagram)voronoi_algo.merge(nodeLeft, nodeRight);
    //Debug.log(v.toString());

    //v = new VoronoiDiagram(new Point(50,50), new Point(100,100));
    //v = new VoronoiDiagram(new Point(50,50), new Point(100,100), new Point(150, 0));


    AlgorithmsContainer algoContainer = new AlgorithmsContainer.AlgorithmsContainerBuilder()
            .addAlgorithm(algo1)
            .addAlgorithm(algo2)
            .addAlgorithm(algo3)
            .addAlgorithm(cha1)
            .addAlgorithm(cha2)
            .addAlgorithm(cha3)
            .buildContainer();
    Debug.log("AlgorithmsContainer=" + algoContainer);

    Algorithm chAlgo = algoContainer.getAlgorithm(AlgorithmName.CONVEX_HULL);
    Debug.log("Algorithm=" + chAlgo);

    tree = new DACTree(points, listAlgo);
    //Debug.log(tree.toString());

    //tree.processAlgorithm(AlgorithmName.CONVEX_HULL);
    //Debug.log(tree.toString());
    Debug.log("Something strange finished.");
  }

  public static void main(String []args) {
    try {
      load_and_preprocess_data(args[0]);

      // Some function for adding DAC tree
      doSomething();

      // Display graphics
      JFrame frame = new JFrame();
      frame.getContentPane().add(new Demo());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(window_width, window_height);
      frame.setVisible(true);
    } catch (NoDataException | AlgorithmDependenciesException | UnknownAlgorithmException ex) {
      //Debug.log(ex.getMessage() + ": " + ex.getMessage());
      ex.printStackTrace();
    }

  }

}
