import java.lang.Boolean;
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

    // Algorithm process status
    private final static boolean NOT_PROCESSED = false;
    private final static boolean PROCESSED = true;

    private ArrayList<DACNode> nodes;
    private ArrayList<Point> points;
    private AlgorithmsContainer algoContainer;
    private Map<String, Boolean> statusMap;

    public DACTree(ArrayList<Point> _points, ArrayList<Algorithm> _algorithms)
            throws AlgorithmDependenciesException, UnknownAlgorithmException {
        // Points
        this.points = new ArrayList<Point>(_points);

        // AlgorithmsContainer
        AlgorithmsContainerBuilder builder = new AlgorithmsContainerBuilder();
        builder.createAlgorithmsContainer(_algorithms);
        this.algoContainer = builder.getAlgorithmsContainer();

        // Algorithm status
        this.statusMap = new HashMap<String, Boolean>();
        for (Algorithm algo : _algorithms) {
            this.statusMap.put(algo.getName(), NOT_PROCESSED);
        }

        // Tree's nodes
        int countPoints = _points.size();
        this.nodes = new ArrayList<DACNode>(countPoints * 4);
        for (int i = 0, countNodes = countPoints * 4; i < countNodes; ++i) {
            this.nodes.add(new DACNode());
        }
    }

    public void processAlgorithm(String name) throws UnknownAlgorithmException {
        // Checking status
        Boolean currentStatus = statusMap.get(name);
        if (currentStatus == PROCESSED) {
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
        int rootIndex = 1;
        int countPoints = points.size();
        try {
            processAlgorithmHelper(algo, rootIndex, 0, countPoints - 1);
        } catch (Exception e) {
            // Do nothing
        }
        // Set status PROCESSED
        statusMap.put(name, PROCESSED);
        Debug.log("Processing finished for algorithm " + algo + ".");
    }

    private void processAlgorithmHelper(Algorithm algo, int nodeIndex, int l, int r) {
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