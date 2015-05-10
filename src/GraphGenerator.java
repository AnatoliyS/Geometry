import java.io.*;
import java.util.ArrayList;
import java.io.File;
import Utils.*;

public class GraphGenerator {
  public static void main(String []args) {
    String file_name = args[0];
    int n = Integer.parseInt(args[1]);
    File graph_file = new File(file_name);
    ArrayList<Double> x_cords = new ArrayList<Double>();
    ArrayList<Point> points = new ArrayList<Point>();
    try {   
      PrintWriter writer = new PrintWriter(graph_file, "UTF-8");
      writer.println(n);
      for (int i = 0; i < n; i++) {
        while (true) {
          double x = Math.round(Math.random()*600);
          double y = Math.round(Math.random()*300);
          if (!x_cords.contains(x)) {
            points.add(new Point(x,y));
            x_cords.add(x);
            break;
          }
        }
      }   
      for (int i = 0; i < n; i++) {
        writer.println(points.get(i).getX() + " " + points.get(i).getY());
      }
      writer.close(); 
    } catch(Exception e) {
      System.out.println("IO error");    
    }
  }
}
