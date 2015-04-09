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
    
    try {
      // Draw convex hull
      ConvexHull c = (ConvexHull)tree.getAlgorithmResult(AlgorithmName.CONVEX_HULL);
      c.render(g2);
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

    // Out list
    for (Algorithm algo : listAlgo) {
      Debug.log("Algo in list =" + algo.getName());
    }

    /*
    ArrayList<Point> lpoints = new ArrayList<Point>();

    lpoints.add(new Point(1, 1));
    lpoints.add(new Point(2, 2));
    lpoints.add(new Point(3, 3));

    ArrayList<Point> rpoints = new ArrayList<Point>();
    rpoints.add(new Point(4, 1));
    rpoints.add(new Point(5, 2));
    rpoints.add(new Point(6, 3));


    ConvexHull chLeft = new ConvexHull(lpoints);
    ConvexHull chRight = new ConvexHull(rpoints);

    Debug.log("Points in left convex hull\t= " + chLeft);
    Debug.log("Points in right convex hull\t= " + chRight);
  
    ConvexHull ch = (ConvexHull)cha.merge(chLeft, chRight);
    Debug.log("Points in merged convex hull\t= " + ch);

    // Creation DACTree
    
    // DACNode Left entry
    DACNode nodeLeft = new DACNode();
    nodeLeft.setDataResult(AlgorithmName.CONVEX_HULL, chLeft);
    nodeLeft.outputDescription();
    
    // DACNode Right entry
    DACNode nodeRight = new DACNode();
    nodeRight.setDataResult(AlgorithmName.CONVEX_HULL, chRight);
    nodeRight.outputDescription();

    // DACNode entry
    DACNode node = new DACNode();
    node.setDataResult(AlgorithmName.CONVEX_HULL, ch);
    node.outputDescription();
    */

    VoronoiDiagram v = new VoronoiDiagram(new Point(-100,-100), new Point(100,100));


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

    tree.processAlgorithm(AlgorithmName.CONVEX_HULL);
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
