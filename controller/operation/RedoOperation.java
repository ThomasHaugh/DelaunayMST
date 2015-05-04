package d_minSpanTree.controller.operation;

import java.util.Stack;

import d_minSpanTree.model.GraphModelInterface;

public class RedoOperation implements GraphOperation {
	private Stack<UndoableGraphOperation> undoStack;
	private Stack<UndoableGraphOperation> redoStack;

	public RedoOperation(Stack<UndoableGraphOperation> uStack, Stack<UndoableGraphOperation> rStack) {
		undoStack = uStack;
		redoStack = rStack;
	}

	public void execute(GraphModelInterface gmi) {
		if (redoStack.size() < 0)
			return;
		UndoableGraphOperation ugo = redoStack.pop();
		ugo.execute(gmi);
		undoStack.push(ugo);

		gmi.runAlgorithms();
	}

	public String getName() {
		return "Redo";
	}
}
