import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import Utils.*;
import Utils.Exceptions.*;

public class ConvexHullAlgo extends Algorithm {

  private final int TRIVIAL_COUNT = 1;

  public ConvexHullAlgo(String _name, ArrayList<String> _deps) {
    super(_name, _deps);
  }

  public Object merge(DACNode l, DACNode r) throws NoDataException {
    String name = getName();
    ConvexHull left  = (ConvexHull)l.getDataResult(name);
    ConvexHull right = (ConvexHull)r.getDataResult(name);

    ArrayList<Point> lpoints = left.getPoints();
    ArrayList<Point> rpoints = right.getPoints();

    // Step 1. Get support lines from convex hulls
    Pair<Integer, Integer> lcs = lowestCommonSupport(
      lpoints,
      rpoints
    );
    Pair<Integer, Integer> ucs = uppestCommonSupport(
      lpoints,
      rpoints
    );

    // Step 2. Merge
    ArrayList<Point> points = mergeConvexHulls(
      lpoints,
      rpoints,
      lcs,
      ucs
    );

    ConvexHull result = new ConvexHull(points);
    return result;
  }

  public boolean isTrivialCase(int count) {
    return (count <= TRIVIAL_COUNT);
  }

  public Object doTrivialCase(ArrayList<Point> points) {
    return new ConvexHull(points);
  }

  private ArrayList<Point> mergeConvexHulls(
    ArrayList<Point> A, 
    ArrayList<Point> B, 
    Pair<Integer, Integer> lcs, 
    Pair<Integer, Integer> ucs) {

    Debug.log("Merging CH....");
    ArrayList<Point> list = new ArrayList<Point>();
    int start = lcs.first; Debug.log("start = " + start);
    int end = ucs.first; Debug.log("end = " + end);
    while (start != end) {
      list.add(A.get(start));
      start = (start + 1) % A.size();
    }
    list.add(A.get(end));

    start = ucs.second;
    end = lcs.second;
    while (start != end) {
      list.add(B.get(start));
      start = (start + 1) % B.size();
    }
    list.add(B.get(end));
    Point[] res = new Point[list.size()];
    res = list.toArray(res);
    Debug.log_points("Merging result", res);
    return list;
  }


  public static Pair<Integer,Integer> lowestCommonSupport(ArrayList<Point> A, ArrayList<Point> B) {
    int left = 0;
    int right = 0;
    int A_size = A.size();
    int B_size = B.size();
    for (int i = 0; i < A_size; i++) {
      if (A.get(i).getX() > A.get(left).getX()) {
        left = i;
      }
    }
    for (int i = 0; i < B_size; i++) {
      if (B.get(i).getX() < B.get(right).getX()) {
        right = i;
      }
    }
    Debug.log("Start with lcs = " + left + ", "+right);
    Line lcs_line = new Line(A.get(left), B.get(right));
    while (true) {
      if (A_size > 1 && lcs_line.checkPointVerticalAlignment(A.get((left + 1) % A_size))
        == Line.POINT_BELOW_LINE) {
        left = (left + 1) % A_size;
        lcs_line = new Line(A.get(left), B.get(right));
        continue;
      }
      if (B_size > 1 && lcs_line.checkPointVerticalAlignment(B.get((right - 1 + B_size) % B_size))
        == Line.POINT_BELOW_LINE) {
        right = (right - 1 + B_size) % B_size;
        lcs_line = new Line(A.get(left), B.get(right));
        continue;
      }
      break;
    }
    return new Pair<Integer,Integer>(left, right);
  }

  public static Pair<Integer,Integer> uppestCommonSupport(ArrayList<Point> A, ArrayList<Point> B) {
    int left = 0;
    int right = 0;
    int A_size = A.size();
    int B_size = B.size();
    for (int i = 0; i < A_size; i++) {
      if (A.get(i).getX() > A.get(left).getX()) {
        left = i;
      }
    }
    for (int i = 0; i < B_size; i++) {
      if (B.get(i).getX() < B.get(right).getX()){
        right = i;
      }
    }
    Line ucs_line = new Line(A.get(left), B.get(right));
    while (true) {
      if (A_size > 1 && ucs_line.checkPointVerticalAlignment(A.get((left - 1 + A_size) % A_size))
        == Line.POINT_ABOVE_LINE) {
        left = (left-1 + A_size) % A_size;
        ucs_line = new Line(A.get(left), B.get(right));
        continue;
      }
      if (B_size > 1 && ucs_line.checkPointVerticalAlignment(B.get((right + 1 + B_size) % B_size))
        == Line.POINT_ABOVE_LINE) {
        right = (right + 1 + B_size) % B_size;
        ucs_line = new Line(A.get(left), B.get(right));
        continue;
      }
      break;
    }
    return new Pair<Integer,Integer>(left, right);
  }

}
