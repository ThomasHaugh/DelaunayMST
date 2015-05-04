package d_minSpanTree.controller.operation.algorithm;

import java.util.ArrayList;
import java.util.Stack;

import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;

public class ConvexHull implements GraphAlgorithm {

	public void execute(GraphModelInterface gmi) {
		if (gmi.getVertices().size() < 3)
			return;
		gmi.getEdges().clear();

		ArrayList<Object> vertices = new ArrayList<Object>();
		vertices.addAll(gmi.getVertices());
		Vertex source = extractSource(vertices);

		(new QuickSort(vertices, new AscPolarAngle(source))).sort();

		Stack<Vertex> stack = new Stack<Vertex>();
		stack.push(source);
		stack.push((Vertex) vertices.get(0));
		stack.push((Vertex) vertices.get(1));
		for (int i = 2; i < vertices.size(); i++) {
			Edge e = new Edge(stack.peek(), (Vertex) vertices.get(i));
			e.setOpacity(.1);
			gmi.getEdges().add(e);

			while (nonLeftTurn((Vertex) vertices.get(i), stack))
				stack.pop();
			stack.push((Vertex) vertices.get(i));
		}

		ArrayList<Vertex> ret = new ArrayList<Vertex>();
		for (int i = 0; i < stack.size(); i++) {
			ret.add(stack.elementAt(i));
		}

		Edge e = null;
		for (int i = 0; i < ret.size(); i++) {
			int next = (i + 1) % ret.size();
			e = new Edge(ret.get(i), ret.get(next));
			e.setOpacity(1);
			gmi.getEdges().add(e);
		}

	}

	private Vertex extractSource(ArrayList<Object> vertices) {
		Vertex curS = null;
		for (Object o : vertices) {
			Vertex v = (Vertex) o;
			if (curS == null)
				curS = v;
			else if (curS.getY() > v.getY())
				curS = v;
			else if (curS.getY() == v.getY())
				if (curS.getX() > v.getX())
					curS = v;
		}

		vertices.remove(curS);
		return curS;
	}

	private boolean nonLeftTurn(Vertex v, Stack<Vertex> s) {
		Vertex p0 = nextToTop(s);
		Vertex p1 = top(s);
		Vertex p2 = v;
		double cross = (p2.getX() - p0.getX()) * (p1.getY() - p0.getY()) - (p1.getX() - p0.getX()) * (p2.getY() - p0.getY());
		if (cross >= 0)
			return true;
		return false;
	}

	private Vertex nextToTop(Stack<Vertex> s) {
		return s.elementAt(s.size() - 2);
	}

	private Vertex top(Stack<Vertex> s) {
		return s.elementAt(s.size() - 1);
	}

	public boolean canLiveUpdate() {
		return true;
	}

	public String getName() {
		return "Convex Hull";
	}
}
