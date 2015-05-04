package d_minSpanTree.view;

import java.io.File;

import d_minSpanTree.controller.ControllerInterface;
import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class GraphViewer implements ViewerInterface {
	private int windowWidth = 800, windowHeight = 700;
	private final int menuBarHeight = 25;

	private GraphModelInterface graphModel;
	private ControllerInterface controller;
	private Stage window;
	private Pane rootPane = new Pane();
	private Pane graphPane = new Pane();

	public GraphViewer(GraphModelInterface model, ControllerInterface control, Stage stage) {
		graphModel = model;
		graphModel.addObserver(this);
		controller = control;
		window = stage;
	}

	public void init() {
		setupWindow();
		drawGraph();

		window.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				int newW = (int) window.getWidth();
				int newH = (int) window.getHeight();
				if (windowWidth != newW || windowHeight != newH) {
					windowWidth = newW;
					windowHeight = newH;
					setupWindow();
					drawGraph();
				}
			}
		});

		window.setTitle("Minimum Spanning Tree");
		window.setScene(new Scene(rootPane, windowWidth, windowHeight));
		window.show();
	}

	private void setupWindow() {
		Menu fileMenu = new Menu("Graph");
		MenuItem newGraph = new MenuItem("New Graph");
		newGraph.setOnAction(e -> {
			controller.newGraph();
		});
		fileMenu.getItems().add(newGraph);

        MenuItem open = new MenuItem("Open");
        open.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Graph");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"), new ExtensionFilter("All Files", "*.*"));
            File selected = fileChooser.showOpenDialog(window);
            if (selected != null) {
                controller.fileOpen(selected.getAbsolutePath());
            }
        });
        fileMenu.getItems().add(open);

        MenuItem save = new MenuItem("Save");
        save.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Graph");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"), new ExtensionFilter("All Files", "*.*"));
            File selected = fileChooser.showSaveDialog(window);
            if (selected != null) {
                controller.fileSave(selected.getAbsolutePath());
            }
        });
        fileMenu.getItems().add(save);

		MenuItem undo = new MenuItem("Undo");
		undo.setOnAction(e -> {
			controller.undo();
		});
		fileMenu.getItems().add(undo);

		MenuItem redo = new MenuItem("Redo");
		redo.setOnAction(e -> {
			controller.redo();
		});
		fileMenu.getItems().add(redo);

        Menu fileMenu2 = new Menu("New Random");
        MenuItem rand1e1n = new MenuItem("Random 10"   );
        rand1e1n.setOnAction(e -> {  controller.randNVertices(true ,10   );  });
        MenuItem rand1e2n = new MenuItem("Random 100"  );
        rand1e2n.setOnAction(e -> {  controller.randNVertices(true ,100  );  });
        MenuItem rand1e3n = new MenuItem("Random 1000" );
        rand1e3n.setOnAction(e -> {  controller.randNVertices(true ,1000 );  });
        MenuItem rand1e4n = new MenuItem("Random 10000");
        rand1e4n.setOnAction(e -> {  controller.randNVertices(true ,10000);  });
        fileMenu2.getItems().addAll(rand1e1n,rand1e2n,rand1e3n,rand1e4n);

        Menu fileMenu3 = new Menu("Add Random");
        MenuItem rand1e1a = new MenuItem("Random 10"   );
        rand1e1a.setOnAction(e -> {  controller.randNVertices(false,10   );  });
        MenuItem rand1e2a = new MenuItem("Random 100"  );
        rand1e2a.setOnAction(e -> {  controller.randNVertices(false,100  );  });
        MenuItem rand1e3a = new MenuItem("Random 1000" );
        rand1e3a.setOnAction(e -> {  controller.randNVertices(false,1000 );  });
        MenuItem rand1e4a = new MenuItem("Random 10000");
        rand1e4a.setOnAction(e -> {  controller.randNVertices(false,10000);  });
        fileMenu3.getItems().addAll(rand1e1a,rand1e2a,rand1e3a,rand1e4a);

		MenuBar mb = new MenuBar();
		mb.setMinSize(windowWidth, menuBarHeight);
		mb.getMenus().addAll(fileMenu,fileMenu2,fileMenu3);

		rootPane.getChildren().add(mb);
	}

	private void drawGraph() {
		rootPane.getChildren().remove(graphPane);
		graphPane = new Pane();
		graphPane.setMinSize(windowWidth, windowHeight - menuBarHeight);
		graphPane.setLayoutY(menuBarHeight);
		graphPane.setOnMouseClicked(e -> {
			controller.mousePressedOnGraph(e);
		});

		drawEdges();
		drawVertices();

		rootPane.getChildren().add(graphPane);
	}

	private void drawVertices() {
		for (Vertex v : graphModel.getVertices()) {
			makeVertexDisplay(v);
		}
	}

	private void drawEdges() {
		for (Edge e : graphModel.getDisplayEdges()) {
			makeEdgeDisplay(e);
		}
	}

	private void makeVertexDisplay(Vertex v) {
		double circRadius = 5;
		Pane vertexPane = new Pane();

		vertexPane.setOnMousePressed(e -> {
			controller.mousePressedOnVertex(e, v);
		});
		vertexPane.setOnMouseReleased(e -> {
			controller.mouseLift(e);
		});
		vertexPane.setOnMouseDragged(e -> {
			controller.mouseDragged(e);
		});

		vertexPane.setPrefSize(circRadius * 2, circRadius * 2);
		vertexPane.relocate(v.getX() - circRadius, v.getY() - circRadius);

		Circle circle = new Circle();
		circle.setCenterX(circRadius);
		circle.setCenterY(circRadius);
		circle.setRadius(circRadius);
		circle.setOpacity(0.15);
		vertexPane.getChildren().add(circle);

		//Text text = new Text(0, 0, v.getDisplayName());
		//text.relocate(circRadius - text.getBoundsInLocal().getWidth() / 2, circRadius - text.getBoundsInLocal().getHeight() / 2);
		//vertexPane.getChildren().add(text);

		graphPane.getChildren().add(vertexPane);
	}

	private void makeEdgeDisplay(Edge e) {
		double x0 = e.getStart().getX();
		double y0 = e.getStart().getY();
		double x1 = e.getEnd().getX();
		double y1 = e.getEnd().getY();

		Line line = new Line();
		line.setStartX(x0);
		line.setStartY(y0);
		line.setEndX(x1);
		line.setEndY(y1);
		line.setOpacity(e.getOpacity());

		graphPane.getChildren().add(line);

		// if (!Double.isNaN(e.getWeight())) {
		// Text text = new Text(0, 0, "" + e.getWeight());
		// text.relocate((x0 + x1) / 2, (y0 + y1) / 2);
		// graphPane.getChildren().add(text);
		// }

	}

	public void update(GraphModelInterface gmi) {
		graphModel = gmi;
		drawGraph();
	}
}
