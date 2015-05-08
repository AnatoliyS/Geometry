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
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || (obj.getClass() != this.getClass())) {
      return false;
    }

    Pair<L, R> p = (Pair<L, R>)obj;
    return ((first == null && p.first == null) || (first != null && first.equals(p.first)))
            && ((second == null && p.second == null) || (second != null && second.equals(p.second)));
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
