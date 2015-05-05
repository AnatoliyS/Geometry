package Utils.Exceptions;

import java.lang.Exception;

public class VoronoiBuildingException extends AlgorithmRuntimeException {
  public VoronoiBuildingException() {
    super(ExceptionMessage.VORONOI_BUILDING_EXCEPTION);
  }
  public VoronoiBuildingException(String cause) {
    super(ExceptionMessage.VORONOI_BUILDING_EXCEPTION + " Caused by " + cause);
  }
}
