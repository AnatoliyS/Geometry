package DCEL;
import Utils.*;

public class Face {
  private HalfEdge incidentEdge;
  private boolean infinite;
  private Point point;

  public Face() {
    infinite = false;
    incidentEdge = null;
    point = null;
  }
  
  public Face(Face other) {
    infinite = other.isInfinite();
    incidentEdge = other.getIncidentEdge();
    point = other.getPoint();
  }

  public void setPoint(Point p) {
    point = p;
  }

  public void setInfinite(boolean inf) {
    infinite = inf;
  }
  
  public boolean isInfinite() {
    return infinite;
  }

  public void setIncidentEdge(HalfEdge _e) {
    incidentEdge = _e;
  }
  
  public HalfEdge getIncidentEdge() {
    return incidentEdge;
  }

  public Point getPoint() {
    return point;
  }

  @Override
  public String toString() {
    String s = "{Face incidentEdges = ";
    HalfEdge e = incidentEdge;
    do {
      s += "[" + /*e.toString()*/ e.getName() + "], ";
      e = e.getNextEdge();
    } while (e != incidentEdge);
    s += "}";
    return s;
  }
}
