package Utils;

import java.lang.Override;
import java.lang.String;

public class Pair<L, R>{
  public L first;
  public R second;

  public Pair(L _first, R _second){
    first = _first;
    second = _second; 
  }

  @Override
  public String toString() {
    String s = new String();
    s += (first == null) ? "null" : first.toString();
    s += ", ";
    s += (second == null) ? "null" : second.toString();
    s = "[" + s + "]";
    return s;
  }
}
