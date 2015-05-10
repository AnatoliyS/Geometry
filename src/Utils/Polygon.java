package Utils;

import java.util.ArrayList;

public class Polygon {
  private ArrayList<Point> points;
  private ByPassType byPassType;
  private int countPoints;

  public Polygon() {
    this.points = new ArrayList<>();
    this.byPassType = ByPassType.CLOCKWISE;
    this.countPoints = 0;
  }

  public Polygon(ArrayList<Point> points) {
    this.points = new ArrayList<>(points);
    this.byPassType = ByPassType.CLOCKWISE;
    this.countPoints = points.size();
  }

  public Polygon(ArrayList<Point> points, ByPassType byPassType) {
    this.points = new ArrayList<>(points);
    this.byPassType = byPassType;
    this.countPoints = points.size();
  }

  public ArrayList<Point> getPoints() {
    return points;
  }

  public int getCountPoints() {
    return countPoints;
  }

  public boolean isEmpty() {
    return points.isEmpty();
  }

  public Point getPoint(int index) {
    return points.get(index);
  }

  public Point getNextPoint(int index) {
    index = (index + 1 + countPoints) % countPoints;
    return points.get(index);
  }

  public Point getNextPoint(int index, ByPassType type) {
    if (type == byPassType) {
      index++;
    } else {
      index--;
    }
    index = (index + countPoints) % countPoints;
    return points.get(index);
  }

  public void clear() {
    points.clear();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || (obj.getClass() != this.getClass())) {
      return false;
    }

    Polygon p = (Polygon)obj;
    return (points.equals(p.points)) && (byPassType == p.byPassType) && (countPoints == p.countPoints);
  }


  @Override
  public String toString() {
    String s = "";
    for(Point p : points)
      s += p.toString() + ", ";
    s = "{" + s + "}";
    return s;
  }
}
