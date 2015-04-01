package DCEL;

public class HalfEdge {
  private HalfEdge previousEdge;
  private HalfEdge nextEdge;
  private HalfEdge twinEdge;
  private Vertex origin;
  private Face leftIncidentFace;
    
  public HalfEdge() {
    previousEdge = null;
    nextEdge = null;
    twinEdge = null;
    origin = null;
    leftIncidentFace = null;
  }

  public void setPreviousEdge(HalfEdge _e) {
    previousEdge = _e;
  }
  public void setNextEdge(HalfEdge _e) {
    nextEdge = _e;
  }
  public void setTwinEdge(HalfEdge _e) {
    twinEdge = _e;
  }
  public void setOrigin(Vertex _v) {
    origin = _v;
  }
  public void setLeftIncidentFace(Face _f) {
    leftIncidentFace = _f;
  }

}
