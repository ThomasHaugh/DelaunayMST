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

	public AddNRandomVertices(boolean reset, int nRandVert, Stack<UndoableGraphOperation> commandStack) {
	    this.reset = reset;
	    nRandomVertices = nRandVert;
        rand.setSeed(seed++);
	}

	public void execute(GraphModelInterface gmi) {
	    // Start from no graph (Without this, it should be just an append of new vertices.)
	    if (reset) {
            gmi.getVertices().clear();
            gmi.getEdges().clear();
	    }

	    //TODO: (group assignment) modify below to get the code run faster
        long startTime = System.nanoTime(); // Start the total timing
		for (int i=0; i<nRandomVertices; i++) {
            double xPos = rand.nextDouble() * 800; // todo: relate these to actual window space dimensions
            double yPos = rand.nextDouble() * 670; // or, in the very least to GraphViewer.windowWidth and Height
            Vertex v = new Vertex("p"+gmi.getVertices().size(), xPos, yPos);
            
            // Edge creation O(n^2). 
            for (Vertex vB : gmi.getVertices()) {
                Edge e = new Edge(v, vB);
                double weight = Math.sqrt(
                        (v.getX()-vB.getX())*(v.getX()-vB.getX())+
                        (v.getY()-vB.getY())*(v.getY()-vB.getY())  );
                e.setWeight(weight);

                gmi.getEdges().add(e);
            }

            // Vertex creation O(n)
            gmi.getVertices().add(v);
		}
        long endTime  = System.nanoTime(); // Finish the total timing
        float timeElapsed = (endTime-startTime)/1000000.0f; // milliseconds
        System.out.println("Edge creation O(f(nE,nV)???):" + timeElapsed);

		gmi.runAlgorithms();
	}

	public String getName() {
		return "Random 100";
	}

}
