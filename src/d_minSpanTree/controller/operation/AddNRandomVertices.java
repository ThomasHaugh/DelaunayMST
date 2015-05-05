package d_minSpanTree.controller.operation;

import java.util.Random;
import java.util.Stack;

import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;

public class AddNRandomVertices implements GraphOperation {
  boolean reset;
  int nRandomVertices;
  Random rand = new Random(); // One can seed with a parameter variable here
  static int seed = 1;

  public AddNRandomVertices(final boolean reset, final int nRandVert,
      final Stack<UndoableGraphOperation> commandStack)
  {
    this.reset = reset;
    nRandomVertices = nRandVert;
    rand.setSeed(seed++);
  }

  @Override
  public void execute(final GraphModelInterface gmi) {
    // Start from no graph (Without this, it should be just an append of new vertices.)
    if (reset) {
      gmi.getVertices().clear();
      gmi.getEdges().clear();
    }

    // TODO: (group assignment) modify below to get the code run faster
    final long startTime = System.nanoTime(); // Start the total timing

    int ii = 0;
    for (int i = 0; i < nRandomVertices; i++) {
      final double xPos = rand.nextDouble() * 800; // todo: relate these to actual window space
      // dimensions
      final double yPos = rand.nextDouble() * 670; // or, in the very least to
      // GraphViewer.windowWidth and Height
      final Vertex v = new Vertex("p" + gmi.getVertices().size(), xPos, yPos);

      // Edge creation O(n^2).
      for (final Vertex vB : gmi.getVertices()) {
        final Edge e = new Edge(v, vB);
        ii++;

        final double weight = Math.sqrt((v.getX() - vB.getX())
            * (v.getX() - vB.getX()) + (v.getY() - vB.getY())
            * (v.getY() - vB.getY()));
        e.setWeight(weight);

        gmi.getEdges().add(e);
      }

      // Vertex creation O(n)
      gmi.getVertices().add(v);
    }
    final long endTime = System.nanoTime(); // Finish the total timing
    final float timeElapsed = (endTime - startTime) / 1000000.0f; // milliseconds
    System.out.println("Edge creation O(f(nE,nV)???):" + timeElapsed);

    System.out.println("ii");
    System.out.println(ii);
    gmi.runAlgorithms();
  }

  @Override
  public String getName() {
    return "Random 100";
  }

}
