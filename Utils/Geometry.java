package Utils;
import java.util.ArrayList;
import Utils.Exceptions.*;

public class Geometry {
  /**
   * Checks if value is = 0 with allowable error which is set
   * in Constants
   * @param double value
   * @return boolean True if value is = 0 with EPS error
   */
  public static boolean equalZero(double value) {
    return Math.abs(value) <= Constants.EPS;
  }

  /**
   * Cross product of vectors
   * http://en.wikipedia.org/wiki/Cross_product
   * @param Vector
   * @param Vector
   * @return double cross product
   */
  public static double crossProduct(Vector a, Vector b) {
    return a.getX()*b.getY() - a.getY()*b.getX();
  }
 
  /**
   * Scalar product of vectors
   * @param Vector
   * @param Vector
   * @return double scalar product
   */ 
  public static double scalarProduct(Vector a, Vector b) {
    return a.getX()*b.getX() + a.getY()*b.getY();
  }

  /**
   * Get Point of intersection of two Lines.
   * @param Line
   * @param Line
   * @throws NoIntersectionException if lines do not cross
   * @return Point Point of intersection
   */
  public static Point intersect(Line a, Line b) throws NoIntersectionException {
    double dir_product = crossProduct(b.direction, a.direction);
    // If lines are parallel
    if (equalZero(dir_product)) {
      throw new NoIntersectionException();
    } else {
      double product = crossProduct(a.direction, new Vector(a.first, b.first));
      double t = product / dir_product;
      double x = b.first.getX() + t * b.direction.getX();
      double y = b.first.getY() + t * b.direction.getY();
      return new Point(x, y);
    }
  }

  /**
   * Get Point of intersection of Line and Segment.
   * @param Line
   * @param Segment
   * @throws NoIntersectionException if they do not cross
   * @return Point Point of intersection
   */
  public static Point intersect(Line a, Segment b) throws NoIntersectionException {
    double dir_product = crossProduct(b.direction, a.direction);
    // If line and segment are parallel
    if (equalZero(dir_product)) {
      throw new NoIntersectionException();
    } else {
      double product = crossProduct(a.direction, new Vector(a.first, b.first));
      double t = product / dir_product;

      // Check that point lies on segment
      if (t < 0 || t > 1) {
        throw new NoIntersectionException();
      } else {
        double x = b.first.getX() + t * b.direction.getX();
        double y = b.first.getY() + t * b.direction.getY();
        return new Point(x, y);
      }
    }
  }

  /**
   * Get Point of intersection of Ray and Segment.
   * @param Ray
   * @param Segment
   * @throws NoIntersectionException if they do not cross
   * @return Point Point of intersection
   */
  public static Point intersect(Ray a, Segment b) throws NoIntersectionException {
    double dir_product = crossProduct(b.direction, a.direction);
    // If line and segment are parallel
    if (equalZero(dir_product)) {
      throw new NoIntersectionException();
    } else {
      double product = crossProduct(a.direction, new Vector(a.start, b.first));
      double t = product / dir_product;

      // Check that point lies on segment
      if (t < 0 || t > 1) {
        throw new NoIntersectionException();
      } else {
        double x = b.first.getX() + t * b.direction.getX();
        double y = b.first.getY() + t * b.direction.getY();
        Point p = new Point(x, y);
        
        // Check that point lies on ray
        // This is same check that parameter t for ray > 0
        if ((p.getX() - a.start.getX()) * a.direction.getX() < 0 ||
            (p.getY() - a.start.getY()) * a.direction.getY() < 0) {
          throw new NoIntersectionException();
        }
        
        return p;
      }
    }
  }
  
  /* 
   * Get IntersectionResult instance of intersection of two Lines
   * @param Line
   * @param Line
   * @return IntersectionResult
   */
  public static IntersectionResult getIntersection(Line a, Line b) {
    double dir_product = crossProduct(b.direction, a.direction);
    double product = crossProduct(a.direction, new Vector(a.first, b.first));

    // Variables for return result
    IntersectionType type;
    Pair<Point, Point> result;

    // If lines are parallel
    if (equalZero(dir_product)) {
      // If lines are equal
      if (equalZero(product)) {
        // INFINITY
        type = IntersectionType.INFINITY;
        double diffX = a.first.getX() - b.first.getX();
        if (equalZero(diffX)) {
          result = new Pair<Point, Point>(a.first, b.second);
        } else {
          result = new Pair<Point, Point>(a.first, b.first);
        }
      } else {
        // NO_INTERSECTION
        type = IntersectionType.NO_INTERSECTION;
        result = new Pair<Point, Point>(null, null);
      }
    } else {
      // POINT
      double t = product / dir_product;
      double x = b.first.getX() + t * b.direction.getX();
      double y = b.first.getY() + t * b.direction.getY();

      type = IntersectionType.POINT;
      Point p = new Point(x, y);
      result = new Pair<Point, Point>(p, null);
    }

    IntersectionResult interResult = new IntersectionResult(type, result);
    return interResult;
  }

  /**
   * Get IntersectionResult of intersection of Segment and Segment
   * @param Segment
   * @param Segment
   * @return IntersectionResult
   */
  public static IntersectionResult getIntersection(Segment a, Segment b) {
    Line lineA = new Line(a.getFirst(), a.getSecond());
    Line lineB = new Line(b.getFirst(), b.getSecond());

    // Intersection for according lines
    IntersectionResult lineIntersectionResult = getIntersection(lineA, lineB);
    IntersectionType lineIntersectionType = lineIntersectionResult.getType();

    // Variables for answer for segments' intersection
    IntersectionType type;
    Pair<Point, Point> result;

    // NO_INTERSECTION for lines
    if (lineIntersectionType == IntersectionType.NO_INTERSECTION) {
      type = IntersectionType.NO_INTERSECTION;
      result = new Pair<Point, Point>(null, null);
    } else {
      Pair<Point, Point> lineResult = lineIntersectionResult.getResult();

      // POINT for lines
      if (lineIntersectionType == IntersectionType.POINT) {
        Point p = lineResult.first;
        // Each segment contain point p
        if (a.onSegment(p) && b.onSegment(p)) {
          type = IntersectionType.POINT;
          result = new Pair<Point, Point>(p, null);
        } else {
          type = IntersectionType.NO_INTERSECTION;
          result = new Pair<Point, Point>(null, null);
        }
      } else {
        // INFINITY for lines

        // Get intersection of segments on line
        IntersectionResult interSegmentOnLine = getSegmentIntersectionOnLine(a, b);
        type = interSegmentOnLine.getType();
        result = interSegmentOnLine.getResult();
      }
    }

    IntersectionResult interResult = new IntersectionResult(type, result);
    return interResult;
  }

  /**
   * Helper function for getting intersection of two segments
   * which laying on the same line!!!
   * @param Segment a           = first segment
   * @param Segment b           = second segment
   * @return IntersectionResult = Instance of IntersectionResult which contain
   * intersection type and according to type result
   */
  private static IntersectionResult getSegmentIntersectionOnLine(Segment a, Segment b) {
    Point A0 = a.first;
    Point A1 = a.second;
    Point B0 = b.first;
    Point B1 = b.second;

    // TODO: Rewrite this code using swap function for wraping objects

    // Left point of segment a must have less x coordinate than right point
    if (A0.getX() > A1.getX()) {
      Point temp = A0;
      A0 = A1;
      A1 = temp;
    }

    // Left point of segment b must have less x coordinate than right point
    if (B0.getX() > B1.getX()) {
      Point temp = B0;
      B0 = B1;
      B1 = temp;
    }

    // Segment a must have less x coordinate than segment b
    if (A0.getX() > B0.getX()) {
      // swap A0 B0
      Point temp = A0;
      A0 = B0;
      B0 = temp;
      // swap A1 B1
      temp = A1;
      A1 = B1;
      B1 = temp;
    }

    Point A;
    Point B;
    a = new Segment(A0, A1);
    b = new Segment(B0, B1);

    IntersectionType type;
    Pair<Point, Point> result;

    // Segments haven't intersection on line
    if (!a.onSegment(B0)) {
      // NO_INTERSECTION
      type = IntersectionType.NO_INTERSECTION;
      result = new Pair<Point, Point>(null, null);
    } else {
      // Left point of result will be left point of segment b
      A = B0;

      // Define right point of result
      if (b.onSegment(A1)) {
        B = A1;
      } else {
        B = B1;
      }

      double diffX = A.getX() - B.getX();
      if (equalZero(diffX)) {
        // POINT
        type = IntersectionType.POINT;
        result = new Pair<Point, Point>(A, null);
      } else {
        // INFINITY
        type = IntersectionType.INFINITY;
        result = new Pair<Point, Point>(A, B);
      }
    }

    IntersectionResult interResult = new IntersectionResult(type, result);
    return interResult;
  }

  /**
   * Get left perpendicular vector of length 1 to given vector.
   * By left we mean that shortest rotataion from Vector d to
   * given vector will be to the left (crossProduct of them is greater 0)
   * @param Vector
   * @return Vector Left perpendicular normalized vector
   */
  public static Vector getLeftNormalizedPerpendicular(Vector d) {
    if (equalZero(d.getY())) {
      return new Vector(0.0, 1.0);
    } else {
      double dx_squared = d.getX() * d.getX();
      double dy_squared = d.getY() * d.getY();
      // To get right perpendicular one needs to write - sign before sqrt
      double x = Math.sqrt(1.0 / (1.0 + dx_squared / dy_squared));
      double y = - x * d.getX() / d.getY();
      return new Vector(x, y);
    }
  }

  /**
   * Exclude same (with EPS error) points from ArrayList.
   * @param ArrayList<Point> ArrayList which can store same (with EPS error)
   * points.
   * @return ArrayList<Point> ArralyList of unique points
   */
  public static ArrayList<Point> excludeSamePoints(ArrayList<Point> points) {
    ArrayList<Point> unique_points = new ArrayList<Point>();
    
    for (Point test_point : points) {
      boolean test_point_is_unique = true;
      
      for (Point unique_point : unique_points) {
        // Find difference in Points' coordinates
        double dx = test_point.getX() - unique_point.getX();
        double dy = test_point.getY() - unique_point.getY();

        if (equalZero(dx) && equalZero(dy)) {
          test_point_is_unique = false;
          break;
        }
      }

      if (test_point_is_unique) {
        unique_points.add(test_point);
      }
    }

    return unique_points;
  }

  public static Point getMiddlePoint(Segment s) {
    double x = (s.first.getX() + s.second.getX()) / 2.0;
    double y = (s.first.getY() + s.second.getY()) / 2.0;
    return new Point(x, y);
  }

  /**
   * Helper methods for calculating Minimum Area Polygon Algorithm
   */

  // TODO: Refactor some helper functions

  /**
   * Validate if polygon is simple (without internal intersections)
   * @param points = Points of polygon
   * @return
   */
  public static boolean isSimplePolygon(ArrayList<Point> points) {
    Debug.log("Checking isSimplePolygon for points started");
    ArrayList<Segment> edges = new ArrayList<Segment>();
    int countPoints = points.size();
    Point lastPoint = points.get(countPoints - 1);

    // Initialise as last point
    Point prevPoint = lastPoint;
    Point currPoint;

    // Process each edge
    for (int i = 0; i < countPoints; i++) {
      currPoint = points.get(i);
      Segment currEdge = new Segment(prevPoint, currPoint);

      Debug.log("\ni=" + i + " CurrEdge=" + currEdge);

      // Checking for intersection currEdge with each previous edges
      for (int j = 0; j < edges.size() - 1; j++) {
        Segment edge = edges.get(j);

        IntersectionResult interResult = getIntersection(currEdge, edge);
        IntersectionType type = interResult.getType();

        Debug.log("Intersection with edge=" + edge + " is " + interResult);

        // Handling intersection for last point
        if (i == countPoints - 1) {
          Pair<Point, Point> currInterResult = interResult.getResult();
          Pair<Point, Point> lastInterResult = new Pair<Point, Point>(lastPoint, null);
          if (type == IntersectionType.POINT && currInterResult.equals(lastInterResult)) {
            continue;
          }
        }

        // Found intersection
        if (type != IntersectionType.NO_INTERSECTION) {
          return false;
        }
      }

      // Add current edge to the list of edges
      edges.add(currEdge);
      Debug.log("Edges=" + edges);
      prevPoint = currPoint;
    }

    Debug.log("Edges=" + edges);

    Debug.log("Checking isSimplePolygon for points finished");
    return true;
  }

  /**
   * Validate if polygon is simple (without internal intersections)
   * @param polygon
   * @return
   */
  public static boolean isSimplePolygon(Polygon polygon) {
    Debug.log("Checking isSimplePolygon for polygon started");
    if (polygon == null) {
      Debug.log("::isSimplePolygon NullPointer polygon");
      return false;
    }

    if (polygon.isEmpty()) {
      Debug.log("::isSimplePolygon Empty polygon");
      return true;
    }

    ArrayList<Segment> edges = new ArrayList<>();
    int countPoints = polygon.getCountPoints();
    Point lastPoint = polygon.getPoint(countPoints - 1);

    // Initialise as last point
    Point prevPoint = lastPoint;
    Point currPoint;

    // Process each edge
    for (int i = 0; i < countPoints; i++) {
      currPoint = polygon.getPoint(i);
      Segment currEdge = new Segment(prevPoint, currPoint);

      Debug.log("\ni=" + i + " CurrEdge=" + currEdge);

      // Checking for intersection currEdge with each previous edges
      for (int j = 0; j < edges.size() - 1; j++) {
        Segment edge = edges.get(j);

        IntersectionResult interResult = getIntersection(currEdge, edge);
        IntersectionType type = interResult.getType();

        Debug.log("Intersection with edge=" + edge + " is " + interResult);

        // Handling intersection for last point
        if (i == countPoints - 1) {
          Pair<Point, Point> currInterResult = interResult.getResult();
          Pair<Point, Point> lastInterResult = new Pair<Point, Point>(lastPoint, null);
          if (type == IntersectionType.POINT && currInterResult.equals(lastInterResult)) {
            continue;
          }
        }

        // Found intersection
        if (type != IntersectionType.NO_INTERSECTION) {
          return false;
        }
      }

      // Add current edge to the list of edges
      edges.add(currEdge);
      Debug.log("Edges=" + edges);
      prevPoint = currPoint;
    }

    Debug.log("Edges=" + edges);

    Debug.log("Checking isSimplePolygon for polygon finished");
    return true;
  }

  /**
   * Check if segment, which created by one external point and one point of polygon, intersected with at
   * least one edge of polygon
   * @param externalPoint = Point which is lay outside of polygon
   * @param indexOfPoint = Index of polygon's point
   * @param points = Points of polygon
   * @return
   *  true = If segment intersected with edge of polygon, but not point with index indexOfPoint
   *  false = If segment doesn't intersected with each edge of polygon.
   *    If edge intersected with each edges only in point of polygon with index indexOfPoint
   */
  @Deprecated
  public static boolean isInternalIntersected(Point externalPoint, int indexOfPoint, ArrayList<Point> points) {
    Debug.log("isInternalIntersected started");
    Point polygonPoint = points.get(indexOfPoint);
    int countPoints = points.size();

    Segment segm = new Segment(externalPoint, polygonPoint);
    //Debug.log("Segment=" + segm + " points=" + points);
    for (int i = 0; i < countPoints; i++) {
      Point currPoint = points.get(i);
      Point nextPoint = points.get((i + 1) % countPoints);
      Segment edge = new Segment(currPoint, nextPoint);

      IntersectionResult interResult = getIntersection(segm, edge);
      IntersectionType type = interResult.getType();
      //Debug.log("\ni=" + i + " edge=" + edge + " interResult=" + interResult);
      if (type == IntersectionType.POINT) {
        Point p = interResult.getResult().first;
        if (!p.equals(polygonPoint)) {
          return true;
        }
      } else if (type == IntersectionType.INFINITY) {
        return true;
      } else {
        // Do nothing
      }
    }
    Debug.log("isInternalIntersected finished");
    return false;
  }

  /**
   * Check if segment, which created by one external point and one point of polygon, intersected with at
   * least one edge of polygon
   * @param externalPoint = Point which is lay outside of polygon
   * @param indexOfPoint = Index of polygon's point
   * @param polygon = Polygon
   * @return
   *  true = If segment intersected with edge of polygon, but not point with index indexOfPoint
   *  false = If segment doesn't intersected with each edge of polygon.
   *    If edge intersected with each edges only in point of polygon with index indexOfPoint
   */
  public static boolean isInternalIntersected(Point externalPoint, int indexOfPoint, Polygon polygon) {
    Debug.log("::isInternalIntersected started for polygon ");

    if (externalPoint == null) {
      Debug.log("::isInternalIntersected NullPointer externalPoint");
      return false;
    }

    if (polygon == null) {
      Debug.log("::isInternalIntersected NullPointer polygon");
      return false;
    }

    Point polygonPoint = polygon.getPoint(indexOfPoint);
    int countPoints = polygon.getCountPoints();

    Segment segm = new Segment(externalPoint, polygonPoint);
    //Debug.log("Segment=" + segm + " points=" + points);
    for (int i = 0; i < countPoints; i++) {
      Point currPoint = polygon.getPoint(i);
      Point nextPoint = polygon.getNextPoint(i);
      Segment edge = new Segment(currPoint, nextPoint);

      IntersectionResult interResult = getIntersection(segm, edge);
      IntersectionType type = interResult.getType();
      //Debug.log("\ni=" + i + " edge=" + edge + " interResult=" + interResult);
      if (type == IntersectionType.POINT) {
        Point p = interResult.getResult().first;
        if (!p.equals(polygonPoint)) {
          return true;
        }
      } else if (type == IntersectionType.INFINITY) {
        return true;
      } else {
        // Do nothing
      }
    }
    Debug.log("::isInternalIntersected finished for polygon");
    return false;
  }

  /**
   * Get internal intersection status for each point from arraylists
   * @param points1 = First polygon's points
   * @param points2 = Second polygon's points
   * @return
   *    result[i][j] = true : Found intersection for edge [point1[i], point2[j]] with at least one segment, which has
   *                        been created by one point from first polygon and another point from second polygon
   *    result[i][j] = false : Didn't found intersection for edge [point1[i], point2[j]]
   */
  @Deprecated
  public static boolean[][] getPIIS(ArrayList<Point> points1, ArrayList<Point> points2) {
    Debug.log("getPIIS started.");
    int countPoints1 = points1.size();
    int countPoints2 = points2.size();

    boolean[][] interStatus = new boolean[countPoints1][countPoints2];

    // Calculate status for each segment [point1[i]; points2[j]]
    for (int i = 0; i < countPoints1; i++) {
      Point p1 = points1.get(i);
      for (int j = 0; j < countPoints2; j++) {
        // Check segment [p1; points2[j]] for intersection with second polygon
        boolean status = isInternalIntersected(p1, j, points2);

        if (status == false) {
          Point p2 = points2.get(j);
          // Check segment [p2; points1[i]] for intersection with first polygon
          status = isInternalIntersected(p2, i, points1);
        }

        interStatus[i][j] = status;
      }
    }
    //Debug.log("interStatus=" + interStatus);
    Debug.log("getPIIS finished.");
    return interStatus;
  }

  /**
   * Get internal intersection status for each polygon's point
   * @param polygon1 = First polygon
   * @param polygon2 = Second polygon
   * @return
   *    result[i][j] = true : Found intersection for edge [point1[i], point2[j]] with at least one segment, which has
   *                        been created by one point from first polygon and another point from second polygon
   *    result[i][j] = false : Didn't found intersection for edge [point1[i], point2[j]]
   */
  public static boolean[][] getPolygonsInternalIntersectionStatus(Polygon polygon1, Polygon polygon2) {
    Debug.log("::getPolygonsInternalIntersectionStatus started.");
    int countPoints1 = polygon1.getCountPoints();
    int countPoints2 = polygon2.getCountPoints();

    boolean[][] interStatus = new boolean[countPoints1][countPoints2];

    // Calculate status for each segment [point1[i]; points2[j]]
    for (int i = 0; i < countPoints1; i++) {
      Point p1 = polygon1.getPoint(i);
      for (int j = 0; j < countPoints2; j++) {
        // Check segment [p1; points2[j]] for intersection with second polygon
        boolean status = isInternalIntersected(p1, j, polygon2);

        if (status == false) {
          Point p2 = polygon2.getPoint(j);
          // Check segment [p2; points1[i]] for intersection with first polygon
          status = isInternalIntersected(p2, i, polygon1);
        }

        interStatus[i][j] = status;
      }
    }
    //Debug.log("interStatus=" + interStatus);
    Debug.log("::getPolygonsInternalIntersectionStatus finished.");
    return interStatus;
  }

  /**
   * Calculate signed area of polygon
   * @param points = Polygon_'s points
   * @return Area value
   */
  public static double polygonSignedArea(ArrayList<Point> points) {
    double area = 0.0;

    int countPoints = points.size();
    for (int i = 0; i < countPoints; i++) {
      Point currPoint = points.get(i);
      Point nextPoint = points.get((i + 1) % countPoints);

      double diffX = currPoint.getX() - nextPoint.getX();
      double sumY = currPoint.getY() + nextPoint.getY();
      area += sumY * diffX;
    }

    area *= 0.5;
    return area;
  }

  /**
   * Calculate area of polygon
   * @param polygon = Polygon_'s points
   * @return Area value
   */
  public static double polygonArea(ArrayList<Point> points) {
    double area = polygonSignedArea(points);
    area = Math.abs(area);
    return area;
  }

  /**
   * Calculate area of polygon
   * @param polygon = Polygon_'s points
   * @return Area value
   */
  public static double polygonArea(Polygon polygon) {
    ArrayList<Point> points = polygon.getPoints();
    return polygonArea(points);
  }

  /**
   * Find first simple polygon for two polygons which contain point
   * from first polygon : points1[indexOfPoint1] and next after that point in clockwise order;
   * from second polygon : points2[indexOfPoint2] and point after that in counterclockwise order until first point which
   * accept simple polygon condition
   * @param points1 = First polygon's points
   * @param indexOfPoint1 = Index of start point in first polygon
   * @param points2 = Second polygon's points
   * @param indexOfPoint2 = Index of start point in second polygon
   * @param intersectionStatus = Internal intersection status between two polygons
   * @return
   *  First simple polygon
   *  If first simple doesn't exist return null
   */
  @Deprecated
  public static ArrayList<Point> getFirstSimplePolygon(final ArrayList<Point> points1, final int indexOfPoint1,
                                                       final ArrayList<Point> points2, final int indexOfPoint2,
                                                       final boolean[][] intersectionStatus) {
    Debug.log("\ngetFirstSimplePolygon started.");

    Debug.log("indexOfPoint1=" + indexOfPoint1 + " indexOfPoint2=" + indexOfPoint2);

    // Result polygon
    ArrayList<Point> polygon;

    // Segment [points1[indexOfPoint1]; points2[indexOfPoint2]] has internal intersection with at least one of polygon
    if (intersectionStatus[indexOfPoint1][indexOfPoint2] == false) {

      int countPoints1 = points1.size();
      int countPoints2 = points2.size();

      Point currPoint1 = points1.get(indexOfPoint1);
      Point currPoint2 = points2.get(indexOfPoint2);

      int indexOfNextPoint1 = (indexOfPoint1 + 1) % countPoints1;
      Point nextPoint1 = points1.get(indexOfNextPoint1);

      Segment segmentUp = new Segment(currPoint1, currPoint2);

      int index = (indexOfPoint2 - 1 + countPoints2) % countPoints2;
      while (index != indexOfPoint2) {
        // If down segment hasn't internal intersection
        if (intersectionStatus[indexOfNextPoint1][index] == false /*&& intersectionStatus[indexOfPoint1][index] == false*/) {
          Point p = points2.get(index);
          Segment segmentDown = new Segment(nextPoint1, p);

          IntersectionResult interResult = getIntersection(segmentDown, segmentUp);
          IntersectionType type = interResult.getType();

          // Segments haven't intersection => Found first simple polygon
          if (type == IntersectionType.NO_INTERSECTION) {
            break;
          }
        }
        // Calculate index
        index = (index - 1 + countPoints2) % countPoints2;
      }

      // Doesn't exist simple polygon
      if (index == indexOfPoint2) {
        Debug.log("Simple polygon doesn't exist for:" + "\npoints1=" + points1 + " indexOfPoint1=" + indexOfPoint1
                + "\npoints2=" + points2 + " indexOfPoint2=" + indexOfPoint2);
        return null;
      }

      /** Build simple polygon */
      polygon = new ArrayList<>();

      // Add 1-st current point
      polygon.add(currPoint1);
      // Add points from 2-nd polygon [indexOfPoint2, indexOfPoint2-1, ..., index] % (countPoints2)
      for (int i = indexOfPoint2; i != index; i = (i - 1 + countPoints2) % countPoints2) {
        polygon.add(points2.get(i));
      }
      polygon.add(points2.get(index));
      // Add next point for current point of 1-st polygon
      polygon.add(nextPoint1);
    } else {
      Debug.log("Simple polygon doesn't exist because found internal intersection for indexes:" +
              "\nindex1=" + indexOfPoint1 + " index2=" + indexOfPoint2);
      polygon = null;
    }
    Debug.log("Result polygon=" + polygon);
    Debug.log("getFirstSimplePolygon finished.");
    return polygon;
  }

  /**
   * Find first simple polygon for two polygons which contain point
   * from first polygon : points1[indexOfPoint1] and next after that point in clockwise order;
   * from second polygon : points2[indexOfPoint2] and point after that in counterclockwise order until first point which
   * accept simple polygon condition
   * @param polygon1 = First polygon
   * @param indexOfPoint1 = Index of start point in first polygon
   * @param polygon2 = Second polygon
   * @param indexOfPoint2 = Index of start point in second polygon
   * @param intersectionStatus = Internal intersection status between two polygons
   * @return
   *  First simple polygon
   *  If first simple doesn't exist return null
   */
  public static Polygon getFirstSimplePolygon(final Polygon polygon1, final int indexOfPoint1,
                                              final Polygon polygon2, final int indexOfPoint2,
                                              final boolean[][] intersectionStatus) {
    Debug.log("\n::getFirstSimplePolygon started.");

    Debug.log("Polygon1=" + polygon1);
    Debug.log("Polygon2=" + polygon2);
    Debug.log("indexOfPoint1=" + indexOfPoint1 + " indexOfPoint2=" + indexOfPoint2);

    if (polygon1 == null) {
      Debug.log("-----::getFirstSimplePolygon NullPointer polygon1.");
      return null;
    }

    if (polygon2 == null) {
      Debug.log("-----::getFirstSimplePolygon NullPointer polygon2.");
      return null;
    }


    // Result polygon
    Polygon polygon;
    ArrayList<Point> polygonPoints;

    // Segment [points1[indexOfPoint1]; points2[indexOfPoint2]] has internal intersection with at least one of polygon
    if (intersectionStatus[indexOfPoint1][indexOfPoint2] == false) {

      int countPoints1 = polygon1.getCountPoints();
      int countPoints2 = polygon2.getCountPoints();

      if (countPoints1 == 1) {

      }

      if (countPoints2 == 1) {

      }

      Point currPoint1 = polygon1.getPoint(indexOfPoint1);
      Point currPoint2 = polygon2.getPoint(indexOfPoint2);

      int indexOfNextPoint1 = (indexOfPoint1 + 1) % countPoints1;
      Point nextPoint1 = polygon1.getPoint(indexOfNextPoint1);

      Segment segmentUp = new Segment(currPoint1, currPoint2);

      int index = (indexOfPoint2 - 1 + countPoints2) % countPoints2;

      while (index != indexOfPoint2) {
        // If down segment hasn't internal intersection
        if (intersectionStatus[indexOfNextPoint1][index] == false) {
          Point p = polygon2.getPoint(index);
          Segment segmentDown = new Segment(nextPoint1, p);

          IntersectionResult interResult = getIntersection(segmentDown, segmentUp);
          IntersectionType type = interResult.getType();

          // Segments haven't intersection => Found first simple polygon
          if (type == IntersectionType.NO_INTERSECTION) {
            break;
          } else if (type == IntersectionType.POINT) {
            Point interPoint = interResult.getResult().first;
            if (currPoint1.equals(interPoint)) {
              Debug.log("-----Watch to this case!!!!! One point in first polygon?");
              break;
            }
          }
        }
        // Calculate index
        index = (index - 1 + countPoints2) % countPoints2;
      }

      // Doesn't exist simple polygon
      if (index == indexOfPoint2) {
        Debug.log("-----ERROR We mustn't be there: Simple polygon doesn't exist for:");
        Debug.log("polygon1=" + polygon1 + " indexOfPoint1=" + indexOfPoint1);
        Debug.log("polygon2=" + polygon2 + " indexOfPoint2=" + indexOfPoint2);
        return null;
      }

      /** Build simple polygon bypassing in clockwise*/
      polygonPoints = new ArrayList<>();

      // Add 1-st current point
      polygonPoints.add(currPoint1);
      // Add points from 2-nd polygon [indexOfPoint2, indexOfPoint2-1, ..., index] % (countPoints2)
      for (int i = indexOfPoint2; i != index; i = (i - 1 + countPoints2) % countPoints2) {
        polygonPoints.add(polygon2.getPoint(i));
      }
      polygonPoints.add(polygon2.getPoint(index));
      // Add next point for current point of 1-st polygon
      if (!currPoint1.equals(nextPoint1)) {
        polygonPoints.add(nextPoint1);
      } else {
        // Do nothing
      }

      polygon = new Polygon(polygonPoints, ByPassType.CLOCKWISE);
    } else {
      Debug.log("-----Simple polygon doesn't exist because found internal intersection for indexes:" +
              "\nindex1=" + indexOfPoint1 + " index2=" + indexOfPoint2);
      polygon = null;
    }
    Debug.log("Result polygon=" + polygon);
    Debug.log("::getFirstSimplePolygon finished.");
    return polygon;
  }

  @Deprecated
  public static ArrayList<Point> getMinimalAreaIntermediatePolygon(final ArrayList<Point> points1,
                                                                   final ArrayList<Point> points2) {
    Debug.log("getMinimalAreaIntermediatePolygon started.");

    // Precalculate intersection status
    boolean[][] interStatus = getPIIS(points1, points2);

    int countPoints1 = points1.size();
    int countPoints2 = points2.size();

    ArrayList<Point> polygon = null;
    double area = Double.POSITIVE_INFINITY;
    for (int indexOfPoint1 = 0; indexOfPoint1 < countPoints1; indexOfPoint1++) {
      for (int indexOfPoint2 = 0; indexOfPoint2 < countPoints2; indexOfPoint2++) {
        ArrayList<Point> tempPolygonPoints = getFirstSimplePolygon(points1, indexOfPoint1, points2, indexOfPoint2, interStatus);
        /******/
        if (tempPolygonPoints != null) {
          Polygon tempPolygon = new Polygon(tempPolygonPoints);
          double tempArea = polygonArea(tempPolygon);
          Debug.log("Temp area=" + tempArea);
          if (area > tempArea) {
            area = tempArea;
            polygon = tempPolygonPoints;
            Debug.log("Optimize to area=" + area);
          }
        }
      }
    }

    if (Math.abs(area - Double.POSITIVE_INFINITY) < Constants.EPS){
      Debug.log("Polygon_ doesn't exist for each pairs!");
    }

    Debug.log("Result polygon=" + polygon);
    Debug.log("getMinimalAreaIntermediatePolygon finished.");
    return polygon;
  }

  /**
   * Find minimum area simple polygon aproximation between two polygons
   * @param polygon1 = First polygon
   * @param polygon2 = Second polygon
   * @return Polygon with minimum area between two polygons
   */
  public static Polygon getMinimalAreaIntermediatePolygon(final Polygon polygon1, final Polygon polygon2) {
    Debug.log("::getMinimalAreaIntermediatePolygon started.");

    // Precalculate intersection status
    boolean[][] interStatus = getPolygonsInternalIntersectionStatus(polygon1, polygon2);

    int countPoints1 = polygon1.getCountPoints();
    int countPoints2 = polygon2.getCountPoints();

    Polygon polygon = null;
    double area = Double.POSITIVE_INFINITY;
    for (int indexOfPoint1 = 0; indexOfPoint1 < countPoints1; indexOfPoint1++) {
      for (int indexOfPoint2 = 0; indexOfPoint2 < countPoints2; indexOfPoint2++) {
        Polygon tempPolygon = getFirstSimplePolygon(polygon1, indexOfPoint1,
                polygon2, indexOfPoint2, interStatus);
        if (tempPolygon != null) {
          double tempArea = polygonArea(tempPolygon);
          Debug.log("Temp area=" + tempArea);
          if (area > tempArea) {
            area = tempArea;
            polygon = tempPolygon;
            Debug.log("Optimize to area=" + area);
          }
        }
      }
    }

    if (Math.abs(area - Double.POSITIVE_INFINITY) < Constants.EPS){
      Debug.log("::getMinimalAreaIntermediatePolygon Polygon doesn't exist for each pairs!");
      polygon = null;
    }

    Debug.log("Result polygon=" + polygon);
    Debug.log("::getMinimalAreaIntermediatePolygon finished.");
    return polygon;
  }

  public static Polygon getMinimalAreaIntermediatePolygonResult(final Polygon polygon1, final Polygon polygon2) {
    Debug.log("\n::getMinimalAreaIntermediatePolygon started.");
    Debug.log("Polygon1=" + polygon1);
    Debug.log("Polygon2=" + polygon2);
    // Precalculate intersection status
    boolean[][] interStatus = getPolygonsInternalIntersectionStatus(polygon1, polygon2);

    int countPoints1 = polygon1.getCountPoints();
    int countPoints2 = polygon2.getCountPoints();

    Integer indexFrom1 = -1;
    Integer indexTo1 = -1;
    Integer indexFrom2 = -1;
    Integer indexTo2 = -1;

    Polygon polygon = null;
    double area = Double.POSITIVE_INFINITY;
    for (int indexOfPoint1 = 0; indexOfPoint1 < countPoints1; indexOfPoint1++) {
      for (int indexOfPoint2 = 0; indexOfPoint2 < countPoints2; indexOfPoint2++) {
        Polygon tempPolygon = getFirstSimplePolygon(polygon1, indexOfPoint1, polygon2, indexOfPoint2, interStatus);
        if (tempPolygon != null) {
          double tempArea = polygonArea(tempPolygon);
          Debug.log("Temp area=" + tempArea);
          if (area > tempArea + Constants.EPS) {
            area = tempArea;
            polygon = tempPolygon;

            int countPoints = polygon.getCountPoints();
            indexFrom1 = (indexOfPoint1 + 1) % countPoints1;
            indexTo1 = indexOfPoint1;

            indexFrom2 = indexOfPoint2;
            //indexTo2 = ((indexOfPoint2 - countPoints - 1) % countPoints2 + countPoints2) % countPoints2;
            int stepCounterClockwise = countPoints - 3;
            indexTo2 = ((indexOfPoint2 - stepCounterClockwise) % countPoints2 + countPoints2) % countPoints2;

            Debug.log("Optimize to area=" + area);
          }
        }
      }
    }

    if (Math.abs(area - Double.POSITIVE_INFINITY) < Constants.EPS){
      Debug.log("-----::getMinimalAreaIntermediatePolygon Polygon doesn't exist for each pairs!");
      polygon = null;
    } else {
      Debug.log("-----::getMinimalAreaIntermediatePolygon Building minimum polygon for indexes");
      Debug.log("indexes1=[" + indexFrom1 + ", " + indexTo1 + "]");
      Debug.log("indexes2=[" + indexFrom2 + ", " + indexTo2 + "]");

      /** Build minimum area polygon after merging */
      ArrayList<Point> points = new ArrayList<>();
      Point p;

      // Add points from 1-st polygon
      Debug.log("Add points from 1-st polygon:");
      for (int index = indexFrom1; index != indexTo1; index = (index + 1) % countPoints1) {
        p = polygon1.getPoint(index);
        points.add(p);
        Debug.log("+ " + p);
      }
      p = polygon1.getPoint(indexTo1);
      points.add(p);
      Debug.log("+ " + p);

      // Add points from 2-st polygon
      Debug.log("Add points from 2-nd polygon:");
      for (int index = indexFrom2; index != indexTo2; index = (index + 1) % countPoints2) {
        p = polygon2.getPoint(index);
        points.add(p);
        Debug.log("+ " + p);
      }
      p = polygon2.getPoint(indexTo2);
      points.add(p);
      Debug.log("+ " + p);

      //polygon = new Polygon(points, ByPassType.CLOCKWISE);
      if (!Geometry.isSimplePolygon(points)) {
        Debug.log("*****ERROR: Complex polygon");
      }


      ByPassType type = getByPassingTypePolygon(points);
      if (type == ByPassType.COUNTERCLOCKWISE) {
        Debug.log("*****ERROR: Counterclockwise by passing found5 for points=" + points);

        // Reverse points
        int countPoints = points.size();
        for (int i = 0; i < countPoints / 2; i++) {
          Point A = new Point(points.get(i));
          Point B = new Point(points.get(countPoints - 1 - i));
          points.set(i, B);
          points.set(countPoints - 1 - i, A);
        }
      }

      polygon = new Polygon(points, ByPassType.CLOCKWISE);
    }

    Debug.log("*****Result polygon=" + polygon);
    Debug.log("::getMinimalAreaIntermediatePolygon finished.");
    return polygon;
  }

  @Deprecated
  private static boolean isRightPointTriplet(Point A, Point B, Point C) {
    Vector v1 = new Vector(A, B);
    Vector v2 = new Vector(B, C);
    double product = Geometry.crossProduct(v1, v2);
    return (product <= -Constants.EPS);
  }

  private static ByPassType getByPassingTypePolygon(ArrayList<Point> points) {
    double signArea = polygonSignedArea(points);
    ByPassType type;
    if (signArea > 0) {
      type = ByPassType.COUNTERCLOCKWISE;
    } else {
      type = ByPassType.CLOCKWISE;
    }
    return type;
  }
}
