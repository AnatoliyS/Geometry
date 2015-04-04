package Utils.Exceptions;

import java.lang.Exception;

public class NoIntersectionException extends Exception {
  public NoIntersectionException() {
    super(ExceptionMessage.NO_INTERSECTION);
  }
}
