package DCEL;

public class Vertex {
  private double x;
  private double y;
  private HalfEdge incidentEdge;
  private boolean infinite;

  public Vertex(double _x, double _y) {
    x = _x;
    y = _y;
    incidentEdge = null;
  }
  
  public Vertex(double _x, double _y, boolean inf) {
    x = _x;
    y = _y;
    infinite = inf;
    incidentEdge = null;
  }

  public void setInfinite(boolean inf) {
    infinite = inf;
  }

  public boolean isInfinite() {
    return infinite;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
  
  public void setIncidentEdge(HalfEdge _e) {
    incidentEdge = _e;
  }

  @Override
  public String toString() {
    String inf = (infinite) ? "(infinite)" : "";
    return "Vertex" + inf + "[" + x + ", " + y + "]";
  }
}
