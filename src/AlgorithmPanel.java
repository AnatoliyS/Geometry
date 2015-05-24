import Utils.*;
import Utils.Exceptions.NoDataException;
import Utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class AlgorithmPanel extends JPanel{
  private ArrayList<VisualData> objects;
  private ArrayList<Utils.Point> points;

  public AlgorithmPanel() {
    objects = new ArrayList<>();
    points = new ArrayList<>();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
    );
    AffineTransform oldAT = g2.getTransform();

    // 1. Flip Y axis and translate coords to go from
    // screen coords system to cartesian.
    // Now we draw in cartesian system
    g2.setTransform(DrawHelper.invertYAxisAffineTransform());

    // Draw coordinate axes
    DrawHelper.drawCoordinateAxes(g2);

    for(VisualData object : objects) {
        object.render(g2);
    }
    // 2. Flip back to screen cords system to draw text
    // Now we draw in Screen-coordinate system
    g2.setTransform(oldAT);

    DrawHelper.drawPoints(g2, points, Color.black, false);
    g2.dispose();
  }

  void setObjectsToBeDrawn(ArrayList<VisualData> objects) {
    this.objects = objects;
  }

  void setPointsToBeDrawn(ArrayList<Point> points) {
    this.points = points;
  }
}
