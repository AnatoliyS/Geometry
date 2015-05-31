import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import Utils.*;
import Utils.Exceptions.*;

/**
 * Divide and Conquer Tree
 */
public class DACTree {
  public static final int ROOT_INDEX = 1;
  // Algorithm process status
  private enum AlgorithmProcessStatus {
    NOT_PROCESSED,
    PROCESSED
  }

  private ArrayList<DACNode> nodes;
  private ArrayList<Point> points;
  private AlgorithmsContainer algoContainer;
  private Map<String, AlgorithmProcessStatus> statusMap;

  public DACTree(ArrayList<Point> _points, ArrayList<Algorithm> _algorithms)
      throws AlgorithmDependenciesException, UnknownAlgorithmException {
    // Points
    this.points = new ArrayList<Point>(_points);

    // AlgorithmsContainer
    AlgorithmsContainer.AlgorithmsContainerBuilder builder =
            new AlgorithmsContainer.AlgorithmsContainerBuilder();
    for (Algorithm algo : _algorithms) {
        builder = builder.addAlgorithm(algo);
    }
    this.algoContainer = builder.buildContainer();

    // Algorithm status
    this.statusMap = new HashMap<String, AlgorithmProcessStatus>();
    for (Algorithm algo : _algorithms) {
        this.statusMap.put(algo.getName(), AlgorithmProcessStatus.NOT_PROCESSED);
    }

    // Tree's nodes
    int countPoints = _points.size();
    this.nodes = new ArrayList<DACNode>(countPoints * 4);
    for (int i = 0, countNodes = countPoints * 4; i < countNodes; ++i) {
      // assuming, that in future we will assign i*2 and i*2+1 indexes for sons of this node
      if (i > ROOT_INDEX) {
        this.nodes.add(new DACNode(nodes.get(i/2)));
      } else {
        this.nodes.add(new DACNode(null));
      }
    }
  }

  public void processAlgorithm(String name)
      throws UnknownAlgorithmException, NoDataException, AlgorithmRuntimeException {
    // Checking status
    AlgorithmProcessStatus currentStatus = statusMap.get(name);
    if (currentStatus == AlgorithmProcessStatus.PROCESSED) {
      Debug.log("Algorithm " + name + " have been processed.");
      return;
    }

    Algorithm algo = algoContainer.getAlgorithm(name);
    Debug.log("Processing algorithm " + algo + "...");

    // Processing for depent algorithms
    ArrayList<String> depentAlgorithms = algo.getDependencies();
    for (String depAlgo : depentAlgorithms) {
      processAlgorithm(depAlgo);
    }

    // Processing for algorithm
    Debug.log("Processing algorithm " + algo + " itself...");
    if (algo.getName() == AlgorithmName.SPANNING_TREE) {
      DACNode node = nodes.get(ROOT_INDEX);
      DelaunayTriangulation delaunayTriangulation =
          (DelaunayTriangulation)getAlgorithmResult(AlgorithmName.DELAUNAY_TRIANGULATION);
      SpanningTreeKruskal spanningTreeKruskal = ((SpanningTreeKruskalAlgo)algo).calculate(delaunayTriangulation);
      node.setDataResult(algo.getName(), spanningTreeKruskal);
    } else {
      int countPoints = points.size();
      try {
        processAlgorithmHelper(algo, ROOT_INDEX, 0, countPoints - 1);
      } catch (AlgorithmRuntimeException e) {
        throw e;
      } catch (Exception e) {
        Debug.log(e.getMessage());
        return;
      }
    }
    // Set status PROCESSED
    statusMap.put(name, AlgorithmProcessStatus.PROCESSED);
    Debug.log("Processing finished for algorithm " + algo + ".");
  }

  private void processAlgorithmHelper(Algorithm algo, int nodeIndex, int l, int r) 
    throws Exception {
    Debug.log("l=" + l + " r=" + r);
    if (l > r) {
      return;
    }

    Object result;
    int countPoints = r - l + 1;

    // Handling for trivial cases
    if (algo.isTrivialCase(countPoints)) {
      List subPoints = points.subList(l, r + 1);
      ArrayList<Point> pts = new ArrayList<Point>(subPoints);
      result = algo.doTrivialCase(pts);
    } else {
      int m = (l + r) / 2;
      int lNodeIndex = nodeIndex * 2;
      int rNodeIndex = nodeIndex * 2 + 1;

      // Processing for subNodes
      processAlgorithmHelper(algo, lNodeIndex, l, m);
      processAlgorithmHelper(algo, rNodeIndex, m + 1, r);

      DACNode lNode = nodes.get(lNodeIndex);
      DACNode rNode = nodes.get(rNodeIndex);

      // Merging received data from subNodes
      result = algo.merge(lNode, rNode);
    }
    // Adding result in node
    DACNode node = nodes.get(nodeIndex);
    node.setDataResult(algo.getName(), result);
  }

  public Object getAlgorithmResult(String name) throws NoDataException {
    return nodes.get(ROOT_INDEX).getDataResult(name);
  }

  @Override
  public String toString() {
    String s = "DACTree";
    s += "\nnodesSize=" + nodes.size();
    s += "\nnodes=" + nodes.toString();
    s += "\nstatusMap=" + statusMap.toString();
    s += "\npoints=" + points.toString();
    s += "\nalgoContainer=" + algoContainer.toString();
    return s;
  }

}
