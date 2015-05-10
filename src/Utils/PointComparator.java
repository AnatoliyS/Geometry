package Utils;
import java.util.Comparator;

public class PointComparator implements Comparator<Point> {
  @Override
  public int compare(Point a, Point b) {
    return 
      (a.getX() < b.getX() || a.getX() == b.getX() && a.getY() < b.getY()) ? -1 :
              (a.getX() == b.getX() && a.getY() == b.getY()) ? 0 : 1;
  }

}
