package hashMapPack;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents hash map of locations for the name label of players,
 * for their money label (getY() + 40) and status label (getY() + 50).
 * @author Naor Anhaisy
 *
 */
public class NameLblHashMap {
	private static final int LEVEL_0_VERTICAL = 0;
	private static final int LEVEL_1_VERTICAL = 160;
	private static final int LEVEL_2_VERTICAL = 600;
	private Map<Integer, Point> nameLblMap;
	
	/**
	 * Initialize the locations of the name's labels of the players.
	 */
	public NameLblHashMap() {
		nameLblMap = new HashMap<Integer, Point>();
		nameLblMap.put(0, new Point(940, LEVEL_2_VERTICAL));
		nameLblMap.put(1, new Point(140, LEVEL_2_VERTICAL));
		nameLblMap.put(2, new Point(40, LEVEL_1_VERTICAL));
		nameLblMap.put(3, new Point(160, LEVEL_0_VERTICAL));
		nameLblMap.put(4, new Point(940, LEVEL_0_VERTICAL));
		nameLblMap.put(5, new Point(1100, LEVEL_1_VERTICAL));
	}
	
	/**
	 * @param playerIndex index of player (0 - 5).
	 * @return the name label location of specific player.
	 */
	public Point getNameLblLocation(int playerIndex) {
		return nameLblMap.get(playerIndex);
	}
}
