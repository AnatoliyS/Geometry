package DCEL;

public class Face {
  private HalfEdge incidentEdge;
  private boolean infinite;

  public Face() {
    infinite = false;
    incidentEdge = null;
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
}
