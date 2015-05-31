import Utils.Point;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileWriter {
  public static final String DEFAULT_FILE_NAME = "graph_dump.txt";

  public static void savePoints(String filename, ArrayList<Point> points) {
    if (filename.isEmpty()) {
      filename = DEFAULT_FILE_NAME; //default dump file
    }
    File graph_file = new File(filename);
    try {
      PrintWriter writer = new PrintWriter(graph_file, "UTF-8");
      writer.println(points.size());
      for (int i = 0; i < points.size(); i++) {
        writer.println(points.get(i).getX() + " " + points.get(i).getY());
      }
      writer.close();
    } catch(Exception e) {
      System.out.println("FileWriter :: IO error");
    }
  }
}
