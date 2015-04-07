import java.io.*;
import java.lang.String;
import java.lang.Override;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import Utils.*;
import Utils.Exceptions.*;

public class AlgorithmsContainer {
  
  private Map<String, Algorithm> algorithmsMap;

  private AlgorithmsContainer(AlgorithmsContainerBuilder builder) {
    this.algorithmsMap = new HashMap<String, Algorithm>(builder.algorithmsMap);
  }

  public Algorithm getAlgorithm(String name) throws UnknownAlgorithmException {
    Algorithm algo = algorithmsMap.get(name);
    if (algo == null) {
      throw new UnknownAlgorithmException(ExceptionMessage.UNKNOWN_ALGORITHM + name);
    }
    return algo;
  }

  @Override
  public String toString() {
    String s = "Container [";
    for (String nameAlgo : algorithmsMap.keySet())
      s += nameAlgo + ", ";
    s += "]";
    return s;
  }

  /**
   * Class for building AlgorithmsContainer instances
   */
  public static class AlgorithmsContainerBuilder {

    private Map<String, Algorithm> algorithmsMap;

    public AlgorithmsContainerBuilder() {
      algorithmsMap = new HashMap<String, Algorithm>();
    }

    public AlgorithmsContainerBuilder addAlgorithm(Algorithm algo) {
      algorithmsMap.put(algo.getName(), algo);
      return this;
    }

    /**
     * Build instance of AlgorithmsContainer
     * @return Instance of AlgorithmsContainer
     * @throws AlgorithmDependenciesException
     * @throws UnknownAlgorithmException
     */
    public AlgorithmsContainer buildContainer()
            throws AlgorithmDependenciesException, UnknownAlgorithmException {
      AlgorithmsContainer algoContainer = new AlgorithmsContainer(this);
      validateAlgorithmsContainer(algoContainer);
      return algoContainer;
    }

    /**
     * Validating algorithm container instance
     * @param algoContainer - Instance of AlgorithmsContainer
     * @throws AlgorithmDependenciesException
     * @throws UnknownAlgorithmException
     */
    private void validateAlgorithmsContainer(AlgorithmsContainer algoContainer)
            throws AlgorithmDependenciesException, UnknownAlgorithmException {
      Map<String, Algorithm> algorithmsMap
              = new HashMap<String, Algorithm>(algoContainer.algorithmsMap);

      // List of algorithm's instances which present in container
      ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
      for (String algoName : algorithmsMap.keySet()) {
        Algorithm algo = algoContainer.getAlgorithm(algoName);
        algorithms.add(algo);
      }

      // Handling for unknown algorithms
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
    }

    /**
     * Helpers for validation AlgorithmsContainer instance
     */

    /**
     * Getting all unkwons algorithms from graph dependencies
     * @param algorithms - List of Algorithm elements
     * @return Data structure of all unknown algorithms
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
        if(hasCycleUtil(algo.getName(), checkedAlgorithms, recStack, algorithmsMap)) {
          return true;
        }
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

}
