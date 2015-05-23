import java.util.Hashtable;

public class DisjointSetUnion {
  Hashtable<Object, Object> parentTable;
  Hashtable<Object, Integer> rank;
  DisjointSetUnion() {
    parentTable = new Hashtable<>();
    rank = new Hashtable<>();
  }

  DisjointSetUnion(Iterable array) {
    for (Object element : array) {
      makeSet(element);
    }
  }

  void makeSet (Object element) {
    parentTable.put(element, element);
    rank.put(element, 0);
  }

  Object find(Object element) {
    if (parentTable.containsKey(element)) {
      Object parent = parentTable.get(element);
      if (parent.equals(element)) {
        return element;
      } else {
        return find(parent);
      }
    } else {
      return null;
    }
  }
  boolean union(Object first, Object second) {
    Object firstParent = find(first);
    Object secondParent = find(second);
    if (null == firstParent && null == secondParent) {
      return false;
    }
    if (!firstParent.equals(secondParent)) {
      Object leader;
      Object looser;
      if (rank.get(firstParent) > rank.get(secondParent)) {
        leader = firstParent;
        looser = secondParent;
      } else {
        leader = secondParent;
        looser = firstParent;
      }
      parentTable.put(looser, leader);
      if (rank.get(firstParent) == rank.get(secondParent)) {
        rank.put(leader, rank.get(firstParent) + 1);
      }
    }
    return true;
  }
}
