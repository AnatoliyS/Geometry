import java.lang.Override;
import java.lang.String;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import Utils.*;
import Utils.Exceptions.*;

public class DACNode {

  /**
   * Don't need use pointers for left and right nodes,
   * because all instance of this DACNode will be saved
   * in array. So we can determinate sun nodes by index
   * of current node.
   */
  private Map<String, Object> data;

  public DACNode() {
    data = new HashMap<String, Object>();
  }

  public Object getDataResult(String name) throws NoDataException {
    if (!data.containsKey(name)) {
      throw new NoDataException();
    } else {
      Object obj = data.get(name);
      return obj;
    }
  }

  public void setDataResult(String name, Object result) {
    data.put(name, result);
  }

  public void outputDescription() throws NoDataException {
    for (String name : data.keySet())
      Debug.log("DACNode contain data for name=" + name + " dataResult=" + getDataResult(name));
  }

  public String toString() {
    String s = "Node=" + data.toString() + "\n";
    return s;
  }

}
