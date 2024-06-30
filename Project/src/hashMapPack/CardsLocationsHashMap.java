package hashMapPack;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents hash map of the cards images locations, 
 * as a parameter of the player and the number of the card.
 * @author Naor Anhaisy
 *
 */
public class CardsLocationsHashMap {
	private Map<Integer, Point> card1Map, card2Map;
	
	/**
	 * Initialize the cards locations 2 hash maps - 
	 * 1 hash map for the first card, and the other map is for the
	 * other card.
	 */
	public CardsLocationsHashMap() {
		card1Map = new HashMap<Integer, Point>();
		card1Map.put(0, new Point(650, 490));
		card1Map.put(1, new Point(460, 490));
		card1Map.put(2, new Point(250, 350));
		card1Map.put(3, new Point(470, 250));
		card1Map.put(4, new Point(770, 250));
		card1Map.put(5, new Point(870, 350));
		
		card2Map = new HashMap<Integer, Point>();
		card2Map.put(0, new Point(740, 490));
		card2Map.put(1, new Point(370, 490));
		card2Map.put(2, new Point(250, 430));
		card2Map.put(3, new Point(380, 250));
		card2Map.put(4, new Point(680, 250));
		card2Map.put(5, new Point(870, 430));
	}
	
	/**
	 * Gets from the map the location of the specific card image for the specific player.
	 * @param playerIndex index of the player that holds the card.
	 * @param numCard the number of the card. Can be 0 or 1 only.
	 * @return point of the location of the specific card image for the specific player.
	 * @throws @{@link IllegalArgumentException} if the numCard is unavailable.
	 */
	public Point getCardsLocation(int playerIndex, int numCard) {
		if (numCard == 0) 
			return card1Map.get(playerIndex);
		if (numCard == 1)
			return card2Map.get(playerIndex);
		throw new IllegalArgumentException("numCard must be 0 Or 1 Only!");
	}
}
