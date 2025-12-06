package graph;

import javax.swing.JFrame;
import edu.princeton.cs.algs4.Graph;
/**
 * Race represents the Visual elements of the graph race
 * 
 * @author Trevor_Austin + Matthew_Fitzgerald
 */
public class Race {
    private MazeVisualizer visualizer;
    private int RandStart;
    
    private MazeGraph graph;

    public Race(int vertices) {
        this.graph = new MazeGraph(vertices);
        graph.generateRandomGraph(RandStart);
        this.RandStart = graph.getRandStart();
        graph.generateMSTGraph();
    }

    public void setupVisualization() {
        visualizer = new MazeVisualizer(graph);
        visualizer.setDFSGraph(new Graph(graph.getVertices())); 
        visualizer.setBFSGraph(new Graph(graph.getVertices())); 

        JFrame frame = new JFrame("Maze Race");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(visualizer);
        frame.pack();
        frame.setVisible(true);
       

        visualizer.runDFS(RandStart);
        visualizer.runBFS(RandStart);
        visualizer.runKruskal();
        visualizer.runPrim(RandStart);
    }
}