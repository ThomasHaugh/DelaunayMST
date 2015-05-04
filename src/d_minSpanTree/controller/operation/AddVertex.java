package d_minSpanTree.controller.operation;

import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;

public class AddVertex implements UndoableGraphOperation {
	private Vertex add;
	private double posX, posY;

	public AddVertex(double x, double y) {
		posX = x;
		posY = y;
	}

	public void execute(GraphModelInterface gmi) {
		if (add == null)
			add = new Vertex(gmi.getVertices().size() + "", posX, posY);

		gmi.getVertices().add(add);

		gmi.runAlgorithms();
	}

	public void undo(GraphModelInterface gmi) {
		gmi.getVertices().remove(add);
	}

	public String getName() {
		return "Add Vertex";
	}

}
