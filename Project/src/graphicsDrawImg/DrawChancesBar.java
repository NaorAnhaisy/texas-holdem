package graphicsDrawImg;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import hashMapPack.ImageBank;
import javaPoker.TexasHoldEm;
import panels.PokerGame;

/**
 * Represents the chances bar drawing, next to my player.
 * This chances bar shows the player how good is his hand, compare to others hand, 
 * so what are his chances to win.
 * As much as the bright side of the bar is greener, as better his hand.
 * @author Naor Anhaisy
 *
 */
public class DrawChancesBar implements DrawAble {
	
	private static final int FPS = 15;
	private static final int MIN_WIDTH_FOR_CHANCES_BAR = 1;
	private static final int BAR_X = 940;
	private static final int BAR_Y = 730;
	private TexasHoldEm texasHoldEm;
	private PokerGame pokerGame;
	private BufferedImage chancesBar, chancesBarContainer;
	public static boolean isBarRemoved;
	private double chances;
	private int currentDrawingChances;
	
	/**
	 * Default contractor to create draw chances bar object.
	 * Initialize images and required fields, and updates the chances bar of my player.
	 * @param texasHoldEm TexasHoldEm of the gamePlay.
	 * @param pokerGame Panel of the game to repaint.
	 */
	public DrawChancesBar(TexasHoldEm texasHoldEm, PokerGame pokerGame) {
		chancesBar = ImageBank.getImage("src/img/ChancesBar.png");
		chancesBarContainer = ImageBank.getImage("src/img/ChancesBarContainer.png");
		this.texasHoldEm = texasHoldEm;
		this.pokerGame = pokerGame;
		isBarRemoved = false;
		currentDrawingChances = MIN_WIDTH_FOR_CHANCES_BAR;
		updateChancesBar();
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(chancesBarContainer, BAR_X, BAR_Y, null);
		if (!isBarRemoved)
			g.drawImage(getImageByWidth(chancesBar, currentDrawingChances), BAR_X, BAR_Y, null);
	}
	
	/**
	 * If chances bar isn't updated, calculate the chances to win for the player,
	 * and update the chances bar.
	 */
	private void calculateMyChnacesOnce() {
		chances = 100 * texasHoldEm.getComputerPlayersAI().calculateChencesToWin(0, texasHoldEm.getNumOfPlayersInHand(), texasHoldEm.getBoard().getAllBoardCards());
		System.out.println("My player chances : " + (int) chances + " %");
	}
	
	/**
	 * Update the chances bar.
	 * Calculates the chances of the player to win, and moving chances
	 * as an animation to the wanted place.
	 */
	public void updateChancesBar() {
		
		calculateMyChnacesOnce();
		moveChancesOnBar();
	}

	private void moveChancesOnBar() {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (currentDrawingChances != (int) chances) {
					
					if (currentDrawingChances > chances) {
						currentDrawingChances--;
					} else 
						currentDrawingChances++;
					
					pokerGame.repaint();
					
					try {
						Thread.sleep(FPS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
	
	/**
	 * Remove the chances bar from it's container.
	 */
	public static void removeChancesBar() {
		isBarRemoved = true;
	}
	
	/**
	 * Returns an image, which includes a specific width from the whole original image.
	 * @param image bufferedImage of the original image.
	 * @param width width of the part image.
	 * @return the specific part image from the whole original image.
	 */
	private Image getImageByWidth(BufferedImage image, double width) {
		double newWidth = image.getWidth() * (width / 100);
		if (newWidth < MIN_WIDTH_FOR_CHANCES_BAR)
			newWidth = MIN_WIDTH_FOR_CHANCES_BAR;
		return image.getSubimage(0, 0, (int) newWidth, image.getHeight());
	}
}
