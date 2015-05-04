package d_minSpanTree.controller.operation.algorithm;

import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelInterface;

public class Default implements GraphAlgorithm {

	public void execute(GraphModelInterface gmi) {
		for (Edge e : gmi.getEdges()) {
			e.setOpacity(.1);
		}
	}

	public boolean canLiveUpdate() {
		return true;
	}

	public String getName() {
		return "Default";
	}

}
