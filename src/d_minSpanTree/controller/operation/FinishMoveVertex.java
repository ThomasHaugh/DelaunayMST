package d_minSpanTree.controller.operation;

import d_minSpanTree.model.GraphModelInterface;

public class FinishMoveVertex implements GraphOperation {

	public void execute(GraphModelInterface gmi) {
		gmi.runAlgorithms();
	}

	public String getName() {
		return "Move Vertex";
	}

}
