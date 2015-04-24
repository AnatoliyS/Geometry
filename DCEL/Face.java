package DCEL;

public class Face {
  private HalfEdge incidentEdge;
  private boolean infinite;

  public Face() {
    infinite = false;
    incidentEdge = null;
  }
  
  public Face(Face other) {
    infinite = other.isInfinite();
    incidentEdge = other.getIncidentEdge();
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

  @Override
  public String toString() {
    String s = "{Face incidentEdges = ";
    HalfEdge e = incidentEdge;
    do {
      s += "[" + e.toString() + "], ";
      e = e.getNextEdge();
    } while (e != incidentEdge);
    s += "}";
    return s;
  }
}
