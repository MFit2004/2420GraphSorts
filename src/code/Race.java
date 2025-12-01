package code;

import javax.swing.JFrame;

import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.DepthFirstPaths;
import edu.princeton.cs.algs4.Graph;

public class Race {
    private MazeVisualizer visualizer;
    private int start;
    private int target;
    private MazeGraph graph;

    public Race(int vertices, int start, int target) {
        this.graph = new MazeGraph(vertices); // build your maze
        this.start = start;
        this.target = target;
    }

    public void setupVisualization() {
        visualizer = new MazeVisualizer(graph, start, target);
        visualizer.setDFSGraph(new Graph(graph.getVertices())); 
        visualizer.setBFSGraph(new Graph(graph.getVertices())); 

        JFrame frame = new JFrame("Maze Race");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(visualizer);
        frame.pack();
        frame.setVisible(true);

        visualizer.runDFS(start);
        visualizer.runBFS(start);
    }

    private Graph computeDFSGraph() {
        Graph maze = graph.getMazeGraph();
        DepthFirstPaths dfs = new DepthFirstPaths(maze, start);
        Graph g = new Graph(graph.getVertices());

        if (dfs.hasPathTo(target)) {
            int prev = start;
            for (int v : dfs.pathTo(target)) {
                if (v != prev) g.addEdge(prev, v);
                prev = v;
            }
        }
        return g;
    }

    private Graph computeBFSGraph() {
        Graph maze = graph.getMazeGraph();
        BreadthFirstPaths bfs = new BreadthFirstPaths(maze, start);
        Graph g = new Graph(graph.getVertices());

        if (bfs.hasPathTo(target)) {
            int prev = start;
            for (int v : bfs.pathTo(target)) {
                if (v != prev) g.addEdge(prev, v);
                prev = v;
            }
        }
        return g;
    }
}