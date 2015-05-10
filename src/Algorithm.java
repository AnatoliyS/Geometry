import java.lang.String;
import java.util.ArrayList;
import Utils.*;
import Utils.Exceptions.*;

public abstract class Algorithm {

  private String name;
  private ArrayList<String> dependencies;
  
  public Algorithm(String _name, ArrayList<String> _deps) {
    name = _name;
    dependencies = _deps;
  }

  public String getName() {
    return name;
  }

  public ArrayList<String> getDependencies() {
    return dependencies;
  }

  // We need exactly DACNode nodes for getting result using nodes' data
  public abstract Object merge(DACNode left, DACNode right) 
    throws NoDataException, AlgorithmRuntimeException;
  public abstract boolean isTrivialCase(int count);
  public abstract Object doTrivialCase(ArrayList<Point> points);

  @Override
  public String toString() {
    return name;
  }
}
