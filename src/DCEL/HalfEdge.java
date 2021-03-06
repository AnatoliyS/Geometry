package DCEL;

public class HalfEdge {
  private String id;
  private HalfEdge previousEdge;
  private HalfEdge nextEdge;
  private HalfEdge twinEdge;
  private Vertex origin;
  private Face leftIncidentFace;
    
  public HalfEdge() {
    id = "noname";
    previousEdge = null;
    nextEdge = null;
    twinEdge = null;
    origin = null;
    leftIncidentFace = null;
  }
  
  public HalfEdge(HalfEdge other) {
    id = "copy" + other.getName();
    previousEdge = other.getPreviousEdge();
    nextEdge = other.getNextEdge();
    twinEdge = other.getTwinEdge();
    origin = other.getOrigin();
    leftIncidentFace = other.getLeftIncidentFace();
  }
  
  public HalfEdge(String _id) {
    id = _id;
    previousEdge = null;
    nextEdge = null;
    twinEdge = null;
    origin = null;
    leftIncidentFace = null;
  }

  public String getName() {
    return id;
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
  
  public HalfEdge getPreviousEdge() {
    return previousEdge;
  }
  public HalfEdge getNextEdge() {
    return nextEdge;
  }
  public HalfEdge getTwinEdge() {
    return twinEdge;
  }
  public Vertex getOrigin() {
    return origin;
  }
  public Face getLeftIncidentFace() {
    return leftIncidentFace;
  }

  public boolean isInfinite() {
    return leftIncidentFace.isInfinite() || twinEdge.getLeftIncidentFace().isInfinite();
  }

  @Override
  public String toString() {
    String s = "{ HalfEdge name = \"" + id + "\", ";
    s += "Origin = \"" + origin.getX() + ", " + origin.getY() +"\"";
    s += " Next_edge_origin = \"" + nextEdge.getOrigin().getX() + ", " + nextEdge.getOrigin().getY() +"\"";
    s += " Next_edge_name = \"" + nextEdge.getName() +"\"";
    s += " prev_edge_name = \"" + previousEdge.getName() +"\"";
    s += "}";
    return s;
  }
}
