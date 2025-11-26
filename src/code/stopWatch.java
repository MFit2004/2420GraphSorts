package code;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;

/**
 * @author Matthew_Fitzgerald
 * @course CS-2420-001 Algorithms & Data Structures Fall 2025
 * @last_modified Nov 24, 2025
 * @assignment Team Project:
 *              Algorithm Racing
 * @description Jpanel Component stopwatch-timer
 */
public class stopWatch extends JPanel {

    private static final long serialVersionUID = 1L;

    private Timer stopwatchTimer = new Timer(); // live timer
    private boolean running = false;

    private JLabel timeVal; // display timer
    private JButton toggle; // start/stop button

    public stopWatch() {

        JPanel content = container(); // frame for sub-components
        add(content);

        toggle.addActionListener(e -> {
            running = !running;

            if (running) {
                stopwatchTimer.Start();
                toggle.setText("Stop");
            } else {
                stopwatchTimer.Stop();
                toggle.setText("Start");
            }
        });

        dynamicTimer();
    }
    
    /**
     * create framework for components
     * @return container with all components 
     */
    private JPanel container() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS)); 

        JPanel timePanel = timer(content); // timer framework
        content.add(timePanel);
        
        JPanel btnPanel = buttons(); // buttons framework
        content.add(btnPanel);

        return content;
    }
    
    /**
     * framework for timer
     * @param content upper framework structure
     * @return timer container with all components
     */
    private JPanel timer(JPanel content) {
        JPanel timePanel = new JPanel();
        timePanel.setAlignmentY(CENTER_ALIGNMENT);
        timePanel.setLayout(new GridLayout(1, 2));
        timePanel.setAlignmentY(CENTER_ALIGNMENT);
        
        JLabel timerLabel = label(timePanel); // timer label
        timePanel.add(timerLabel);

        timeVal = counter(timePanel); // live time
        timePanel.add(timeVal);
        
        return timePanel;
    }

	/**
	 * Label component settings
	 * @param timePanel content upper framework structure
	 * @return completed JLabel component
	 */
	private JLabel label(JPanel timePanel) {
		JLabel timerLabel = new JLabel("Timer: ");
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setVerticalAlignment(SwingConstants.CENTER);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        return timerLabel;
	}
	
	/**
	 * label with live time
	 * @param timePanel upper framework structure
	 * @return JLabel with time
	 */
	private JLabel counter(JPanel timePanel) {
        timeVal = new JLabel("0");
        timeVal.setVerticalAlignment(SwingConstants.CENTER);
        timeVal.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        return timeVal;
    }
	
    /**
     * framework for buttons
     * @return buttons JPanel with all components
     */
	private JPanel buttons() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setAlignmentY(CENTER_ALIGNMENT);
        toggle();
        btnPanel.add(toggle);
        return btnPanel;
    }
	
    /**
     * button controler for timer (field)
     */
	private void toggle() {
		toggle = new JButton("Start");
		toggle.setFocusPainted(false);
	    toggle.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // slight border
	    toggle.setContentAreaFilled(true); 
	    toggle.setContentAreaFilled(true); 
	    toggle.setPreferredSize(new Dimension(80, 25));
    }

    /**
     * timer creation and processing (using field)
     */
	private void dynamicTimer() {
        javax.swing.Timer swingTimer = new javax.swing.Timer(100, e -> {
            double billion = 1_000_000_000.0;
        		double timeSec = stopwatchTimer.getTime() / billion;
            long minutes = (long)(timeSec / 60);
            double seconds = timeSec % 60;
            timeVal.setText(String.format("%02d:%05.2f", minutes, seconds));
        });
        swingTimer.start();
    }

    public void Start() {
        stopwatchTimer.Start(); // start timer
    }

    public void Stop() {
        stopwatchTimer.Stop(); // stop timer
    }
}
