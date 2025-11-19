import edu.princeton.cs.algs4.Graph;

public class MazeGraph {
    private Graph graph;
    private int vertices;
    private int edges;
    private int gridSize;
    
    public MazeGraph(int v) {
        this.vertices = v;
        this.gridSize = (int) Math.sqrt(v);
        this.vertices = gridSize * gridSize;
        this.graph = new Graph(this.vertices);
        this.edges = 0;
        GenerateGraph(this.vertices);
    }
    
    public Graph getGraph() {
        return graph;
    }
    
    public int getVertices() {
        return vertices;
    }
    
    public int getEdges() {
        return edges;
    }
    
    public int getGridSize() {
        return gridSize;
    }
    
    public void GenerateGraph(int v) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int current = i * gridSize + j;
                
                if (j < gridSize - 1) {
                    graph.addEdge(current, current + 1);
                    edges++;
                }
                
                if (i < gridSize - 1) {
                    graph.addEdge(current, current + gridSize);
                    edges++;
                }
            }
        }
    }
    
    public int[] vertexToCoordinates(int vertex) {
        return new int[]{vertex / gridSize, vertex % gridSize};
    }
    
    public int coordinatesToVertex(int row, int col) {
        return row * gridSize + col;
    }
    
    public void displayMaze() {
        System.out.println("Maze Structure: " + gridSize + "x" + gridSize + " grid");
        System.out.println("Total vertices: " + vertices);
        System.out.println("Total edges: " + edges);
    }
}
