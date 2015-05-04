package d_minSpanTree.controller.operation.algorithm;

import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;

public class DelanayTriangulation implements GraphAlgorithm {

    public void execute(GraphModelInterface gmi) {
	gmi.getEdges().clear();

	//TODO: make add the edges for the delanay triangulation
    }

    public String getName() {
	return "Delanay Triangulation";
    }

    @Override
    public boolean canLiveUpdate() {
        // TODO Auto-generated method stub
        return true;
    }

}
