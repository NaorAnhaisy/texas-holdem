package javaPoker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import graphicsDrawImg.DrawBoardCards;
import graphicsDrawImg.DrawBurnCards;
import other.Sound;
import other.StaticFunctions;
import panels.PokerGame;
import panels.StartPage;

/**
 * Represents Texas holde'm GamePlay.
 * @author Naor Anhaisy
 *
 */
public class TexasHoldEm extends MiniTexasHoldEm {
	
	private static final int ENDING_ROUND = 4;
	public static final String ALL_IN = "ALL IN";
	public static final String RAISE = "Raise";
	public static final String CALL = "Call";
	public static final String CHECK = "Check";
	public static final String FOLD = "Fold";
	public static final String OUT = "Out";
	public static final int NUM_PLAYERS = 6;
    private ComputerPlayersAI computerPlayersAI;
	private PokerGame pokerGame;
	private Player[] player;
	private int currentRoundBet;
	private int sumBetOnTable;
	private int round;
	private Random rand = new Random();
	private int currentPlayerIndex;
	public static int smallBlind, bigBlind;
	private int lastRaisePlayer;
	private List<Player> winnerPlayers;
	
	/**
	 * Default constructor, that initialise all necessary field and create 
	 * the new Texas holde'm game.
	 * @param frame frame to create the game panel on.
	 * @param isNewGame true if the game is new (Coming from the start page), else false.
	 * @param players if game is not new, this holds the last game players. Else, null.
	 * @throws Exception if game not loaded right.
	 */
	public TexasHoldEm (JFrame frame, boolean isNewGame, Player[] players) throws Exception {
		
		super(NUM_PLAYERS, null, null, -1, players);
		initialiseFields();
		
        if (isNewGame) {
        	smallBlind = rand.nextInt(6);
        	bigBlind = (smallBlind + 1) % NUM_PLAYERS;
        	
            inisialisePlayersNames();
            
        } else {
        	advanceBlinds();
        	System.out.println("smallBlind " + smallBlind);
    		System.out.println("bigBlind " + bigBlind);
        }
        
        computerPlayersAI = new ComputerPlayersAI(player);
        
        pokerGame = new PokerGame(this, frame);
        
    	for (int i = 0; i < NUM_PLAYERS; i++) {
    		if (player[i].isFold())
    			pokerGame.getStatusMoneyManager().play(OUT, 0, i);
    	}
    }

	/**
	 * Advance the blinds to the first position they can.
	 */
	private void advanceBlinds() {
		smallBlind = getNextPlayerInGame(smallBlind);
		bigBlind = getNextPlayerInGame(smallBlind);
	}
	
	/**
	 * gets the first index the blinds can be, start from their positions.
	 * @param playerIndex index of the blind.
	 * @return index of the place the blind will be now.
	 */
	private int getNextPlayerInGame(int playerIndex) {
		do {
			playerIndex = (playerIndex + 1) % NUM_PLAYERS;
		} while (player[playerIndex].isFold());
		return playerIndex;
	}
	
	/**
	 * Initialise the field for starting new gamePlay.
	 */
	private void initialiseFields() {
		
		player = super.players;
		currentRoundBet = 0;
		sumBetOnTable = 0;
		round = 0;
	}
	
	/**
	 * Initialise the players names.
	 */
	private void inisialisePlayersNames() {
		player[0].setName(StartPage.name);
        player[1].setName("John");
        player[2].setName("Mike");
        player[3].setName("David");
        player[4].setName("Thomas");
        player[5].setName("Gal-Cohen");
	}
	
	/**
	 * Start play, by small and big blinds bet.
	 * Then, do a computer round bet.
	 */
	public void startPlay() {
		playRaiseOrAllIn(smallBlind, 20);
		playRaiseOrAllIn(bigBlind, 40);
		pokerGame.repaint();
		try {
			Sound.playSound("StartBets");
			Thread.sleep(2000);
		} catch (IOException | InterruptedException e) { 
			System.out.println("Sound \"start bets\" not found or thread interrupted.");
		}
		pokerGame.getStatusMoneyManager().addMoneyLabelToPlayer(smallBlind);
		pokerGame.getStatusMoneyManager().addMoneyLabelToPlayer(bigBlind);
		currentPlayerIndex = (bigBlind + 1) % NUM_PLAYERS;
		computerBetLoop();
		if (round != ENDING_ROUND) {
			pokerGame.getButtonsManager().updateButtonsText();
			pokerGame.getButtonsManager().setButtonsEnabled(true);
			pokerGame.addTimer(currentPlayerIndex);
		} else {
			pokerGame.newHand();
		}
	}
	
	/**
	 * handle the switches between rounds:
	 * 0 - Flop
	 * 1 - Turn
	 * 2 - River
	 * 4 - End round
	 * also, collects the money of the player's bets to the table, initialize all
	 * players bets, the current player index, the last raise player,
	 * the current round bet, updating the chances bar of my player, and disappear 
	 * the actions of all the players who can still change their decision
	 * if to stay in game or not.
	 * and
	 */
	public void nextRound() {
		try {
			pokerGame.t.stopTimer();
		} catch (NullPointerException e) {
			System.out.println("Timer already stopped");
		}
		pokerGame.t = null;
		currentPlayerIndex = smallBlind;
		lastRaisePlayer = -1;
		currentRoundBet = 0;
		updateSumBetOnTable();
		pokerGame.getStatusMoneyManager().updateSumBetOnTableLabel();
		initBets();
		try {
			if (round == 3) {
				Thread.sleep(1000);
				Sound.playSound("LetsSeeEm");
				Thread.sleep(1000);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		switch (round) {
			case 0:
				startFlop();
				round++;
				break;
			case 1:
				startTurn();
				round++;
				break;
			case 2:
				startRiver();
				round++;
				break;
			case 3:
				endHandOfTexasHoldem();
				round++;
				break;
			default:
				break;
		}
		
		pokerGame.drawChancesBar.updateChancesBar();
		pokerGame.getStatusMoneyManager().setAllChangingPlayersStatusLabelsVisible(false);
	}
	
	/**
	 * Adds next card in deck to board.
	 */
	private void addCardToBoard() {
		Card newCard = holdemDeck.getCard(cardCounter++);
		moveCardToBoard(newCard);
		board.addBoardCard(newCard);
	}
	
	/**
	 * Burns the next card in the deck.
	 */
	private void addBurnCardToBoard() {
		moveCardToBoard(new Card((short)-1, (short)-1));
		board.addBurnCard(holdemDeck.getCard(cardCounter++));
	}

	/**
	 * Update the image of card to move to the newest card, and sets it's
	 * destination point, and move the card.
	 * @param newCard card to draw moving from dealer to board / burns.
	 * If this is burn card to move, rank and suit will be -1.
	 */
	private void moveCardToBoard(Card newCard) {
		StaticFunctions.isNewRound = true;
		DrawBoardCards.updateCardToDraw(newCard);
		if (newCard.getRank() != -1) {
			try {
				Sound.playSound("cardSlide8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			StaticFunctions.moveCard(pokerGame, 400 + DrawBoardCards.SPACE_BETWEEN_DECK_CARDS * board.getBoardSize(), 365, true);
		} else {
			try {
				Sound.playSound("cardShove2");
			} catch (IOException e) {
				e.printStackTrace();
			}
			StaticFunctions.moveCard(pokerGame, DrawBurnCards.X_START_DECK_CARDS + DrawBurnCards.SPACE_BETWEEN_DECK_CARDS * board.getBoardSize(), DrawBurnCards.Y_DECK_CARDS, false);
		}
		StaticFunctions.isNewRound = false;
		pokerGame.repaint();
	}
	
	/**
	 * Starting the flop round - burn 1 card and then add 3 cards to community cards.
	 */
	public void startFlop() {
		
		addBurnCardToBoard();
		
        for (int i = 0; i < 3; i++){
            addCardToBoard();
        }
	}
	
	/**
	 * Starting the turn round - burn 1 card and then add 1 card to community cards.
	 */
	public void startTurn() {
		
		addBurnCardToBoard();
        addCardToBoard();
	}
	
	/**
	 * Starting the river round - burn 1 card and then add 1 card to community cards.
	 */
	public void startRiver() {
		
		addBurnCardToBoard();
        addCardToBoard();
	}
    
	/**
	 * Starting the end round - Prints:
	 * all money on table.
	 * all deck and burn cards.
	 * all players cards.
	 * strength of each player's hand.
	 * the winner player.
	 */
	private void endHandOfTexasHoldem() {

		/**
		 * ------------------------
		 * end dealing board
		 * ------------------------
		 */
        System.out.println("The hand is complete...\n");
        System.out.println("Sum Money on Table: " + sumBetOnTable);
        
        /**
         * print deck
         */
        holdemDeck.printDeck();

        /**
         * print board
         */
        board.printBoard();
        
        /**
         * print player cards
         */
        System.out.println("The player cards are the following:\n");
        for (int i = 0; i < NUM_PLAYERS; i++) {
            player[i].printPlayerCards(i);
        }

        /**
         * print burn cards
         */
        board.printBurnCards();
        
        List<Integer> winnersId = endHand();
        
        winnerPlayers = new LinkedList<Player>();
        for (Integer playerId : winnersId) {
			winnerPlayers.add(player[playerId]);
		}
        printWinnerName(winnerPlayers);
        
        /**
         * Prints the winners names in console :
         */
        int numWinners = winnerPlayers.size();
        if (numWinners > 1) {
        	System.out.println("Some winners: ");
        	for (int i = 0; i < numWinners; i++) {
        		System.out.println(winnerPlayers.get(i).getName());
            	winnerPlayers.get(i).setMoney(winnerPlayers.get(i).getMoney() + (sumBetOnTable / numWinners));
            }
        }
        else {
        	System.out.println("Winner is: " + winnerPlayers.get(0).getName());
        	winnerPlayers.get(0).setMoney(winnerPlayers.get(0).getMoney() + sumBetOnTable);
        }
	}
	
	/**
	 * Set in winner field the string of the winners names.
	 * @param winnerPlayers list of all the winners.
	 */
	private void printWinnerName(List<Player> winnerPlayers) {
		if (winnerPlayers.size() == 1) {
			if (winnerPlayers.get(0) == player[0])
	        	winner = "You Won The Hand!";
			else
	        	winner = "Hand's Winner is: " + winnerPlayers.get(0).getName();
		}
		else {
			winner = "Winners Are: ";
			winner += winnerPlayers.get(0).getName();
			for (int i = 1; i < winnerPlayers.size(); i++) {
				winner += ", " + winnerPlayers.get(i).getName();
			}
		}
	}
	
	/**
	 * Make my player's action, and than starts bet round of all computer players.
	 * @param bet - my player's bet received from PokerGame panel.
	 */
	public void betRound(int bet) {
		makeMyPlayerBet(bet);
		if (lastRaisePlayer == -1)
			lastRaisePlayer = 0;
		
		currentPlayerIndex = (currentPlayerIndex + 1) % NUM_PLAYERS;
		computerBetLoop();
	}
	
	/**
	 * Makes my player bet, only if he didn't folded or in all in mode.
	 * @param bet - my player's bet.
	 */
	public void makeMyPlayerBet(int bet) {
		if (!player[0].isFold() && !player[0].isAllIn()) {
			if (bet == player[0].getMoney() && !player[0].isFold())
				makeAllIn(0);
			else {
				player[0].addSumHandBet(bet - player[0].getBet());
				player[0].makeBet(bet);
			}
			if (bet > currentRoundBet) {
				currentRoundBet = bet;
				lastRaisePlayer = 0;
			}
			pokerGame.getStatusMoneyManager().addMoneyLabelToPlayer(0);
		}
	}
	
	/**
	 * Loop through all computer players, and makes their actions.
	 */
	public void computerBetLoop() {
		loopComputerPlayersUntilMeOrLastRaise();
		if (currentPlayerIndex == lastRaisePlayer) {
			nextRound();
			pokerGame.repaint();
			if (currentPlayerIndex != 0) {
				loopComputerPlayersUntilMeOrLastRaise();
				if (currentPlayerIndex == lastRaisePlayer) {
					nextRound();
					pokerGame.repaint();
				}
			}
		}
		checkOnePlayerRemianing();
		if (round != ENDING_ROUND && !player[0].isFold() && !player[0].isAllIn()) {
			pokerGame.getButtonsManager().setButtonsEnabled(true);
			pokerGame.getButtonsManager().updateButtonsText();
		}
	}
	
	/**
	 * Loops while doesn't reach to my player or to last raise player or while the 
	 * game is still on (it is not round 4), and foreach player make it's move.
	 */
	private void loopComputerPlayersUntilMeOrLastRaise() {
		if (currentPlayerIndex != lastRaisePlayer && currentPlayerIndex != 0) {
			do {
				checkOnePlayerRemianing();
				if (round != ENDING_ROUND) {
					try {
						computerPlay(currentPlayerIndex);
					} catch (Exception e) {
						e.printStackTrace();
					}
					currentPlayerIndex = (currentPlayerIndex + 1) % NUM_PLAYERS;
				}
			} while (currentPlayerIndex != 0 && currentPlayerIndex != lastRaisePlayer && round != ENDING_ROUND);
		}
	}

	/**
	 * Checks if left only one player that didn't fold.
	 * If it does, the game gets to the final round, and announce the only player that didn't fold as the winner.
	 */
	private void checkOnePlayerRemianing() {
		if (getNumOfPlayersAvailableToAct() == 0 || (getNumOfPlayersAvailableToAct() == 1 && player[currentPlayerIndex].getBet() == currentRoundBet)) {
			while (round < ENDING_ROUND) {
				nextRound();
			}
		}
	}

	/**
	 * Do action for the computer player.
	 * Check by it's cards, what action will it do - Raise, Call or Fold.
	 * @param playerIndex index of player (1 - 5)
	 * @throws Exception if illegal playerIndex received.
	 */
	public void computerPlay(int playerIndex) throws Exception {
		if (playerIndex > 5 || playerIndex < 1) {
			throw new RuntimeException("Illegal player index received");
		}
		pokerGame.repaint();
		
		/**
		 * Happens only when the player didn't fold.
		 */
		if (!player[playerIndex].isFold() && player[playerIndex].getMoney() != 0) {
			pokerGame.addTimer(playerIndex);
			try {
				//int x = rand.nextInt(5500) + 500;
				int x = 1000;
				Thread.sleep(x);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Action actionToMake = computerPlayersAI.makeAction(player[playerIndex], playerIndex, currentRoundBet, getMoneyInTable(), getNumOfPlayersInHand(), board.getAllBoardCards());
			ActionType actionType = actionToMake.getActionType();
			
			if (actionType != ActionType.RAISE)
				System.out.println("Player number " + playerIndex + " choose to " + actionType.name());
			else
				System.out.println("Player number " + playerIndex + " choose to " + actionType.name() + " in " + actionToMake.getSumToRaise());
			
			if (actionType == ActionType.RAISE)
				playRaiseOrAllIn(playerIndex, actionToMake.getSumToRaise());
			else if (actionType == ActionType.CALL)
				playCallOrAllIn(playerIndex);
			else if (actionType == ActionType.FOLD)
				playFold(playerIndex);
		}
		
		pokerGame.getStatusMoneyManager().addMoneyLabelToPlayer(playerIndex);
	}
	
	/**
	 * Make Raise - if the bet is is not bigger than the money he has.
	 * Else, make all in.
	 * @param playerIndex index of player.
	 * @param raiseSum money the player want to raise to.
	 */
	public void playRaiseOrAllIn(int playerIndex, int raiseSum) {
		if (player[playerIndex].getMoney() - raiseSum > 0)  {
			player[playerIndex].addSumHandBet(raiseSum - player[playerIndex].getBet());
			player[playerIndex].makeBet(raiseSum);
			currentRoundBet = raiseSum;
			lastRaisePlayer = playerIndex;
			pokerGame.getStatusMoneyManager().play(RAISE, currentRoundBet, playerIndex);
		} else {
			if (raiseSum > currentRoundBet)
				lastRaisePlayer = playerIndex;
			makeAllIn(playerIndex);
		}
	}
	
	/**
	 * Make Check - in case the current round bet equals to his bet.
	 * Else make Call - if the current round bet is is not bigger than the money he has.
	 * Else, make all in.
	 * @param playerIndex index of player.
	 */
	public void playCallOrAllIn(int playerIndex) {
		if (player[playerIndex].getMoney() + player[playerIndex].getBet() - currentRoundBet > 0)
		{
			if (player[playerIndex].getBet() == currentRoundBet)
				pokerGame.getStatusMoneyManager().play(CHECK, 0, playerIndex);
			else {
				player[playerIndex].addSumHandBet(currentRoundBet - player[playerIndex].getBet());
				player[playerIndex].makeBet(currentRoundBet);
				pokerGame.getStatusMoneyManager().play(CALL, currentRoundBet, playerIndex);
			}
			if (lastRaisePlayer == -1)
				lastRaisePlayer = playerIndex;
		} else
			makeAllIn(playerIndex);
	}

	/**
	 * Make fold for a certain player.
	 * @param playerIndex index of player.
	 */
	public void playFold(int playerIndex) {
		player[playerIndex].setFold(true);
		pokerGame.getStatusMoneyManager().play(FOLD, 0, playerIndex);
	}

	/**
	 * Makes all in action for a player.
	 * @param playerIndex index of player.
	 */
	public void makeAllIn(int playerIndex) {
		System.out.println(playerIndex + " make all in");
		player[playerIndex].setAllIn(true);
		int raiseSum = player[playerIndex].getMoney() + player[playerIndex].getBet();
		player[playerIndex].makeBet(raiseSum);
		if (currentRoundBet < raiseSum) {
			currentRoundBet = raiseSum;
			lastRaisePlayer = playerIndex;
		}
		
		pokerGame.getStatusMoneyManager().play(ALL_IN, raiseSum, playerIndex);
	}
	
	/**
	 * Returns the number of players available to act 
	 * (Players that aren't fold or all in (out player are already fold)).
	 * @return the number of players available to act.
	 */
	private int getNumOfPlayersAvailableToAct() {
		int count = 0;
		for (int i = 0; i < NUM_PLAYERS; i++)
			if (player[i].isFold() || player[i].isAllIn())
				count++;
		return NUM_PLAYERS - count;
	}
	
	/**
	 * Returns the number of players in hand - 
	 * those are the players who can still win the hand.
	 * @return the number of players in hand.
	 */
	public int getNumOfPlayersInHand() {
		int count = 0;
		for (int i = 0; i < NUM_PLAYERS; i++)
			if (player[i].isFold())
				count++;
		return NUM_PLAYERS - count;
	}
	
	/**
	 * @return the current round bet.
	 */
	public int getCurrentBet() {
		return currentRoundBet;
	}

	/**
	 * Goes through all players, and adds their bet to the whole money on table.
	 * @return
	 */
	public int updateSumBetOnTable() {
		int sum = 0;
		for (int i = 0; i < NUM_PLAYERS; i++)
			sum += player[i].getBet();
		sumBetOnTable += sum;
		return sumBetOnTable;
	}
	
	/**
	 * Goes through all the players, and sum their sum hand bet.
	 * @return the sum of money in table.
	 */
	private int getMoneyInTable() {
		int sum = 0;
		for (int i = 0; i < player.length; i++) {
			sum += player[i].getSumHandBet();
		}
		return sum;
	}

	/**
	 * @return the sum hand bet on the table.
	 */
	public int getSumBetOnTable() {
		return sumBetOnTable;
	}

	/**
	 * @return the game's current round.
	 */
	public int getRound() {
		return round;
	}
	
	/**
	 * @return the current player index.
	 */
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
    
	/**
	 * @return the poker panel of the game.
	 */
	public PokerGame getPokerGame() {
		return pokerGame;
	}
	
	/**
	 * Game is over if only one player remaining.
	 * @return true if the game is over. Else, return false.
	 */
	public boolean isWholeGameOver() {
		int downPlayersCount = 0;
		for (int i = 0; i < NUM_PLAYERS; i++) {
			if (player[i].getMoney() == 0)
				downPlayersCount++;
		}
		if (downPlayersCount == NUM_PLAYERS - 1)
			return true;
		return false;
	}
	
	/**
	 * @return true if all bets equal between the players
	 * available to act, else return false.
	 */
	public boolean checkBetsEquals() {
		for (int i = 1; i < NUM_PLAYERS; i++) {
			if (!player[i].isFold() && !player[i].isAllIn())
				if (!(player[i].getBet() == currentRoundBet))
					return false;
		}
		return true;
	}
	
	/**
	 * @return list contains the winner players.
	 */
	public List<Player> getWinnerPlayers() {
		return winnerPlayers;
	}

	public ComputerPlayersAI getComputerPlayersAI() {
		return computerPlayersAI;
	}
}
