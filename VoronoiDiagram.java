import Utils.*;
import DCEL.*;

public class VoronoiDiagram{
  private Point[] point;
  private HalfEdge[] edge;
  private Vertex[] vertex;
  private Face[] face;
  private double minX = 0;
  private double maxX = 100;
  private double minY = 0;
  private double maxY = 100;
  private int n;
  private Point[] convexHull;

  // Trivial case: diagram for 1 point
  public VoronoiDiagram(Point a){
    n = 1;
    point = new Point[1];
    convexHull = new Point[1];
    vertex = new Vertex[4];
    edge = new HalfEdge[8];
    face = new Face[2];
    
    point[0] = a;
    convexHull[0] = a;

    vertex[0] = new Vertex(minX, minY);
    vertex[1] = new Vertex(minX, maxY);
    vertex[2] = new Vertex(maxX, minY);
    vertex[3] = new Vertex(maxX, maxY);

    edge[0] = new HalfEdge();
    edge[1] = new HalfEdge();
    edge[2] = new HalfEdge();
    edge[3] = new HalfEdge();
    edge[4] = new HalfEdge();
    edge[5] = new HalfEdge();
    edge[6] = new HalfEdge();
    edge[7] = new HalfEdge();
    
    face[0] = new Face();
    face[1] = new Face();
    
    vertex[0].setIncidentEdge(edge[0]);
    vertex[1].setIncidentEdge(edge[1]);
    vertex[2].setIncidentEdge(edge[2]);
    vertex[3].setIncidentEdge(edge[3]);

    edge[0].setOrigin(vertex[0]);
    edge[1].setOrigin(vertex[1]);
    edge[2].setOrigin(vertex[2]);
    edge[3].setOrigin(vertex[3]);

    edge[0].setTwinEdge(edge[4]);
    edge[1].setTwinEdge(edge[5]);
    edge[2].setTwinEdge(edge[6]);
    edge[3].setTwinEdge(edge[7]);

    edge[4].setTwinEdge(edge[0]);
    edge[5].setTwinEdge(edge[1]);
    edge[6].setTwinEdge(edge[2]);
    edge[7].setTwinEdge(edge[3]);

    edge[0].setPreviousEdge(edge[3]);
    edge[1].setPreviousEdge(edge[0]);
    edge[2].setPreviousEdge(edge[1]);
    edge[3].setPreviousEdge(edge[2]);
    edge[4].setPreviousEdge(edge[5]);
    edge[5].setPreviousEdge(edge[6]);
    edge[6].setPreviousEdge(edge[7]);
    edge[7].setPreviousEdge(edge[4]);

    edge[0].setNextEdge(edge[1]);
    edge[1].setNextEdge(edge[2]);
    edge[2].setNextEdge(edge[3]);
    edge[3].setNextEdge(edge[0]);
    edge[4].setNextEdge(edge[7]);
    edge[5].setNextEdge(edge[4]);
    edge[6].setNextEdge(edge[5]);
    edge[7].setNextEdge(edge[6]);

    edge[0].setLeftIncidentFace(face[0]);
    edge[1].setLeftIncidentFace(face[0]);
    edge[2].setLeftIncidentFace(face[0]);
    edge[3].setLeftIncidentFace(face[0]);
    
    edge[4].setLeftIncidentFace(face[1]);
    edge[5].setLeftIncidentFace(face[1]);
    edge[6].setLeftIncidentFace(face[1]);
    edge[7].setLeftIncidentFace(face[1]);

    face[0].setIncidentEdge(edge[0]);
    face[1].setIncidentEdge(edge[4]);
  }
}
