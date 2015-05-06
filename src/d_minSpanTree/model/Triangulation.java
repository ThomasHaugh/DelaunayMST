package d_minSpanTree.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Triangulation {
    
    //private Map<Edge, List<Triangle>> edgeToTriangleSet;
    private List<List<Vertex>> triangles;

    public Triangulation() {
        triangles = new ArrayList<>();
    }
    
    public boolean add(List<Vertex> triangle) {
        Collections.sort(triangle);
        return triangles.add(triangle);
    }
    
    public List<Vertex> get(int index) {
        return triangles.get(index);
    }
    
    public int size() {
        return triangles.size();
    }
    
    public boolean remove(List<Vertex> triangle) {
        Collections.sort(triangle);
        return triangles.remove(triangle);
    }
    
    public void removeTrianglesWithVertex(Vertex v) {
        List<List<Vertex>> removals = new ArrayList<>();
        
        for (List<Vertex> triangle : triangles) {
          if (triangle.contains(v)) {
            removals.add(triangle);
          }
        }
        triangles.removeAll(removals);
    }
}
