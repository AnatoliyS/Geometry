package Utils.Exceptions;

import java.lang.Exception;

public final class ExceptionMessage {
  public static final String NO_INTERSECTION = "No intersection ";
  public static final String NO_DATA = "No data result in DACNode instance ";
  public static final String CYCLE_DEPENDENCIES = "Can't build AlgorithmContainer : dependencies cycle had found ";
  public static final String UNKNOWN_ALGORITHM = "Unknown algorithm had found ";
  public static final String HALF_EDGE_IS_NOT_VALID = "Some of fields of Half Edge structure are NULL. Make sure you initialized all fields.";
}
