import java.util.ArrayList;

import Utils.*;

public class GraphGenerator {
  public static void main(String []args) {
    String file_name = args[0];
    int n = Integer.parseInt(args[1]);
    ArrayList<Point> points = generateData(n);
    FileWriter.savePoints(file_name, points);
  }

  /**
   * Randomly generates input set of points
   */
  public static ArrayList<Point> generateData (int numberOfPoints) {
    ArrayList<Point> points = new ArrayList<>();
    ArrayList<Double> x_cords = new ArrayList<Double>();
    for (int i = 0; i < numberOfPoints; i++) {
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
    return points;
  }
}