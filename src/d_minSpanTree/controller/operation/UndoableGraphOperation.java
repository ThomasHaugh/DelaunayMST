package d_minSpanTree.controller.operation;

import d_minSpanTree.model.GraphModelInterface;

public interface UndoableGraphOperation extends GraphOperation {
	public void undo(GraphModelInterface gmi);
}
