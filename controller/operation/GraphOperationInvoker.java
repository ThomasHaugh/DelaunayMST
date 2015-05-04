package d_minSpanTree.controller.operation;

import java.util.Stack;

import d_minSpanTree.model.GraphModelInterface;

public class GraphOperationInvoker {
	private GraphModelInterface graphModel;
	private Stack<UndoableGraphOperation> undoStack = new Stack<UndoableGraphOperation>();
	private Stack<UndoableGraphOperation> redoStack = new Stack<UndoableGraphOperation>();

	public GraphOperationInvoker(GraphModelInterface gmi) {
		graphModel = gmi;
	}

	public void doOperation(GraphOperation op) {
		if (op instanceof UndoableGraphOperation) {
			undoStack.push((UndoableGraphOperation) op);
			redoStack.clear();
		}

		op.execute(graphModel);

		graphModel.notifyObservers();
	}

	public Stack<UndoableGraphOperation> getUndoStack() {
		return undoStack;
	}

	public Stack<UndoableGraphOperation> getRedoStack() {
		return redoStack;
	}

}
