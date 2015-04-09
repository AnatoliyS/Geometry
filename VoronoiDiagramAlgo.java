import java.io.*;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.Color;
import Utils.*;
import Utils.Exceptions.*;

public class VoronoiDiagramAlgo extends Algorithm {

  private final int TRIVIAL_COUNT = 1;

  public VoronoiDiagramAlgo(String _name, ArrayList<String> _deps) {
    super(_name, _deps);
  }

  // TODO: add realization
  public Object merge(DACNode l, DACNode r) throws NoDataException {
    String name = getName();
    VoronoiDiagram left  = (VoronoiDiagram)l.getDataResult(name);
    VoronoiDiagram right = (VoronoiDiagram)r.getDataResult(name);

    return null;
  }

  public boolean isTrivialCase(int count) {
    return (count <= TRIVIAL_COUNT);
  }

  public Object doTrivialCase(ArrayList<Point> points) {
    return new VoronoiDiagram(points);
  }

}
