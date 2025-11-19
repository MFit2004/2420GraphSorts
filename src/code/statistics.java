public class statistics {
	private int distance = 0;
	private int moves	= 0; 
    private timer timer = new timer();
	
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
