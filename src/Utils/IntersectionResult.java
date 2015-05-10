package Utils;

import java.lang.Override;
import java.lang.String;

public class IntersectionResult {

  private IntersectionType type;
  private Pair<Point, Point> result;

  public IntersectionResult(IntersectionType _type, Pair<Point, Point> _result) {
    this.type = _type;
    this.result = _result;
  }

  public IntersectionType getType() {
    return type;
  }

  public Pair<Point, Point> getResult() {
    return result;
  }

  @Override
  public String toString() {
    String s = new String();
    s += "type=" + type.toString();
    s += " result=" + result.toString();
    s = "{" + s + "}";
    return s;
  }

}
