package graphicsDrawImg;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import hashMapPack.BlindsHashMap;
import hashMapPack.ImageBank;
import javaPoker.TexasHoldEm;

/**
 * Represents drawing images of the big and small blinds, in their specific places.
 * @author Naor Anhaisy
 *
 */
public class BigSmallBlinds implements DrawAble {
	
	private static final int BLIND_IMAGE_SIZE = 40;
	private BufferedImage smallBlind;
	private BufferedImage bigBlind;
	private BlindsHashMap blindsHashMap;
	private int smallBlindIndex = TexasHoldEm.smallBlind;
	private int bigBlindIndex = TexasHoldEm.bigBlind;
    
	/**
	 * Initialize the small and big blind images, and create new blinds hash map
	 * for it's location.
	 * @throws IOException if small / big blinds images weren't found.
	 */
	public BigSmallBlinds() throws IOException {
		String path = "src/img/SmallBlind.png";
	    smallBlind = ImageBank.getImage(path);
	    
	    String path2 = "src/img/BigBlind.png";
	    bigBlind = ImageBank.getImage(path2);
	    
	    blindsHashMap = new BlindsHashMap();
	}
	
	@Override
	public void draw(Graphics g) {
		Point small = blindsHashMap.getBlindImageLocation(smallBlindIndex);
		Point big = blindsHashMap.getBlindImageLocation(bigBlindIndex);
		g.drawImage(smallBlind, (int) small.getX() + 200, (int) small.getY(), BLIND_IMAGE_SIZE, BLIND_IMAGE_SIZE, null);
		g.drawImage(bigBlind, (int) big.getX() + 200, (int) big.getY(), BLIND_IMAGE_SIZE, BLIND_IMAGE_SIZE, null);
	}
}
