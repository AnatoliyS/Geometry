package DCEL;

import java.util.Arrays;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashSet;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;
import Utils.*;
import Utils.Exceptions.*;
import DCEL.*;

/**
 * Doubly connected edge list
 */
public class DCEL {
  protected ArrayList<HalfEdge> edge;
  protected ArrayList<Vertex> vertex;
  protected ArrayList<Face> face;
  protected HashMap<Point, Face> face_searcher;

  // max interations (for check function)
  private static final int maxIterations = 100;
 
  public DCEL() {
    edge = new ArrayList<HalfEdge>();
    vertex = new ArrayList<Vertex>();
    face = new ArrayList<Face>();
    face_searcher = new HashMap<Point, Face>();
  }

  public DCEL(
    ArrayList<HalfEdge> e, 
    ArrayList<Vertex> v, 
    ArrayList<Face> f,
    HashMap<Point, Face> fs
  ) {
    edge = e;
    vertex = v; 
    face = f; 
    face_searcher = fs; 
  }

  /**
   * Copy constructor
   * @param DCEL Base DCEL
   * @return DCEL Copy of base DCEL
   */
  public DCEL(
    DCEL other
  ) {
    edge = new ArrayList<HalfEdge>();
    vertex = new ArrayList<Vertex>();
    face = new ArrayList<Face>();
    face_searcher = new HashMap<Point, Face>();
    
    // Hash map from others' diagram elements to new one
    // If we already copied some element, we store this connection
    // in these maps
    HashMap<HalfEdge, HalfEdge> edges_map = new HashMap<HalfEdge, HalfEdge>();
    HashMap<Vertex, Vertex> vertex_map = new HashMap<Vertex, Vertex>();
    HashMap<Face, Face> faces_map = new HashMap<Face, Face>();
   
    for (HalfEdge e : other.getEdges()) {
      edge.add(
        deepEdgeCopy(
          e,
          edges_map,
          vertex_map,
          faces_map
        )
      );
    }
    
    for (Vertex v : other.getVertexes()) {
      vertex.add(
        deepVertexCopy(
          v,
          edges_map,
          vertex_map,
          faces_map
        )
      );
    }

    for (Face f : other.getFaces()) {
      face.add(
        deepFaceCopy(
          f,
          edges_map,
          vertex_map,
          faces_map
        )
      );
    }

    for (Map.Entry<Point, Face> entry : other.getFS().entrySet()) {
      Point key = entry.getKey();
      Face value = entry.getValue();
      face_searcher.put(
        key,
        deepFaceCopy(
          value,
          edges_map,
          vertex_map,
          faces_map
        )
      );
    }

  }

  private Vertex deepVertexCopy(
    Vertex v,
    HashMap<HalfEdge, HalfEdge> edges_map,
    HashMap<Vertex, Vertex> vertex_map,
    HashMap<Face, Face> faces_map
  ) {
    if (vertex_map.containsKey(v)) {
      return vertex_map.get(v);
    } else {
      Vertex new_v = new Vertex(v.getX(), v.getY(), v.isInfinite());
      vertex_map.put(v, new_v);

      new_v.setIncidentEdge(
          deepEdgeCopy(
            v.getIncidentEdge(),
            edges_map,
            vertex_map,
            faces_map
          )
      );
      return new_v;
    }
  }

  private Face deepFaceCopy(
    Face f,
    HashMap<HalfEdge, HalfEdge> edges_map,
    HashMap<Vertex, Vertex> vertex_map,
    HashMap<Face, Face> faces_map
  ) {
    if (faces_map.containsKey(f)) {
      return faces_map.get(f);
    } else {
      Face new_f = new Face(f);
      faces_map.put(f, new_f);

      new_f.setIncidentEdge(
          deepEdgeCopy(
            f.getIncidentEdge(),
            edges_map,
            vertex_map,
            faces_map
          )
      );
      return new_f;
    }
  }

  private HalfEdge deepEdgeCopy(
    HalfEdge e,
    HashMap<HalfEdge, HalfEdge> edges_map,
    HashMap<Vertex, Vertex> vertex_map,
    HashMap<Face, Face> faces_map
  ) {
    if (edges_map.containsKey(e)) {
      return edges_map.get(e);
    } else {
      HalfEdgeBuilder new_e_builder = new HalfEdgeBuilder(e.getName());
      edges_map.put(e, new_e_builder.getHalfEdgeReference());
      new_e_builder
        .setOrigin(
            deepVertexCopy(
              e.getOrigin(),
              edges_map,
              vertex_map,
              faces_map
            )
        )
        .setNextEdge(
          deepEdgeCopy(
            e.getNextEdge(),
            edges_map,
            vertex_map,
            faces_map
          )
        )
        .setPreviousEdge(
          deepEdgeCopy(
            e.getPreviousEdge(),
            edges_map,
            vertex_map,
            faces_map
          )
        )
        .setTwinEdge(
          deepEdgeCopy(
            e.getTwinEdge(),
            edges_map,
            vertex_map,
            faces_map
          )
        )
        .setLeftIncidentFace(
          deepFaceCopy(
            e.getLeftIncidentFace(),
            edges_map,
            vertex_map,
            faces_map
          )
        );
      try {
        return new_e_builder.getHalfEdge();
      } catch (Exception exc) {
        Debug.log(exc.getMessage());
        return null;
      }
    }
  }
  
  public ArrayList<HalfEdge> getEdges() {
    return edge;
  }

  public ArrayList<Vertex> getVertexes() {
    return vertex;
  }

  public ArrayList<Face> getFaces() {
    return face;
  }

  public HashMap<Point, Face> getFS() {
    return face_searcher;
  }

  /**
   * Get Face asociated with some Point
   * @param Point
   * @return Face
   */
  public Face getFaceAroundPoint(Point point) throws NoDataException {
    // TODO: refactor this method (Set with custom comparator)
    for (Point p : face_searcher.keySet()) {
      if (p.getX() == point.getX() && p.getY() == point.getY()) {
        Face f = face_searcher.get(p);
        return f;
      }
    }
    throw new NoDataException();
  }
  
  /**
   * Checks that DCEL is correct and deletes unused elements
   * * Check that structure is not contraversial (no links conflicts,
   * not null links, no sense conflicts in structure)
   * * Removes everything that can't be accesed
   * @return boolean true if DCEL is correct
   */
  public boolean checkAndClean() {
    Debug.log("checking result...");
    Debug.log("---------------------------------------");
    
    HashSet<Face> used_face = new HashSet<Face>();
    HashSet<Vertex> used_vertex = new HashSet<Vertex>();
    HashSet<HalfEdge> used_edge = new HashSet<HalfEdge>();

    String debug_output = "";
    for (Point p : face_searcher.keySet()) {
      debug_output += "For point " + p + "\n\n";
      debug_output += "---------------------------------------";

      Face f = face_searcher.get(p);
      used_face.add(f);
      debug_output += "Found face: \n";
      HalfEdge start = f.getIncidentEdge();
      HalfEdge temp = start;

      // Iterate through Face edges
      int iteration = 0;
      do {
        iteration++;
        if (iteration > maxIterations) {
          debug_output += "INFINITE CYCLE!\n\n";
          Debug.log(debug_output);
          return false;
        }
        used_edge.add(temp);
        Vertex v = temp.getOrigin();
        used_vertex.add(v);
        debug_output += "face edge = "+ temp + "\n\n";
        
        // If edge is INF, print asociated infinite Face
        if (temp.isInfinite()) {
          debug_output += "this is infinite edge. Start to show inf. face edges\n";
          HalfEdge twin = temp.getTwinEdge();
          HalfEdge e = twin;
          int it = 0;
          do {
            it++;
            if (it > maxIterations) {
              debug_output += "INFINITE CYCLE!\n";
              Debug.log(debug_output);
              return false;
            }
            used_edge.add(e);

            debug_output += "  face edge = "+ e + "\n";
            e = e.getNextEdge();
          } while (e != twin);
          used_face.add(twin.getLeftIncidentFace());
          debug_output += "end of showing inf. face edges\n";
        }
       
        // Check that every edge asociated with same Face 
        if (temp.getLeftIncidentFace() != f) {
          debug_output += "Incorrect face!! "+ temp.getLeftIncidentFace()+ "\n";
          Debug.log(debug_output);
          return false;
        }

        temp = temp.getNextEdge();
      } while(start != temp);

    }
   
    // Clean usused elements 
    Iterator<Vertex> it = vertex.iterator();
    while(it.hasNext()) {
      Vertex v = it.next();
      if (!used_vertex.contains(v)) {
        debug_output += "Removing vertex" + v + "\n";
        it.remove();
        v = null;
      } else {
        if (!used_edge.contains(v.getIncidentEdge())) {
          debug_output += "Wrong link from vertex " + v + "to edge " + v.getIncidentEdge() + "\n";
          Debug.log(debug_output);
          return false;
        }
      }
    }

    Iterator<Face> fit = face.iterator();
    while(fit.hasNext()) {
      Face f = fit.next();
      debug_output += "Considering Face = " + f + "\n";
      if (!used_face.contains(f)) {
        debug_output += "Removing face" + f;
        fit.remove();
        f = null;
      } else {
        if (!used_edge.contains(f.getIncidentEdge())) {
          Debug.log(debug_output);
          Debug.log("Wrong link from face " + f + "to edge " + f.getIncidentEdge());
          return false;
        }
      }
    }

    Iterator<HalfEdge> hit = edge.iterator();
    while(hit.hasNext()) {
      HalfEdge e = hit.next();
      if (!used_edge.contains(e)) {
       debug_output += "Removing edge" + e;
        hit.remove();
        e = null;
      } else {
        if (!used_edge.contains(e.getNextEdge())) {
          Debug.log(debug_output);
          Debug.log("Wrong link from edge.next " + e + "to edge " + e.getNextEdge());
          return false;
        }
        if (!used_edge.contains(e.getPreviousEdge())) {
          Debug.log(debug_output);
          Debug.log("Wrong link from edge.prev " + e + "to edge " + e.getPreviousEdge());
          return false;
        }
        if (!used_edge.contains(e.getTwinEdge())) {
          Debug.log(debug_output);
          Debug.log("Wrong link from edge.twin " + e + "to edge " + e.getTwinEdge());
          return false;
        }
      }
    }

    Debug.log("checking was succesful");
    Debug.log("Edges: " + edge.size());
    Debug.log("Vertex: " + vertex.size());
    Debug.log("Face: " + face.size());
    Debug.log("---------------------------------------");
    return true;
  }

}
