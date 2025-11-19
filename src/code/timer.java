
public class timer {
	private static long start = 0;
	private static long stop = 0;
	private int time = 0; // Not needed TODO
	statistics stat = new statistics();

	public static void Start() {
		start = System.nanoTime();
	}
	
	public static void Stop() {
		stop = System.nanoTime();
	}
	/**
	 * Current time meant to be called repeatedly 
	 * 
	 * @return time difference from start to stop
	 */
	public long getTime() {
		stop = System.nanoTime();
		return stop - start;
	}
	
}
