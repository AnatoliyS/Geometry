package Utils.Exceptions;

import java.lang.Exception;

public final class ExceptionMessage {
  public static final String NO_INTERSECTION = "No intersection ";
  public static final String NO_DATA = "No data result in DACNode instance ";
  public static final String CYCLE_DEPENDENCIES = "Can't build AlgorithmContainer : dependencies cycle had found ";
  public static final String UNKNOWN_ALGORITHM = "Unknown algorithm had found ";
  public static final String HALF_EDGE_IS_NOT_VALID = "Some of fields of Half Edge structure are NULL. Make sure you initialized all fields.";
  public static final String VORONOI_BUILDING_EXCEPTION = "Exception in VORONOI MERGE.";
  public static final String FACE_TRAVERSING_FAIL
    = "Can't get to start edge during Face traversing. Link incidentEdge might not be correct.";
  public static final String ALGO_RUNTIME_EXCEPTION = "Exception happend while preforming algortithm.";
}
