package hashMapPack;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents hash map of the gray players images locations and sizes, 
 * as a parameter of the player.
 * @author Naor Anhaisy
 *
 */
public class PlayersImagesLocations {
	private Map<Integer, Point> locationsMap;
	private Map<Integer, Point> sizeMap;
	/**
	 * Initialize all locations and sizes for all the players.
	 */
	public PlayersImagesLocations() {
		locationsMap = new HashMap<Integer, Point>();
		locationsMap.put(0, new Point(773, 572));
		locationsMap.put(1, new Point(266, 564));
		locationsMap.put(2, new Point(20, 256));
		locationsMap.put(3, new Point(310, 40));
		locationsMap.put(4, new Point(760, 24));
		locationsMap.put(5, new Point(1022, 247));
		
		sizeMap = new HashMap<Integer, Point>();
		sizeMap.put(0, new Point(172, 236));
		sizeMap.put(1, new Point(187, 243));
		sizeMap.put(2, new Point(229, 280));
		sizeMap.put(3, new Point(156, 209));
		sizeMap.put(4, new Point(144, 228));
		sizeMap.put(5, new Point(179, 296));
	}
	
	/**
	 * Gets from the map the location of the gray player for the specific player.
	 * @param playerIndex index of the player.
	 * @return point of the location of the gray player for the specific player.
	 */
	public Point getGrayPlayersLocation(int playerIndex) {
		return locationsMap.get(playerIndex);
	}
	
	/**
	 * Gets from the map the size of the image of the gray player for the specific player.
	 * @param playerIndex index of the player.
	 * @return size of the image of the gray player for the specific player.
	 */
	public Point getGrayPlayersSize(int playerIndex) {
		return sizeMap.get(playerIndex);
	}
}
