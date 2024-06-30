package graphicsDrawImg;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import hashMapPack.ImageBank;

/**
 * Represents the dealer drawing images.
 * @author Naor Anhaisy
 *
 */
public class DealerGraphic implements DrawAble {

	private BufferedImage jotonDealerIcon;
	private BufferedImage manDealerImage;
	
	/**
	 * Initialise the 2 dealer images.
	 * @throws IOException if any dealer icon not found.
	 */
	public DealerGraphic() throws IOException {
		String path = "src/img/Dealer.png";
		jotonDealerIcon = ImageBank.getImage(path);
		
	    String path2 = "src/img/DealerIcon.png";
	    manDealerImage = ImageBank.getImage(path2);
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(jotonDealerIcon, 500, 100, 100, 100, null);
	    g.drawImage(manDealerImage, 560, 78, 146, 153, null);
	}
}
