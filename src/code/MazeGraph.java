package code;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.GraphGenerator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stack;

public class MazeGraph {
	private Graph mazeGraph; // grid with all possible vertices
	Graph randomGraph; // random connections
	private int vertices;
	private int gridSize;
	public int start = 0;
	public int target = 0;

	/**
	 * build graphs based on total number of vertices
	 * 
	 * @param approxVertices
	 */
	public MazeGraph(int approxVertices) {
		this.gridSize = (int) Math.sqrt(approxVertices);
		this.vertices = gridSize * gridSize;

		this.mazeGraph = new Graph(vertices);
		generateMazeGraph();
	}

	private void generateMazeGraph() {
		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				int v = coordinatesToVertex(row, col);
				if (col < gridSize - 1)
					mazeGraph.addEdge(v, coordinatesToVertex(row, col + 1));
				if (row < gridSize - 1)
					mazeGraph.addEdge(v, coordinatesToVertex(row + 1, col));
			}
		}
	}

	/**
	 * DFS and algs4 inspired algorithm to generate random graph of int vertices
	 * 
	 * @param start starting vertex
	 * @param end   destination vertex
	 */
	public void generateRandomGraph(int start) {
	    this.start = start;
	    randomGraph = new Graph(vertices);
	    int maxRandomVertices = Math.max(2, vertices / 2); 
	    boolean[] visited = new boolean[vertices];
	    int[] stack = new int[vertices];
	    int stackSize = 0;

	    stack[stackSize++] = start;
	    visited[start] = true;
	    int countVisited = 1;
	    int[] includedVertices = new int[vertices];
	    int includedCount = 0;
	    includedVertices[includedCount++] = start;
	    while (stackSize > 0 && countVisited < maxRandomVertices) {
	        int current = stack[--stackSize];
	        int degree = mazeGraph.degree(current);
	        int[] neighbors = new int[degree];
	        int idx = 0;
	        for (int w : mazeGraph.adj(current)) {
	            neighbors[idx++] = w;
	        }
	        for (int i = degree - 1; i > 0; i--) {
	        		int r = StdRandom.uniformInt(i + 1);
	            int tmp = neighbors[i];
	            neighbors[i] = neighbors[r];
	            neighbors[r] = tmp;
	        }

	        for (int w : neighbors) {
	            if (!visited[w] && countVisited < maxRandomVertices) {
	                randomGraph.addEdge(current, w);
	                visited[w] = true;
	                stack[stackSize++] = w;
	                countVisited++;
	                includedVertices[includedCount++] = w;
	            }
	        }
	    }
	    int t;
	    do {
	    		t = includedVertices[StdRandom.uniformInt(includedCount)];
	    } while (t == start);
	    target = t;
	}
	
	private boolean isConnected(Graph g, int s, int t) {
	    boolean[] visited = new boolean[g.V()];
	    dfsVisit(g, s, visited);
	    return visited[t];
	}

	private void dfsVisit(Graph g, int v, boolean[] visited) {
	    visited[v] = true;
	    for (int w : g.adj(v)) {
	        if (!visited[w]) dfsVisit(g, w, visited);
	    }
	}

	/**
	 * algs4 (heavily) inspired swap method
	 * 
	 * @param vals int[] containing values
	 * @param a    first el to swap
	 * @param b    other el to swap
	 */
	private void swap(int[] vals, int a, int b) {
		int target = vals[a];
		vals[a] = vals[b];
		vals[b] = target;
	}

	public Graph getMazeGraph() {
		return mazeGraph;
	}

	public Graph getRandomGraph() {
		return randomGraph;
	}

	public int getVertices() {
		return vertices;
	}

	public int getGridSize() {
		return gridSize;
	}

	public int getStart() {
		return start;
	}

	public int getTarget() {
		return target;
	}

	public int[] vertexToCoordinates(int v) {
		return new int[] { v / gridSize, v % gridSize };
	}

	public int coordinatesToVertex(int row, int col) {
		return row * gridSize + col;
	}

	public void displayMaze() {
		System.out.println(
				"Maze: " + gridSize + "x" + gridSize + ", vertices = " + vertices + ", edges = " + mazeGraph.E());
	}
}