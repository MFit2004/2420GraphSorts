package code;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import edu.princeton.cs.algs4.Graph;

public class MazeVisualizer extends JPanel {

    private static final long serialVersionUID = 1L;
    private MazeGraph mazeGraph;
    private int gridSize;
    private int cellSize = 40;
    private int startVertex;
    private int targetVertex;
    private boolean showDFS = true;
    private boolean showBFS = true;

    private Graph dfsGraph;
    private Graph bfsGraph;
    private int dfsCurrentIndex = 0;
    private int bfsCurrentIndex = 0;
    private boolean dfsFinished = false;
    private boolean bfsFinished = false;

    protected stopWatch dfsTimer;
    protected stopWatch bfsTimer;

    private static final Color BG_COLOR = new Color(240, 240, 240);
    private static final Color WALL_COLOR = new Color(50, 50, 50);
    private static final Color START_COLOR = new Color(0, 200, 0);
    private static final Color TARGET_COLOR = new Color(200, 0, 0);
    private static final Color DFS_COLOR = new Color(255, 100, 100);
    private static final Color BFS_COLOR = new Color(100, 100, 255);
    private static final Color DFS_TRAIL_COLOR = new Color(255, 200, 200);
    private static final Color BFS_TRAIL_COLOR = new Color(200, 200, 255);

    private Graph random;
    private static final Color RANDOM_COLOR = Color.BLACK;

    public MazeVisualizer(MazeGraph mazeGraph, int startVertex, int target) {
        this.mazeGraph = mazeGraph;
        this.gridSize = mazeGraph.getGridSize();

        this.startVertex = startVertex;
        mazeGraph.generateRandomGraph(startVertex);
        this.targetVertex = mazeGraph.getTarget();
        this.random = mazeGraph.getRandomGraph();

        this.dfsGraph = new Graph(mazeGraph.getVertices());
        this.bfsGraph = new Graph(mazeGraph.getVertices());
        this.dfsCurrentIndex = startVertex;
        this.bfsCurrentIndex = startVertex;
        this.dfsFinished = false;
        this.bfsFinished = false;

        this.cellSize = 40;
        setPreferredSize(new Dimension(gridSize * cellSize + 300 + 200, gridSize * cellSize + 150));
        setBackground(BG_COLOR);

        dfsTimer = new stopWatch("DFS");
        bfsTimer = new stopWatch("BFS");
        dfsTimer.setPreferredSize(new Dimension(200, 30));
        bfsTimer.setPreferredSize(new Dimension(200, 30));

        setLayout(new BorderLayout());
        JPanel timerPanel = new JPanel();
        timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
        timerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        timerPanel.add(dfsTimer);
        timerPanel.add(bfsTimer);
        add(timerPanel, BorderLayout.EAST);
    }

    public void setRandomGraph(Graph g) {
        this.random = g;
        repaint();
    }

    public void setDFSGraph(Graph g) {
        this.dfsGraph = g;
        this.dfsFinished = false;
        repaint();
    }

    public void setBFSGraph(Graph g) {
        this.bfsGraph = g;
        this.bfsFinished = false;
        repaint();
    }

    public void startRace(int start) {
        SwingUtilities.invokeLater(() -> {
            runDFS(start);
            runBFS(start);
        });
    }

    public boolean isDFSFinished() {
        return dfsFinished;
    }

    public boolean isBFSFinished() {
        return bfsFinished;
    }

    public boolean bothFinished() {
        return dfsFinished && bfsFinished;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int leftOffset = 50;
        int topOffset = 50;

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("ALGORITHM RACE", leftOffset, 30);

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int x = leftOffset + col * cellSize;
                int y = topOffset + row * cellSize;
                g2d.setColor(Color.WHITE);
                g2d.fillRect(x, y, cellSize, cellSize);
                g2d.setColor(WALL_COLOR);
                g2d.drawRect(x, y, cellSize, cellSize);
            }
        }
        randGraph(g2d, leftOffset, topOffset);
        drawDFS(g2d, leftOffset, topOffset);

        drawBFS(g2d, leftOffset, topOffset);

        drawEnds(g2d, leftOffset, topOffset);

        legend(g2d, leftOffset, topOffset);
    }

	/**
	 * @param g2d
	 * @param leftOffset
	 * @param topOffset
	 */
	private void drawEnds(Graphics2D g2d, int leftOffset, int topOffset) {
		int[] startCoord = mazeGraph.vertexToCoordinates(startVertex);
        int[] targetCoord = mazeGraph.vertexToCoordinates(targetVertex);

        g2d.setColor(START_COLOR);
        g2d.fillOval(leftOffset + startCoord[1]*cellSize + 5, topOffset + startCoord[0]*cellSize + 5,
                cellSize - 10, cellSize - 10);
        g2d.setColor(Color.WHITE);
        g2d.drawString("S", leftOffset + startCoord[1]*cellSize + cellSize/2 - 4,
                topOffset + startCoord[0]*cellSize + cellSize/2 + 5);

        g2d.setColor(TARGET_COLOR);
        g2d.fillOval(leftOffset + targetCoord[1]*cellSize + 5, topOffset + targetCoord[0]*cellSize + 5,
                cellSize - 10, cellSize - 10);
        g2d.setColor(Color.WHITE);
        g2d.drawString("T", leftOffset + targetCoord[1]*cellSize + cellSize/2 - 4,
                topOffset + targetCoord[0]*cellSize + cellSize/2 + 5);
	}

	/**
	 * @param g2d
	 * @param leftOffset
	 * @param topOffset
	 */
	private void legend(Graphics2D g2d, int leftOffset, int topOffset) {
		int legendX = leftOffset + gridSize * cellSize + 20;
        int legendY = topOffset;
        int boxSize = 20;
        int spacing = 30;

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Legend:", legendX, legendY);

        g2d.setColor(DFS_COLOR);
        g2d.fillOval(legendX, legendY + spacing - 15, boxSize, boxSize);
        g2d.setColor(Color.BLACK);
        g2d.drawString("DFS Current", legendX + boxSize + 10, legendY + spacing);

        g2d.setColor(DFS_TRAIL_COLOR);
        g2d.fillRect(legendX, legendY + 2 * spacing - 15, boxSize, boxSize / 2);
        g2d.setColor(Color.BLACK);
        g2d.drawString("DFS Trail", legendX + boxSize + 10, legendY + 2 * spacing);

        g2d.setColor(BFS_COLOR);
        g2d.fillOval(legendX, legendY + 3 * spacing - 15, boxSize, boxSize);
        g2d.setColor(Color.BLACK);
        g2d.drawString("BFS Current", legendX + boxSize + 10, legendY + 3 * spacing);

        g2d.setColor(BFS_TRAIL_COLOR);
        g2d.fillRect(legendX, legendY + 4 * spacing - 15, boxSize, boxSize / 2);
        g2d.setColor(Color.BLACK);
        g2d.drawString("BFS Trail", legendX + boxSize + 10, legendY + 4 * spacing);

        g2d.setColor(START_COLOR);
        g2d.fillOval(legendX, legendY + 5 * spacing - 15, boxSize, boxSize);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Start", legendX + boxSize + 10, legendY + 5 * spacing);

        g2d.setColor(TARGET_COLOR);
        g2d.fillOval(legendX, legendY + 6 * spacing - 15, boxSize, boxSize);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Target", legendX + boxSize + 10, legendY + 6 * spacing);
	}

	/**
	 * @param g2d
	 * @param leftOffset
	 * @param topOffset
	 */
	private void drawBFS(Graphics2D g2d, int leftOffset, int topOffset) {
		if (bfsGraph != null && (showBFS || !bfsFinished)) {
            g2d.setColor(BFS_TRAIL_COLOR);
            g2d.setStroke(new BasicStroke(2));
            for (int v = 0; v < bfsGraph.V(); v++) {
                for (int w : bfsGraph.adj(v)) {
                    if (v < w) {
                        int[] from = mazeGraph.vertexToCoordinates(v);
                        int[] to = mazeGraph.vertexToCoordinates(w);
                        g2d.drawLine(
                                leftOffset + from[1] * cellSize + 3 * cellSize / 4,
                                topOffset + from[0] * cellSize + 3 * cellSize / 4,
                                leftOffset + to[1] * cellSize + 3 * cellSize / 4,
                                topOffset + to[0] * cellSize + 3 * cellSize / 4
                        );
                    }
                }
            }
        }

        if (!bfsFinished) {
            int[] coord = mazeGraph.vertexToCoordinates(bfsCurrentIndex);
            g2d.setColor(BFS_COLOR);
            g2d.fillOval(leftOffset + coord[1] * cellSize + 12, topOffset + coord[0] * cellSize + 12,
                    cellSize - 24, cellSize - 24);
        }
	}

	/**
	 * @param g2d
	 * @param leftOffset
	 * @param topOffset
	 */
	private void drawDFS(Graphics2D g2d, int leftOffset, int topOffset) {
		if (dfsGraph != null && (showDFS || !dfsFinished)) {
            g2d.setColor(DFS_TRAIL_COLOR);
            g2d.setStroke(new BasicStroke(2));
            for (int v = 0; v < dfsGraph.V(); v++) {
                for (int w : dfsGraph.adj(v)) {
                    if (v < w) {
                        int[] from = mazeGraph.vertexToCoordinates(v);
                        int[] to = mazeGraph.vertexToCoordinates(w);
                        g2d.drawLine(
                                leftOffset + from[1] * cellSize + cellSize / 4,
                                topOffset + from[0] * cellSize + cellSize / 4,
                                leftOffset + to[1] * cellSize + cellSize / 4,
                                topOffset + to[0] * cellSize + cellSize / 4
                        );
                    }
                }
            }
        }

        if (!dfsFinished) {
            int[] coord = mazeGraph.vertexToCoordinates(dfsCurrentIndex);
            g2d.setColor(DFS_COLOR);
            g2d.fillOval(leftOffset + coord[1] * cellSize + 12, topOffset + coord[0] * cellSize + 12,
                    cellSize - 24, cellSize - 24);
        }
	}

	/**
	 * @param g2d
	 * @param leftOffset
	 * @param topOffset
	 */
	private void randGraph(Graphics2D g2d, int leftOffset, int topOffset) {
		if (random != null) {
            g2d.setStroke(new BasicStroke(2));
            for (int v = 0; v < random.V(); v++) {
                for (int w : random.adj(v)) {
                    if (v < w) {
                        int[] from = mazeGraph.vertexToCoordinates(v);
                        int[] to = mazeGraph.vertexToCoordinates(w);
                        g2d.drawLine(
                            leftOffset + from[1] * cellSize + cellSize / 2,
                            topOffset + from[0] * cellSize + cellSize / 2,
                            leftOffset + to[1] * cellSize + cellSize / 2,
                            topOffset + to[0] * cellSize + cellSize / 2
                        );
                    }
                }
            }
        }
	}

    public void runDFS(int startIgnored) {
        Graph g = mazeGraph.getRandomGraph();
        int start = startVertex;
        int target = targetVertex;

        dfsGraph = new Graph(g.V());
        boolean[] visited = new boolean[g.V()];
        Stack<Integer> stack = new Stack<>();
        stack.push(start);
        dfsCurrentIndex = start;
        dfsFinished = false;

        new Thread(() -> {
            dfsTimer.Reset();
            dfsTimer.Start();

            while (!stack.isEmpty() && !dfsFinished) {
                int v = stack.pop();
                if (!visited[v]) {
                    visited[v] = true;
                    dfsCurrentIndex = v;

                    for (int w : g.adj(v)) {
                        if (!visited[w]) {
                            dfsGraph.addEdge(v, w);
                            stack.push(w);
                        }
                    }

                    SwingUtilities.invokeLater(this::repaint);
                    try { Thread.sleep(300); } catch (InterruptedException ignored) {}

                    if (v == target) dfsFinished = true;
                }
            }

            dfsTimer.Stop();
            dfsFinished = true;
            SwingUtilities.invokeLater(this::repaint);
        }).start();
    }

    public void runBFS(int startIgnored) {
        Graph g = mazeGraph.getRandomGraph();
        int start = startVertex;
        int target = targetVertex;

        bfsGraph = new Graph(g.V());
        boolean[] visited = new boolean[g.V()];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        bfsCurrentIndex = start;
        bfsFinished = false;

        new Thread(() -> {
            bfsTimer.Reset();
            bfsTimer.Start();

            while (!queue.isEmpty() && !bfsFinished) {
                int v = queue.poll();
                if (!visited[v]) {
                    visited[v] = true;
                    bfsCurrentIndex = v;

                    for (int w : g.adj(v)) {
                        if (!visited[w]) {
                            bfsGraph.addEdge(v, w);
                            queue.add(w);
                        }
                    }

                    SwingUtilities.invokeLater(this::repaint);
                    try { Thread.sleep(300); } catch (InterruptedException ignored) {}

                    if (v == target) bfsFinished = true;
                }
            }

            bfsTimer.Stop();
            bfsFinished = true;
            SwingUtilities.invokeLater(this::repaint);
        }).start();
    }

    public void toggleDFS() {
        if (!dfsFinished) {
            System.out.println("DFS is running, cannot toggle trail.");
            return;
        }
        showDFS = !showDFS;
        SwingUtilities.invokeLater(this::repaint);
        System.out.println("DFS trail " + (showDFS ? "shown" : "hidden"));
    }

    public void toggleBFS() {
        if (!bfsFinished) {
            System.out.println("BFS is running, cannot toggle trail.");
            return;
        }
        showBFS = !showBFS;
        SwingUtilities.invokeLater(this::repaint);
        System.out.println("BFS trail " + (showBFS ? "shown" : "hidden"));
    }
}
