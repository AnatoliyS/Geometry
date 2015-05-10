import java.io.File;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

public class MinimalAreaPolygonAlgoChecker {

  private static void writePointFromFile(String fileName, PrintWriter writer) {
    File f = new File(fileName);
    try {
      Scanner sc = new Scanner(f);
      sc.useLocale(Locale.ENGLISH);

      String line;
      while (sc.hasNextLine()) {
        line = sc.nextLine() + "\n";
        writer.write(line);
      }
      sc.close();
    } catch(Exception e) {
      System.out.println("IO error:" + e.toString());
    }
  }

  public static void main(String []args) {
    String generatorFileName = args[0];
    String generatorCountPointsString = args[1];
    GraphGenerator generator = new GraphGenerator();
    String[] argsGenerator = new String[]{generatorFileName, generatorCountPointsString};

    String checkStatisticsFileName = args[2];
    File file = new File(checkStatisticsFileName);
    int tests = Integer.parseInt(args[3]);

    double maxMistakeCoef = 1.0;
    double sumCoef = 0.0;
    try {
      PrintWriter writer = new PrintWriter(file, "UTF-8");
      writer.write("CountPoints=" + generatorCountPointsString);
      writer.write("\nCountTests=" + tests + "\n");

      for (int i = 0; i < tests; i++) {
        generator.main(argsGenerator);
        try {
          //Demo demo = new Demo();
          Demo.loadAndPreprocessData(generatorFileName);
          System.out.println(i);
          double coef = Demo.getMinimumAreaPolygonAlgoMistakeCoefficient();
          sumCoef += coef;

          writer.write("\ni=" + i + "\tMistakeCoefficient=" + coef + "\n");
          // Write points
          writePointFromFile(generatorFileName, writer);
          writer.flush();

          if (maxMistakeCoef < coef) {
            maxMistakeCoef = coef;
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
      double middleMistakeCoef = sumCoef / tests;
      writer.write("\n\nMiddleMistakeCoefficient=" + middleMistakeCoef);
      writer.write("\nMaximalMistakeCoefficient=" + maxMistakeCoef + "\n");
      writer.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }
}
