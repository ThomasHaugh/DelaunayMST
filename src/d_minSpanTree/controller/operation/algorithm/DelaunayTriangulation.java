package d_minSpanTree.controller.operation.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Triangulation;
import d_minSpanTree.model.Vertex;

public class DelaunayTriangulation implements GraphAlgorithm {
    
    private Triangulation triangulation;
    
  @Override
  public void execute(final GraphModelInterface gmi) {
    gmi.getEdges().clear();

    long startTime = System.nanoTime(); // Start the total timing
    
    // First we just need to grab a reference to
    // the points to be triangulated.
    final List<Vertex> vertices = new ArrayList<Vertex>();
    vertices.addAll(gmi.getVertices());
    Collections.sort(vertices); // This gives us a lexicographic sort on the members
    
    if (vertices.size() < 2) {
        return; // Then we can't make any edges.
    }
    
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
    // Inside it just has an arraylist of triangles
    // (which are currently arraylists of vertex objects).
    triangulation = new Triangulation();
    triangulation.add(bigTriangle);

    // We permute the remaining elements to prevent edgecase behavior
    Collections.shuffle(vertices); // I don't know if this method exist, but it's a good place
    // holder

    // Now the main for loop over the remaining elements
    for (final Vertex vert : vertices) {
      // Right now I am leaving out an if statment that should be there to make the
      // initial part of getting the code running smoothly easy

      // We find which member of the triangulation for now this might just
      // be a linear scan and we return its index in the triangulation data structure
      final int ctIndex = getContainingTriangleIndex(vert);
      final List<Vertex> containingTriangle = triangulation.get(ctIndex);
      // we remove the triangle we will refine and then add in the three new triangles
      triangulation.remove(containingTriangle);
      //System.out.println("removed surrounding triangle");
      
      final List<Vertex> l1 = new ArrayList<Vertex>();
      l1.add(vert);
      l1.add(containingTriangle.get(0));
      l1.add(containingTriangle.get(1));
      triangulation.add(l1);

      final List<Vertex> l2 = new ArrayList<Vertex>();
      l2.add(vert);
      l2.add(containingTriangle.get(0));
      l2.add(containingTriangle.get(2));
      triangulation.add(l2);

      final List<Vertex> l3 = new ArrayList<Vertex>();
      l3.add(vert);
      l3.add(containingTriangle.get(1));
      l3.add(containingTriangle.get(2));
      triangulation.add(l3);
      // Now we need to improve the local result
      // We use the legalizeEdge method to do this
      // For every pair of points there are at most two triangles containg that pair
      // in a triangulation, so we are checking to see which crossing edge is better
      // the current crossing or the one going from vert to the third point in the
      // other triangle containing the two adjacent edges

      // TODO: Add if statement to handle the case when vert lies
       //on one of the edges of the triangle.
      legalizeEdge(vert, containingTriangle.get(0), containingTriangle.get(1));
      legalizeEdge(vert, containingTriangle.get(0), containingTriangle.get(2));
      legalizeEdge(vert, containingTriangle.get(1), containingTriangle.get(2));
    }

    // Now we remove the fake points i.e. triangle[1], triangle[2]
    triangulation.removeTrianglesWithVertex(fakeVertex1);
    triangulation.removeTrianglesWithVertex(fakeVertex2);

    // Add all of our edges to the gmi.
    addTriangulationEdges(gmi);
    
    long endTime   = System.nanoTime(); // Finish the total timing
    float timeElapsed = (endTime-startTime)/1000000.0f; // milliseconds
    System.out.println("Edge creation O(f(nE,nV)???):" + timeElapsed);
    
    // Just for testing.
    for (final Edge e : gmi.getEdges()) {
        e.setOpacity(1);
      }
    gmi.getDisplayEdges().clear();
    gmi.getDisplayEdges().addAll(gmi.getEdges());
  }

  private void addTriangulationEdges(final GraphModelInterface gmi) {
      
      //System.out.println("raw input of triangles: " + triangulation.size());
      // Use a set to avoid adding duplicate edges.
      final Set<Edge> edgeSet = new TreeSet<Edge>();
      for (int i=0; i < triangulation.size(); i++) {
          List<Vertex> triangle = triangulation.get(i);
          
          Vertex v0 = triangle.get(0);
          Vertex v1 = triangle.get(1);
          Vertex v2 = triangle.get(2);
          Edge e1 = new Edge(v0, v1);
          Edge e2 = new Edge(v0, v2);
          Edge e3 = new Edge(v1, v2);
        
          double dx1 = v1.getX() - v0.getX();
          double dy1 = v1.getY() - v0.getY();
          double dx2 = v2.getX() - v0.getX();
          double dy2 = v2.getY() - v0.getY();
          double dx3 = v2.getX() - v1.getX();
          double dy3 = v2.getY() - v1.getY();
        
          e1.setWeight(Math.sqrt(dx1*dx1 + dy1*dy1));
          e2.setWeight(Math.sqrt(dx2*dx2 + dy2*dy2));
          e1.setWeight(Math.sqrt(dx3*dx3 + dy3*dy3));
        
          edgeSet.add(e1);
          edgeSet.add(e2);
          edgeSet.add(e3);
      }
      //System.out.println("edge set: ");
//      for (Edge e : edgeSet) {
//          System.out.println(e.toString());
//      }
      
      gmi.getEdges().addAll(edgeSet);
      edgeSet.clear();
  }

  //from http://stackoverflow.com/questions/4103405/what-is-the-algorithm-for-finding-the-center-of-a-circle-from-three-points
  public Vertex getCircleCenter(Vertex A, Vertex B, Vertex C) {

      double yDelta_a = B.getY() - A.getY();
      double xDelta_a = B.getX() - A.getX();
      double yDelta_b = C.getY() - B.getY();
      double xDelta_b = C.getX() - B.getX();
      Vertex center = new Vertex("center",0,0);

      double aSlope = yDelta_a/xDelta_a;
      double bSlope = yDelta_b/xDelta_b;

      Vertex AB_Mid = new Vertex("ab_mid",(A.getX()+B.getX())/2, (A.getY()+B.getY())/2);
      Vertex BC_Mid = new Vertex("bc_mid",(B.getX()+C.getX())/2, (B.getY()+C.getY())/2);

      if(yDelta_a == 0)         //aSlope == 0
      {
          center.setX(AB_Mid.getX());
          if (xDelta_b == 0)         //bSlope == INFINITY
          {
              center.setY(BC_Mid.getY());
          }
          else
          {
              center.setY(BC_Mid.getY() + (BC_Mid.getX()-center.getX())/bSlope);
          }
      }
      else if (yDelta_b == 0)               //bSlope == 0
      {
          center.setX( BC_Mid.getX());
          if (xDelta_a == 0)             //aSlope == INFINITY
          {
              center.setY(AB_Mid.getY());
          }
          else
          {
              center.setY( AB_Mid.getY() + (AB_Mid.getX()-center.getX())/aSlope);
          }
      }
      else if (xDelta_a == 0)        //aSlope == INFINITY
      {
          center.setY( AB_Mid.getY());
          center.setX( bSlope*(BC_Mid.getY()-center.getY()) + BC_Mid.getX());
      }
      else if (xDelta_b == 0)        //bSlope == INFINITY
      {
          center.setY( BC_Mid.getY());
          center.setX( aSlope*(AB_Mid.getY()-center.getY()) + AB_Mid.getX());
      }
      else
      {
          center.setX( (aSlope*bSlope*(AB_Mid.getY()-BC_Mid.getY()) - aSlope*BC_Mid.getX() + bSlope*AB_Mid.getX())/(bSlope-aSlope));
          center.setY( AB_Mid.getY() - (center.getX() - AB_Mid.getX())/aSlope);
      }

      return center;
  }
  
  private void legalizeEdge(final Vertex v, final Vertex p1, final Vertex p2) {
      System.out.println("hi");
    for (int triangleIndex = 0; triangleIndex < triangulation.size(); triangleIndex++) {
      // final List<Vertex> triangle : triangulation) {
        System.out.println("tri index: " + triangleIndex);
        List<Vertex> triangle = null;
        try {
            triangle = triangulation.get(triangleIndex);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("TRIANGLE INDEX: " + triangleIndex);
        }
      
      Vertex p3 = null;
      final int i1 = triangle.indexOf(p1);
      final int i2 = triangle.indexOf(p2);
      if (i1 != -1 && i2 != -1) { // Then this triangle contains our edge.
        // Reed's method
        p3 = triangle.get(3 - (i1 + i2));
        if (p3.compareTo(v) == 0) {
            continue;
        }
        // now we know p3 is not equal to v
        // check if p3 is in the circle made by the points v,p1,p2
        Vertex center = getCircleCenter(v,p1,p2);
        double radius = distance(center,p1);
        if (distance(center,p3) < radius) {
            // flip edge
            
         // destroy triangle (v, p1, p2)
          if (triangulation.remove(
                  Arrays.asList(new Vertex[] { v, p1, p2 }))) {
            triangleIndex--;
          }
    
          // destroy triangle (p1, p2, p3)
          if (triangulation.remove(
              Arrays.asList(new Vertex[] { p1, p2, p3 }))) {
            triangleIndex--;
          }
          if (triangleIndex < 0) {
              triangleIndex = 0;
          }
    
          // add triangle (v, p1, p3)
          triangulation.add(Arrays.asList(new Vertex[] { v, p1, p3 }));
          
          // add triangle (v, p2,p3)
          triangulation.add(Arrays.asList(new Vertex[] { v, p2, p3 }));
          
          // Recursive calls!
          legalizeEdge(v, p1, p3);
          legalizeEdge(v, p3, p2);
        }

        }
      }
    }
        
        
        
        // TODO: Check if there are points inside the triangle.
//        final int comp = Double.compare(distance(p1, p2), distance(v, p3));
//        if (comp > 0) {
//          // list destroy v, p1, p2
//          if (deleteFromTriangulation(triangulation,
//              Arrays.asList(new Vertex[] { v, p1, p2 }))) {
//            triangleIndex--;
//          }
//
//          // list destroy p1, p2, p3
//          if (deleteFromTriangulation(triangulation,
//              Arrays.asList(new Vertex[] { p1, p2, p3 }))) {
//            triangleIndex--;
//          }
//
//          // list add v, p1, p3
//          addToTriangulation(triangulation,
//              Arrays.asList(new Vertex[] { v, p1, p3 }));
//          // list add v, p2,p3
//          addToTriangulation(triangulation,
//              Arrays.asList(new Vertex[] { v, p2, p3 }));
//          
//          // Now some recursion.
//          legalizeEdge(v, p1, p3, triangulation);
//          legalizeEdge(v, p3, p2, triangulation);
//        }
//
//        
//      }
//    }
//
//  }

  private double distance(final Vertex v1, final Vertex v2) {
    return Math
        .pow(
            Math.pow(v1.getX() - v2.getX(), 2)
            + Math.pow(v1.getY() - v2.getY(), 2), .5);
  }

  // http://stackoverflow.com/questions/2049582/how-to-determine-a-point-in-a-triangle
  public boolean isInTriangle(final Vertex vertex, final List<Vertex> tri) {
//    final boolean b1, b2, b3;
//
//    b1 = sign(vertex.getX(), vertex.getY(), tri.get(0).getX(), tri.get(0)
//        .getY(), tri.get(1).getX(), tri.get(1).getY()) < 0.0;
//    b2 = sign(vertex.getX(), vertex.getY(), tri.get(1).getX(), tri.get(1)
//        .getY(), tri.get(2).getX(), tri.get(2).getY()) < 0.0;
//    b3 = sign(vertex.getX(), vertex.getY(), tri.get(2).getX(), tri.get(2)
//        .getY(), tri.get(0).getX(), tri.get(0).getY()) < 0.0;
//
//    return ((b1 == b2) && (b2 == b3));
     //System.out.println("triangle size is " + tri.size());
     final Vertex p0 = tri.get(0);
     final Vertex p1 = tri.get(1);
     final Vertex p2 = tri.get(2);
     // Doesn't check edges
     if (p0.getX() == p1.getX()) {
     if (Double.compare(vertex.getX(), p1.getX()) != Double.compare(p2.getX(),
     p1.getX())) {
     return false;
     }
     } else {
     final double slope = (p0.getY() - p1.getY()) / (p0.getX() - p1.getX());
     if (Double
     .compare(p2.getY() - p1.getY(), slope * (p2.getX() - p1.getX())) != Double
     .compare(vertex.getY() - p1.getY(),
     slope * (vertex.getX() - p1.getX()))) {
     return false;
     }
     }
     if (p0.getX() == p2.getX()) {
     if (Double.compare(vertex.getX(), p2.getX()) != Double.compare(p1.getX(),
     p2.getX())) {
     return false;
     }
     } else {
     final double slope = (p0.getY() - p2.getY()) / (p0.getX() - p2.getX());
     if (Double
     .compare(p1.getY() - p2.getY(), slope * (p1.getX() - p2.getX())) != Double
     .compare(vertex.getY() - p2.getY(),
     slope * (vertex.getX() - p2.getX()))) {
     return false;
     }
     }
     if (p2.getX() == p1.getX()) {
     if (Double.compare(vertex.getX(), p1.getX()) != Double.compare(p0.getX(),
     p1.getX())) {
     return false;
     }
     } else {
     final double slope = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
     if (Double
     .compare(p0.getY() - p1.getY(), slope * (p0.getX() - p1.getX())) != Double
     .compare(vertex.getY() - p1.getY(),
     slope * (vertex.getX() - p1.getX()))) {
     return false;
     }
     }
     return true;
  }

  double sign(final double p1x, final double p1y, final double p2x,
      final double p2y, final double p3x, final double p3y) {
    return (p1x - p3x) * (p2y - p3y) - (p2x - p3x) * (p1y - p3y);
  }

  public int getContainingTriangleIndex(final Vertex vertex) {
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

//    System.out.println("this is what's in vertices");
//    for (final Vertex v : vertices) {
//      System.out.println(v.toString());
//    }
//    System.out.println("end vertices");

    double maxSlope = Double.NEGATIVE_INFINITY;
    for (final Vertex v : vertices) {
      //System.out.println("this is in vertices " + v.toString());
      if (startVert.getX() < v.getX()) {
        final double vSlope = (v.getY() - startVertY) / (v.getX() - startVertX);
        //System.out.println(vSlope);
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
      //System.out.println(v.toString());
      final double vSlope = (v.getY() - p1Y) / (v.getX() - p1X);
      //System.out.println("vslope is " + vSlope);
      if (vSlope > maxSlope) {
        maxSlope = vSlope;
      }
    }

    //System.out.println("max slope is " + maxSlope);
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
