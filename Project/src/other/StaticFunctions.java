package other;

import java.awt.image.BufferedImage;

import panels.PokerGame;

/**
 * Represents static functions used, that don't belong into a specific class
 * in the project.
 * @author Naor Anhaisy
 *
 */
public abstract class StaticFunctions {
	
	private static final int BURN_CARD_ITERATION_MILLIS = 2;
	private static final int NORMAL_CARD_ITERATION_MILLIS = 4;
	public static boolean isNewRound = false;
	public static BufferedImage cardToDraw = null;
	public static int currentX, currentY;
	
	/**
	 * Returns big numbers in shortcuts.
	 * Thousands with K, millions with M, and billions with B.
	 * For example, 10000 = 10K, 1000000 = 1M and etc..
	 * @param num number to write in shortcut.
	 * @return big numbers in shortcuts.
	 */
	public static String returnBigNumbersInShortcat(int num) {
		if (num >= 10000 && num < 1000000) {
			return String.valueOf(num / 1000) + "." + String.valueOf(num % 1000 / 100) + "K";
		}
		if (num >= 1000000 && num < 1000000000) {
			return String.valueOf(num / 1000000) + "." + String.valueOf(num % 1000000 / 100000) + "M";
		}
		if (num >= 1000000000) {
			return String.valueOf(num / 1000000000) + "." + String.valueOf(num % 1000000000 / 100000000) + "B";
		}
		return String.valueOf(num);
	}
	
	/**
	 * Responsible for moving card that starts in certain point, toward a destination point.
	 * @param finalDestX final destination X coordinator
	 * @param finalDestY final destination Y coordinator
	 * @param isBurn if this is a burn card - true, else - false.
	 */
	public static void moveCard(PokerGame pokerGame, int finalDestX, int finalDestY, boolean isBurn) {
		
		if (isNewRound) {
			while (currentX != (finalDestX) || currentY != finalDestY) {
				if (currentX < finalDestX)
					currentX++;
				else if (currentX > finalDestX)
					currentX --;
				if (currentY < finalDestY)
					currentY++;
				else if (currentY > finalDestY)
					currentY --;
				try {
					pokerGame.repaint();
					if (isBurn)
						Thread.sleep(BURN_CARD_ITERATION_MILLIS);
					else
						Thread.sleep(NORMAL_CARD_ITERATION_MILLIS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
