package code;

import edu.princeton.cs.algs4.StdOut;

/**
 * timer test without GUI, not to be used in final
 * should now less cumbersome to run
 * goal: prove Timer updates are live
 * @author Matthew_Fitzgerald
 */
public class Test {
    public static void main(String[] args) {
        Timer timer = new Timer();
        StdOut.println("go!");
        boolean going = true;
        timer.Start();

        long endTime = System.nanoTime() + 1_000_000_000;
        // run for 1 second
        while (going) {
            StdOut.println(timer.getTime());

            if (System.nanoTime() >= endTime) {
                going = false;
            }

            try {
                Thread.sleep(2); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        timer.Stop();
        StdOut.println("Final time: " + timer.getTime());
    }
}