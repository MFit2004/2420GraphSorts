package code;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.GraphGenerator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stack;

public class MazeGraph {
	private Graph mazeGraph; // grid with all possible vertices
	protected Graph randomGraph; // random connections
	protected EdgeWeightedGraph mstGraph;
	protected int vertices;
	protected int gridSize;
	protected int startRand = 0;
	protected int targetRand = 0;
	protected int startMST = 0;
	private int maxRandomVertices; // limit number of random vertices

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
	 * @goal: random sparse graph for DFS and BFS
	 * DFS and algs4 inspired algorithm to generate random graph
	 * generate a random graph from start, pick endpoint from graph
	 * @param start starting vertex
	 */
	public void generateRandomGraph(int start) {
	    this.startRand = start;
	    randomGraph = new Graph(vertices);
	    maxRandomVertices = Math.max(2, vertices / 2);
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
	                if (stackSize < stack.length) {
	                    stack[stackSize++] = w;
	                }
	                countVisited++;
	                includedVertices[includedCount++] = w;
	            }
	        }
	    }
	    int t;
	    do {
	    		t = includedVertices[StdRandom.uniformInt(includedCount)];
	    } while (t == start);
	    targetRand = t;
	}
	
	/**
	 * @goal: random sparse graph for DFS and BFS
	 * DFS {@link edu.princeton.cs.algs4.DepthFirstSearch} 
	 * and algs4 inspired algorithm<br> 
	 * Generate random EdgeWeightedgraph from random start in bounds 
	 * pick endpoint from graph 
	 * weights between (0,1000)
	 */
	public void generateMSTGraph() {
		int start = StdRandom.uniformInt(0, maxRandomVertices);
		mstGraph = new EdgeWeightedGraph(vertices);
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
	        int degree = vertices - 1;
	        int[] neighbors = new int[degree];
	        int idx = 0;
	        for (int v = 0; v < vertices; v++) {
	            if (v != current) {
	                neighbors[idx++] = v;
	            }
	        }
	        
	        for (int i = degree - 1; i >= 1; i--) {
	        		int r = StdRandom.uniformInt(i + 1);
	            int tmp = neighbors[i];
	            neighbors[i] = neighbors[r];
	            neighbors[r] = tmp;
	        }

	        for (int w : neighbors) {
	            if (!visited[w] && countVisited < maxRandomVertices) {
	            		int randVal = StdRandom.uniformInt(1000);
	            		Edge newEdge = new Edge(current, w, randVal);
	            		mstGraph.addEdge(newEdge);
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
	    targetRand = t;
	}

	private void dfsVisit(Graph g, int v, boolean[] visited) {
	    visited[v] = true;
	    for (int w : g.adj(v)) {
	        if (!visited[w]) dfsVisit(g, w, visited);
	    }
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
		return startRand;
	}

	public int getTarget() {
		return targetRand;
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
