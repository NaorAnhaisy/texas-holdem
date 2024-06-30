package hashMapPack;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents hash map of the timer cake locations, 
 * as a parameter of the player.
 * @author Naor Anhaisy
 *
 */
public class TimerCakeHashMap {
	private Map<Integer, Point> timerMap;
	
	/**
	 * Initialize all locations of timer cake for all the players.
	 */
	public TimerCakeHashMap() {
		timerMap = new HashMap<Integer, Point>();
		timerMap.put(0, new Point(730, 680));
		timerMap.put(1, new Point(400, 680));
		timerMap.put(2, new Point(130, 440));
		timerMap.put(3, new Point(280, 160));
		timerMap.put(4, new Point(860, 170));
		timerMap.put(5, new Point(1040, 440));
	}
	
	/**
	 * Gets from the map the location of the timer cake for the specific player.
	 * @param playerIndex index of the player.
	 * @return point of the location of the timer cake for the specific player.
	 */
	public Point getTimerCakeLocation(int playerIndex) {
		return timerMap.get(playerIndex);
	}
}
