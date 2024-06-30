package javaPoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Represents deck of cards.
 * @author Naor Anhaisy
 *
 */
public class Deck {
	
    private static final int NUMBER_OF_RANKS = 13;
	private static final int NUMBER_OF_SUITS = 4;
	private Card[] cards = new Card[NUMBER_OF_RANKS * NUMBER_OF_SUITS];

    /**
     * Default Constructor, initialize the cards array.
     */
    public Deck() {
        int i = 0;
        for (short j = 0; j < NUMBER_OF_SUITS; j++) {
            for (short k = 0; k < NUMBER_OF_RANKS; k++) {
                cards[i] = new Card(k, j);
                i++;
            }
        }
    }
    
    /**
     * @return cards array.
     */
    public Card[] getCards() {
		return cards;
	}

    /**
     * Print entire deck in order.
     */
    protected void printDeck() {
    	System.out.println("Length: " + cards.length);
        for (int i = 0; i < cards.length; i++) {
            System.out.println(i + 1 + ": " + cards[i].printCard());
        }
        System.out.println("\n");
    }
    
    /**
     * Find card in deck in a linear fashion
     * Use this method if deck is shuffled/random
     * @param card Card to find.
     * @return index of card if found. else, return -1 if card not found.
     */
    protected int findCard(Card card){
        for (int i = 0; i < cards.length; i++) {
            if (Card.sameCard(cards[i], card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * return specified card from deck
     * @param cardNum index of the card in cards array.
     * @return the Card in the specific index.
     */
    protected Card getCard(int cardNum) {
        return cards[cardNum];
    }
    
    /**
     * Removes card from deck
     * @param card Card to remove
     */
    public void removeCard(Card card) {
    	List<Card> cardsList = new ArrayList<Card> (Arrays.asList(cards));
    	cardsList.remove(card);
    	cards = cardsList.toArray(new Card[cardsList.size()]);
    	
//    	FORDEBUG
//    	if (ComputerPlayersAI.nowNum == 5) {
//    		System.out.println(Card.rankAsString(card.getRank()) + " of " + Card.suitAsString(card.getSuit()) + " Removed \n");
//    	}
    }
    
    /**
     * Shuffles the deck of cards.
     */
    protected void shuffle() {
        int length = cards.length;
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int change = i + random.nextInt(length - i);
            swapCards(i, change);
        }
    }
    
    /**
     * Cuts the deck of the cards.
     * (Used for shuffles)
     */
    protected void cutDeck() {
        Deck tempDeck = new Deck();
        Random random = new Random();
        int cutNum = random.nextInt(cards.length);
        for (int i = 0; i < cutNum; i++) {
            tempDeck.cards[i] = this.cards[cards.length - cutNum + i];            
        }
        for (int j = 0; j < cards.length - cutNum; j++) {
            tempDeck.cards[j + cutNum] = this.cards[j];           
        }
        this.cards = tempDeck.cards;
    }
    
	/**
	 * Make a String that represents the card's name as an image, in 'cards' folder.
	 * @param card card to represent it's name.
	 * @return The name of the card as a image in 'cards' folder.
	 */
    public static String getStringCard(Card card) {
    	
    	String suitStr = "";
		String rankStr = "";
		
		short rank = (short) (card.getRank() + 1);
		short suit = card.getSuit();
			
		switch (suit) {
			case 0:
				suitStr = "Diamonds";
				break;
			case 1:
				suitStr = "Clubs";
				break;
			case 2:
				suitStr = "Hearts";
				break;
			case 3:
				suitStr = "Spades";
				break;
		}
		
		switch (rank) {
			case 1:
				rankStr = "Ace";
				break;
			case 11:
				rankStr = "Jack";
				break;
			case 12:
				rankStr = "Queen";
				break;
			case 13:
				rankStr = "King";
				break;
			default:
				rankStr = String.valueOf(rank);
		}
		
		return (rankStr + suitStr);
    }

	/**
	 * Swap cards in array to 'shuffle' the deck.
	 * @param a first index in cards array.
	 * @param b second index in cards array.
	 */
    private void swapCards(int a, int b) {      
        Card temp = cards[a];
        cards[a] = cards[b];
        cards[b] = temp;
    }
}
