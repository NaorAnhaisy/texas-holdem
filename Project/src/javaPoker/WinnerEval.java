package javaPoker;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents Winner Evaluator to the Texas Holde'm game.
 * @author Naor Anhaisy
 *
 */
public class WinnerEval {
	private List<HandEval> hands;
	private List<Player> players;
	
	/**
	 * Create new WinnerEval object, and initialize hands and players lists.
	 */
	public WinnerEval() {
		hands = new ArrayList<HandEval>();
		players = new ArrayList<Player>();
	}
	
	/**
	 * Adds hand to the hands list.
	 * @param he HandEvalNumber object that represents hand to add.
	 * @param player the player that holds that hand.
	 */
	public void addHand(HandEval he, Player player) {
		hands.add(he);
		players.add(player);
	}
	
	/**
	 * Calculate which hand of all is the strongest. 
	 * (First by max win type, and then by max rank)
	 * @return List contains all players that won the hand.
	 */
	public List<Player> getWinner() {
		List<Player> winners = new ArrayList<Player>();
		int maxWinningType = 0, maxValue = 0;
		
		for (int i = 0; i < hands.size(); i++) {
			
			// find the highest winning type :
			if (hands.get(i).evaluateHand().getWinType() > maxWinningType) {
				maxValue = hands.get(i).evaluateHand().getMaxRank();
				maxWinningType = hands.get(i).evaluateHand().getWinType();
			}
			
			// if some players with the same winning type, compare their max rank :
			else if (hands.get(i).evaluateHand().getWinType() == maxWinningType) {
				if (hands.get(i).evaluateHand().getMaxRank() > maxValue) {
					maxValue = hands.get(i).evaluateHand().getMaxRank();
				}
			}
		}
		
		for (int i = 0; i < hands.size(); i++) {
			if (hands.get(i).evaluateHand().getWinType() == maxWinningType && hands.get(i).evaluateHand().getMaxRank() == maxValue) {
				winners.add(players.get(i));
			}
		}
		return winners;
	}
}
