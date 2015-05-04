package d_minSpanTree.controller.operation.algorithm;

import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;

public class ConnectAllVertices implements GraphAlgorithm {

	public void execute(GraphModelInterface gmi) {
		gmi.getEdges().clear();

		for (int i0 = 0; i0 < gmi.getVertices().size(); i0++) {
			for (int i1 = i0 + 1; i1 < gmi.getVertices().size(); i1++) {
				Vertex v = gmi.getVertices().get(i0);
				Vertex v2 = gmi.getVertices().get(i1);
				Edge e = new Edge(v, v2);
				gmi.getEdges().add(e);
			}
		}
	}

	public String getName() {
		return "Connect All Vertices";
	}

	public boolean canLiveUpdate() {
		return true;
	}

}
