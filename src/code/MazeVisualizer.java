package code;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.UF;

public class MazeVisualizer extends JPanel {

	// general utilities
	private static final long serialVersionUID = 1L;
	private MazeGraph mazeGraph; // all vertices
	private int gridSize;
	private int cellSize = 40;
	private static final Color BG_COLOR = new Color(240, 240, 240);
	private static final Color WALL_COLOR = new Color(50, 50, 50);
	
	
	private int RandStartV;
	private int RandEndV;
	
	protected stopWatch dfsTimer;
	private boolean showDFS = true;
	private Graph dfsGraph;
	private int dfsCurrentIndex = 0;
	private boolean dfsFinished = false;
	private static final Color DFS_COLOR = new Color(255, 100, 100);
	private static final Color DFS_TRAIL_COLOR = new Color(255, 200, 200);

	protected stopWatch bfsTimer;
	private boolean showBFS = true;
	private Graph bfsGraph;
	private int bfsCurrentIndex = 0;
	private boolean bfsFinished = false;
	private static final Color BFS_COLOR = new Color(100, 100, 255);
	private static final Color BFS_TRAIL_COLOR = new Color(200, 200, 255);
	
	
	private static final Color START_COLOR = new Color(0, 200, 0);
	private static final Color TARGET_COLOR = new Color(200, 0, 0);
	
	private static final Color KRUSKAL_COLOR = new Color(255, 140, 0);
	private static final Color KRUSKAL_TRAIL = new Color(255, 200, 120);
	private static final Color PRIM_COLOR = new Color(150, 80, 200);
	private static final Color PRIM_TRAIL = new Color(210, 180, 255);
	protected stopWatch kruskalTimer;
	protected stopWatch primTimer;
	
	private EdgeWeightedGraph kruskalGraph; 
	private EdgeWeightedGraph primGraph;

	private int kruskalIndex = -1;
	private int primIndex = -1;

	private boolean kruskalFinished = false;
	private boolean primFinished = false;

	private Graph random;
	private EdgeWeightedGraph MST;
	private static final Color RANDOM_COLOR = Color.BLACK;
	private static final Color MST_COLOR = Color.YELLOW;

	private int refreshRate = 350;
	private static final Stroke BOLD_STROKE = new BasicStroke(
		    3.0f, 
		    BasicStroke.CAP_ROUND, 
		    BasicStroke.JOIN_ROUND
		); 
	private static final Dimension STOPWATCH_SIZE = new Dimension(75, 40);

	public MazeVisualizer(MazeGraph mazeGraph) {
		this.mazeGraph = mazeGraph;
		this.gridSize = mazeGraph.getGridSize();

		this.RandStartV = mazeGraph.startRand;
		mazeGraph.generateRandomGraph(RandStartV);
		this.RandEndV = mazeGraph.getRandTarget();
		this.random = mazeGraph.getRandomGraph();

		this.MST = mazeGraph.getMstGraph();

		this.dfsGraph = new Graph(mazeGraph.getVertices());
		this.bfsGraph = new Graph(mazeGraph.getVertices());
		this.dfsCurrentIndex = RandStartV;
		this.bfsCurrentIndex = RandStartV;
		this.dfsFinished = false;
		this.bfsFinished = false;

		this.cellSize = 40;
		setPreferredSize(new Dimension(gridSize * cellSize + 300 + 300, gridSize * cellSize + 150));
		setBackground(BG_COLOR);

		dfsTimer = new stopWatch("DFS");
		bfsTimer = new stopWatch("BFS");
		kruskalTimer = new stopWatch("Kruskal");
		primTimer = new stopWatch("Prim");
		dfsTimer.setPreferredSize(STOPWATCH_SIZE);
		bfsTimer.setPreferredSize(STOPWATCH_SIZE);
		kruskalTimer.setPreferredSize(STOPWATCH_SIZE);
		primTimer.setPreferredSize(STOPWATCH_SIZE);

		setLayout(new BorderLayout());
		JPanel timerPanel = timers();
		add(timerPanel, BorderLayout.EAST);
	}

	/**
	 * create and format timers
	 * @return JPanel timerPanel, container with elements and formating
	 */
	private JPanel timers() {
		JPanel timerPanel = new JPanel();
		
		timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
		timerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		Font header = new Font("Arial", Font.BOLD, 18);
		JLabel randLbl = new JLabel("Random graph traversal");
		randLbl.setForeground(Color.BLACK);
		randLbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
		randLbl.setFont(header);
		timerPanel.add(randLbl);
		timerPanel.add(dfsTimer);
		timerPanel.add(bfsTimer);
		JLabel mstLbl = new JLabel("Find the MST");
		mstLbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
		mstLbl.setFont(header);
		timerPanel.add(mstLbl);
		timerPanel.add(kruskalTimer);
		timerPanel.add(primTimer);
		return timerPanel;
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

		int leftOffsetAlgs = 75;
		int topOffsetAlgs = 75;

		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.BOLD, 24));
		g2d.drawString("ALGORITHM RACE", leftOffsetAlgs, 30);

		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				int x = leftOffsetAlgs + col * cellSize;
				int y = topOffsetAlgs + row * cellSize;
				g2d.setColor(Color.WHITE);
				g2d.fillRect(x, y, cellSize, cellSize);
				g2d.setColor(Color.WHITE);
				g2d.drawRect(x, y, cellSize, cellSize);
			}
		}
		randGraph(g2d, leftOffsetAlgs, topOffsetAlgs);
		drawDFS(g2d, leftOffsetAlgs, topOffsetAlgs);
		drawBFS(g2d, leftOffsetAlgs, topOffsetAlgs);
		MSTGraph(g2d, leftOffsetAlgs, topOffsetAlgs);
		drawKruskal(g2d, leftOffsetAlgs, topOffsetAlgs);
		drawPrim(g2d, leftOffsetAlgs, topOffsetAlgs);
		drawRandEnds(g2d, leftOffsetAlgs, topOffsetAlgs);
		drawMSTStart(g2d, leftOffsetAlgs, topOffsetAlgs);
		drawMSTEnd(g2d, leftOffsetAlgs, topOffsetAlgs);
		legend(g2d, leftOffsetAlgs, topOffsetAlgs);
	}
	
	/**
	 * @param g2d        2dgraphices to draw
	 * @param leftOffset x-cooridinate placement
	 * @param topOffset  y-cooridinate placement
	 */
	private void randGraph(Graphics2D g2d, int leftOffset, int topOffset) {
	    if (random != null) {
	        g2d.setColor(RANDOM_COLOR);
	        g2d.setStroke(BOLD_STROKE);
	        
	        int dotSize = 10; // diameter of the dot
	        Set<Integer> drawnVertices = new HashSet<>(); // track which vertices already got a dot

	        for (int v = 0; v < random.V(); v++) {
	            for (int w : random.adj(v)) {
	                // Draw dot for v if not already drawn
	                if (!drawnVertices.contains(v)) {
	                    int[] from = mazeGraph.vertexToCoordinates(v);
	                    int dotX = leftOffset + from[1] * cellSize + cellSize / 2 - dotSize / 2;
	                    int dotY = topOffset + from[0] * cellSize + cellSize / 2 - dotSize / 2;
	                    g2d.fillOval(dotX, dotY, dotSize, dotSize);
	                    drawnVertices.add(v);
	                }

	                // Draw dot for w if not already drawn
	                if (!drawnVertices.contains(w)) {
	                    int[] to = mazeGraph.vertexToCoordinates(w);
	                    int dotX = leftOffset + to[1] * cellSize + cellSize / 2 - dotSize / 2;
	                    int dotY = topOffset + to[0] * cellSize + cellSize / 2 - dotSize / 2;
	                    g2d.fillOval(dotX, dotY, dotSize, dotSize);
	                    drawnVertices.add(w);
	                }

	                // Draw the edge
	                if (v < w) { // avoid drawing the same edge twice
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

	
	/**
	 * display edges of (future) MST graph
	 * @param g2d
	 * @param leftOffset
	 * @param topOffset
	 */
	private void MSTGraph(Graphics2D g2d, int leftOffset, int topOffset) {
	    if (MST != null) {
	        g2d.setColor(MST_COLOR);
	        g2d.setStroke(BOLD_STROKE);
	        int circleSize = 10; // diameter of the circle

	        Set<Integer> drawnVertices = new HashSet<>(); // track which vertices already got a circle

	        for (Edge e : MST.edges()) { 
	            int v = e.either();
	            int w = e.other(v);
	            String weight = String.format("%.0f", e.weight());

	            // Draw unfilled circle for v if not already drawn
	            if (!drawnVertices.contains(v)) {
	                int[] coords = mazeGraph.vertexToCoordinates(v);
	                int x = leftOffset + coords[1] * cellSize + cellSize / 2 - circleSize / 2;
	                int y = topOffset + coords[0] * cellSize + cellSize / 2 - circleSize / 2;
	                g2d.drawOval(x, y, circleSize, circleSize);
	                drawnVertices.add(v);
	            }

	            // Draw unfilled circle for w if not already drawn
	            if (!drawnVertices.contains(w)) {
	                int[] coords = mazeGraph.vertexToCoordinates(w);
	                int x = leftOffset + coords[1] * cellSize + cellSize / 2 - circleSize / 2;
	                int y = topOffset + coords[0] * cellSize + cellSize / 2 - circleSize / 2;
	                g2d.drawOval(x, y, circleSize, circleSize);
	                drawnVertices.add(w);
	            }

	            // Draw the edge
	            int[] from = mazeGraph.vertexToCoordinates(v);
	            int[] to = mazeGraph.vertexToCoordinates(w);
	            int x1 = leftOffset + from[1] * cellSize + cellSize / 2 + 6;
	            int y1 = topOffset + from[0] * cellSize + cellSize / 2 + 6;
	            int x2 = leftOffset + to[1] * cellSize + cellSize / 2 + 6;
	            int y2 = topOffset + to[0] * cellSize + cellSize / 2 + 6;
	            int midX = (x1 + x2) / 2;
	            int midY = (y1 + y2) / 2;

	            g2d.drawLine(x1, y1, x2, y2);
	            g2d.setColor(Color.BLACK);
	            g2d.setFont(new Font("Arial", Font.BOLD, 10));
	            g2d.drawString(weight, midX, midY);
	            g2d.setColor(MST_COLOR);
	        }
	    }
	}

	
	/**
	 * draw DFS progress live
	 * @param g2d        2dgraphices to draw
	 * @param leftOffset x-cooridinate placement
	 * @param topOffset  y-cooridinate placement
	 */
	private void drawDFS(Graphics2D g2d, int leftOffset, int topOffset) {
		if (dfsGraph != null && (showDFS || !dfsFinished)) {
			g2d.setColor(DFS_COLOR);
			g2d.setStroke(BOLD_STROKE);
			for (int v = 0; v < dfsGraph.V(); v++) {
				for (int w : dfsGraph.adj(v)) {
					if (v < w) {
						int[] from = mazeGraph.vertexToCoordinates(v);
						int[] to = mazeGraph.vertexToCoordinates(w);
						g2d.drawLine(leftOffset + from[1] * cellSize + cellSize / 4,
								topOffset + from[0] * cellSize + cellSize / 4,
								leftOffset + to[1] * cellSize + cellSize / 4,
								topOffset + to[0] * cellSize + cellSize / 4);
					}
				}
			}
		}

		if (!dfsFinished) {
			int[] coord = mazeGraph.vertexToCoordinates(dfsCurrentIndex);
			g2d.setColor(DFS_COLOR);
			g2d.fillOval(leftOffset + coord[1] * cellSize + 12, topOffset + coord[0] * cellSize + 12, cellSize - 24,
					cellSize - 24);
		}
	}
	
	/**
	 * live drawing of BFS progress
	 * @param g2d        2dgraphices to draw
	 * @param leftOffset x-cooridinate placement
	 * @param topOffset  y-cooridinate placement
	 */
	private void drawBFS(Graphics2D g2d, int leftOffset, int topOffset) {
		if (bfsGraph != null && (showBFS || !bfsFinished)) {
			g2d.setColor(BFS_COLOR);
			g2d.setStroke(BOLD_STROKE);
			for (int v = 0; v < bfsGraph.V(); v++) {
				for (int w : bfsGraph.adj(v)) {
					if (v < w) {
						int[] from = mazeGraph.vertexToCoordinates(v);
						int[] to = mazeGraph.vertexToCoordinates(w);
						g2d.drawLine(leftOffset + from[1] * cellSize + 3 * cellSize / 4,
								topOffset + from[0] * cellSize + 3 * cellSize / 4,
								leftOffset + to[1] * cellSize + 3 * cellSize / 4,
								topOffset + to[0] * cellSize + 3 * cellSize / 4);
					}
				}
			}
		}

		if (!bfsFinished) {
			int[] coord = mazeGraph.vertexToCoordinates(bfsCurrentIndex);
			g2d.setColor(BFS_COLOR);
			g2d.fillOval(leftOffset + coord[1] * cellSize + 12, topOffset + coord[0] * cellSize + 12, cellSize - 24,
					cellSize - 24);
		}
	}

	/**
	 * draw end points of random graph
	 * @param g2d 2dgraphices to draw
	 * @param leftOffset x-cooridinate placement
	 * @param topOffset  y-cooridinate placement
	 */
	private void drawRandEnds(Graphics2D g2d, int leftOffset, int topOffset) {
		drawRandStart(g2d, leftOffset, topOffset);
		drawRandEnd(g2d, leftOffset, topOffset);
	}

	/**
	 * draw start of Random graph
	 * @param g2d 2dgraphices to draw
	 * @param leftOffset x-cooridinate placement
	 * @param topOffset  y-cooridinate placement
	 */
	private void drawRandStart(Graphics2D g2d, int leftOffset, int topOffset) {
		int[] startCoord = mazeGraph.vertexToCoordinates(RandStartV);
		g2d.setColor(START_COLOR);
		g2d.fillOval(leftOffset + startCoord[1] * cellSize + 5, topOffset + startCoord[0] * cellSize + 5, cellSize - 10,
				cellSize - 10);
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.BOLD, 16));
		FontMetrics fm = g2d.getFontMetrics();
		String sText = "R";
		int sWidth = fm.stringWidth(sText);
		int sHeight = fm.getAscent();
		g2d.drawString(sText, leftOffset + startCoord[1] * cellSize + cellSize / 2 - sWidth / 2,
				topOffset + startCoord[0] * cellSize + cellSize / 2 + sHeight / 2 - 2);	
	}
	
	/**
	 * draw end of random graph
	 * @param g2d 2dgraphices to draw
	 * @param leftOffset x-cooridinate placement
	 * @param topOffset  y-cooridinate placement
	 */
	private void drawRandEnd(Graphics2D g2d, int leftOffset, int topOffset) {
		int[] targetCoord = mazeGraph.vertexToCoordinates(RandEndV);
		g2d.setColor(TARGET_COLOR);
		g2d.fillOval(leftOffset + targetCoord[1] * cellSize + 5, topOffset + targetCoord[0] * cellSize + 5,
				cellSize - 10, cellSize - 10);
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.BOLD, 16));
		String tText = "R";
		FontMetrics fm = g2d.getFontMetrics();
		int tWidth = fm.stringWidth(tText);
		int tHeight = fm.getAscent();
		g2d.drawString(tText, leftOffset + targetCoord[1] * cellSize + cellSize / 2 - tWidth / 2,
				topOffset + targetCoord[0] * cellSize + cellSize / 2 + tHeight / 2 - 2);
	}
	
	/**
	 * draw start of future MST graph
	 * @param g2d 2dgraphices to draw
	 * @param leftOffset x-cooridinate placement
	 * @param topOffset  y-cooridinate placement
	 */
	private void drawMSTStart(Graphics2D g2d, int leftOffset, int topOffset) {
	    int start = mazeGraph.getMSTStart();
	    int[] coord = mazeGraph.vertexToCoordinates(start);

	    g2d.setColor(START_COLOR);
	    g2d.fillOval(
	        leftOffset + coord[1] * cellSize + 5,
	        topOffset + coord[0] * cellSize + 5,
	        cellSize - 10,
	        cellSize - 10
	    );

	    g2d.setColor(Color.WHITE);
	    g2d.setFont(new Font("Arial", Font.BOLD, 16));
	    FontMetrics fm = g2d.getFontMetrics();

	    String sText = "M";   
	    int sWidth = fm.stringWidth(sText);
	    int sHeight = fm.getAscent();

	    g2d.drawString(
	        sText,
	        leftOffset + coord[1] * cellSize + cellSize / 2 - sWidth / 2,
	        topOffset + coord[0] * cellSize + cellSize / 2 + sHeight / 2 - 2
	    );
	}
	
	/**
	 * track Kruskal progress live
	 * @param g2d graphics used to draw
	 * @param leftOffset format settings
	 * @param topOffset format settings
	 */
	private void drawKruskal(Graphics2D g2d, int leftOffset, int topOffset) {
		if (kruskalGraph == null)
			return;

		g2d.setStroke(BOLD_STROKE);

		g2d.setColor(KRUSKAL_COLOR);
		for (Edge e : kruskalGraph.edges()) {
			int v = e.either();
			int w = e.other(v);
			int[] from = mazeGraph.vertexToCoordinates(v);
			int[] to = mazeGraph.vertexToCoordinates(w);
			int fromX = leftOffset + from[1] * cellSize + cellSize / 2;
			int fromY = topOffset + from[0] * cellSize + cellSize / 2;
			int toX = leftOffset + to[1] * cellSize + cellSize / 2;
			int toY = topOffset + to[0] * cellSize + cellSize / 2;
			int offsetX = -10;
			int offsetY = -10;

			g2d.drawLine(fromX + offsetX, fromY + offsetY, toX + offsetX, toY + offsetY);
		}

		if (!kruskalFinished && kruskalIndex >= 0) {
			int[] coord = mazeGraph.vertexToCoordinates(kruskalIndex);
			g2d.setColor(KRUSKAL_COLOR);
			g2d.fillOval(leftOffset + coord[1] * cellSize + 12, topOffset + coord[0] * cellSize + 12, cellSize - 24,
					cellSize - 24);
		}
	}
	
	/**
	 * track Prim progress live
	 * @param g2d graphics used to draw
	 * @param leftOffset format settings
	 * @param topOffset format settings
	 */
	private void drawPrim(Graphics2D g2d, int leftOffset, int topOffset) {
	    if (primGraph == null) 
	        return;

	    g2d.setStroke(BOLD_STROKE);

	    g2d.setColor(PRIM_COLOR);
	    for (Edge e : primGraph.edges()) {
	        int v = e.either();
	        int w = e.other(v);

	        int[] from = mazeGraph.vertexToCoordinates(v);
	        int[] to   = mazeGraph.vertexToCoordinates(w);

	        int fromX = leftOffset + from[1] * cellSize + cellSize / 2;
	        int fromY = topOffset  + from[0] * cellSize + cellSize / 2;
	        int toX   = leftOffset + to[1]   * cellSize + cellSize / 2;
	        int toY   = topOffset  + to[0]   * cellSize + cellSize / 2;

	        int offsetX = -10;
	        int offsetY = -10;

	        g2d.drawLine(fromX + offsetX, fromY + offsetY,
	                     toX   + offsetX, toY   + offsetY);
	    }

	    if (!primFinished && primIndex >= 0) {
	        int[] coord = mazeGraph.vertexToCoordinates(primIndex);
	        g2d.setColor(PRIM_COLOR);
	        g2d.fillOval(
	            leftOffset + coord[1] * cellSize + 12,
	            topOffset  + coord[0] * cellSize + 12,
	            cellSize - 24, cellSize - 24
	        );
	    }
	}

	/**
	 * draw end of future MST graph
	 * @param g2d 2dgraphices to draw
	 * @param leftOffset x-cooridinate placement
	 * @param topOffset  y-cooridinate placement
	 */
	private void drawMSTEnd(Graphics2D g2d, int leftOffset, int topOffset) {
	    int end = mazeGraph.getMSTTarget();
	    int[] coord = mazeGraph.vertexToCoordinates(end);

	    g2d.setColor(TARGET_COLOR);
	    g2d.fillOval(
	        leftOffset + coord[1] * cellSize + 5,
	        topOffset + coord[0] * cellSize + 5,
	        cellSize - 10,
	        cellSize - 10
	    );

	    g2d.setColor(Color.WHITE);
	    g2d.setFont(new Font("Arial", Font.BOLD, 16));
	    FontMetrics fm = g2d.getFontMetrics();

	    String tText = "M"; 
	    int tWidth = fm.stringWidth(tText);
	    int tHeight = fm.getAscent();

	    g2d.drawString(
	        tText,
	        leftOffset + coord[1] * cellSize + cellSize / 2 - tWidth / 2,
	        topOffset + coord[0] * cellSize + cellSize / 2 + tHeight / 2 - 2
	    );
	}

	/**
	 * draw start symbol legend
	 * @param g2d 2dgraphices to draw
	 * @param leftOffset x-cooridinate placement
	 * @param topOffset  y-cooridinate placement
	 */
	private void legend(Graphics2D g2d, int leftOffset, int topOffset) {
		int legendX = leftOffset + gridSize * cellSize + 20;
		int legendY = topOffset;
		int boxSize = 20;
		int spacing = 30;

		legendTitle(g2d, legendX, legendY);
		//vertexLabel(g2d, legendX, legendY, spacing);
		RandGLbl(g2d, legendX, legendY, spacing);		
		legendDFS(g2d, legendX, legendY, boxSize, spacing);
		legendBFS(g2d, legendX, legendY, boxSize, spacing);

		g2d.fillRect(legendX + 5, legendY + 7 * spacing + 0, boxSize, boxSize / 2);
		g2d.drawString("Random Graph", legendX + boxSize + 17, legendY + 7 * spacing + 10);

		startRandLbl(g2d, legendX, legendY, boxSize, spacing);

		endRandLbl(g2d, legendX, legendY, boxSize, spacing);
		
		mstHeader(g2d, legendX, legendY, boxSize, spacing);
		kruskalSbl(g2d, legendX, legendY, boxSize, spacing);
		primSbl(g2d, legendX, legendY, boxSize, spacing);
		mstEndpts(g2d, legendX, legendY, boxSize, spacing);
	}

	private void legendTitle(Graphics2D g2d, int legendX, int legendY) {
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.BOLD, 16));
		g2d.drawString("Legend", legendX, legendY);
	}
	
	/**
	 * legend vertex representation
	 */
	private void vertexLabel(Graphics2D g2d, int legendX, int legendY, int spacing) {
		int keyCellSize = 20;
		int keyX = legendX;
		int keyY = legendY + 15; 
		g2d.setColor(Color.WHITE);
		g2d.fillRect(keyX + 5, keyY + 1 * spacing/7, keyCellSize, keyCellSize);
		g2d.setColor(WALL_COLOR);
		g2d.drawRect(keyX + 5, keyY + 1 * spacing/7, keyCellSize, keyCellSize);
		g2d.setColor(Color.BLACK); 
		g2d.drawString("×1 Vertex", keyX + keyCellSize + 15, keyY + keyCellSize - 5 + 1 * spacing/7);
	}
	
	/**
	 * random graph elements header
	 */
	private void RandGLbl(Graphics2D g2d, int legendX, int legendY, int spacing) {
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.BOLD, 16));
		g2d.drawString("Random Graph items:", legendX, legendY + (7) * spacing/3);
	}
	
	/**
	 * legend DFS symbols
	 */
	private void legendDFS(Graphics2D g2d, int legendX, int legendY, int boxSize, int spacing) {
		g2d.setColor(DFS_COLOR);
		g2d.fillOval(legendX + 5, legendY + 60 + spacing - 5, boxSize, boxSize);
		g2d.setColor(Color.BLACK);
		g2d.drawString("DFS Current", legendX + boxSize + 17, legendY + 3 * spacing + 10);

		g2d.setColor(DFS_TRAIL_COLOR);
		g2d.fillRect(legendX + 5, legendY + 5 * spacing - 30, boxSize, boxSize / 2);
		g2d.setColor(Color.BLACK);
		g2d.drawString("DFS Trail", legendX + boxSize + 17, legendY + 4 * spacing + 10);
	}
	
	/**
	 * legend BFS symbols
	 */
	private void legendBFS(Graphics2D g2d, int legendX, int legendY, int boxSize, int spacing) {
		g2d.setColor(BFS_COLOR);
		g2d.fillOval(legendX + 5, legendY + 5 * spacing - 5, boxSize, boxSize);
		g2d.setColor(Color.BLACK);
		g2d.drawString("BFS Current", legendX + boxSize + 17, legendY + 5 * spacing + 10);

		g2d.setColor(BFS_TRAIL_COLOR);
		g2d.fillRect(legendX + 5, legendY + 6 * spacing - 0, boxSize, boxSize / 2);
		g2d.setColor(Color.BLACK);
		g2d.drawString("BFS Trail", legendX + boxSize + 17, legendY + 6 * spacing + 10);
	}

	/**
	 * legend random graph start label
	 */
	private void startRandLbl(Graphics2D g2d, int legendX, int legendY, int boxSize, int spacing) {
		g2d.setColor(START_COLOR);
		g2d.fillOval(legendX + 5, legendY + 8 * spacing - 5, boxSize, boxSize);
		g2d.setColor(Color.BLACK);
		g2d.drawString("Start", legendX + boxSize + 17, legendY + 8 * spacing + 10);		
		g2d.setColor(Color.WHITE);
		g2d.drawString("r", legendX + 12, legendY + 8 * spacing + 8);
	}
	
	/**
	 * legend random graph target label
	 */
	private void endRandLbl(Graphics2D g2d, int legendX, int legendY, int boxSize, int spacing) {
		g2d.setColor(TARGET_COLOR);
		g2d.fillOval(legendX + 5, legendY + 9 * spacing - 5, boxSize, boxSize);
		g2d.setColor(Color.BLACK);
		g2d.drawString("Target", legendX + boxSize + 17, legendY + 9 * spacing + 10);
		g2d.setColor(Color.WHITE);
		g2d.drawString("r", legendX + 12, legendY + 9 * spacing + 8);
	}
	
	/**
	 * MST legend symbols
	 */
	private void mstHeader(Graphics2D g2d, int legendX, int legendY, int boxSize, int spacing) {
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.BOLD, 16));
		g2d.drawString("MST items:", legendX, legendY + 10 * spacing + 10);
		g2d.setColor(MST_COLOR);
		g2d.fillRect(legendX + 5, legendY + 11 * spacing - 1, boxSize, boxSize / 2);
		g2d.setColor(Color.BLACK);
		g2d.drawString("MST Graph", legendX + boxSize + 17, legendY + 11 * spacing + 10);
	}
	/**
	 * Kruskal legend symbols
	 */
	private void kruskalSbl(Graphics2D g2d, int legendX, int legendY, int boxSize, int spacing) {
		g2d.setColor(KRUSKAL_COLOR);
		g2d.fillOval(legendX + 5, legendY + 12 * spacing - 10, boxSize, boxSize);
		g2d.setColor(Color.BLACK);
		g2d.drawString("Kruskal current", legendX + boxSize + 17, legendY + 12 * spacing + 7);
		g2d.setColor(KRUSKAL_TRAIL);
		g2d.fillRect(legendX + 5, legendY + 13 * spacing - 5, boxSize, boxSize / 2);
		g2d.setColor(Color.BLACK);
		g2d.drawString("Kruskal trail", legendX + boxSize + 17, legendY + 12 * spacing + 35);
	}
	
	/**
	 * prim legend symbols
	 */
	private void primSbl(Graphics2D g2d, int legendX, int legendY, int boxSize, int spacing) {
		g2d.setColor(PRIM_COLOR);
		g2d.fillOval(legendX + 5, legendY + 14 * spacing - 10, boxSize, boxSize);
		g2d.setColor(Color.BLACK);
		g2d.drawString("Prim current", legendX + boxSize + 17, legendY + 14 * spacing + 7);
		g2d.setColor(PRIM_TRAIL);
		g2d.fillRect(legendX + 5, legendY + 15 * spacing - 5, boxSize, boxSize / 2);
		g2d.setColor(Color.BLACK);
		g2d.drawString("Prim trail", legendX + boxSize + 17, legendY + 15 * spacing + 4);
	}
	
	/**
	 * draw MST graph end points (start and target)
	 */
	private void mstEndpts(Graphics2D g2d, int legendX, int legendY, int boxSize, int spacing) {
		g2d.setColor(START_COLOR);
		g2d.fillOval(legendX + 5, legendY + 16 * spacing - 10, boxSize, boxSize);
		g2d.setColor(Color.WHITE);
		g2d.drawString("m", legendX + 9, legendY + 16 * spacing + 6);
		g2d.setColor(Color.BLACK);
		g2d.drawString("Start", legendX + boxSize + 17, legendY + 16 * spacing + 4);
		g2d.setColor(TARGET_COLOR);
		g2d.fillOval(legendX + 5, legendY + 17 * spacing - 10, boxSize, boxSize);
		g2d.setColor(Color.WHITE);
		g2d.drawString("m", legendX + 9, legendY + 17 * spacing + 6);
		g2d.setColor(Color.BLACK);
		g2d.drawString("Target", legendX + boxSize + 17, legendY + 17 * spacing + 4);
	}
	
	/**
	 * draw DFS traversal
	 * @param localStart current index of DFS
	 */
	public void runDFS(int localStart) {
		Graph g = mazeGraph.getRandomGraph();
		int start = RandStartV;
		int target = RandEndV;

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
					try {
						Thread.sleep(refreshRate);
					} catch (InterruptedException ignored) {
					}

					if (v == target)
						dfsFinished = true;
				}
			}

			dfsTimer.Stop();
			dfsFinished = true;
			SwingUtilities.invokeLater(this::repaint);
		}).start();
	}

	/**
	 * draw BFS traversal
	 * @param localStart current index of BFS
	 */
	public void runBFS(int localStart) {
		Graph g = mazeGraph.getRandomGraph();
		int start = RandStartV;
		int target = RandEndV;

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
					try {
						Thread.sleep(300);
					} catch (InterruptedException ignored) {
					}

					if (v == target)
						bfsFinished = true;
				}
			}

			bfsTimer.Stop();
			bfsFinished = true;
			SwingUtilities.invokeLater(this::repaint);
		}).start();
	}
	
	/**
	 * find path of travel for Kruskal algorithm
	 */
	
	public void runKruskal() {
		EdgeWeightedGraph g = mazeGraph.getMstGraph();
		kruskalGraph = new EdgeWeightedGraph(g.V());
		kruskalFinished = false;

		int numEdges = g.E();
		Edge[] edges = new Edge[numEdges];

		int k = 0;
		for (Edge e : g.edges()) {
			edges[k++] = e;
		}

		for (int i = 1; i < numEdges; i++) {
			Edge key = edges[i];
			double w = key.weight();
			int j = i - 1;

			while (j >= 0 && edges[j].weight() > w) {
				edges[j + 1] = edges[j];
				j--;
			}
			edges[j + 1] = key;
		}

		UF uf = new UF(g.V());

		new Thread(() -> {
			kruskalTimer.Reset();
			kruskalTimer.Start();

			for (int i = 0; i < numEdges; i++) {
				Edge e = edges[i];
				int v = e.either();
				int w = e.other(v);

				if (uf.find(v) != uf.find(w)) {
					uf.union(v, w);
					kruskalGraph.addEdge(new Edge(v, w, e.weight()));
					int[] vCoord = mazeGraph.vertexToCoordinates(v);
					int[] wCoord = mazeGraph.vertexToCoordinates(w);
					int r = vCoord[0];
					int c = vCoord[1];
					while (r != wCoord[0]) {
						kruskalIndex = mazeGraph.coordinatesToVertex(r, c);
						SwingUtilities.invokeLater(this::repaint);
						try {
							Thread.sleep(refreshRate);
						} catch (InterruptedException ignored) {
						}
						r += (wCoord[0] > r) ? 1 : -1;
					}
					while (c != wCoord[1]) {
						kruskalIndex = mazeGraph.coordinatesToVertex(r, c);
						SwingUtilities.invokeLater(this::repaint);
						try {
							Thread.sleep(refreshRate);
						} catch (InterruptedException ignored) {
						}
						c += (wCoord[1] > c) ? 1 : -1;
					}
					kruskalIndex = w;
					SwingUtilities.invokeLater(this::repaint);
					try {
						Thread.sleep(refreshRate);
					} catch (InterruptedException ignored) {
					}
				}
			}

			kruskalFinished = true;
			kruskalTimer.Stop();
			SwingUtilities.invokeLater(this::repaint);

		}).start();

	}
	
	/**
	 * draw prim traversal
	 */
	public void runPrim(int start) {
	    EdgeWeightedGraph g = mazeGraph.getMstGraph();
	    primGraph = new EdgeWeightedGraph(g.V());

	    boolean[] marked = new boolean[g.V()];
	    MinPQ<Edge> pq = new MinPQ<>();
	    primIndex = start;

	    markAndInsertEdges(start, g, marked, pq);
	    //primTimer.Reset();
	    new Thread(() -> {

	    	SwingUtilities.invokeLater(() -> {
	    	    primTimer.Reset();
	    	    primTimer.Start();
	    	});

	        while (!pq.isEmpty()) {

	            Edge e = pq.delMin();
	            int v = e.either();
	            int w = e.other(v);

	            if (marked[v] && marked[w]) {
	                continue;
	            }

	            int next = marked[v] ? w : v;

	            primGraph.addEdge(new Edge(v, w, e.weight()));

	            int[] vCoord = mazeGraph.vertexToCoordinates(v);
	            int[] wCoord = mazeGraph.vertexToCoordinates(w);

	            int r = vCoord[0];
	            int c = vCoord[1];

	            while (r != wCoord[0]) {
	                primIndex = mazeGraph.coordinatesToVertex(r, c);
	                SwingUtilities.invokeLater(this::repaint);

	                try {
	                    Thread.sleep(refreshRate);
	                } catch (InterruptedException ignored) {}

	                r += (wCoord[0] > r) ? 1 : -1;
	            }

	            while (c != wCoord[1]) {
	                primIndex = mazeGraph.coordinatesToVertex(r, c);
	                SwingUtilities.invokeLater(this::repaint);

	                try {
	                    Thread.sleep(refreshRate);
	                } catch (InterruptedException ignored) {}

	                c += (wCoord[1] > c) ? 1 : -1;
	            }

	            primIndex = next;
	            SwingUtilities.invokeLater(this::repaint);

	            try {
	                Thread.sleep(refreshRate);
	            } catch (InterruptedException ignored) {}

	            markAndInsertEdges(next, g, marked, pq);
	        }

	        SwingUtilities.invokeLater(() -> {
	            primTimer.Stop();
	            repaint();
	        });
	        
	        primFinished = true;

	    }).start();
	}

	
	private void markAndInsertEdges(int v, EdgeWeightedGraph g,
	                                boolean[] marked, MinPQ<Edge> pq) {
	    marked[v] = true;
	    for (Edge e : g.adj(v)) {
	        int w = e.other(v);
	        if (!marked[w]) {
	            pq.insert(e);      
	        }
	    }
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
