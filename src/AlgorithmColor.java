import java.awt.*;

public enum AlgorithmColor {
  CONVEX_HULL(new Color((float)Color.CYAN.getRed()/255,
                        (float)Color.CYAN.getGreen()/255,
                        (float)Color.CYAN.getBlue()/255,
                        (float)0.3)),   // prevent hiding other algorithm results
  DELAUNAY_TRIANGULATION(Color.BLUE),
  MINIMUM_AREA_POLYGON(new Color((float)Color.RED.getRed()/255,
                                 (float)Color.RED.getGreen()/255,
                                 (float)Color.RED.getBlue()/255,
                                 (float)0.3)), // prevent hiding other algorithm results
  VORONOI_DIAGRAM(Color.PINK),
  ALL_NEAREST_NEIGHBOURS(Color.DARK_GRAY),
  MINIMUM_SPANNING_TREE(Color.MAGENTA);

  private Color color;

  AlgorithmColor(Color _color) {
    color = _color;
  }

  Color getColor() {
    return color;
  }
}
