package graphicsDrawImg;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import hashMapPack.CardsLocationsHashMap;
import hashMapPack.ImageBank;
import javaPoker.Deck;
import javaPoker.TexasHoldEm;
import panels.PokerGame;

/**
 * Represents the drawing of the player cards, and their show once 
 * it's the end of a hand.
 * @author Naor Anhaisy
 *
 */
public class DrawPlayersCards implements DrawAble {
	
	private static final int CARD_WIDTH = PokerGame.CARD_WIDTH;
	private static final int CARD_HEIGHT = PokerGame.CARD_HEIGHT;
	private BufferedImage redBack, redBackRotated, myPlayerimage, myPlayerimage2;
	private TexasHoldEm texasHoldEm;
	private static CardsLocationsHashMap cardsLocationsHashMap;
	
	/**
	 * Initialize the upside down cards images (rotated and unrotated), 
	 * initialize my player cards images to show them, and initialize
	 * the Texas hold'm game.
	 * @param texasHoldEm Texas holde'm game.
	 */
	public DrawPlayersCards(TexasHoldEm texasHoldEm) {
		
		redBack = ImageBank.getImage("src/cards/red_back.png");
		redBackRotated = ImageBank.getImage("src/cards/red_back_Rotated.png");
		
		String s = Deck.getStringCard(texasHoldEm.getMyPlayerCards()[0]);
		String path3 = "src/cards/" + s + ".png";
		myPlayerimage = ImageBank.getImage(path3);
	    
	    String s2 = Deck.getStringCard(texasHoldEm.getMyPlayerCards()[1]);
	    String path4 = "src/cards/" + s2 + ".png";
	    myPlayerimage2 = ImageBank.getImage(path4);
	    
	    this.texasHoldEm = texasHoldEm;
	    cardsLocationsHashMap = new CardsLocationsHashMap();
	}
	
	/**
	 * Draws all the upside down players cards images (only if they are in the game), 
	 * and draws my player cards.
	 */
	@Override
	public void draw(Graphics g) {
		
		g.drawImage(myPlayerimage, (int) cardsLocationsHashMap.getCardsLocation(0, 0).getX(), (int) cardsLocationsHashMap.getCardsLocation(0, 0).getY(), CARD_WIDTH, CARD_HEIGHT, null); // 0's Player (My Player)
		g.drawImage(myPlayerimage2, (int) cardsLocationsHashMap.getCardsLocation(0, 1).getX(), (int) cardsLocationsHashMap.getCardsLocation(0, 1).getY(), CARD_WIDTH, CARD_HEIGHT, null);
		
		for (int i = 1; i < TexasHoldEm.NUM_PLAYERS; i++) {
			if (!texasHoldEm.getPlayer()[i].isOutOfGame()) {
				for (int j = 0; j < 2; j++) {
					if (i == 2 || i == 5) {
						g.drawImage(redBackRotated, (int) cardsLocationsHashMap.getCardsLocation(i, j).getX(), (int) cardsLocationsHashMap.getCardsLocation(i, j).getY(), CARD_HEIGHT, CARD_WIDTH, null);
					} else {
						g.drawImage(redBack, (int) cardsLocationsHashMap.getCardsLocation(i, j).getX(), (int) cardsLocationsHashMap.getCardsLocation(i, j).getY(), CARD_WIDTH, CARD_HEIGHT, null);
					}
				}
			}
		}
	}
	
	/**
	 * Shows all players card, in condition they didn't fold.
	 * @param g Graphics g
	 */
	public void showPlayerCards(Graphics g) {
		
		for (int i = 0; i < TexasHoldEm.NUM_PLAYERS; i++) {
			
			if (!texasHoldEm.getPlayer()[i].isFold()) {
				
				String s = Deck.getStringCard(texasHoldEm.getPlayer()[i].getHoleCards()[0]);
				String path = "src/cards/" + s + ".png";
				BufferedImage image = ImageBank.getImage(path);
			    
				String s2 = Deck.getStringCard(texasHoldEm.getPlayer()[i].getHoleCards()[1]);
			    String path2 = "src/cards/" + s2 + ".png";
				BufferedImage image2 = ImageBank.getImage(path2);
					
				if (i == 2 || i == 5) {
					g.drawImage(ImageTool.rotate(image, 90), (int) cardsLocationsHashMap.getCardsLocation(i, 0).getX(), (int) cardsLocationsHashMap.getCardsLocation(i, 0).getY(), null);
					g.drawImage(ImageTool.rotate(image2, 90), (int) cardsLocationsHashMap.getCardsLocation(i, 1).getX(), (int) cardsLocationsHashMap.getCardsLocation(i, 1).getY(), null);
				} else {
					g.drawImage(image, (int) cardsLocationsHashMap.getCardsLocation(i, 0).getX(), (int) cardsLocationsHashMap.getCardsLocation(i, 0).getY(), CARD_WIDTH, CARD_HEIGHT, null); // 1's Player
				    g.drawImage(image2, (int) cardsLocationsHashMap.getCardsLocation(i, 1).getX(), (int) cardsLocationsHashMap.getCardsLocation(i, 0).getY(), CARD_WIDTH, CARD_HEIGHT, null);
				}
			}
		}
	}
}
