package Utils.Exceptions;

import java.lang.Exception;

public class VoronoiHalfEdgeIsNotValidException extends Exception {
  public VoronoiHalfEdgeIsNotValidException() {
    super(ExceptionMessage.HALF_EDGE_IS_NOT_VALID);
  }
  public VoronoiHalfEdgeIsNotValidException(String cause) {
    super(ExceptionMessage.HALF_EDGE_IS_NOT_VALID + " Caused by " + cause);
  }
}
