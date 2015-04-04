import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import Utils.*;
import Utils.Exceptions.*;

public class DACNode {
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
}
