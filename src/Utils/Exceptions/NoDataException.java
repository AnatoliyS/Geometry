package Utils.Exceptions;

import java.lang.Exception;

public class NoDataException extends Exception {
  public NoDataException() {
    super(ExceptionMessage.NO_DATA);
  }
}
