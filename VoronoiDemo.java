import java.util.Scanner;
import java.util.Arrays;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import DCEL.*;
import Utils.*;

public class VoronoiDemo extends JPanel{
  private static VoronoiDiagram voronoi;
  private static Point[] points;
  private static final boolean DEBUG = true; 
  private static final Color red = Color.red;  
  
  public void paint(Graphics g){
    Graphics2D g2 = (Graphics2D) g;
    int n = points.length;
    double xpoints[] = new double[n];
    double ypoints[] = new double[n];
    
    g2.setPaint(red);    
    for(int i = 0; i < n; i++){
      xpoints[i] = points[i].getX();
      ypoints[i] = points[i].getY();

      g2.fill(new Ellipse2D.Double(xpoints[i], ypoints[i], 5, 5));
    }
    // g.fillPolygon(xpoints, ypoints, n);
  }

  private static void load_data(String file_name){
    File graph_file = new File(file_name);
    try{   
      Scanner sc = new Scanner(graph_file);
      int n = sc.nextInt();
      points = new Point[n];
      for(int i = 0; i < n; i++){
        double x = sc.nextDouble();
        double y = sc.nextDouble();
        points[i] = new Point(x,y);
      }

      Arrays.sort(points, new PointComparator());
      
      if( DEBUG ){
        for(int i = 0; i < n; i++){
          System.out.printf("%d) (%f, %f)\n", i, points[i].getX(), points[i].getY());    
        }
      }

      sc.close();
    }catch(Exception e){
      System.out.println("IO error:" + e.toString());    
    }
  }
  
  public static void main(String []args){

    voronoi = new VoronoiDiagram(new Point(5, 5));
    
    //points = new Point[10];
    //int n = 0;
    
    
    load_data(args[0]);
    System.out.println("Done.");    


    // Display panel
    JFrame frame = new JFrame();
    frame.getContentPane().add(new VoronoiDemo());

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600,300);
    frame.setVisible(true);


  }
}
