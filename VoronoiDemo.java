import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.FontMetrics;
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
  private static final int scene_inf_x = 600;
  private static final int scene_inf_y = 300;

  @Override  
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    g2.translate(60, 60);
    
    g2.setPaint(Color.gray);
    g2.draw(new Line2D.Double(0, -scene_inf_y, 0, 2*scene_inf_y));
    g2.draw(new Line2D.Double(-scene_inf_x, scene_inf_y, 2*scene_inf_x, scene_inf_y));
    
    int n = points.length;
    double xpoints[] = new double[n];
    double ypoints[] = new double[n];

    Path2D path = new Path2D.Double();

    Point[] c = voronoi.getConvexHull();
    path.moveTo(c[0].getX(), -c[0].getY() + 300);
    for(int i = 1; i < c.length; i++){
      path.lineTo(c[i].getX(), -c[i].getY() + 300);
    }
    path.closePath();
    g2.setPaint(Color.gray);    
    g2.fill(path);

    FontMetrics fm = g2.getFontMetrics();

    g2.setPaint(Color.black);    
    for(int i = 0; i < n; i++){
      xpoints[i] = points[i].getX();
      ypoints[i] = -points[i].getY() + 300;
        
      String text = points[i].toString();
      g2.drawString(text,(float)( xpoints[i] - fm.stringWidth(text)/2.0), (float)(ypoints[i] - fm.getHeight()) );
      g2.fill(new Ellipse2D.Double(xpoints[i], ypoints[i], 5, 5));
    }
    // g.fillPolygon(xpoints, ypoints, n);
    g2.dispose();
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

    
    //points = new Point[10];
    //int n = 0;
    
    
    load_data(args[0]);
    voronoi = (new VoronoiBuilder(points)).getVoronoi();
    //System.out.println("Precision: " + Constants.EPS);
    System.out.println("Done.");    


    // Display panel
    JFrame frame = new JFrame();
    frame.getContentPane().add(new VoronoiDemo());

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(700,400);
    frame.setVisible(true);


  }
}
