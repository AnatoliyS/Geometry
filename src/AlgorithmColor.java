import java.awt.*;

public enum AlgorithmColor {

  CONVEX_HULL(Color.MAGENTA),
  DELAUNAY_TRIANGULATION(Color.BLUE),
  MINIMUM_AREA_POLYGON(Color.RED),
  VORONOI_DIAGRAM(Color.PINK),
  ALL_NEAREST_NEIGHBOURS(Color.DARK_GRAY),
  MINIMUM_SPANNING_TREE(Color.CYAN);

  private Color color;

  AlgorithmColor(Color _color) {
    color = _color;
  }

  Color getColor() {
    return color;
  }
}
