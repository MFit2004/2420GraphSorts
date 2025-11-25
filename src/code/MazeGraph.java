package a5;

import edu.princeton.cs.algs4.Graph;

/**
 * MazeGraph constructs and manages a square grid maze represented
 * as an undirected Graph from the algs4 library. The class builds
 * all edges between horizontally and vertically adjacent vertices,
 * supports coordinate/vertex conversions, and provides basic maze info.
 */
public class MazeGraph {
    private Graph graph;
    private int vertices;
    private int edges;
    private int gridSize;
    
    /**
     * Constructs a MazeGraph based on an approximate vertex count.
     * The value is converted into the nearest square grid (n x n),
     * and an undirected graph is created connecting adjacent cells.
     *
     * @param v the approximate number of vertices requested
     */
    public MazeGraph(int v) {
        this.vertices = v;
        this.gridSize = (int) Math.sqrt(v);
        this.vertices = gridSize * gridSize; // force perfect square grid
        this.graph = new Graph(this.vertices);
        this.edges = 0;
        GenerateGraph(this.vertices);
    }
    
    /**
     * @return the generated graph representing the maze
     */
    public Graph getGraph() {
        return graph;
    }
    
    /**
     * @return total number of vertices in the maze grid
     */
    public int getVertices() {
        return vertices;
    }
    
    /**
     * @return total number of edges created in the maze graph
     */
    public int getEdges() {
        return edges;
    }
    
    /**
     * @return the side length of the square grid (n for n×n)
     */
    public int getGridSize() {
        return gridSize;
    }
    
    /**
     * Generates the graph by connecting each vertex to its right
     * and downward neighbors (where valid). This forms a standard
     * grid structure with adjacency edges.
     *
     * @param v the number of vertices (only used to trigger the loop)
     */
    public void GenerateGraph(int v) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int current = i * gridSize + j;
                
                // Connect to right neighbor
                if (j < gridSize - 1) {
                    graph.addEdge(current, current + 1);
                    edges++;
                }
                
                // Connect to bottom neighbor
                if (i < gridSize - 1) {
                    graph.addEdge(current, current + gridSize);
                    edges++;
                }
            }
        }
    }
    
    /**
     * Converts a vertex index into (row, column) coordinates.
     *
     * @param vertex the vertex index in the graph
     * @return an array {row, column}
     */
    public int[] vertexToCoordinates(int vertex) {
        return new int[]{vertex / gridSize, vertex % gridSize};
    }
    
    /**
     * Converts (row, column) coordinates back into a vertex index.
     *
     * @param row the row of the cell
     * @param col the column of the cell
     * @return the corresponding vertex index
     */
    public int coordinatesToVertex(int row, int col) {
        return row * gridSize + col;
    }
    
    /**
     * Prints a summary of the maze's structure including size,
     * total vertices, and total edges created.
     */
    public void displayMaze() {
        System.out.println("Maze Structure: " + gridSize + "x" + gridSize + " grid");
        System.out.println("Total vertices: " + vertices);
        System.out.println("Total edges: " + edges);
    }
}
