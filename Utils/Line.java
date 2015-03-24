package Utils;

public class Line{
  private double A;
  private double B;
  private double C;

  public Line(Point a, Point b){
    double xa = a.getX();
    double ya = a.getY();
    double xb = b.getX();
    double yb = b.getY();
    A = yb - ya;
    B = xa - xb;
    C = ya*(xb - xa) - xa*(yb - ya);
  }
  
  public boolean onLine(Point p){
    return (Math.abs(A*p.getX() + B*p.getY() + C) <= Constants.EPS);
  }

  public boolean pointIsBelowLine(Point p){
    Debug.log("Cheking point" + p.toString());
    Debug.log("Is below = " + (p.getY() < (-A*p.getX() - C) / B) + "y = " + (-A*p.getX() - C) / B);
    return (p.getY() < (-A*p.getX() - C) / B);
  }
  
  public boolean pointIsAboveLine(Point p){
    return (p.getY() > (-A*p.getX() - C) / B);
  }

}
