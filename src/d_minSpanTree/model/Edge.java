package d_minSpanTree.model;

public class Edge implements Comparable<Edge> {
	private Vertex start, end;
	private double weight, opacity;
	private EdgeState state;

	public Edge(Vertex v1, Vertex v2) {
		this(v1, v2, Double.NaN, 0.1, EdgeState.UNDIRECTED);
	}

	public Edge(Vertex v1, Vertex v2, double weight, double opacity, EdgeState state) {
		start = v1;
		end = v2;
		this.weight = weight;
		this.opacity = opacity;
		this.state = state;

		v1.getAdjacent().add(v2);
		v2.getAdjacent().add(v1);
	}

	public Vertex getStart() {
		return start;
	}

	public void setStart(Vertex v) {
		start = v;
	}

	public Vertex getEnd() {
		return end;
	}

	public void setEnd(Vertex v) {
		end = v;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double w) {
		weight = w;
	}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double o) {
		opacity = o;
	}

	public EdgeState getState() {
		return state;
	}

	public void setState(EdgeState e) {
		state = e;
	}

	// We just want this so that our set can tell
	// when two objects are equal.
    @Override
    public int compareTo(Edge o) {
        int compX = Double.compare(start.getX(), o.start.getX());
        int compY = Double.compare(start.getY(), o.start.getY());
        
        if (compX != 0) {
            return compX;
        } else if (compY != 0) {
            return compY;
        } else { // Edges are equal.
            return 0;
        }
    }

}