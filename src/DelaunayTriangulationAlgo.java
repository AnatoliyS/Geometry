import Utils.Exceptions.AlgorithmRuntimeException;
import Utils.Exceptions.NoDataException;
import Utils.Point;

import java.util.ArrayList;

public class DelaunayTriangulationAlgo extends Algorithm {
  private final int TRIVIAL_COUNT = 3;

  public DelaunayTriangulationAlgo(String _name, ArrayList<String> _deps) {
    super(_name, _deps);
  }

  @Override
  public Object merge(DACNode left, DACNode right) throws NoDataException, AlgorithmRuntimeException {
    DACNode parentOfLeft = left.getParent();
    DACNode parentOfRight = right.getParent();
    //left and right node should be previously merged in one node and have the same parent
    assert (parentOfLeft.equals(parentOfRight));
    VoronoiDiagram voronoiDiagram = (VoronoiDiagram)parentOfLeft.getDataResult(AlgorithmName.VORONOI_DIAGRAM);
    return new DelaunayTriangulation(voronoiDiagram);
  }

  @Override
  public boolean isTrivialCase(int count) {
    return count <= TRIVIAL_COUNT;
  }

  @Override
  public Object doTrivialCase(ArrayList<Point> points) {
    return new DelaunayTriangulation(points);
  }
}
