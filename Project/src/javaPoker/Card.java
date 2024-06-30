package javaPoker;

import java.util.Comparator;

/**
 * Represents Card Object
 * @author Naor Anhaisy
 *
 */
public class Card {
	
    private short rank, suit;
    
    // Strings of card's ranks : Ace, 2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King
    public static String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    
    // Strings of card's suits : Diamonds, Clubs, Hearts, Spades
    public static String[] suits = {"Diamonds", "Clubs", "Hearts", "Spades"};
    
    /**
     * Constructor that build new card
     * @param rank rank of card (0 ~ 12)
     * @param suit suit (shape) of card (0 ~ 3)
     */
    public Card(short rank, short suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * @return suit of card
     */
    public short getSuit() {
        return suit;
    }

    /**
     * @return rank of card
     */
    public short getRank() {
        return rank;
    }

    /**
     * @param _rank rank of card
     * @return String of the card's rank
     */
    public static String rankAsString(int _rank) {
        return ranks[_rank];
    }

    /**
     * @param _suit suit of card
     * @return String of the card's suit
     */
    public static String suitAsString(int _suit) {
        return suits[_suit];
    }

    /**
     * @return Prints the card's details
     */
    protected String printCard() {
        return ranks[rank] + " of " + suits[suit];
    }

    /**
     * Determine if two cards are the same (Ace of Diamonds == Ace of Diamonds)
     * @param card1 card number 1
     * @param card2 card number 2
     * @return true if the 2 cards are the same. Else, @return false.
     */
    public static boolean sameCard(Card card1, Card card2) {
        return (card1.rank == card2.rank && card1.suit == card2.suit);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rank;
		result = prime * result + suit;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (rank != other.rank)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}
}

/**
 * Represents comparator of rank between 2 cards.
 * @author Naor Anhaisy
 *
 */
class rankComparator implements Comparator<Object> {
	
	/**
	 * Compare between two object, and return 0 if they are cards with the same rank.
	 */
    public int compare(Object card1, Object card2) throws ClassCastException {
        // verify two Card objects are passed in
        if (!((card1 instanceof Card) && (card2 instanceof Card))){
            throw new ClassCastException("A Card object was expeected. "
            		+ "Parameter 1 class: " + card1.getClass() 
                    + " Parameter 2 class: " + card2.getClass());
        }

        short rank1 = ((Card)card1).getRank();
        short rank2 = ((Card)card2).getRank();

        return rank1 - rank2;
    }
}

/**
 * Represents comparator of suit between 2 cards.
 * @author Naor
 *
 */
class suitComparator implements Comparator<Object> {
	
	/**
	 * Compare between two object, and return 0 if they are cards with the same suit.
	 */
    public int compare(Object card1, Object card2) throws ClassCastException {
        // verify two Card objects are passed in
        if (!((card1 instanceof Card) && (card2 instanceof Card))){
            throw new ClassCastException("A Card object was expeected. "
            		+ "Parameter 1 class: " + card1.getClass() 
                    + " Parameter 2 class: " + card2.getClass());
        }

        short suit1 = ((Card)card1).getSuit();
        short suit2 = ((Card)card2).getSuit();

        return suit1 - suit2;
    }
}
