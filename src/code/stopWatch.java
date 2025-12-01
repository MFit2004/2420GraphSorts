package code;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matthew_Fitzgerald
 * @course CS-2420-001 Algorithms & Data Structures Fall 2025
 * @last_modified Nov 26, 2025
 * @assignment Team Project:
 *              Algorithm Racing
 * @description live timer for use with JFrame
 */
public class stopWatch extends JPanel {

    private static final long serialVersionUID = 1L;

    private Timer stopwatchTimer = new Timer(); 
    private boolean running = false;
    private String algorithm = "default";   

    private JLabel timeVal;  
    private JLabel nameLabel;
    private JButton toggleBtn;
    protected boolean algorithmEnabled = true;

    public static void main(String[] args) {
        // Use the Swing event dispatch thread
        SwingUtilities.invokeLater(() -> {
            // Create the frame
            JFrame frame = new JFrame("StopWatch Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 100);
            frame.getContentPane().setLayout(new BorderLayout());

            // Create StopWatch instance
            stopWatch stopwatch = new stopWatch("Algorithm A");
            stopwatch.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Add the stopwatch to the center of the frame
            frame.getContentPane().add(stopwatch, BorderLayout.CENTER);

            // Show the frame
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Start the stopwatch for demo
            stopwatch.Start();
        });
    }
    
    public stopWatch(String name) {

		this.algorithm = name;

		// Keep horizontal BoxLayout
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		int minHeight = 28;
		setMinimumSize(new Dimension(0, minHeight));
		setPreferredSize(new Dimension(200, minHeight));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, minHeight));

		// ==== TIME PANEL ====
		// ==== TIME PANEL ====
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));
		timePanel.setAlignmentY(Component.CENTER_ALIGNMENT);

		// Timer label
		JLabel timerLabel = new JLabel("Timer:");
		timerLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		timePanel.add(timerLabel);

		// small horizontal spacing
		timePanel.add(Box.createHorizontalStrut(5));

		// Time value
		timeVal = new JLabel("0:00.0000");
		timeVal.setAlignmentY(Component.CENTER_ALIGNMENT);
		timePanel.add(timeVal);

		add(timePanel);

		nameLabel = new JLabel(this.algorithm);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
		nameLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

		add(nameLabel);


		javax.swing.Timer swingTimer = new javax.swing.Timer(50, e -> updateLabel());
		swingTimer.start();
	}
    
    public void setNameLabel(String name) {
        this.algorithm = name;
        nameLabel.setText(name);
    }

    private void updateLabel() {
        double timeSec = stopwatchTimer.getTime() / 1_000_000_000.0;
        long minutes = (long)(timeSec / 60);
        double seconds = timeSec % 60;
        timeVal.setText(String.format("%d:%06.3f", minutes, seconds));
    }

    public void Start() {
        running = true;
        stopwatchTimer.Start();
    }

    public void Stop() {
        running = false;
        stopwatchTimer.Stop();
    }

    public void Reset() {
        running = false;
        stopwatchTimer.Stop();
        stopwatchTimer = new Timer();
        updateLabel();
    }

    public double getTimeSeconds() {
        return stopwatchTimer.getTime() / 1_000_000_000.0;
    }
}
