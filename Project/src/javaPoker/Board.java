package javaPoker;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents Texas Holde'm Board.
 * @author Naor Anhaisy
 *
 */
public class Board {
	
	private List<Card> board;
	private List<Card> burnCards;
	
	/**
	 * Create new Board object, and initialise the board cards and the burn cards lists.
	 */
	public Board() {
		board = new LinkedList<Card>();
		burnCards = new LinkedList<Card>();
	}
	
	/**
	 * Adds new card to the board cards.
	 * @param card Card to add.
	 */
	protected void addBoardCard(Card card) {
		board.add(card);
	}
	
	/**
	 * Adds new card to the burn cards.
	 * @param card Card to add.
	 */
	protected void addBurnCard(Card card) {
		burnCards.add(card);
	}
	
	/**
	 * return card in the index place from the board cards.
	 * @param cardNum index in the board cards list.
	 * @return board card in that index.
	 */
	public Card getBoardCard(int cardNum) {
		return board.get(cardNum);
	}
	
	/**
	 * return card in the index place from the burn cards.
	 * @param cardNum index in the burn cards list.
	 * @return burn card in that index.
	 */
	public Card getBurnCard(int cardNum) {
		return burnCards.get(cardNum);
	}
	
	/**
	 * returns all board cards list.
	 * @return the board cards list.
	 */
	public List<Card> getAllBoardCards() {
		return board;
	}
	
    /**
     * @return board's size
     */
	public int getBoardSize() {
        return board.size();
    }
    
    /**
     * @return burn cards' size
     */
	public int getBurnSize() {
        return burnCards.size();
    }

    /**
     * Prints the board's cards.
     */
	public void printBoard() {
		int i = 0;
        System.out.println("The board contains the following cards:");
        for (Card card : board) {
            System.out.println(i++ + ": " + card.printCard());
		}
        System.out.println("\n");
    }

    /**
     * Prints the burn cards.
     */
	public void printBurnCards() {
		int i = 0;
        System.out.println("The burn cards are:");
        for (Card burnCard : burnCards) {
            System.out.println(i++ + ": " + burnCard.printCard());
		}
        System.out.println("\n");
    }
}
