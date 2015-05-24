import Utils.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

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
      for(int i = 0; i < n; i++) {
        double x = sc.nextDouble();
        double y = sc.nextDouble();
        points.add(new Point(x, y));
      }
      sc.close();
    } catch(Exception e) {
      System.out.println("FileReader :: IO error:" + e.toString());
    }
    return points;
  }
}
