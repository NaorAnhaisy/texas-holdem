package graphicsDrawImg;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Represents manager of all the Flying Background Confettis.
 * Manage to create them only when can, and draw them on the main panel.
 * @author Naor Anhaisy
 *
 */
public class ConfettiManager implements DrawAble {
	
	private static int FPS = 60;
	private static int ITERATION_MILLIS = 1100 / FPS;
	private static final int MAX_FLY_IMAGES = 5;
	private static final long TIME_BETWEEN_CONFETTIS_IN_MILIS = 1800;
	private List<FlyingBackgroundConfetti> drawBackgroundConfettis;
	private List<FlyingBackgroundConfetti> removebackgroundConfettis;
	private long lastTimeConfettiEntered;
	private int lastXEntered;
	private JPanel mainPanel;
	
	/**
	 * Create new confetti manager object, which initialize the current drawing confettis,
	 * and information about the last confetti entered.
	 * @param mainPanel
	 */
	public ConfettiManager(JPanel mainPanel) {
		this.mainPanel = mainPanel;
		drawBackgroundConfettis = new LinkedList<FlyingBackgroundConfetti>();
		removebackgroundConfettis = new LinkedList<FlyingBackgroundConfetti>();
		lastTimeConfettiEntered = 0;
		lastXEntered = 0;
	}
	
	/**
	 * Handle to create new confetti if it's possible, and responsible to
	 * update all the confettis in the game.
	 * Also, remove conffeti if it's out of borders.
	 * Finally, Repainting and sleeping the function to make animation.
	 */
	public void timePassed() {
		
		/* Add new conffeti to the panel if allowed */
		if (drawBackgroundConfettis.size() < MAX_FLY_IMAGES && System.currentTimeMillis() - lastTimeConfettiEntered >= TIME_BETWEEN_CONFETTIS_IN_MILIS) {
			FlyingBackgroundConfetti newConfetti = new FlyingBackgroundConfetti(ConfettiKinds.getRandomKind(), lastXEntered);
			lastXEntered = newConfetti.getX();
			drawBackgroundConfettis.add(newConfetti);
			lastTimeConfettiEntered = System.currentTimeMillis();
		}
		
		/* Updating all the confetties */
		for (FlyingBackgroundConfetti drawBackgroundConfetti : drawBackgroundConfettis) {
			drawBackgroundConfetti.timePassedConfetti();
			if (drawBackgroundConfetti.isOutOfBorders()) {
				removebackgroundConfettis.add(drawBackgroundConfetti);
			}
		}
		
		/* Remove the out of the borders conffeties */
		for (FlyingBackgroundConfetti removebackgroundConfetti : removebackgroundConfettis) {
			drawBackgroundConfettis.remove(removebackgroundConfetti);
		}
		
		removebackgroundConfettis.clear();
		
		mainPanel.repaint();
		try {
			Thread.sleep(ITERATION_MILLIS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics g) {
		for (FlyingBackgroundConfetti drawBackgroundConfetti : new ArrayList<FlyingBackgroundConfetti>(drawBackgroundConfettis)) {
			drawBackgroundConfetti.draw(g);
		}
	}
}
