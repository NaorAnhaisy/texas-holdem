package javaPoker;

/**
 * Represents player object in the game.
 * @author Naor Anhaisy
 *
 */
public class Player {
	
    private static final int START_MONEY = 10000;
	private Card[] holeCards = new Card[2];
    private int bet;
    private int sumHandBet;
    private int money;
	private boolean isFold;
	private boolean isAllIn;
    private String name;
    private int id;
    
    /**
     * Default constructor, that create new Texas holde'm player player,
     * and initialize his money to the start money, his bet and sum hand bet to 0,
     * makes him unfolded and not all in, and initialize his id number.
     * @param id Id of a player (0 - 5)
     */
    public Player(int id) {
    	money = START_MONEY;
        bet = 0;
        sumHandBet = 0;
        isFold = false;
        isAllIn = false;
        this.id = id;
    }
    
    /**
     * @return hole the cards the player holds.
     */
    public Card[] getHoleCards() {
		return holeCards;
	}
    
    /**
     * @return player's current bet.
     */
    public int getBet() {
		return bet;
	}
    
    /**
     * Sets player's bet.
     * @param bet bet to set to the player.
     */
    public void setBet(int bet) {
    	this.bet = bet;
    }
    
    /**
     * Makes bet for a player.
     * To how much money.
     * @param bet player's bet.
     */
	public void makeBet(int bet) {
		money += this.bet;
		this.bet = bet;
		money -= bet;
	}
	
	/**
	 * Sets new sum hand bet to player.
	 * @param sumHandBet sum hand bet to player.
	 */
	public void setSumHandBet(int sumHandBet) {
		this.sumHandBet = sumHandBet;
	}
	
	/**
	 * @return the sum hand bet of a player.
	 */
	public int getSumHandBet() {
		return sumHandBet;
	}
	
	/**
	 * Adds sum hand bet to the player.
	 * @param sumHandBet money to add to the sum hand bet of player.
	 */
	public void addSumHandBet(int sumHandBet) {
		this.sumHandBet += sumHandBet;
	}

	/**
	 * @return money of the player.
	 */
	public int getMoney() {
		return money;
	}
	
	/**
	 * Sets money for the player.
	 * @param money money to set to the player.
	 */
	public void setMoney(int money) {
		this.money = money;
	}
	
	/**
	 * @return id of a player.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return true if the player is folded, else return false.
	 */
	public boolean isFold() {
		return isFold;
	}

	/**
	 * Sets player as folded or not.
	 * @param isFold is player folded?
	 */
	public void setFold(boolean isFold) {
		this.isFold = isFold;
	}

	/**
	 * @return true if the player is all in, else return false.
	 */
	public boolean isAllIn() {
		return isAllIn;
	}

	/**
	 * Sets player as all in or not.
	 * @param isAllIn is player all in?
	 */
	public void setAllIn(boolean isAllIn) {
		this.isAllIn = isAllIn;
	}

	/**
	 * @return the name of the player.
	 */
    public String getName() {
		return name;
	}

    /**
     * Sets the name of the player.
     * @param name name for the player.
     */
	public void setName(String name) {
		this.name = name;
	}
    
	/**
	 * Sets new card for the player.
	 * @param card card to set for the player.
	 * @param cardNum the index of the card in the player's hand (0 or 1).
	 */
    protected void setCard(Card card, int cardNum){
        holeCards[cardNum] = card;
    }

    /**
     * @param cardNum the index of the card in the player's hand (0 or 1).
     * @return the card of the player in the index place.
     */
    protected Card getCard(int cardNum){
        return holeCards[cardNum];
    }

    /**
     * @return the hole cards size (2).
     */
	protected int holeCardsSize(){
        return holeCards.length;
    }
	
	/**
	 * Prints the player cards.
	 * @param playerIndex index of the player.
	 */
    protected void printPlayerCards(int playerIndex) {
        System.out.println("Player " + (playerIndex + 1) + " hole cards:");
        for (int i = 0; i < 2; i++) {
            System.out.println(holeCards[i].printCard());
        }
        System.out.println("\n");
    }
    
    /**
     * @return true if player is out of the game, else return false.
     */
    public boolean isOutOfGame() {
    	return money == 0 && isFold == true;
    }
}