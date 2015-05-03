package Utils.Exceptions;

import java.lang.Exception;

public class HalfEdgeIsNotValidException extends Exception {
  public HalfEdgeIsNotValidException() {
    super(ExceptionMessage.HALF_EDGE_IS_NOT_VALID);
  }
  public HalfEdgeIsNotValidException(String cause) {
    super(ExceptionMessage.HALF_EDGE_IS_NOT_VALID + " Caused by " + cause);
  }
}
