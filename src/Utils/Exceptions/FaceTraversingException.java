package Utils.Exceptions;

import java.lang.Exception;

public class FaceTraversingException extends Exception {
  public FaceTraversingException() {
    super(ExceptionMessage.FACE_TRAVERSING_FAIL);
  }
  public FaceTraversingException(String cause) {
    super(ExceptionMessage.FACE_TRAVERSING_FAIL + "\n " + cause);
  }
}
