import Utils.Constants;
import Utils.Point;

import java.io.File;
import java.util.*;

/**
 * Loads points from file into points[]
 */
public class FileReader {
  public static ArrayList<Point> loadData(String file_name) {
    ArrayList<Point> points = new ArrayList<>();
    File points_file = new File(file_name);
    try {
      Scanner sc = new Scanner(points_file);
      sc.useLocale(Locale.ENGLISH);

      // Read points
      int n = sc.nextInt();
      points = new ArrayList<Point>();
      HashSet<Double> x_coordinates = new HashSet<>();
      for(int i = 0; i < n; i++) {
        double x = sc.nextDouble();
        double y = sc.nextDouble();
        while (x_coordinates.contains(x)) {
          x = x + Constants.EPS;
        }
        x_coordinates.add(x);
        points.add(new Point(x, y));
      }
      sc.close();
    } catch(Exception e) {
      System.out.println("FileReader :: IO error:" + e.toString());
    }
    return points;
  }
}
