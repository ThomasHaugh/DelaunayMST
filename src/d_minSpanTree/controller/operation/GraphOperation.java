package d_minSpanTree.controller.operation;

import d_minSpanTree.model.GraphModelInterface;

public interface GraphOperation {

	public void execute(GraphModelInterface gmi);

	public String getName();

}
