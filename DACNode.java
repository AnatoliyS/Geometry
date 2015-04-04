import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import Utils.*;


public class DACNode {
  //private DACNode lNode, rNode;
  //private ArrayList<Point> points;
  private Map<String, Object> data;

  public DACNode() {
    lNode = null;
    rNode = null;
    points = new ArrayList<Point>();
    data = new HashMap<String, Object>();
  }

  public DACNode(DACNode l, DACNode r, ArrayList<Point> _points) {
    lNode = l;
    rNode = r;
    points = new ArrayList<Point>(_points);
    data = new HashMap<String, Object>();
  }

  public Object getDataResult(String name) {
      Object obj = data.get(name);
      return obj;
/*    try {
      Object obj = data.get(name);
      return obj;
    } catch (NoDataException ex) {
      throw new Exception("No data for algorithm " + name);
    }
*/
  }

  public void setDataResult(String name, Object result) {
    data.put(name, result);
  }
  
  public void outputDescription() {
    Debug.log("DACNode contains points=" + points);

    for (String name : data.keySet())
      Debug.log("DACNode contain data for name=" + name + " dataResult=" + getDataResult(name));
  }
}
