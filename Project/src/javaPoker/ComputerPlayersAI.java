package javaPoker;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Represents Artificial Intelligence (AI) of the computer players.
 * @author Naor Anhaisy
 *
 */
public class ComputerPlayersAI {
	
	private static final double MINIMUM_RISK_VALUE = 0.75;
	private static final int NUMBER_OF_SIMULATIONS = 10000;
	public static int nowNum;
	private Card[][] cardsOfPlayers;
	private Random rand = new Random();
	
	/**
	 * Create new poker computer AI object.
	 * Initialize all the players cards with the parameter given, and
	 * initialize the community cards field.
	 * @param players array of the players.
	 */
	public ComputerPlayersAI(Player[] players) {
		initialisePlayersCards(players);
	}
	
	/**
	 * Initialize the matrix that hold each player's cards.
	 * @param players all the players array.
	 */
	private void initialisePlayersCards(Player[] players) {
        
		cardsOfPlayers = new Card[TexasHoldEm.NUM_PLAYERS][2];
		for (int i = 0; i < cardsOfPlayers.length; i++) {
			cardsOfPlayers[i][0] = players[i].getCard(0);
			cardsOfPlayers[i][1] = players[i].getCard(1);
			System.out.println("Player number " + (i + 1) + " holds: ");
			System.out.print(Card.rankAsString(cardsOfPlayers[i][0].getRank()) + " of " + Card.suitAsString(cardsOfPlayers[i][0].getSuit()) + " , And ");
			System.out.println(Card.rankAsString(cardsOfPlayers[i][1].getRank()) + " of " + Card.suitAsString(cardsOfPlayers[i][1].getSuit()));
		}
	}
	
	/**
	 * Main process in the computer player AI.
	 * Calculates the best action player can do in known only his cards
	 * and the opened community cards.
	 * @param currentPlayer current player to play.
	 * @param currentPlayerIndex index of the current player to play.
	 * @param currentRoundBet the current round bet in the game.
	 * @param sumMoneyOnTable the sum money that is on the table.
	 * @param round number of round.
	 * @param numPlayersInHand number of players in the current hand.
	 * @param communityCards List of all the current board cards.
	 * @return the optimal action the player should do.
	 */
	public Action makeAction(Player currentPlayer, int currentPlayerIndex, int currentRoundBet, int sumMoneyOnTable, int numPlayersInHand, List<Card> communityCards) {
		System.out.println(" \n\n---------- player " + currentPlayerIndex + " start action --------------");
		double chances = calculateChencesToWin(currentPlayerIndex, numPlayersInHand, communityCards);
		Action action = doActionByPotentialBenefit(currentPlayer, chances, currentRoundBet, sumMoneyOnTable, numPlayersInHand);
		System.out.println("Current Player : " + (currentPlayerIndex) + ", chances " + chances);
		return action;
	}
	
	/**
	 * Calculates the chances of player to win a game.
	 * Do so by making NUMBER_OF_SIMULATIONS simulations - new game but
	 * with the known cards, and check in each game if he won.
	 * if so, increase his score by 1 / number of winners.
	 * @param currentPlayerIndex the current player index.
	 * @param numOfPlayerInHand number of players in hand.
	 * @param communityCards list of all the current board cards.
	 * @return the player's score (his chances to win).
	 */
	public double calculateChencesToWin(int currentPlayerIndex, int numOfPlayerInHand, List<Card> communityCards) {
		double score = 0;
		for (int i = 0; i < NUMBER_OF_SIMULATIONS; i++) {
//			FORDEBUG
//			nowNum = i;
			MiniTexasHoldEm miniTexasHoldEm = new MiniTexasHoldEm(numOfPlayerInHand, cardsOfPlayers[currentPlayerIndex], communityCards, currentPlayerIndex, null);
			List<Integer> winnersId = miniTexasHoldEm.endHand();
			
			// Checking if the current player is in the winners list.
			if (winnersId.contains(currentPlayerIndex))
				score += 1.0 / winnersId.size();
		}
		return score / NUMBER_OF_SIMULATIONS;
	}
	
	/**
	 * Calculate the max potential benefit of all the potential raises,
	 * and by this decide what is the best action the player can do.
	 * @param currentPlayer the current player.
	 * @param chances the player's chances to win the hand.
	 * @param currentRoundBet the current round bet.
	 * @param round the current round.
	 * @param sumMoneyOnTable the sum money on table right now.
	 * @param numOfPlayerInHand the number of players in the hand.
	 * @return the optimal action the player should do.
	 */
	private Action doActionByPotentialBenefit(Player currentPlayer, double chances, int currentRoundBet, int sumMoneyOnTable, int numOfPlayerInHand) {
		Action action = null;
		int howMuchMoneyDoINeedToCall = (currentRoundBet - currentPlayer.getBet());
		int howMuchMoneyWillThereBeInTheBoardIfEveryoneAfterMeCalls = sumMoneyOnTable + (currentRoundBet * numOfPlayerInHand);
		HashMap<Integer, Double> raiseToPotential = new HashMap<Integer, Double>();
		
		for (int raise = 0; raise <= currentPlayer.getMoney(); raise += 20) {
			double potentialBenefitForRaise = potentialBenefitForRaise(chances, numOfPlayerInHand, howMuchMoneyDoINeedToCall,
					howMuchMoneyWillThereBeInTheBoardIfEveryoneAfterMeCalls, raise, currentPlayer.getMoney(), currentRoundBet);
			
			raiseToPotential.put(raise, potentialBenefitForRaise);
//			System.out.println(raise + " : " + potentialBenefitForRaise);
		}
		
		action = doMax(currentPlayer, raiseToPotential, currentRoundBet);
		return action;
	}
	
	/**
	 * Chooses the max raise to potential between all the values in the
	 * raiseToPotenional hash map, and select the best action for it.
	 * @param currentPlayer the current player object.
	 * @param raiseToPotenional the expected values hash map of the player's choices.
	 * @param currentRoundBet the current round bet.
	 * @return If all the values are under -1, returns check if available, else fold. If the maximum is raise 0, returns call.
	 * Else, returns raise action, with the raise that has the max potential benefit.
	 */
	private Action doMax(Player currentPlayer, HashMap<Integer, Double> raiseToPotenional, int currentRoundBet) {
		Action action = null;
		double max = 0;
		int maxRaise = -1;
		for (Integer raise : raiseToPotenional.keySet()) {
			if (raiseToPotenional.get(raise) > max) {
				max = raiseToPotenional.get(raise);
				maxRaise = raise;
			}
		}
		if (maxRaise == -1) {
			action = chooseCheckOrFold(currentPlayer, currentRoundBet);
			return action;
		}
		if (maxRaise == 0) {
			action = new Action(ActionType.CALL);
			return action;
		}
		action = new Action(ActionType.RAISE, maxRaise + currentRoundBet);
		return action;
	}

	/**
	 * Calculates the potential benefit for a certain raise, for a player.
	 * @param chances the chances of the player to win the hand.
	 * @param numOfPlayerInHand the number of players in the hand.
	 * @param howMuchMoneyDoINeedToCall how much money do the player need to call to.
	 * @param howMuchMoneyWillThereBeInTheBoardIfEveryoneAfterMeCalls how much money will be on board after everyone after the player calls.
	 * @param raise what is the raise to calculate for.
	 * @param moneyOfPlayer how much money does the player have.
	 * @param currentRoundBet how much money is the current round bet.
	 * @return the potential benefit for the raise.
	 */
	private double potentialBenefitForRaise(double chances, int numOfPlayerInHand, int howMuchMoneyDoINeedToCall,
			int howMuchMoneyWillThereBeInTheBoardIfEveryoneAfterMeCalls, int raise, int moneyOfPlayer, int currentRoundBet) {
		double risk = calculateRisk(raise, moneyOfPlayer, howMuchMoneyWillThereBeInTheBoardIfEveryoneAfterMeCalls);
//		System.out.println("Risk: " + risk + ", moneyOfPlayer: " + moneyOfPlayer + " currentRoundBet: " + currentRoundBet);
		return -howMuchMoneyDoINeedToCall - raise + (chances * risk) * (howMuchMoneyWillThereBeInTheBoardIfEveryoneAfterMeCalls + (raise * numOfPlayerInHand));
	}
	
	/**
	 * Calculates the risk a player allow himself to take in his turn.
	 * @param raise raise the player asked for to raise.
	 * @param moneyOfPlayer how much money does the player has.
	 * @param moneyOnBoardAfterCall how much money will be on board after everyone calls my bet.
	 * @return the risk a player allow himself to take in his turn.
	 */
	private double calculateRisk(int raise, int moneyOfPlayer, int moneyOnBoardAfterCall) {
		if (raise == 0) {
			return 1;
		}
		double randomValue = MINIMUM_RISK_VALUE + (0.95 - MINIMUM_RISK_VALUE) * rand.nextDouble();
		// minimum = 0.75 , maximum = 0.95
		
		return Math.max(randomValue - ((double)raise / moneyOfPlayer) - ((double)raise / Math.max(moneyOnBoardAfterCall, 20) / 100), 0.2);
	}
	
	/**
	 * Player will select check if he can. If not, will select fold.
	 * @param currentPlayer the current player to play.
	 * @param currentRoundBet the current round bet.
	 * @return call action if the player's bet equals the current round bet. 
	 * Else, returns fold action.
	 */
	private Action chooseCheckOrFold(Player currentPlayer, int currentRoundBet) {
		Action action;
		if (currentPlayer.getBet() == currentRoundBet)
			action = new Action(ActionType.CALL);
		else
			action = new Action(ActionType.FOLD);
		return action;
	}
}
