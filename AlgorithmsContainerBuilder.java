import java.io.*;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
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
   * @param algorithms - List of Algorithm elements
   * @return true - Algorithms dependencies' graph is valid
   * @throws AlgorithmDependenciesException
   * @throws UnknownAlgorithmException
   */
  private boolean checkAlgoDependencies(ArrayList<Algorithm> algorithms)
          throws AlgorithmDependenciesException, UnknownAlgorithmException {
    // Handling for unknown algorithm
    HashSet<String> unknownAlgorithms =
            new HashSet<String>(getUnknownAlgorithms(algorithms));
    if (!unknownAlgorithms.isEmpty()) {
      throw new UnknownAlgorithmException(
              ExceptionMessage.UNKNOWN_ALGORITHM + unknownAlgorithms.toString());
    }

    // Checking for a cycle in algorithms dependencies graph
    if (hasCycle(algorithms)) {
      throw new AlgorithmDependenciesException(ExceptionMessage.CYCLE_DEPENDENCIES);
    }

    return true;
  }

  /**
   * Check algorithm graph dependencies for unknowm algorithms
   * @param algorithms - List of Algorithm elements
   * @return true - All algorithms are known
   * @throws UnknownAlgorithmException
   */
  private HashSet<String> getUnknownAlgorithms(ArrayList<Algorithm> algorithms) {
    // All algorithms' name which present in list of algorithms
    HashSet<String> presentAlgorithms = new HashSet<String>();
    // All depent algorithms for each algorithm in list of algorithms
    HashSet<String> depentAlgorithms = new HashSet<String>();
    for (Algorithm algo : algorithms) {
      presentAlgorithms.add(algo.getName());
      depentAlgorithms.addAll(algo.getDependencies());
    }

    Debug.log("presentAlgorithms=" + presentAlgorithms.toString());
    Debug.log("depentAlgorithms=" + depentAlgorithms.toString());

    /**
     * After removing all present algorithms in depentAlgorithms will be only
     * unknown algorithms
     */
    depentAlgorithms.removeAll(presentAlgorithms);
    return depentAlgorithms;
  }

  /**
   * Check algorithm graph dependencies for a cycle
   * @param list - List of Algorithm elements
   * @return true - Cycle presents in algorithm dependencies graph
   */
  private boolean hasCycle(ArrayList<Algorithm> list) {
    // All algorithms' names which have already been checked
    HashSet<String> checkedAlgorithms = new HashSet<String>();
    // All algorithms' names which present in current algorithm's recursion stack
    HashSet<String> recStack = new HashSet<String>();

    Map<String, ArrayList<String>> algorithmsMap = new HashMap<String, ArrayList<String>>();
    for (Algorithm algo : list) {
      algorithmsMap.put(algo.getName(), algo.getDependencies());
    }

    for (Algorithm algo : list) {
      if(hasCycleUtil(algo.getName(), checkedAlgorithms, recStack, algorithmsMap)) return true;
    }

    return false;
  }

  /**
   * Check if current algorithm is a part of algorithm graph dependencies' cycle (depth first search)
   * @param algorithm - Current algorithm to check being a part of algorithm graph dependencies' cycle
   * @param checkedAlgorithms - All algorithms' names which have already been checked
   * @param recStack - All algorithms' names which present in current algorithm's recursion stack
   * @param algorithmsMap - Map for each algorithm its dependencies
   * @return true - Current algoritm is a part of a cycle in algorithm dependencies graph
   */
  private boolean hasCycleUtil(
          String algorithm,
          HashSet<String> checkedAlgorithms,
          HashSet<String> recStack,
          Map<String, ArrayList<String>> algorithmsMap) {
    if (!checkedAlgorithms.contains(algorithm)) {
      checkedAlgorithms.add(algorithm);
      recStack.add(algorithm);

      for (String algo : algorithmsMap.get(algorithm)) {
        if (!checkedAlgorithms.contains(algo) && hasCycleUtil(algo, checkedAlgorithms, recStack, algorithmsMap)) {
          return true;
        }

        if (recStack.contains(algo)) {
          return true;
        }
      }
    }

    recStack.remove(algorithm);
    return false;
  }

}
