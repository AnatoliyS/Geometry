package Utils;

public class Point{
  private double x;
  private double y;

  public Point(double _x, double _y){
    x = _x;
    y = _y; 
  }
  public double getX(){
    return x;
  }
  public double getY(){
    return y;
  }
  public void setX(double _x){
    x = _x;
  }
  public void setY(double _y){
    y = _y;
  }

  @Override
  public String toString(){
    String s = new String();
    s = "[" + x + ", " + y + "]";
    return s; 
  }

}
