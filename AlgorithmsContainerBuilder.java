import java.io.*;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import Utils.*;
import Utils.Exceptions.*;

public class AlgorithmsContainerBuilder {

  private AlgorithmsContainer ac;

  /**
   * Create AlgorithmsContainer instance
   * @param list List of Algorithm elements which will be added in AlgorithmsContainer
   * @throws AlgorithmDependenciesException
   * @throws UnknownAlgorithmException
   */
  public void createAlgorithmsContainer(ArrayList<Algorithm> list)
          throws AlgorithmDependenciesException, UnknownAlgorithmException {
    if (checkAlgoDependencies(list)) {
      Map<String, Algorithm> algorithmsMap = new HashMap<String, Algorithm>();
      for (Algorithm alg : list) {
        algorithmsMap.put(alg.getName(), alg);
      }
      ac = new AlgorithmsContainer(algorithmsMap);
    }
  }

  /**
   * Get instance of AlgorithmsContainer
   * @return Instance of AlgorithmsContainer
   */
  public AlgorithmsContainer getAlgorithmsContainer() {
    return ac;
  }

  /**
   * Checking algorithm dependencies
   * @param list List of Algorithm elements
   * @return true - algorithms dependencies is valid
   * @throws AlgorithmDependenciesException
   * @throws UnknownAlgorithmException
   */
  private boolean checkAlgoDependencies(ArrayList<Algorithm> list)
          throws AlgorithmDependenciesException, UnknownAlgorithmException {
    if (hasCycle(list)) {
      throw new AlgorithmDependenciesException(ExceptionMessage.CYCLE_DEPENDENCIES);
    }
    if (!checkAlgoSet(list)) {
      throw new AlgorithmDependenciesException(ExceptionMessage.UNKNOWN_ALGORITHM);
    }
    return true;
  }

  /**
   * Check algorithm graph dependencies for unknowm algorithms
   * @param list - List of Algorithm elements
   * @return true - All algorithms are known
   * @throws UnknownAlgorithmException
   */
  private boolean checkAlgoSet(ArrayList<Algorithm> list)
          throws UnknownAlgorithmException {
    // All algorithms' name which present in list of algorithms
    HashSet<String> presentAlgorithms = new HashSet<String>();
    // All depent algorithms for each algorithm in list of algorithms
    HashSet<String> depentAlgorithms = new HashSet<String>();
    for (Algorithm algo : list) {
      presentAlgorithms.add(algo.getName());
      depentAlgorithms.addAll(algo.getDependencies());
    }

    Debug.log("presentAlgorithms=" + presentAlgorithms.toString());
    Debug.log("depentAlgorithms=" + depentAlgorithms.toString());

    depentAlgorithms.removeAll(presentAlgorithms);
    Debug.log("unknownAlgorithms=" + depentAlgorithms.toString());

    if (!depentAlgorithms.isEmpty()) {
      throw new UnknownAlgorithmException(
              ExceptionMessage.UNKNOWN_ALGORITHM + depentAlgorithms.toString());
    }
    return true;
  }

  /**
   * Check algorithm graph dependencies for a cycle
   * @param list - List of Algorithm elements
   * @return true - Cycle presents in algorithm dependencies graph
   */
  private boolean hasCycle(ArrayList<Algorithm> list) {
    // All algorithms' names which have already been checked
    HashSet<String> checkedAlgorithms = new HashSet<String>();
    // Stack of algorithms for depth first search
    Stack<String> algoStack = new Stack<String>();

    Map<String, ArrayList<String>> algorithmsMap = new HashMap<String, ArrayList<String>>();
    for (Algorithm algo : list) {
      algorithmsMap.put(algo.getName(), algo.getDependencies());
    }

    for (Algorithm algo : list) {
      if(hasCycleUtil(algo.getName(), checkedAlgorithms, algoStack, algorithmsMap)) return true;
    }

    return false;
  }

  /**
   * Check if current algorithm is a part of algorithm graph dependencies' cycle (depth first search)
   * @param algorithm - Current algorithm to check being a part of algorithm graph dependencies' cycle
   * @param checkedAlgorithms - All algorithms' names which have already been checked
   * @param algoStack - Stack of algorithms for depth first search
   * @param algorithmsMap - Map for each algorithm its dependencies
   * @return true - Current algoritm is a part of a cycle in algorithm dependencies graph
   */
  private boolean hasCycleUtil(
          String algorithm,
          HashSet<String> checkedAlgorithms,
          Stack<String> algoStack,
          Map<String, ArrayList<String>> algorithmsMap){
    if(checkedAlgorithms.contains(algorithm)) return false;

    checkedAlgorithms.add(algorithm);
    algoStack.push(algorithm);

    for(String algo : algorithmsMap.get(algorithm)){
      if(!checkedAlgorithms.contains(algo) && hasCycleUtil(algo, checkedAlgorithms, algoStack, algorithmsMap)) return true;
      if(algo.equals(algoStack.peek())) return true;
    }

    algoStack.pop();
    return false;
  }

}
