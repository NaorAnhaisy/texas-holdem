package javaPoker;

import java.util.LinkedList;
import java.util.List;

import other.Sound;

/**
 * Represents a Texas holde'm game, without the game play (turns).
 * Used also for the computer player AI.
 * @author Naor Anhaisy
 *
 */
public class MiniTexasHoldEm {
	
	private static final int NUMBER_OF_SHUFFELES = 3;
	private int numOfPlayers;
	private int currentPlayer;
	protected Deck holdemDeck;
	protected int cardCounter;
    protected Board board;
    protected Player[] players;
    protected String winner;
    protected String winnersHand;
    protected Result[] handsValues;
	private WinnerEval winnerEval;
	
    /**
     * Create new game (initialize all fields), shuffle the deck, initialize the players
     * and and hand the cards.
     * Also, if it's used for the computer AI, it's remove the known cards from deck
     * and initialize all known community cards.
     * @param numOfPlayers number of players in hand.
     * @param cardsOfPlayer if for computer player AI, it is the cards the player holds.
     * else, this parameter is null.
     * @param communityCards if for computer player AI, it is list of all community cards known.
     * else, this parameter is null.
     * @param currentPlayer if for computer player AI, it is the current player that need to make action.
     * else, this parameter is -1.
     * @param playersArr if for computer player AI, it is null.
     * else, this parameter is the players array of the hand.
     */
	public MiniTexasHoldEm (int numOfPlayers, Card[] cardsOfPlayer, List<Card> communityCards, int currentPlayer, Player[] playersArr) {
		
		initialiseFields(numOfPlayers, currentPlayer);
		
		Shuffles();
		
		if (cardsOfPlayer != null)
			removeKnownCardsFromDeck(cardsOfPlayer, communityCards);
    	
		if (playersArr == null) {
			players = new Player[numOfPlayers];
	        for (int i = 0; i < numOfPlayers; i++) {
	        	players[i] = new Player(i);
	        }
	        
    	} else {
    		players = playersArr;
        	for (int i = 0; i < numOfPlayers; i++) {
        		players[i].setBet(0);
        		if (players[i].getMoney() == 0)
        			players[i].setFold(true);
        		else
        			players[i].setFold(false);
        		players[i].setAllIn(false);
        		players[i].setSumHandBet(0);
        	}
    	}	
        
        handCards(cardsOfPlayer, currentPlayer);
        
        if (currentPlayer != -1)
        	intialiseCommunityCards(communityCards);
    }

	/**
	 * Initialize all necessary field.
	 * @param numOfPlayers number of player in hand.
	 * @param currentPlayer the index of the current player.
	 */
	protected void initialiseFields(int numOfPlayers, int currentPlayer) {
		
		this.currentPlayer = currentPlayer;
		this.numOfPlayers = numOfPlayers;
		board = new Board();
		handsValues = new Result[numOfPlayers];
		winnerEval = new WinnerEval();
		holdemDeck = new Deck();
	    cardCounter = 0;
	}

	/**
	 * Shuffles the deck of cards.
	 */
	protected void Shuffles() {
		
		/**
		 * 3 shuffles just like in real life.
		 */
        for (int i = 0; i < NUMBER_OF_SHUFFELES; i++) {
            holdemDeck.shuffle();
        }
        
        /**
         * Cut Deck
         */
        holdemDeck.cutDeck();
	}
	
	/**
	 * Remove the known cards from the deck of cards.
	 * @param cardsOfPlayer array of 2 cards the player holds.
	 * @param communityCards list of the community cards.
	 */
	private void removeKnownCardsFromDeck(Card[] cardsOfPlayer, List<Card> communityCards) {
		for (Card communityCard : communityCards) {
			holdemDeck.removeCard(communityCard);
		}
		holdemDeck.removeCard(cardsOfPlayer[0]);
		holdemDeck.removeCard(cardsOfPlayer[1]);
	}
	
	/**
	 * Main processing
	 * Deal hole cards to players.
	 * Remainder : Hand the currentPlayer his own cards,
	 * in case it is a simulation of computer player AI.
	 */
	public void handCards(Card[] cardsOfPlayer, int currentPlayer) {
    	for (int i = 0; i < 2; i++) {
    		for (int j = 0; j < numOfPlayers; j++) {
    			if (j == currentPlayer)
    			{
    		    	players[j].setCard(cardsOfPlayer[0], 0);
    		    	players[j].setCard(cardsOfPlayer[1], 1);
    			} else {
    				players[j].setCard(holdemDeck.getCard(cardCounter++), i);
    			}
    		}
    	}
    }
	
	/**
	 * Initialize the community cards. All the known community cards sets in row, 
	 * and after them the rest is puts from the deck.
	 * @param communityCards list of all known communityCards.
	 */
	private void intialiseCommunityCards(List<Card> communityCards) {
		for (Card communityCard : communityCards) {
			board.addBoardCard(communityCard);
		}
		while (board.getBoardSize() < 5) {
			board.addBoardCard(holdemDeck.getCard(cardCounter++));
		}
	}
	
	/**
	 * @return list of player's id who won the hand.
	 * (Because in some cases there might be more than one winner).
	 */
	public List<Integer> endHand() {
		
//		FORDEBUG
		
//        if (ComputerPlayersAI.nowNum == 5) {
//        	
//            /**
//             * print deck
//             */
//            holdemDeck.printDeck();
//            
//            /*
//             * print board
//             */
//            board.printBoard();
//            
//            /**
//             * print player cards
//             */
//            System.out.println("The player cards are the following:\n");
//            
//            for (int i = 0; i < numOfPlayers; i++) {
//            	players[i].printPlayerCards(i);
//            }
//        }
        
		/**
		 * ------------------------
		 * end dealing board
		 * ------------------------
		 */

        /**
         * ------------------------
         * Begin hand comparison and checking the winners
         * ------------------------
         */
        handComparison();
        
        List<Player> winnerPlayers = winnerEval.getWinner();
        winnersHand = handsValues[winnerPlayers.get(0).getId()].getHandString();
        
        if (currentPlayer == -1)
        	Sound.playSoundOfWinType(handsValues[winnerPlayers.get(0).getId()].getWinType());
        
        List<Integer> winnersId = new LinkedList<Integer>();
        for (Player player : winnerPlayers) {
			winnersId.add(player.getId());
		}
		return winnersId;
	}

	/**
	 * Main hand comparison between the players.
	 * Each player's hand is evaluated, and puts in handsValues array, and 
	 * added to the winnerEval object.
	 */
	private void handComparison() {
		
		for (int i = 0; i < numOfPlayers; i++) {
			
			int cardNum = 0;
			
        	if (!players[i].isFold()) {
                HandEval handToEval = new HandEval();
                
                // populate with player cards
                for (; cardNum < players[i].holeCardsSize(); cardNum++){
                	handToEval.addCard(players[i].getCard(cardNum), cardNum);
                }

                // populate with board cards
                for (Card card : board.getAllBoardCards()) {
                	handToEval.addCard(card, cardNum++);
				}
                
                Result evaluator = handToEval.evaluateHand();
                
                if (currentPlayer == -1) {
                    System.out.println("Player " + (i + 1) + " hand value: " + evaluator.getHandString());
                	System.out.println("Player " + (i + 1) + " hand win type value: " + evaluator.getWinType());
                	System.out.println("Player " + (i + 1) + " max rank: " + (evaluator.getMaxRank() + 1));
                }
                
                handsValues[i] = evaluator;
                winnerEval.addHand(handToEval, players[i]);
        	}
        }
	}
	
	/**
	 * @return the players array.
	 */
	public Player[] getPlayer() {
		return players;
	}
	
	/**
	 * @return String of the winner's name/s
	 */
	public String getWinner() {
		return winner;
	}
	
	/**
	 * @return String of the winner's hand.
	 */
	public String getWinnersHand() {
		return winnersHand;
	}
	
	/**
	 * Returns the board object of the game.
	 * @return the board of the game.
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Initialize all the players bets.
	 */
	protected void initBets() {
		for (Player player : players) {
			player.setBet(0);
		}
	}
	
	/**
	 * @return my player (player[0]) cards.
	 */
	public Card[] getMyPlayerCards() {
		Card[] firstPlayerCards = players[0].getHoleCards();
		return firstPlayerCards;
	}
}
