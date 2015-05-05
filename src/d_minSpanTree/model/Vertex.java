package d_minSpanTree.model;

import java.util.ArrayList;

public class Vertex implements Comparable<Vertex> {
  private final double position[] = new double[2];
  private final String name;
  private String displayName;
  private final ArrayList<Vertex> adj = new ArrayList<Vertex>();

  public Vertex(final String name, final double x, final double y) {
    this.name = name;
    displayName = name;
    position[0] = x;
    position[1] = y;
  }
  
  public double distanceTo(Vertex other) {
      return Math.sqrt(Math.pow(getX() - other.getX(),2) + Math.pow(getY() - other.getY(), 2));
  }

  public String getName() {
    return name;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(final String text) {
    displayName = text;
  }

  public double getX() {
    return position[0];
  }

  public double getY() {
    return position[1];
  }

  public void setX(final double x) {
    position[0] = x;
  }

  public void setY(final double y) {
    position[1] = y;
  }

  public ArrayList<Vertex> getAdjacent() {
    return adj;
  }

  @Override
  public int compareTo(final Vertex other) {
    int comp = Double.compare(getY(), other.getY());
    if (comp != 0) {
      return -comp;
    } else {
      return -Double.compare(getX(), other.getX());
    }
  }

}
