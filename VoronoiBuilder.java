import java.util.Arrays;
import java.util.ArrayList;
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
  private VoronoiDiagram result;

  public VoronoiBuilder(Point[] pts){
    points = pts;
    n = points.length;
    Arrays.sort(points, new PointComparator());
    result = buildHelper(points, 0, n-1);
  
  }

  public VoronoiDiagram getVoronoi(){
    return result;
  }

  public VoronoiDiagram buildHelper(Point[] pts, int l, int r){
    Debug.log("Build voronoi for l = "+ l + " r = "+ r);
    if(l == r){
      VoronoiDiagram v = new VoronoiDiagram(pts[l]);
      return v;
    }else{
      int m = (l+r) / 2;
      VoronoiDiagram vleft = buildHelper(pts, l, m);
      VoronoiDiagram vright = buildHelper(pts, m+1, r);
      VoronoiDiagram v = merge(vleft, vright);
      Debug.log("Merge done.");
      return v;
    }
  }
  
  public VoronoiDiagram merge(VoronoiDiagram left_diagram, VoronoiDiagram right_diagram) {
    Debug.log("Merging");  
    Debug.log_points("A", left_diagram.getConvexHull());
    Debug.log_points("B", right_diagram.getConvexHull());
    // Step 1. Get support lines from convex hulls
    Pair<Integer, Integer> lcs = 
      lowest_common_support(left_diagram.getConvexHull(), right_diagram.getConvexHull());  
    Pair<Integer, Integer> ucs = 
      uppest_common_support(left_diagram.getConvexHull(), right_diagram.getConvexHull());
     
    Point[] C = merge_convex_hulls(left_diagram.getConvexHull(), right_diagram.getConvexHull(), lcs, ucs );
    VoronoiDiagram v = new VoronoiDiagram(new Point(0,0));
    v.setConvexHull(C);
    return v;
  }

  public Point[] merge_convex_hulls(Point[] A, Point[] B, Pair<Integer, Integer> lcs, Pair<Integer, Integer> ucs){
    Debug.log("Merging CH....");
    ArrayList<Point> list = new ArrayList<Point>();
    int start = lcs.first; Debug.log("start = "+start);
    int end = ucs.first; Debug.log("end = "+end);
    while(start != end){
      list.add(A[start]);
      start = (start + 1) % A.length;
    }
    list.add(A[end]);

    start = ucs.second;
    end = lcs.second;
    while(start != end){
      list.add(B[start]);
      start = (start + 1) % B.length;
    }
    list.add(B[end]);
    Point[] res = new Point[list.size()];
    res = list.toArray(res);
    Debug.log_points("Merging result", res);
    return res;
  }

  public Pair<Integer,Integer> lowest_common_support(Point[] A, Point[] B){
    int left = 0;
    int right = 0;
    int A_size = A.length;
    int B_size = B.length;
    for(int i = 0; i < A_size; i++){
      if (A[i].getX() > A[left].getX()){
        left = i;
      }
    }
    for(int i = 0; i < B_size; i++){
      if (B[i].getX() < B[right].getX()){
        right = i;
      }
    }
    Debug.log("Start with lcs = "+left + ", "+right);
    Line lcs_line = new Line(A[left], B[right]);
    while(true){
      if(lcs_line.pointIsBelowLine( A[(left + 1) % A_size] ) ){
        left = (left+1) % A_size; 
        lcs_line = new Line(A[left], B[right]);
        continue;
      }
      if(lcs_line.pointIsBelowLine( B[(right - 1 + B_size) % B_size] ) ){
        right = (right - 1 + B_size) % B_size;
        lcs_line = new Line(A[left], B[right]);
        continue;
      } 
      break; 
    }
    
    return new Pair<Integer,Integer>(left, right);
  }
  
  public Pair<Integer,Integer> uppest_common_support(Point[] A, Point[] B){
    int left = 0;
    int right = 0;
    int A_size = A.length;
    int B_size = B.length;
    for(int i = 0; i < A_size; i++){
      if (A[i].getX() > A[left].getX()){
        left = i;
      }
    }
    for(int i = 0; i < B_size; i++){
      if (B[i].getX() < B[right].getX()){
        right = i;
      }
    }
    Line ucs_line = new Line(A[left], B[right]);
    while(true){
      if(ucs_line.pointIsAboveLine( A[(left - 1 + A_size) % A_size] ) ){
        left = (left-1 + A_size) % A_size;
        ucs_line = new Line(A[left], B[right]);
        continue;
      } 
      if(ucs_line.pointIsAboveLine( B[(right + 1 + B_size) % B_size] ) ){
        right = (right + 1 + B_size) % B_size;
        ucs_line = new Line(A[left], B[right]);
        continue;
      } 
      break; 
    }
    return new Pair<Integer,Integer>(left, right);
  }

}
