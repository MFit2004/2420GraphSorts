import edu.princeton.cs.algs4.Graph;

public class MazeGraph {
    private Graph graph;
    private int vertices;
    private int edges;

    public MazeGraph(int v) {
        this.vertices = v;
        this.graph = new Graph(v);
        GenerateGraph(v);
    }

    public Graph getGraph() {
        return graph;
    }

    public void GenerateGraph(int v) {
        // TODO: fill with maze connections
        // simple version: random edges
    }
}
