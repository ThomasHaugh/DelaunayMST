package d_minSpanTree.controller.operation.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;

public class MinimumSpanningTree implements GraphAlgorithm {
  private ArrayList<ArrayList<Vertex>> forest;

  @Override
  public void execute(final GraphModelInterface gmi) {
    forest = new ArrayList<ArrayList<Vertex>>();
    for (final Edge e : gmi.getEdges()) {
      e.setOpacity(.05);
    }
    final ArrayList<Edge> finalTree = new ArrayList<Edge>();

    final ArrayList<Edge> edges = new ArrayList<Edge>();
    edges.addAll(gmi.getEdges());

    //Set the edge weights since for some reason that is not done
    for (final Edge e : edges) {
	Vertex v1 = e.getStart();
	Vertex v2 = e.getEnd();
	double w  = v1.distanceTo(v2);
	e.setWeight(w);
    }

    for (final Vertex v : gmi.getVertices()) {
      final ArrayList<Vertex> tree = new ArrayList<Vertex>();
      tree.add(v);
      forest.add(tree);
    }

    // Kruskal's algorithm O(f(nE,nV)???) in an efficient time complexity implementation.
    final long startTime = System.nanoTime(); // Start the total timing
    // Quicksort of the entire edge array puts us at O(???) time complexity
    //(new QuickSort(edges, new AscEdgeWeight())).sort();
    //We broke this I think so I am just using java sort
    Collections.sort(edges, new Comparator<Edge>() {
	    public int compare(Edge e1, Edge e2) {
		double w1 = e1.getWeight();
		double w2 = e2.getWeight();
		return Double.compare(w1,w2);
	    }
	}); 
    
    System.out.println("edges");
    System.out.println(edges.size());
    for (int i = 0; i < edges.size(); i++) {
      final Edge e = (Edge) edges.get(i);
      final ArrayList<Vertex> tree1 = findTree(e.getStart());
      final ArrayList<Vertex> tree2 = findTree(e.getEnd());
      if (tree1 != tree2) {
        finalTree.add(e);
        tree1.addAll(tree2);
        forest.remove(tree2);
      }
    }
    final long endTime = System.nanoTime(); // Finish the total timing
    final float timeElapsed = (endTime - startTime) / 1000000.0f; // milliseconds
    System.out.println("MST (Kruskal) time O(f(nE,nV)???):" + timeElapsed);

    for (final Edge e : finalTree) {
      e.setOpacity(1);
    }

    gmi.getDisplayEdges().clear();
    gmi.getDisplayEdges().addAll(finalTree); // Only adding the MST edges for display
    // gmi.getDisplayEdges().addAll(gmi.getEdges()); // Adding all edges for display (Use this only
    // if Delaunay)
  }

  private ArrayList<Vertex> findTree(final Vertex vert) {
    // System.out.println("forest");
    // System.out.println(forest.size());
    for (final ArrayList<Vertex> tree : forest) {
      // System.out.println("tree");
      // System.out.println(tree.size());
      for (final Vertex v : tree)
        if (vert == v)
          return tree;
    }
    return null;
  }

  @Override
  public String getName() {
    return "Minimum Spanning Tree";
  }

  @Override
  public boolean canLiveUpdate() {
    return true;
  }

}
