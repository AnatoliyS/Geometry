package Utils;

import java.util.ArrayList;

public class Permutation {
  ArrayList<Integer> a;
  int n;

  public Permutation(int count) {
    initialise(count);
  }

  public Permutation(ArrayList<Integer> perm) {
    this.a = new ArrayList<>(perm);
    this.n = perm.size();
  }

  private void initialise(int count) {
    this.a = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      this.a.add(i);
    }
    this.n = count;
  }

  public ArrayList<Integer> getOrder() {
    return a;
  }

  public boolean nextPermutation() {
    Debug.log("Permutation::nextPermutation started");
    Debug.log("a=" + a.toString());
    // Result
    boolean fIsExistNext;

    // Index of first element in array c[i] < c[i + 1]
    int i = findFirstWrong();
    Debug.log("i=" + i);

    if (i != -1) {
      int x = a.get(i);
      int j = findFirstHigher(x);

      // Swap i-th and j-th elements
      swapByIndexes(i, j);

      // Reverse tail
      reverse(i + 1, n - 1);
      fIsExistNext = true;
    } else {
      fIsExistNext = false;
    }

    Debug.log("a=" + a.toString());
    Debug.log("Permutation::nextPermutation finished");
    return fIsExistNext;
  }

  private int findFirstWrong() {
    int index = n - 2;
    while (a.get(index) >= a.get(index + 1)) {
      index--;
      if (index == -1) {
        break;
      }
    }
    return index;
  }

  private int findFirstHigher(int value) {
    int index = n - 1;
    while (a.get(index) <= value) {
      index--;
    }
    return index;
  }

  private void reverse(int i, int j) {
    while (i < j) {
      swapByIndexes(i, j);
      i++;
      j--;
    }
  }

  private void swapByIndexes(int i, int j) {
    int tmp = a.get(i);
    a.set(i, a.get(j));
    a.set(j, tmp);
  }

  @Override
  public String toString() {
    String s = "[" + n + "] : " + a.toString();
    return s;
  }
}
