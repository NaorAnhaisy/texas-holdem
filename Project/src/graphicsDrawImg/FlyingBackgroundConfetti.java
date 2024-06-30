package graphicsDrawImg;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import hashMapPack.ImageBank;
import main.Main;

/**
 * Represents a single conffeti, that flyies in the background of the panel.
 * @author Naor Anhaisy
 *
 */
public class FlyingBackgroundConfetti implements DrawAble {
	
	private static final int OFFSET_BETWEEN_CURRENT_X_AND_LAST_X = 50;
	private double angleOfConfetti;
	private int x, y;
	private Random rand;
	private BufferedImage ConfettiImage;
	private Image ConfettiRotatedImage;
	
	/**
	 * Create new FlyingBackgroundConfetti object.
	 * Initialize the conffeti image, it's angle, and it's location.
	 * @param kind the kind of conffeti, in the ConfettiKinds.
	 * @param lastXEntered the last X coordination entered, so
	 * the next X will not be close to it's previous.
	 */
	public FlyingBackgroundConfetti(ConfettiKinds kind, int lastXEntered) {
		
		String path = null;
		switch (kind) {
		case SPADES:
			path = "src/img/spadesfly.png";
			break;
			
		case DIAMONDS:
			path = "src/img/diamondfly.png";
			break;
			
		case HEART:
			path = "src/img/heartfly.png";
			break;
			
		case CLUBS:
			path = "src/img/clubsfly.png";
			break;
			
		default:
			break;
		}
		
		rand = new Random();
		ConfettiImage = ImageBank.getImage(path);
		angleOfConfetti = 0;
		do {
			x = rand.nextInt(Main.START_PAGE_WIDTH);
		} while ((x + OFFSET_BETWEEN_CURRENT_X_AND_LAST_X > lastXEntered) &&
				(x - OFFSET_BETWEEN_CURRENT_X_AND_LAST_X < lastXEntered));
		y = -ConfettiImage.getHeight();
	}
	
	/**
	 * Handle the changing in rotation and location of the conffeti.
	 */
	public void timePassedConfetti() {
		y += 2;
		angleOfConfetti++;
		ConfettiRotatedImage = ImageTool.rotate(ConfettiImage, angleOfConfetti);
	}
	
	/**
	 * Check whether the image is out of the borders of the panel.
	 * @return true if the image is out of the borders, else return false.
	 */
	public boolean isOutOfBorders() {
		return ((y > Main.START_PAGE_HEIGHT) || (x > Main.START_PAGE_WIDTH) || (x < 0));
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(ConfettiRotatedImage, x, y, null);
	}
	
	/**
	 * @return X coordination of the confetti.
	 */
	public int getX() {
		return x;
	}
}
