package graph;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * JTimer is a child of JFrame that represents a timer 
 * 
 *   @author Trevor_Austin + Matthew_Fitzgerald
 */
public class JTimer extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private Timer timer = new Timer();                    
    private JLabel runningTimeLabel = new JLabel("Time: 0 ms");
    private boolean running;                               

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JTimer frame = new JTimer();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public JTimer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timePanel.add(runningTimeLabel); 
        contentPane.add(timePanel);

        JPanel eventPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton starter = new JButton("Start");
        starter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                running = !running;
                if (running) {
                    timer.Start();
                    starter.setText("Stop");
                } else {
                    timer.Stop();
                    starter.setText("Start");
                }
            }
        });
        eventPanel.add(starter);
        contentPane.add(eventPanel);

        startElapsedTimeUpdater();
    }


    private void startElapsedTimeUpdater() {
        javax.swing.Timer swingTimer = new javax.swing.Timer(100, e -> {
            runningTimeLabel.setText("Time: " + timer.getTime() / 1_000_000 + " ms");
        });
        swingTimer.start();
    }
}
