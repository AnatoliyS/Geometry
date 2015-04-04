import java.io.*;
import java.util.Map;
import java.util.HashMap;

public static class AlgorithmsContainerBuilder {
  
  private AlgorithmsContainer ac;

  private addAlgorithm(Algorithm algo) {
    Map<String, Algorithm> algolist = ac.algolist;
    algolist.put(algo.name, algo);
  }

  public AlgorithmsContainer getAlgorithmsContainer(ArrayList<Algorithm> list) {
    ac = new AlgorithmsContainer();

    // Adding algorithms in container
    for (Algorithm algo : list) {
      addAlgorithm(algo);
    }

    // TODO: Check algorithms and algorithms' tree for cycle
    
  }
}
