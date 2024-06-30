package javaPoker;

import java.util.Arrays;

/**
 * Represents Hand Evaluator
 * Calculates the Hand of Player (if he has one pair, two pair, straight...)
 * @author Naor Anhaisy
 *
 */
public class HandEval { 
	
	/**
	 * Represents 2 private Cards and 5 Community Cards (only those who opened) 
	 */
    private Card[] availableCards = new Card[7];
    
    private final static short ONE = 1;
    private final static short TWO = 2;
    private final static short THREE = 3;
    private final static short FOUR = 4;

	/**
     * add card to the availableCards array
     * @param card card to add
     * @param i index of place to add the card
     */
    protected void addCard(Card card, int i) {
        availableCards[i] = card;
    }

    /**
     * Sorts availableCards according to the rank of the Cards in the array.
     */
    protected void sortByRank() {
        Arrays.sort(availableCards, new rankComparator());
    }

    /**
     * Sort availableCards array first by their rank and then by their suit
     */
    protected void sortByRankThenSuit() {
        Arrays.sort(availableCards, new rankComparator());
        Arrays.sort(availableCards, new suitComparator());
    }

    /**
     * evaluate the Player's hand by checking if his hand matching each available winning hand.
     * @return result of the Hand a Player have.
     */
    public Result evaluateHand() {
    	Result handResult; // holds the player's hand strength.
        short[] rankCounter = new short[13];
        short[] suitCounter = new short[4];

        // Loop through sorted cards and total ranks
        for (int i = 0; i < availableCards.length; i++) {
            rankCounter[availableCards[i].getRank()]++;
            suitCounter[availableCards[i].getSuit()]++;
        }

        //sort cards for evaluation
        sortByRankThenSuit();
        
        // hands are already sorted by rank and suit for royal and straight flush checks.
        handResult = checksForHand(rankCounter, suitCounter);
        
        return handResult;
    }
    
    /**
     * Check for the hand strength of a hand.
     * @param rankCounter rank counter array.
     * @param suitCounter suit counter array.
     * @return result - what is the hand strength.
     */
	private Result checksForHand(short[] rankCounter, short[] suitCounter) {
		Result handResult = null;
		//int handResult = 0;
		
		// check for royal flush
        handResult = evaluateRoyal(rankCounter, suitCounter);

        // check for straight flush
        if (handResult == null) {
            handResult = evaluateStraightFlush(rankCounter, suitCounter);
        }

        // check for four of a kind
        if (handResult == null) {
            handResult = evaluateFourOfAKind(rankCounter);
        }

        // check for full house
        if (handResult == null) {
            handResult = evaluateFullHouse(rankCounter);
        }

        // check for flush
        if (handResult == null) {
            handResult = evaluateFlush(rankCounter, suitCounter);
        }

        // check for straight
        if (handResult == null) {
            // re-sort by rank, up to this point we had sorted by rank and suit
            // but a straight is suit independent.
            sortByRank();
            handResult = evaluateStraight(rankCounter);
        }

        // check for three of a kind
        if (handResult == null) {
            handResult = evaluateThreeOfAKind(rankCounter);
        }

        // check for two pair
        if (handResult == null) {
            handResult = evaluateTwoPair(rankCounter);
        }

        // check for one pair
        if (handResult == null) {
            handResult = evaluateOnePair(rankCounter);
        }

        // check for highCard
        if (handResult == null) {
            handResult = evaluateHighCard(rankCounter);
        }
        
		return handResult;
	}

	/**
	 * Check for Royal Flush (10 - Ace of the same suit).
     * @param rankCounter rank counter array.
     * @param suitCounter suit counter array.
     * @return result if it's royal flush. Else, returns null.
	 */
    private Result evaluateRoyal(short[] rankCounter, short[] suitCounter) {
        Result r = null;
        // Check if there are 5 of one suit, if not royal is impossible.
        if ((rankCounter[9] >= 1 &&       /* 10 */
                rankCounter[10] >= 1 &&   /* Jack */
                rankCounter[11] >= 1 &&  /* Queen */
                rankCounter[12] >= 1 &&  /* King */
                rankCounter[0] >= 1)    /* Ace */
                && (suitCounter[0] > 4 || suitCounter[1] > 4 ||
                        suitCounter[2] > 4 || suitCounter[3] > 4)) {

            // min. requirements for a royal flush have been met, 
            // now loop through records for an ace and check subsequent cards. 
            // Loop through the aces first since they are the first card to 
            // appear in the sorted array of 7 cards. 
        	royalSearch:
                for (int i = 0; i < 3; i++) {
                	
                    // Check if first card is the ace.
                    // Ace must be in position 0, 1 or 2
                    if (availableCards[i].getRank() == 0) {
                    	
                        // because the ace could be the first card in the array
                        // but the remaining 4 cards could start at position
                        // 1, 2 or 3 loop through checking each possibility.
                        for (int j = 1; j < 4 - i; j++) {
                            if ((availableCards[i + j].getRank() == 9 && 
                            	availableCards[i + j + 1].getRank() == 10 &&
                            	availableCards[i + j + 2].getRank() == 11 &&
                            	availableCards[i + j + 3].getRank() == 12) 
                            	&&
                            	(availableCards[i].getSuit() == availableCards[i + j].getSuit() &&
                            	availableCards[i].getSuit() == availableCards[i + j + 1].getSuit() &&
                            	availableCards[i].getSuit() == availableCards[i + j + 2].getSuit() &&
                            	availableCards[i].getSuit() == availableCards[i + j + 3].getSuit())) { 
                            	
                            	// Found royal flush, break and return.
                            	String handRes = "Royal Flush! Suit: " + Card.suitAsString(availableCards[i].getSuit());;
                            	r = new Result(1, 10, handRes);
                            	break royalSearch;              
                            }
                        }
                    }               
                }
        }
        return r;
    }

    /**
     * Straight flush is 5 consecutive cards of the same suit.
     * @param rankCounter rank counter array.
     * @param suitCounter suit counter array.
     * @return result if it's straight flush. Else, returns null.
     */
    private Result evaluateStraightFlush(short[] rankCounter, short[] suitCounter) {
        Result r = null;

        if (suitCounter[0] > 4 || suitCounter[1] > 4 ||
                suitCounter[2] > 4 || suitCounter[3] > 4) {

            // min. requirements for a straight flush have been met.
            // Loop through available cards looking for 5 consecutive cards of the same suit,
            // start in reverse to get the highest value straight flush
            for (int i = availableCards.length - 1; i > 3; i--) {
                if ((availableCards[i].getRank() - ONE == availableCards[i - ONE].getRank() && 
                        availableCards[i].getRank() - TWO == availableCards[i - TWO].getRank() && 
                        availableCards[i].getRank() - THREE == availableCards[i - THREE].getRank() && 
                        availableCards[i].getRank() - FOUR == availableCards[i - FOUR].getRank()) 
                		&&
                        (availableCards[i].getSuit() == availableCards[i - ONE].getSuit() &&
                        availableCards[i].getSuit() == availableCards[i - TWO].getSuit() &&
                        availableCards[i].getSuit() == availableCards[i - THREE].getSuit() &&
                        availableCards[i].getSuit() == availableCards[i - FOUR].getSuit())) {
                            // Found royal flush, break and return.
                	String handRes = "Straight Flush: " + Card.rankAsString(availableCards[i].getRank()) + " high of " + Card.suitAsString(availableCards[i].getSuit());
                	r = new Result(availableCards[i].getRank(), 9, handRes);
                	if (r.getMaxRank() == 0)
                		r.setMaxRank(13);
                    break;
                }
            }
        }
        return r;
    }

    /**
     * Four of a kind is 4 cards with the same rank: 2-2-2-2, 3-3-3-3, etc...
     * @param rankCounter rank counter array.
     * @return result if it's four of kind. Else, returns null.
     */
    private Result evaluateFourOfAKind(short[] rankCounter) {
        Result r = null;
        for (int i = 0; i < rankCounter.length; i++) {
            if (rankCounter[i] == FOUR) {
            	r = new Result(i, 8, "Four of a Kind: " + Card.rankAsString(i) +"'s");
            	if (r.getMaxRank() == 0)
            		r.setMaxRank(13);
                break;
            }
        }
        return r;
    }

    /**
     * evaluate if a deck of cards are Full House
     * Full house is having 3 of a kind of one rank, and two of a kind of a second rank. EX: J-J-J-3-3
     * @param rankCounter rank counter array.
     * @return result if it's full house. Else, returns null.
     */
    private Result evaluateFullHouse(short[] rankCounter) {
        Result r = null;
        short threeOfKindRank = -1;
        short twoOfKindRank = -1;

        for (int i = rankCounter.length; i > 0; i--) {
            if ((threeOfKindRank < (short) 0) || (twoOfKindRank < (short) 0)) {
                if ((rankCounter[i - ONE]) > 2) {
                    threeOfKindRank = (short) (i - ONE);                  
                } else if ((rankCounter[i - ONE]) > 1) {
                    twoOfKindRank = (short) (i - ONE);
                }
            } else {
                break;
            }
        }
        if ((threeOfKindRank >= (short) 0) && (twoOfKindRank >= (short) 0)) {
        	r = new Result(threeOfKindRank, 7, "Full House: " + Card.rankAsString(threeOfKindRank) + "'s full of " + Card.rankAsString(twoOfKindRank) + "'s");
        	if (r.getMaxRank() == 0)
        		r.setMaxRank(13);
        }
        return r;
    }

    
    /**
     * Flush is 5 cards of the same suit.
     * @param rankCounter rank counter array.
     * @param suitCounter suit counter array.
     * @return result if it's flush. Else, returns null.
     */
    private Result evaluateFlush(short[] rankCounter, short[] suitCounter){
        Result r = null;
        
        // verify at least 1 suit has 5 cards or more.
        for (int suit = 0; suit < suitCounter.length; suit++) {
        	for (int i = availableCards.length - 1; i > 3; i--) {
                if (availableCards[i].getSuit() == availableCards[i - ONE].getSuit() &&
                        availableCards[i].getSuit() == availableCards[i - TWO].getSuit() &&
                        availableCards[i].getSuit() == availableCards[i - THREE].getSuit() &&
                        availableCards[i].getSuit() == availableCards[i - FOUR].getSuit()) {
                            // Found flush, break and return.
                	r = new Result(availableCards[i].getRank(), 6, "Flush: " + Card.rankAsString(availableCards[i].getRank()) + " high of " + Card.suitAsString(availableCards[i].getSuit()));
                	if (r.getMaxRank() == 0)
                		r.setMaxRank(13);
                	break;
                }
        	}
        }
        
        if (suitCounter[0] > 4 || suitCounter[1] > 4 ||
                suitCounter[2] > 4 || suitCounter[3] > 4) {
        	
            for (int i = availableCards.length - 1; i > 3; i--) {
                if (availableCards[i].getSuit() == availableCards[i - ONE].getSuit() &&
                        availableCards[i].getSuit() == availableCards[i - TWO].getSuit() &&
                        availableCards[i].getSuit() == availableCards[i - THREE].getSuit() &&
                        availableCards[i].getSuit() == availableCards[i - FOUR].getSuit()) {
                            // Found flush, break and return.
                	r = new Result(availableCards[i].getRank(), 6, "Flush: " + Card.rankAsString(availableCards[i].getRank()) + " high of " + Card.suitAsString(availableCards[i].getSuit()));
                	if (r.getMaxRank() == 0)
                		r.setMaxRank(13);
                	break;
                }
            }           
        }
        return r;
    }

    /**
     * Straight is 5 consecutive cards, regardless of suit. EX: 2-3-4-5-6
     * @param rankCounter rank counter array.
     * @return result if it's straight. Else, returns null.
     */
    private Result evaluateStraight(short[] rankCounter) {
        Result r = null;

        // loop through rank array to check for 5 consecutive
        // index with a value greater than zero
        for (int i = rankCounter.length; i > 4; i--) {
            if ((rankCounter[i - 1] > 0) &&
            		(rankCounter[i - 2] > 0) &&
                    (rankCounter[i - 3] > 0) &&
                    (rankCounter[i - 4] > 0) &&
                    (rankCounter[i - 5] > 0)) {
            	r = new Result(i - ONE, 5, "Straight: " + Card.rankAsString(i - 1) + " high");
                break;
            }
        }
        return r;
    }

    /**
     * Three of a kind is 3 cards of the same rank.
     * @param rankCounter rank counter array.
     * @return result if it's three of a kind. Else, returns null.
     */
    private Result evaluateThreeOfAKind(short[] rankCounter) {
        Result r = null;

        // loop through rank array to check for 5 consecutive
        // index with a value greater than zero
        for (int i = rankCounter.length; i > 0; i--) {
            if (rankCounter[i - 1] == 3) {
            	r = new Result(i - ONE, 4, "Three of a Kind: " + Card.rankAsString(i - 1) + "'s");
                if (r.getMaxRank() == 0)
                	r.setMaxRank(13);
            	break;
            }
        }
        return r;
    }

    /**
     * Two pair is having 2 cards of the same rank, and two
     * different cards of the same rank.  EX: 3-3-7-7-A
     * @param rankCounter rank counter array.
     * @return result if it's two pair. Else, returns null.
     */
    private Result evaluateTwoPair(short[] rankCounter) {
        Result r = null;
        
        short firstPairRank = -1;
        short secondPairRank = -1;
        
        for (int i = rankCounter.length; i > 0; i--) {
        	// Fills first pair and second pair, in condition there is 
        	// two pair of any rank, and they don't filled yet.
            if ((firstPairRank < (short) 0) || (secondPairRank < (short) 0)) {             
                if (((rankCounter[i - ONE]) == 2) && (firstPairRank < (short) 0)) {
                    firstPairRank = (short) (i - ONE);                    
                } else if ((rankCounter[i - ONE]) == 2) {
                    secondPairRank = (short)(i - ONE);
                }
            } else {
                // two pair found, break loop.
                break;
            }
        }

        // populate output
        if ((firstPairRank >= (short) 0) && (secondPairRank >= (short)0)) {
            if (secondPairRank == (short) 0) {
                // Aces serve as top rank but are at the bottom of the rank array
                // swap places so aces show first as highest pair
                r = new Result(13, 3, "Two Pair: " + Card.rankAsString(secondPairRank) + "'s and " + Card.rankAsString(firstPairRank) + "'s");
            } else {
            	r = new Result(firstPairRank, 3, "Two Pair: " + Card.rankAsString(firstPairRank) + "'s and " + Card.rankAsString(secondPairRank) + "'s");
            }           
        }

        return r;
    }

    /**
     * One Pair is two cards of the same rank.
     * @param rankCounter rank counter array.
     * @return result if it's one pair. Else, returns null.
     */
    private Result evaluateOnePair(short[] rankCounter) {
        Result r = null;
        
        // Goes throgh all rank counter array, until find
        // any index in the array that contains 2 (2 cards of same rank).
        // If found, return one pair.
        for (int i = rankCounter.length; i > 0; i--) {
            if((rankCounter[i - ONE]) == 2) {
            	r = new Result(i - ONE, 2, "One Pair: " + Card.rankAsString(i - ONE) + "'s");
                if (r.getMaxRank() == 0)
                	r.setMaxRank(13);
                break;
            }
        }
        
        return r;
    }

    /**
     * high card is the highest card out of the 7 possible cards to be used.
     * @param rankCounter rank counter array.
     * @return result if it's high card. Else, returns null.
     */
    private Result evaluateHighCard(short[] rankCounter) {
    	Result r = null;
    	
    	// Goes through rank counter array, until found any rank in array.
    	// When found, return it.
        for (int i = rankCounter.length; i > 0; i--) {
            if((rankCounter[i - ONE]) > 0) {
                r = new Result(i - ONE, 1, "High Card: " + Card.rankAsString(i - ONE));
                if (r.getMaxRank() == 0)
                	r.setMaxRank(13);
                break;
            }
        }
        
        return r;
    }
}
