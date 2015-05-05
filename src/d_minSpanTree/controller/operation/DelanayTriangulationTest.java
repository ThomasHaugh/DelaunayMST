package d_minSpanTree.controller.operation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import d_minSpanTree.controller.operation.algorithm.DelaunayTriangulation;
import d_minSpanTree.model.Vertex;

public class DelanayTriangulationTest {
  DelaunayTriangulation d;

  @Before
  public void setUp() throws Exception {
    final DelaunayTriangulation d = new DelaunayTriangulation();
  }

  @Test
  public void test() {
    final DelaunayTriangulation d = new DelaunayTriangulation();
    final Vertex v1 = new Vertex("v", 1, 1);
    final Vertex v2 = new Vertex("v", 2, 2);
    final Vertex v3 = new Vertex("v", 1, 2);

    final Vertex v4 = new Vertex("v", 6, 7);
    final Vertex v5 = new Vertex("v", 10, 8);
    final Vertex v6 = new Vertex("v", 100, 8);

    final List<Vertex> tri1 = new ArrayList<Vertex>();
    tri1.add(v1);
    tri1.add(v2);
    tri1.add(v3);

    final List<Vertex> tri2 = new ArrayList<Vertex>();
    tri1.add(v4);
    tri1.add(v1);
    tri1.add(v5);

    final List<List<Vertex>> lists = new ArrayList<List<Vertex>>();
    lists.add(tri1);
    lists.add(tri2);

    //assertEquals(0, d.getContainingTriangleIndex(v2, lists));

    final Vertex startVertex = new Vertex("v", 1, 1);
    final Vertex zerozero = new Vertex("v", 0, 0);
    final List<Vertex> vertices = new ArrayList<Vertex>();
    vertices.add(zerozero);
    final List<Vertex> tri = d.buildBigTriangle(startVertex, vertices);
    for (final Vertex v : tri) {
      System.out.println(v.getX() + " " + v.getY());
    }

  }

}
