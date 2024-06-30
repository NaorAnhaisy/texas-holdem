package graphicsDrawImg;

import java.awt.Graphics;

import hashMapPack.ImageBank;
import javaPoker.Card;
import javaPoker.Deck;
import javaPoker.TexasHoldEm;
import other.StaticFunctions;

/**
 * Represents the drawing of the board cards.
 * @author Naor Anhaisy
 *
 */
public class DrawBoardCards implements DrawAble {

	private TexasHoldEm texasHoldEm;
	
	public static final int START_X = 600;
	public static final int START_Y = 170;
	public static final int BOARD_CARD_HEIGHT = 110;
	public static final int BOARD_CARD_WIDTH = 75;
	public static final int SPACE_BETWEEN_DECK_CARDS = 85;
	
	/**
	 * Initialize the Texas holde'm field.
	 * @param texasHoldEm Texas holde'm game.
	 */
	public DrawBoardCards(TexasHoldEm texasHoldEm) {
		this.texasHoldEm = texasHoldEm;
	}
	
	/**
	 * Update the card to draw by the parameter, and update the
	 * start point to the dealer hand.
	 * @param cardToDraw The card that need to be moved.
	 * If it is a burn card that need to draw, it's rank will be -1.
	 */
	public static void updateCardToDraw(Card cardToDraw) {
		StaticFunctions.currentX = START_X;
		StaticFunctions.currentY = START_Y;
		if (cardToDraw.getRank() != -1) {
			String s = Deck.getStringCard(cardToDraw);
	    	String path = "src/cards/" + s + ".png";
	    	StaticFunctions.cardToDraw = ImageBank.getImage(path);
		} else 
			StaticFunctions.cardToDraw = DrawBurnCards.image;
	}
	
	@Override
	public void draw(Graphics g) {
		if (StaticFunctions.isNewRound) {
			g.drawImage(StaticFunctions.cardToDraw, StaticFunctions.currentX, StaticFunctions.currentY, BOARD_CARD_WIDTH, BOARD_CARD_HEIGHT, null);
		}
		
		for (int i = 0; i < texasHoldEm.getBoard().getBoardSize(); i++) {
			String s = Deck.getStringCard(texasHoldEm.getBoard().getBoardCard(i));
			String path = "src/cards/" + s + ".png";
	    	g.drawImage(ImageBank.getImage(path), 400 + SPACE_BETWEEN_DECK_CARDS * i, 365, BOARD_CARD_WIDTH, BOARD_CARD_HEIGHT, null);
	    }
	}
}
