package d_minSpanTree.controller.operation.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;

public class DelanayTriangulation implements GraphAlgorithm {

  public boolean addToTriangulation(final List<List<Vertex>> triangulation,
      final List<Vertex> triangle) {
    Collections.sort(triangle);
    return triangulation.add(triangle);
  }

  @Override
  public void execute(final GraphModelInterface gmi) {
    gmi.getEdges().clear();

    // First we just need the points to be triangualted
    final List<Vertex> vertices = new ArrayList<Vertex>();
    vertices.addAll(gmi.getVertices());
    Collections.sort(vertices); // This gives us a lexicographic sort on the members

    // We pick the starting element as the first member of the sorted list
    // this point will let us build a triangle around the remaining points
    // due to its position
    final Vertex startVert = vertices.get(0);
    vertices.remove(0);

    // We make a surrounding triangle so that the algorithm is as simple
    // as possible, buildBigTriangle does this by making two fake vertices
    // which will be removed from the final triangulation
    final List<Vertex> bigTriangle = buildBigTriangle(startVert, vertices); // triangle should be a
    final Vertex fakeVertex1 = bigTriangle.get(1);
    final Vertex fakeVertex2 = bigTriangle.get(2);
    // three
    // elem array
    // Positions 1 & 2 of triangle are added points

    // We also make a triangulation data structure
    // We can probably do better than an arraylist
    final List<List<Vertex>> triangulation = new ArrayList<List<Vertex>>();
    Collections.sort(bigTriangle);
    addToTriangulation(triangulation, bigTriangle);

    // We permute the remaining elements to prevent edgecase behavior
    Collections.shuffle(vertices); // I don't know if this method exist, but it's a good place
    // holder

    // Now the main for loop over the remaining elements
    for (final Vertex vert : vertices) {
      // Right now I am leaving out an if statment that should be there to make the
      // initial part of getting the code running smoothly easy

      // We find which member of the triangulation for now this might just
      // be a linear scan and we return its index in the triangulation data structure
      final int ctIndex = getContainingTriangleIndex(vert, triangulation);
      final List<Vertex> containingTriangle = triangulation.get(ctIndex);
      // we remove the triangle we will refine and then add in the three new triangles
      triangulation.remove(ctIndex);
      final List<Vertex> l1 = new ArrayList<Vertex>();
      l1.add(vert);
      l1.add(containingTriangle.get(0));
      l1.add(containingTriangle.get(1));
      addToTriangulation(triangulation, l1);

      final List<Vertex> l2 = new ArrayList<Vertex>();
      l2.add(vert);
      l2.add(containingTriangle.get(0));
      l2.add(containingTriangle.get(2));
      addToTriangulation(triangulation, l2);

      final List<Vertex> l3 = new ArrayList<Vertex>();
      l3.add(vert);
      l3.add(containingTriangle.get(1));
      l3.add(containingTriangle.get(2));
      addToTriangulation(triangulation, l3);
      // Now we need to improve the local result
      // We use the legalizeEdge method to do this
      // For every pair of points there are at most two triangles containg that pair
      // in a triangulation, so we are checking to see which crossing edge is better
      // the current crossing or the one going from vert to the third point in the
      // other triangle containing the two adjacent edges

      // TODO: Add if statement to handle the case when vert lies
      // on one of the edges of the triangle.
      legalizeEdge(vert, containingTriangle.get(0), containingTriangle.get(1),
          triangulation);
      legalizeEdge(vert, containingTriangle.get(0), containingTriangle.get(2),
          triangulation);
      legalizeEdge(vert, containingTriangle.get(1), containingTriangle.get(2),
          triangulation);
    }

    // Now we remove the fake points i.e. triangle[1], triangle[2]
    removeTrianglesWithVertex(triangulation, fakeVertex1);
    removeTrianglesWithVertex(triangulation, fakeVertex2);

    addTriangulationEdges(gmi, triangulation);
  }

  private void removeTrianglesWithVertex(
      final List<List<Vertex>> triangulation, final Vertex vertex) {
    final List<Vertex> removals = new ArrayList<Vertex>();
    for (final List<Vertex> triangle : triangulation) {
      if (triangle.contains(vertex)) {
        removals.add(vertex);
      }
    }
    triangulation.removeAll(removals);
  }

  private void addTriangulationEdges(final GraphModelInterface gmi,
      final List<List<Vertex>> triangulation) {
    final Set<Edge> edgeSet = new TreeSet<Edge>();
    for (final List<Vertex> triangle : triangulation) {
      edgeSet.add(new Edge(triangle.get(0), triangle.get(1)));
      edgeSet.add(new Edge(triangle.get(1), triangle.get(2)));
      edgeSet.add(new Edge(triangle.get(0), triangle.get(2)));
    }
    gmi.getEdges().addAll(edgeSet);
  }

  private boolean deleteFromTriangulation(
      final List<List<Vertex>> triangulation, final List<Vertex> triangle) {
    Collections.sort(triangle);
    return triangulation.remove(triangle);
  }

  private void legalizeEdge(final Vertex v, final Vertex p1, final Vertex p2,
      final List<List<Vertex>> triangulation) {

    for (int triangleIndex = 0; triangleIndex < triangulation.size(); triangleIndex++) {
      // final List<Vertex> triangle : triangulation) {
      final List<Vertex> triangle = triangulation.get(triangleIndex);
      Vertex p3 = null;
      final int i1 = triangle.indexOf(p1);
      final int i2 = triangle.indexOf(p2);
      if (i1 != -1 && i2 != -1) {
        // Reid's method
        p3 = triangle.get(3 - (i1 + i2));
        // TODO: Check if there are points inside the triangle.
        final int comp = Double.compare(distance(p1, p2), distance(v, p3));
        if (comp > 0) {
          // list destroy v, p1, p2
          if (deleteFromTriangulation(triangulation,
              Arrays.asList(new Vertex[] { v, p1, p2 }))) {
            triangleIndex--;
          }

          // list destroy p1, p2, p3
          if (deleteFromTriangulation(triangulation,
              Arrays.asList(new Vertex[] { p1, p2, p3 }))) {
            triangleIndex--;
          }

          // list add v, p1, p3
          addToTriangulation(triangulation,
              Arrays.asList(new Vertex[] { v, p1, p3 }));
          // list add v, p2,p3
          addToTriangulation(triangulation,
              Arrays.asList(new Vertex[] { v, p2, p3 }));
        }

        legalizeEdge(v, p1, p3, triangulation);
        legalizeEdge(v, p3, p2, triangulation);
      }
    }

  }

  private double distance(final Vertex v1, final Vertex v2) {
    return Math
        .pow(
            Math.pow(v1.getX() - v2.getX(), 2)
            + Math.pow(v1.getY() - v2.getY(), 2), .5);
  }

  // http://stackoverflow.com/questions/2049582/how-to-determine-a-point-in-a-triangle
  public boolean isInTriangle(final Vertex vertex, final List<Vertex> tri) {
    final boolean b1, b2, b3;

    b1 = sign(vertex.getX(), vertex.getY(), tri.get(0).getX(), tri.get(0)
        .getY(), tri.get(1).getX(), tri.get(1).getY()) < 0.0;
    b2 = sign(vertex.getX(), vertex.getY(), tri.get(1).getX(), tri.get(1)
        .getY(), tri.get(2).getX(), tri.get(2).getY()) < 0.0;
    b3 = sign(vertex.getX(), vertex.getY(), tri.get(2).getX(), tri.get(2)
        .getY(), tri.get(0).getX(), tri.get(0).getY()) < 0.0;

    return ((b1 == b2) && (b2 == b3));
    // System.out.println("triangle size is " + tri.size());
    // final Vertex p0 = tri.get(0);
    // final Vertex p1 = tri.get(1);
    // final Vertex p2 = tri.get(2);
    // // Doesn't check edges
    // if (p0.getX() == p1.getX()) {
    // if (Double.compare(vertex.getX(), p1.getX()) != Double.compare(p2.getX(),
    // p1.getX())) {
    // return false;
    // }
    // } else {
    // final double slope = (p0.getY() - p1.getY()) / (p0.getX() - p1.getX());
    // if (Double
    // .compare(p2.getY() - p1.getY(), slope * (p2.getX() - p1.getX())) != Double
    // .compare(vertex.getY() - p1.getY(),
    // slope * (vertex.getX() - p1.getX()))) {
    // return false;
    // }
    // }
    // if (p0.getX() == p2.getX()) {
    // if (Double.compare(vertex.getX(), p2.getX()) != Double.compare(p1.getX(),
    // p2.getX())) {
    // return false;
    // }
    // } else {
    // final double slope = (p0.getY() - p2.getY()) / (p0.getX() - p2.getX());
    // if (Double
    // .compare(p1.getY() - p2.getY(), slope * (p1.getX() - p2.getX())) != Double
    // .compare(vertex.getY() - p2.getY(),
    // slope * (vertex.getX() - p2.getX()))) {
    // return false;
    // }
    // }
    // if (p2.getX() == p1.getX()) {
    // if (Double.compare(vertex.getX(), p1.getX()) != Double.compare(p0.getX(),
    // p1.getX())) {
    // return false;
    // }
    // } else {
    // final double slope = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
    // if (Double
    // .compare(p0.getY() - p1.getY(), slope * (p0.getX() - p1.getX())) != Double
    // .compare(vertex.getY() - p1.getY(),
    // slope * (vertex.getX() - p1.getX()))) {
    // return false;
    // }
    // }
    // return true;
  }

  double sign(final double p1x, final double p1y, final double p2x,
      final double p2y, final double p3x, final double p3y) {
    return (p1x - p3x) * (p2y - p3y) - (p2x - p3x) * (p1y - p3y);
  }

  public int getContainingTriangleIndex(final Vertex vertex,
      final List<List<Vertex>> triangulation) {
    for (int i = 0; i < triangulation.size(); i++) {
      if (isInTriangle(vertex, triangulation.get(i))) {
        return i;
      }
    }
    throw new IllegalArgumentException("vertex not in triangulation");
  }

  @Override
  public String getName() {
    return "Delaunay Triangulation";
  }

  @Override
  public boolean canLiveUpdate() {
    return true;
  }

  public List<Vertex> buildBigTriangle(final Vertex startVert,
      final List<Vertex> vertices) {
    // assumes vertices are sorted
    final double maxY = startVert.getY();
    final double minY = getMinY(vertices) - 1;
    final double startVertX = startVert.getX();
    final double startVertY = startVert.getY();

    System.out.println("this is what's in vertices");
    for (final Vertex v : vertices) {
      System.out.println(v.toString());
    }
    System.out.println("end vertices");

    double maxSlope = Double.NEGATIVE_INFINITY;
    for (final Vertex v : vertices) {
      System.out.println("this is in vertices " + v.toString());
      if (startVert.getX() < v.getX()) {
        final double vSlope = (v.getY() - startVertY) / (v.getX() - startVertX);
        System.out.println(vSlope);
        if (vSlope > maxSlope) {
          maxSlope = vSlope;
        }
      }
    }

    double p1X;
    double p1Y;
    if (maxSlope == Double.NEGATIVE_INFINITY) {
      p1X = startVert.getX();
      p1Y = minY;
    } else {
      // Multiply maxSlope by 1/2 in order to keep vertices
      // off of our edges.
      p1X = ((minY - startVertY) / (0.5 * maxSlope)) + startVertX;
      p1Y = minY;
    }

    maxSlope = Double.NEGATIVE_INFINITY;
    for (final Vertex v : vertices) {
      System.out.println(v.toString());
      final double vSlope = (v.getY() - p1Y) / (v.getX() - p1X);
      System.out.println("vslope is " + vSlope);
      if (vSlope > maxSlope) {
        maxSlope = vSlope;
      }
    }

    System.out.println("max slope is " + maxSlope);
    final double p2X = (((maxY + 1) - p1Y) / (0.5 * maxSlope)) + p1X;
    final double p2Y = maxY + 1;

    // final double biggestX = maxs[0];
    // final double biggestY = maxs[1];
    // final double p1X = (biggestY / biggestX) * (biggestY + 1) + biggestX + 1;
    // final double p1Y = startVert.getY();
    // final double p2X = startVert.getX();
    // final double p2Y = (biggestX / biggestY) * (biggestX + 1) + biggestY + 1;

    final List<Vertex> bigTriangle = new ArrayList<>();
    bigTriangle.add(startVert);
    bigTriangle.add(new Vertex("fake point 1", p1X, p1Y));
    bigTriangle.add(new Vertex("fake point2", p2X, p2Y));
    return bigTriangle;
  }

  private Double getMinY(final List<Vertex> vertices) {
    double smallestY = Double.POSITIVE_INFINITY;
    for (final Vertex v : vertices) {
      if (v.getY() < smallestY) {
        smallestY = v.getY();
      }
    }
    return smallestY;
  }

}
