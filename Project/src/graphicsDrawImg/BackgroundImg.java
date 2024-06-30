package graphicsDrawImg;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import hashMapPack.ImageBank;

/**
 * Drawing the table image with the players images.
 * @author Naor Anhaisy
 *
 */
public class BackgroundImg implements DrawAble {

	private BufferedImage image;
	
	/**
	 * Initialize the image field to be the background image.
	 * @throws IOException if the background image weren't found.
	 */
	public BackgroundImg() throws IOException {
		String path = "src/img/PokerTable.png";
	    image = ImageBank.getImage(path);
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(image, 0, 0, 1300, 810, null);
	}
}
