package Utils.Exceptions;

import java.lang.Exception;

public class UnknownAlgorithmException extends Exception {
  public UnknownAlgorithmException(String errorMessage) {
    super(errorMessage);
  }
}
