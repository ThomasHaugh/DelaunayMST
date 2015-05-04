package d_minSpanTree.controller.operation;

import java.util.Stack;

import d_minSpanTree.model.GraphModelInterface;

public class UndoOperation implements GraphOperation {
	private Stack<UndoableGraphOperation> undoStack;
	private Stack<UndoableGraphOperation> redoStack;

	public UndoOperation(Stack<UndoableGraphOperation> uStack, Stack<UndoableGraphOperation> rStack) {
		undoStack = uStack;
		redoStack = rStack;
	}

	public void execute(GraphModelInterface gmi) {
		if (undoStack.size() < 0)
			return;
		UndoableGraphOperation ugo = undoStack.pop();
		ugo.undo(gmi);
		redoStack.push(ugo);

		gmi.runAlgorithms();
	}

	public String getName() {
		return "Undo";
	}

}
