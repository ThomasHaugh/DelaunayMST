package d_minSpanTree.model;

import java.util.ArrayList;

public class Vertex implements Comparable<Vertex> {
  private final double position[] = new double[2];
  private final String name;
  private String displayName;
  private final ArrayList<Vertex> adj = new ArrayList<Vertex>();

  @Override
  public String toString() {
    return getX() + " " + getY();
  }

  public Vertex(final String name, final double x, final double y) {
    this.name = name;
    displayName = name;
    position[0] = x;
    position[1] = y;
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

    final int compX = Double.compare(getX(), other.getX());
    final int compY = Double.compare(getY(), other.getY());

    if (compX != 0) {
        return compX;
    } else if (compY != 0) {
        return compY;
    } else {
      return 0;
    }
  }

}
