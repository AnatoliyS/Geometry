import java.io.*;
import java.lang.Override;
import java.lang.String;
import java.util.Map;
import java.util.HashMap;
import Utils.Exceptions.*;

public class AlgorithmsContainer {
  
  private Map<String, Algorithm> algorithmsMap;

  public AlgorithmsContainer(Map<String, Algorithm> _algorithmsMap) {
    this.algorithmsMap = new HashMap<String, Algorithm>(_algorithmsMap);
  }

  public Algorithm getAlgorithm(String name) throws UnknownAlgorithmException {
    Algorithm a = algorithmsMap.get(name);
    if (a == null) {
      throw new UnknownAlgorithmException(ExceptionMessage.UNKNOWN_ALGORITHM + name);
    }
    return a;
  }

  @Override
  public String toString() {
    String s = "Contain [";
    for (String nameAlgo : algorithmsMap.keySet())
      s += nameAlgo + ", ";
    s += "]";
    return s;
  }
}
