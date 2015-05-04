package d_minSpanTree.controller.operation;

import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;

public class StartMoveVertex implements UndoableGraphOperation {

	private Vertex target;
	private double origXPos, origYPos;

	public StartMoveVertex(Vertex v) {
		target = v;
	}

	public void execute(GraphModelInterface gmi) {
		origXPos = target.getX();
		origYPos = target.getY();
	}

	public void undo(GraphModelInterface gmi) {
		target.setX(origXPos);
		target.setY(origYPos);
	}

	public Vertex getTarget() {
		return target;
	}

	public double getX() {
		return origXPos;
	}

	public double getY() {
		return origYPos;
	}

	public String getName() {
		return "Move Vertex";
	}

}
