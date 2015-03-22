import java.util.Arrays;
import Utils.*;
import DCEL.*;

public class VoronoiBuilder{
  private double minX = 0;
  private double maxX = 100;
  private double minY = 0;
  private double maxY = 100;
  private int n;
  private Point[] points;
  private double[] xValues;
  private double[] yValues;

  public VoronoiBuilder(Point[] pts){
    points = pts;
    n = points.Length;
    Arrays.sort(points, new PointComparator());
    VoronoiDiagram v = buildHelper(points, 0, n-1);
  
  }

  public VoronoiDiagram buildHelper(Point[] pts, int l, int r){
    if(l == r){
      VoronoiDiagram v = new VoronoiDiagram(pts[0]);
      return v;
    }else{
      int m = (l+r) / 2;
      VoronoiDiagram vleft = new VoronoiDiagram(pts, l, m);
      VoronoiDiagram vright = new VoronoiDiagram(pts, m+1, r);
      VoronoiDiagram v = merge(vleft, vright);
    }
  }
  
  public merge(VoronoiDiagram left_diagram, VoronoiDiagram right_diagram) {
      VoronoiDiagram v = new VoronoiDiagram(points[0]);
      return v;
  }

}
