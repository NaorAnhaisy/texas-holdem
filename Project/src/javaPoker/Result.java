package javaPoker;

/**
 * Represents result of hand strength of player.
 * @author Naor Anhaisy
 *
 */
public class Result {
	
	private int maxRank;
	private int winType;
	private String handString;
	
	/**
	 * Create new result object, with the parameters given.
	 * @param maxRank the max rank card in the player's hand.
	 * @param winType the max win type of the player's hand.
	 * @param handString String that represents what is the hand strength of player's hand.
	 */
	public Result(int maxRank, int winType, String handString) {
		this.maxRank = maxRank;
		this.winType = winType;
		this.handString = handString;
	}

	/**
	 * @return the max rank of player.
	 */
	public int getMaxRank() {
		return maxRank;
	}

	/**
	 * Sets max rank to player.
	 * @param maxRank max rank to player.
	 */
	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}

	/**
	 * @return the win type of player.
	 */
	public int getWinType() {
		return winType;
	}

	/**
	 * @return String that describes the hand strength of the player.
	 */
	public String getHandString() {
		return handString;
	}
}
