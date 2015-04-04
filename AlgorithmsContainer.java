import java.io.*;
import java.util.Map;
import java.util.HashMap;

public static class AlgorithmsContainer {
  
  public Map<String, Algorithm> algolist;

  public AlgorithmsContainer() {
    algolist = new HashMap<String, Algorithm>();
  }

  public Algorithm getAlgorithm(String name) {
    Algorithm a = algolist.get(name);
    return a;   
  }

  /*
  *  Output description for algorithms container
  *
  */
  public void getContainerDescription() {
    int sz = algolist.size();
    System.out.println("Algorithms container size = " + sz);
  }
}
