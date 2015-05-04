package d_minSpanTree.controller.operation.algorithm;

import d_minSpanTree.model.Edge;
import d_minSpanTree.model.GraphModelInterface;
import d_minSpanTree.model.Vertex;

import java.util.ArrayList;

public class DelanayTriangualtion implements GraphAlgorithm {

    public void execute(GraphModelInterface gmi) {
	gmi.getEdges().clear();

	//First we just need the points to be triangualted
	vertices = gmi.getVerticies();
	vertices.sort(); // This gives us a lexicographic sort on the members

	//We pick the starting element as the first member of the sorted list
	//this point will let us build a triangle around the remaining points
	//due to its position 
	startVert = vertices.get(0);
	vertices.remove(0); // Removed so it can't be reused in the second phase

	//We make a surrounding triangle so that the algorithm is as simple
	//as possible, buildBigTriangle does this by makeing two fake vertices
	//which will be removed from the final triangulation
	triangle = buildBigTriangle(startVert, vertices); //triangle should be a three elem array
	//Positions 1 & 2 of triangle are added points

	//We also make a triangulation data structure
	//We can probably do better than an arraylist
	ArrayList<Vertex[]> triangulation = new ArrayList<Vertex[]>{};
	triangulation.add(triangle);

	//We permute the remaining elements to prevent edgecase behavior
	vertices.shuffle(); // I don't know if this method exist, but it's a good place holder

	//Now the main for loop over the remaining elements
	for(Vertex vert : vertices) {
	    //Right now I am leaving out an if statment that should be there to make the
	    //initial part of getting the code running smoothly easy
	    
	    //We find which memeber of the triangulation for now this might just
	    //be a linear scan and we return its index in the triangulation data structure
	    ctIndex = getContainingTriangleIndex(vert, triangulation);
	    refiningTriangle = trinagulation.get(ctIndex);
	    //we remove the triangle we will refine and then add in the three new triangles
	    triangulation.remove(ctIndex);
	    triangulation.add(new Vertex[]{vert, refiningTriangle[0], refiningTriangle[1]});
	    triangulation.add(new Vertex[]{vert, refiningTriangle[0], refiningTriangle[2]});
	    triangulation.add(new Vertex[]{vert, refiningTriangle[1], refiningTriangle[2]});
	    //Now we need to improve the local result
	    //We use the legalizeEdge method to do this
	    //For every pair of points there are at most two triangles containg that pair
	    //in a triangulation, so we are checking to see which crossing edge is better
	    //the current crossing or the one going from vert to the third point in the
	    //other triangle containing the two adjacent edges

	    legalizeEdge(vert, refiningTriangle[0], refiningTriangle[1], triangulation);
	    legalizeEdge(vert, refiningTriangle[0], refiningTriangle[2], triangulation);
	    legalizeEdge(vert, refiningTriangle[1], refiningTriangle[2], triangulation);
	}

	//Now we remove the fake points i.e. triangle[1], triangle[2]
	triangulation.removeTrianglesWithVertex(triangle[1]);
	triangulation.removeTrianglesWithVertex(triangle[2]);

	gmi.addTriangulationEdges(triangulation);
    }

    public String getName() {
	return "Delaunay Triangulation";
    }

    public bolean canLiveUpdate {
	return true;
    }

}
