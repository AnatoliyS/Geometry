import Utils.Point;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileWriter {
  public static void savePoints(String filename, ArrayList<Point> points) {
    String file_name;
    if (filename.isEmpty()) {
      file_name = "graph_dump.txt"; //default dump file
    } else {
      file_name = filename;
    }
    File graph_file = new File(file_name);
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
