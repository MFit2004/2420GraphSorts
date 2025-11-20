package code;

/**
 * Provide an object to time Algorithm running
 */
public class Timer {
	private static long start = 0;
	private static long stop = 0;

	/**
	 * initialize Timer Object
	 */
	public Timer() {
		
	}
	
	/**
	 * start the Timer
	 */
	public void Start() {
		start = System.nanoTime();
	}
	
	/**
	 * Stop the Timer
	 */
	public void Stop() {
		stop = System.nanoTime();
	}
	/**
	 * Current time meant to be called repeatedly 
	 * @return time difference from start to stop,
	 * in Nanoseconds (ns)
	 */
	public long getTime() {
		stop = System.nanoTime();
		return stop - start;
	}
	
}
