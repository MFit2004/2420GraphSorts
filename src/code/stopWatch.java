package code;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * @author Matthew_Fitzgerald
 * @course CS-2420-001 Algorithms & Data Structures Fall 2025
 * @last_modified Nov 24, 2025
 * @assignment Team Project:
 * 				Algorithm Racing
 * @description Jpanel Component stopwatch-timer
 * 				of minimal size
 */
public class stopWatch extends JPanel {

    private static final long serialVersionUID = 1L;

    private Timer timer = new Timer();
    private boolean running = false;

    private JLabel timeVal;  // the displayed stopwatch value

    public stopWatch() {

        JPanel content = container();
        //JPanel timePanel = timer(content);

        JPanel btnPanel = new JPanel();
        content.add(btnPanel);

        JButton toggle = new JButton("Start");
        btnPanel.add(toggle);

        toggle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                running = !running;

                if (running) {
                    timer.Start();
                    toggle.setText("Stop");
                } else {
                    timer.Stop();
                    toggle.setText("Start");
                }
            }
        });

        dynamicTimer();
        // -----------------------------------------------------------------
    }


	private void dynamicTimer() {
		final long billion = 1_000_000_000;
        javax.swing.Timer swingTimer = new javax.swing.Timer(100, e -> {
        	double timeSec = timer.getTime() / 1_000_000_000.0;  
        	long minutes = (long)(timeSec / 60);
        	double seconds = timeSec % 60;
        	timeVal.setText(String.format("%d:%05.4f", minutes, seconds));
        });
        swingTimer.start();
	}


    private JPanel timer(JPanel content) {
        JPanel timePanel = new JPanel();
        content.add(timePanel);

        JLabel Timer = new JLabel("Timer: ");
        timePanel.add(Timer);

        timeVal = counter(timePanel);
        return timePanel;
    }

    private JLabel counter(JPanel timePanel) {
        JLabel timeVal = new JLabel("0");
        timePanel.add(timeVal);
        return timeVal;
    }

    private JPanel container() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        add(content);
        return content;
    }

    public void Start() {
        timer.Start();
    }

    public void Stop() {
        timer.Stop();
    }
}

