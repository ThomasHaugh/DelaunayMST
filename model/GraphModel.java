package d_minSpanTree.model;

import java.util.ArrayList;

import d_minSpanTree.controller.operation.algorithm.GraphAlgorithm;

public class GraphModel implements GraphModelInterface {

	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private ArrayList<Edge> displayEdges = new ArrayList<Edge>();
	private ArrayList<GraphAlgorithm> graphAlgorithms = new ArrayList<GraphAlgorithm>();
	private ArrayList<GraphModelObserver> observers = new ArrayList<GraphModelObserver>();

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public ArrayList<Edge> getDisplayEdges() {
        return displayEdges;
    }

	public ArrayList<GraphAlgorithm> getGraphAlgorithms() {
		return graphAlgorithms;
	}

	public void runAlgorithms() {
		for (int i = 0; i < graphAlgorithms.size(); i++)
			graphAlgorithms.get(i).execute(this);
	}

	public void notifyObservers() {
		for (GraphModelObserver o : observers) {
			o.update(this);
		}
	}

	public void addObserver(GraphModelObserver o) {
		observers.add(o);

	}

	public void removeObserver(GraphModelObserver o) {
		observers.add(o);
	}

}
