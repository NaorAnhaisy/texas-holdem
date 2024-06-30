package graphicsDrawImg;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import hashMapPack.ImageBank;
import javaPoker.TexasHoldEm;
import panels.PokerGame;

/**
 * Represents the draw of the burn cards.
 * @author Naor Anhaisy
 *
 */
public class DrawBurnCards implements DrawAble {

	private static final int CARD_HEIGHT = PokerGame.BOARD_CARD_HEIGHT;
	private static final int CARD_WIDTH = PokerGame.BOARD_CARD_WIDTH;
	public static final int Y_DECK_CARDS = 375;
	public static final int SPACE_BETWEEN_DECK_CARDS = PokerGame.SPACE_BETWEEN_DECK_CARDS;
	public static final int X_START_DECK_CARDS = 407;
	
	public static BufferedImage image;
	private TexasHoldEm texasHoldEm;
	
	/**
	 * Initialize the Texas holde'm field, and the burn card image.
	 * @param texasHoldEm Texas holde'm game.
	 */
	public DrawBurnCards(TexasHoldEm texasHoldEm) {
		String path = "src/cards/gray_back.png";
		image = ImageBank.getImage(path);
	    this.texasHoldEm = texasHoldEm;
	}
	
	@Override
	public void draw(Graphics g) {
		
		/**
		 * Drawing the burn cards by the burn counter field in the game.
		 */
		int burnCardsNow = texasHoldEm.getBoard().getBurnSize();
		if (burnCardsNow > 2) {
				g.drawImage(image, X_START_DECK_CARDS + SPACE_BETWEEN_DECK_CARDS * 0, Y_DECK_CARDS, CARD_WIDTH, CARD_HEIGHT, null);
				g.drawImage(image, X_START_DECK_CARDS + SPACE_BETWEEN_DECK_CARDS * 3, Y_DECK_CARDS, CARD_WIDTH, CARD_HEIGHT, null);
				g.drawImage(image, X_START_DECK_CARDS + SPACE_BETWEEN_DECK_CARDS * 4, Y_DECK_CARDS, CARD_WIDTH, CARD_HEIGHT, null);
		} else if (burnCardsNow > 1) {
				g.drawImage(image, X_START_DECK_CARDS + SPACE_BETWEEN_DECK_CARDS * 0, Y_DECK_CARDS, CARD_WIDTH, CARD_HEIGHT, null);
				g.drawImage(image, X_START_DECK_CARDS + SPACE_BETWEEN_DECK_CARDS * 3, Y_DECK_CARDS, CARD_WIDTH, CARD_HEIGHT, null);
		} else if (burnCardsNow > 0) {
			g.drawImage(image, X_START_DECK_CARDS + SPACE_BETWEEN_DECK_CARDS * 0, Y_DECK_CARDS, CARD_WIDTH, CARD_HEIGHT, null);
		}
	}
}
