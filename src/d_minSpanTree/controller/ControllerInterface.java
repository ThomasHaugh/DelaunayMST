package d_minSpanTree.controller;

import d_minSpanTree.controller.operation.GraphOperationInvoker;
import d_minSpanTree.model.Vertex;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public interface ControllerInterface {
	public void mousePressedOnVertex(MouseEvent e, Vertex v);

	public void mousePressedOnGraph(MouseEvent e);

	public void mouseLift(MouseEvent e);

	public void mouseDragged(MouseEvent e);

	public void keyPressed(KeyEvent e);

    public void newGraph();

	public void fileOpen(String path);

    public void fileSave(String path);

	public void undo();

	public void redo();

    public void randNVertices(boolean reset,int nRandVertices);

	public GraphOperationInvoker getInvoker();

}
