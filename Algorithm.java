import java.io.*;
import java.lang.String;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import Utils.*;

public abstract class Algorithm {

  private String name;
  private ArrayList<String> dependencies;
  
  public Algorithm(String _name, ArrayList<String> _deps) {
    name = _name;
    dependencies = _deps;
  }

  public String getName() {
    return name;
  }

  public ArrayList<String> getDependencies() {
    return dependencies;
  }

  public abstract Object merge(Object left, Object right); 
  public abstract void render(Object result, Graphics g);
  // TODO:
  //public abstract boolean isTrivialCase(int count);
  //public abstract void doTrivialCase(DACNode node);

  @Override
  public String toString() {
    String s = name;
    return s;
  }
}
