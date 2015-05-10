package DCEL;
import Utils.Exceptions.*;

public class HalfEdgeBuilder {
  private HalfEdge edge;
    
  public HalfEdgeBuilder() {
    edge = new HalfEdge();
  }
  
  public HalfEdgeBuilder(String _id) {
    edge = new HalfEdge(_id);
  }

  public HalfEdge getHalfEdge() throws HalfEdgeIsNotValidException {
    if (edge.getPreviousEdge() == null) {
      throw new HalfEdgeIsNotValidException("prev of " + edge.getName());
    } 
    if (edge.getNextEdge() == null) {
      throw new HalfEdgeIsNotValidException("next of" + edge.getName());
    }
    if (edge.getTwinEdge() == null) {
      throw new HalfEdgeIsNotValidException("twin of" + edge.getName());
    }
    if (edge.getOrigin() == null) {
      throw new HalfEdgeIsNotValidException("origin of" + edge.getName());
    }
    if (edge.getLeftIncidentFace() == null) {
      throw new HalfEdgeIsNotValidException("face of" + edge.getName());
    }
    
    return edge;    
  }

  public HalfEdge getHalfEdgeReference() {
    return edge;
  }
  
  public HalfEdgeBuilder setPreviousEdge(HalfEdge _e) {
    edge.setPreviousEdge(_e);
    _e.setNextEdge(edge);
    return this;
  }
  public HalfEdgeBuilder setNextEdge(HalfEdge _e) {
    edge.setNextEdge(_e);
    _e.setPreviousEdge(edge);
    return this;
  }
  public HalfEdgeBuilder setTwinEdge(HalfEdge _e) {
    edge.setTwinEdge(_e);
    _e.setTwinEdge(edge);
    return this;
  }
  public HalfEdgeBuilder setOrigin(Vertex _v) {
    edge.setOrigin(_v);
    _v.setIncidentEdge(edge);
    return this;
  }
  public HalfEdgeBuilder setLeftIncidentFace(Face _f) {
    edge.setLeftIncidentFace(_f);
    _f.setIncidentEdge(edge);
    return this;
  }

}
