package hashMapPack;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents hash map of the blinds images locations, 
 * as a parameter of the player.
 * @author Naor Anhaisy
 *
 */
public class BlindsHashMap {
	private Map<Integer, Point> blindsMap;
	
	/**
	 * Initialize all locations of blinds for all the players.
	 */
	public BlindsHashMap() {
		blindsMap = new HashMap<Integer, Point>();
		blindsMap.put(0, new Point(620, 540));
		blindsMap.put(1, new Point(120, 540));
		blindsMap.put(2, new Point(65, 305));
		blindsMap.put(3, new Point(350, 250));
		blindsMap.put(4, new Point(650, 260));
		blindsMap.put(5, new Point(780, 450));
	}
	
	/**
	 * Gets from the map the location of the blind image for the specific player.
	 * @param playerIndex index of the player.
	 * @return point of the location of the blind image for the specific player.
	 */
	public Point getBlindImageLocation(int playerIndex) {
		return blindsMap.get(playerIndex);
	}
}
