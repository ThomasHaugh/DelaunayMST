package d_minSpanTree.controller.operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;

public class LoadGraph implements GraphOperation {
	private File file;
	private Stack<UndoableGraphOperation> undoStack;

	public LoadGraph(String path, Stack<UndoableGraphOperation> commandStack) {
		file = new File(path);
		undoStack = commandStack;
	}

	public void execute(GraphModelInterface gmi) {
		undoStack.clear();
		gmi.getVertices().clear();
		gmi.getEdges().clear();

		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(file));

			HashMap<String, Vertex> verticesByName = new HashMap<String, Vertex>();

			String vertex;
			while ((vertex = bf.readLine()) != null && !vertex.equals("----------")) {
				Scanner in = new Scanner(vertex);
				in.useDelimiter("\\s+");
				String vertexName;
				double xPos;
				double yPos;
				vertexName = in.next();
				if (verticesByName.containsKey(vertexName))
					continue;
				xPos = in.nextDouble();
				yPos = in.nextDouble();
				in.close();

				Vertex v = new Vertex(vertexName, xPos, yPos);
				verticesByName.put(vertexName, v);

				gmi.getVertices().add(v);
			}

			String edge;
			while ((edge = bf.readLine()) != null) {
				Scanner in = new Scanner(edge);
				in.useDelimiter("\\s+");
				String vertexName1;
				String vertexName2;
				double weight;
				vertexName1 = in.next();
				vertexName2 = in.next();
				weight = in.nextDouble();
				in.close();

				Edge e = new Edge(verticesByName.get(vertexName1), verticesByName.get(vertexName2));
				e.setWeight(weight);

				gmi.getEdges().add(e);
			}
		} catch (Exception io) {
			io.printStackTrace();
			if (bf != null)
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		gmi.runAlgorithms();
	}

	public String getName() {
		return "Open";
	}

}
