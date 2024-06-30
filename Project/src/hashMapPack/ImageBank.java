package hashMapPack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Represents ImageBank for all buffered images that exist.
 * It's key is a String which represents the path of the image,
 * and it's value is it's buffered image.
 * @author Naor Anhaisy
 *
 */
public abstract class ImageBank {

	private static Map<String, BufferedImage> imagesMap = new HashMap<String, BufferedImage>();
	
	/**
	 * Return buffered image of a certain path.
	 * If the path already exist in the dictionary, it's value will be returned.
	 * Else, new value will be keeped in the hash map by the key of the path,
	 * and the value will be returned.
	 * @param path string that describe the full path to the image.
	 * @return buffered image of the path.
	 */
	public static BufferedImage getImage(String path) {
		if (imagesMap.containsKey(path)) {
			return imagesMap.get(path);
		}
		File file = new File(path);
		BufferedImage image = null;
	    try {
	    	image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    imagesMap.put(path, image);
	    return image;
	}
}
