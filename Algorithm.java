import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;

/*
* T - Type of Algorithm result. 
* Example : For ConvexHullAlgo is type ConvexHull.
*/
public abstract class Algorithm<T> {

  private static String name;
  private static ArrayList<String> dependencies;
  
  public Algorithm(String _name, ArrayList<String> _deps) {
    name = _name;
    dependencies = _deps;
  }

  public abstract Object merge(Object left, Object right); 
  public abstract void render(Object result, Graphics g);

}
