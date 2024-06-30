package graphicsDrawImg;

import java.util.Random;

/**
 * Represents all the flying images (confettis) kinds available :
 * Spades, Heart, Diamond, and Clubs.
 * @author Naor Anhaisy
 *
 */
public enum ConfettiKinds {
	SPADES, HEART, DIAMONDS, CLUBS;
	
	/**
	 * Random a kind from all the confetties kinds, and return it.
	 * @return a randomized kind from all the kinds.
	 */
	public static ConfettiKinds getRandomKind() {
		return ConfettiKinds.values()[new Random().nextInt(ConfettiKinds.values().length)];
	}
}
