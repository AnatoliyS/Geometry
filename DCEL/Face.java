package DCEL;

public class Face{
  private HalfEdge incidentEdge;

  public Face(){
    incidentEdge = null;
  }

  public void setIncidentEdge(HalfEdge _e){
    incidentEdge = _e;
  }
}
