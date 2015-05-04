package d_minSpanTree.controller.operation.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;

public class DelanayTriangulation implements GraphAlgorithm {

  @Override
  public void execute(final GraphModelInterface gmi) {
    gmi.getEdges().clear();

    // First we just need the points to be triangualted
    final List<Vertex> vertices = gmi.getVertices();
    Collections.sort(vertices);// This gives us a lexicographic sort on the members

    // We pick the starting element as the first member of the sorted list
    // this point will let us build a triangle around the remaining points
    // due to its position
    final Vertex startVert = vertices.get(0);
    vertices.remove(0); // Removed so it can't be reused in the second phase

    // We make a surrounding triangle so that the algorithm is as simple
    // as possible, buildBigTriangle does this by makeing two fake vertices
    // which will be removed from the final triangulation
    final Vertex[] triangle = buildBigTriangle(startVert, vertices); // triangle should be a three
    // elem array
    // Positions 1 & 2 of triangle are added points

    // We also make a triangulation data structure
    // We can probably do better than an arraylist
    final ArrayList<Vertex[]> triangulation = new ArrayList<Vertex[]>();
    triangulation.add(triangle);

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
      final Vertex[] containingTriangle = trinagulation.get(ctIndex);
      // we remove the triangle we will refine and then add in the three new triangles
      triangulation.remove(ctIndex);
      triangulation.add(new Vertex[] { vert, containingTriangle[0],
          containingTriangle[1] });
      triangulation.add(new Vertex[] { vert, containingTriangle[0],
          containingTriangle[2] });
      triangulation.add(new Vertex[] { vert, containingTriangle[1],
          containingTriangle[2] });
      // Now we need to improve the local result
      // We use the legalizeEdge method to do this
      // For every pair of points there are at most two triangles containg that pair
      // in a triangulation, so we are checking to see which crossing edge is better
      // the current crossing or the one going from vert to the third point in the
      // other triangle containing the two adjacent edges

      legalizeEdge(vert, containingTriangle[0], containingTriangle[1],
          triangulation);
      legalizeEdge(vert, containingTriangle[0], containingTriangle[2],
          triangulation);
      legalizeEdge(vert, containingTriangle[1], containingTriangle[2],
          triangulation);
    }

    // Now we remove the fake points i.e. triangle[1], triangle[2]
    triangulation.removeTrianglesWithVertex(triangle[1]);
    triangulation.removeTrianglesWithVertex(triangle[2]);

    gmi.addTriangulationEdges(triangulation);
  }

  private boolean isInTriangle(final Vertex vertex,
      final List<Vertex[]> triangulation) {
    // todo not implemented
    return false;
  }

  private int getContainingTriangleIndex(final Vertex vertex,
      final List<Vertex[]> triangulation) {
    for (int i = 0; i < triangulation.size(); i++) {
      if (isInTriange(vertex, triangulation){
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

  Vertex[] buildBigTriangle(final Vertex startVert, final List<Vertex> vertices) {
    // assumes vertices are sorted

    final Double[] maxs = getMaxXAndY(vertices);
    final double biggestX = maxs[0];
    final double biggestY = maxs[1];
    final double p1X = (biggestY / biggestX) * (biggestY + 1) + biggestX + 1;
    final double p1Y = 0;
    final double p2X = 0;
    final double p2Y = (biggestX / biggestY) * (biggestX + 1) + biggestY + 1;

    final Vertex[] bigTriangle = new Vertex[3];
    bigTriangle[0] = startVert;
    bigTriangle[1] = new Vertex("fake point 1", p1X, p1Y);
    bigTriangle[2] = new Vertex("fake point2", p2X, p2Y);
    return bigTriangle;
  }

  private Double[] getMaxXAndY(final List<Vertex> vertices) {
    double biggestX = Double.MIN_VALUE;
    double biggestY = Double.MIN_VALUE;
    for (final Vertex v : vertices) {
      if (v.getX() > biggestX) {
        biggestX = v.getX();
      }

      if (v.getY() > biggestY) {
        biggestY = v.getY();
      }
    }

    return new Double[] { biggestX, biggestY };
  }

}
