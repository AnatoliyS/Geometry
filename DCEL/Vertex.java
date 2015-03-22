package DCEL;

public class Vertex{
  private double x;
  private double y;
  private HalfEdge incidentEdge;

  public Vertex(double _x, double _y){
    x = _x;
    y = _y;
    incidentEdge = null;
  }

  public void setIncidentEdge(HalfEdge _e){
    incidentEdge = _e;
  }
}
