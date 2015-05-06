package d_minSpanTree;

import d_minSpanTree.controller.Controller;
import d_minSpanTree.controller.ControllerInterface;
import d_minSpanTree.controller.operation.AddGraphAlgorithm;
import d_minSpanTree.controller.operation.algorithm.ConnectAllVertices;
import d_minSpanTree.controller.operation.algorithm.DelaunayTriangulation;
import d_minSpanTree.controller.operation.algorithm.MinimumSpanningTree;
import d_minSpanTree.controller.operation.algorithm.WeightByDistance;
import d_minSpanTree.model.GraphModel;
import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.view.GraphViewer;
import d_minSpanTree.view.ViewerInterface;
import javafx.application.Application;
import javafx.stage.Stage;

public class UltimateGraph extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		GraphModelInterface model = new GraphModel();
		ControllerInterface controller = new Controller(model);
		ViewerInterface view = new GraphViewer(model, controller, stage);
		view.init();

		//controller.getInvoker().doOperation(new AddGraphAlgorithm(new ConnectAllVertices()));
		//controller.getInvoker().doOperation(new AddGraphAlgorithm(new WeightByDistance()));
		controller.getInvoker().doOperation(new AddGraphAlgorithm(new DelaunayTriangulation()));
		controller.getInvoker().doOperation(new AddGraphAlgorithm(new MinimumSpanningTree()));
	}
}
