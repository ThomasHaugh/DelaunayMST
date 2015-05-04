package d_minSpanTree.controller.operation.algorithm;

import d_minSpanTree.model.Vertex;

public class AscPolarAngle implements SortBehavior {
	private Vertex source;

	public AscPolarAngle(Vertex s) {
		source = s;
	}

	public boolean compare(Object o1, Object o2) {
		if (o1 instanceof Vertex && o2 instanceof Vertex) {
			Vertex v1 = (Vertex) o1;
			Vertex v2 = (Vertex) o2;
			return getNormalizedX(v1) > getNormalizedX(v2);
		}
		return false;
	}

	private double getNormalizedX(Vertex v) {
		double xVec = v.getX() - source.getX();
		double yVec = v.getY() - source.getY();
		double dis = Math.sqrt(xVec * xVec + yVec * yVec);
		return xVec /= dis;
	}

}
