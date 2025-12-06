package code;
/**
 * store Timer and moves stats for algorithm 
 * "race"
 * @author Matthew_Fitzgerald + Trevor_Austin
 */
public class Statistics {
	private int distance = 0;
	private int moves	= 0; 
    private Timer timer = new Timer();
	
	public long getTime() {
		return timer.getTime();
	}

	public int getDistance() {
		return distance;
	}

	public int getMoves() {
		return moves;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void setMoves(int moves) {
		this.moves = moves;
	}
	
}
