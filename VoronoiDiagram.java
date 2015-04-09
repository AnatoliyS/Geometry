import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;
import Utils.*;
import DCEL.*;

public class VoronoiDiagram implements VisualData{
  private ArrayList<Point> points;
  private HalfEdge[] edge;
  private Vertex[] vertex;
  private Face[] face;

  // Trivial case
  public VoronoiDiagram(ArrayList<Point> pts) {
    points = new ArrayList<Point>(pts);
  }

  public ArrayList<Point> getPoints() {
    return points;
  }

  public void render(Graphics2D g) {
    Debug.log("rendering voronoi...");
  }

  @Override
  public String toString() {
    String s = "";
    for(Point p: points)
      s += p.toString() + ", ";
    s = "{" + s + "}";
    return s;
  }

}
