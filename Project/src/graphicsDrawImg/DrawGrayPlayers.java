package graphicsDrawImg;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import hashMapPack.PlayersImagesLocations;
import hashMapPack.ImageBank;
import javaPoker.Player;

/**
 * Represents a drawing class for all the out-of-game players.
 * If any player is out of the game, the player is drawing in gray.
 * @author Naor Anhaisy
 *
 */
public class DrawGrayPlayers implements DrawAble {
	
	private Player[] players;
	private BufferedImage[] grayPlayers;
	private PlayersImagesLocations graysHash;
	
	/**
	 * Initialize the images of gray players, and the hash map for locations
	 * and size of the images.
	 * Also, initialize the players field, which include an array of
	 * all the players.
	 * @param players array that hold information of all the players.
	 */
	public DrawGrayPlayers(Player[] players) {
		this.players = players;
		graysHash = new PlayersImagesLocations();
		grayPlayers = new BufferedImage[players.length];
		
		for (int i = 0; i < players.length; i++) {
			grayPlayers[i] = ImageBank.getImage("src/img/gray" + i + ".png");
		}
	}

	@Override
	public void draw(Graphics g) {
		for (int i = 0; i < players.length; i++) {
			if (players[i].isOutOfGame())
				g.drawImage(grayPlayers[i],
						(int) graysHash.getGrayPlayersLocation(i).getX(), (int) graysHash.getGrayPlayersLocation(i).getY(),
						graysHash.getGrayPlayersSize(i).x, graysHash.getGrayPlayersSize(i).y, null);
		}
	}
}
