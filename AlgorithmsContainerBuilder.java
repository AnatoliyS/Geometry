import java.io.*;
import  java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import Utils.Exceptions.*;

public class AlgorithmsContainerBuilder {
  // Check algo graph dependencies for cycling
  private boolean hasCycle(ArrayList<Algorithm> list) {
    //TODO: Add realisation
    return false;
  }

  // Check algo graph dependencies for unknowm algorithms
  private boolean checkAlgoSet(ArrayList<Algorithm> list) {
    //TODO: Add realisation
    return true;
  }

  // TODO: Checks functions don't throw exceptions
  private boolean checkAlgoDependencies(ArrayList<Algorithm> list) throws AlgorithmDependenciesException, UnknownAlgorithmException {
    if (hasCycle(list)) {
      throw new AlgorithmDependenciesException(ExceptionMessage.CYCLE_DEPENDENCIES);
    }
    if (!checkAlgoSet(list)) {
      throw new AlgorithmDependenciesException(ExceptionMessage.UNKNOWN_ALGORITHM);
    }
    return true;
  }

  public AlgorithmsContainer getInstance(ArrayList<Algorithm> list) throws AlgorithmDependenciesException, UnknownAlgorithmException {
    // TODO: Check algorithms and algorithms' tree for cycle
    if (checkAlgoDependencies(list)) {
      Map<String, Algorithm> algorithmsMap = new HashMap<String, Algorithm>();
      for (Algorithm alg : list) {
        algorithmsMap.put(alg.getName(), alg);
      }
      AlgorithmsContainer ac = new AlgorithmsContainer(algorithmsMap);
      return ac;
    }
    return null;
  }
}
