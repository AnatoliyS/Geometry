package Utils;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.*;

public class DrawHelper {
  public static final int scene_inf_x = 1000;
  public static final int scene_inf_y = 1000;
  // The offset of (0,0) point of cartesian coord system
  public static final int scene_offset_x = 100;
  public static final int scene_offset_y = 500;
  // Diameter of point (for render)
  public static final double point_diameter = 6.5;

  /**
   * Trasform to flip Y axis and translate coords from
   * screen coords system to cartesian one
   */
  public static AffineTransform invertYAxisAffineTransform() {
    AffineTransform newAT = new AffineTransform();
    // Move center of coords by offset
    newAT.translate(scene_offset_x, scene_offset_y);
    // Flip Y axis
    newAT.scale(1.0, -1.0);
    return newAT; 
  }

  /**
   * Draws coordinate axes 
   */
  public static void drawCoordinateAxes(Graphics2D g) {
    g.setPaint(Color.gray);
    g.draw(new Line2D.Double(0, -scene_inf_y, 0, scene_inf_y));
    g.draw(new Line2D.Double(-scene_inf_x, 0, scene_inf_x, 0));
  }

  /**
   * Draws points and labels with their coords 
   */
  public static void drawPoints(
    Graphics2D g, 
    ArrayList<Point> points, 
    Color color, 
    boolean labels 
  ) {
    FontMetrics fm = g.getFontMetrics();
    double point_radius = point_diameter / 2.0;
    g.setPaint(color);
    
    for(Point p : points) {
      Point2D.Double point = 
        new Point2D.Double(p.getX(), p.getY());
      // Apply flip Y transform to point (not to text label)
      invertYAxisAffineTransform().transform(point, point);
      
      g.fill(
        new Ellipse2D.Double(
          point.getX() - point_radius, 
          point.getY() - point_radius, 
          point_diameter, 
          point_diameter
        )
      );

      if (labels) {
        String text = p.toString(); 
        g.drawString(
          text,
          (float)(point.getX() - fm.stringWidth(text) / 2.0), 
          (float)(point.getY() - fm.getHeight()) 
        );
      }
    }
  }

  /**
   * Draws polygon filled with color from points
   */
  public static void drawFilledPolygon(
    Graphics2D g,
    ArrayList<Point> points,
    Color color
  ) {
    Path2D polygon_path = new Path2D.Double();

    polygon_path.moveTo(points.get(0).getX(), points.get(0).getY());
    for(int i = 1; i < points.size(); i++) {
      polygon_path.lineTo(points.get(i).getX(), points.get(i).getY());
    }
    polygon_path.closePath();

    g.setPaint(color);
    g.fill(polygon_path);
  }

}
