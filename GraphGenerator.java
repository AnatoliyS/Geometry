import java.io.*;
import java.io.File;

public class GraphGenerator {
  public static void main(String []args) {
    String file_name = args[0];
    int n = Integer.parseInt(args[1]);
    File graph_file = new File(file_name);
    try {   
      PrintWriter writer = new PrintWriter(graph_file, "UTF-8");
      writer.println(n);
      for (int i = 0; i < n; i++) {
        double x = Math.round(Math.random()*600);  
        double y = Math.round(Math.random()*300);  
        writer.println(x + " " + y);
      }   
     writer.close(); 
    } catch(Exception e) {
      System.out.println("IO error");    
    }
  }
}
