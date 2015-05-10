package Utils.Exceptions;

import java.lang.Exception;

/**
 * General runtime exception for every algorithm
 */
public class AlgorithmRuntimeException extends Exception {
  public AlgorithmRuntimeException() {
    super(ExceptionMessage.ALGO_RUNTIME_EXCEPTION);
  }
  public AlgorithmRuntimeException(String cause) {
    super(ExceptionMessage.ALGO_RUNTIME_EXCEPTION + " Caused by " + cause);
  }
}
