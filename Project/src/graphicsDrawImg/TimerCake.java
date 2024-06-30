package graphicsDrawImg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Timer;

import javaPoker.Player;
import panels.PokerGame;

/**
 * Represents the timer that appears to each player in his turn.
 * If the timer over to the player, he will fold in the round bet bigger that his own bet, 
 * otherwise the player will check.
 * @author Naor Anhaisy
 *
 */
public class TimerCake {
	private static final int CHANGING_IN_TIME = 5; // If want to faster - increase it. Else, decrease it.
	private static final int START_ANGLE = 90;
	private static final int MAX_CHANGE_ANGLE = 360;
	private static final double CHANGING_IN_ARC = 0.4;
	private static final double CHANGING_IN_COLOR = 0.5;
	private static final int TIMER_TICK = 1000;
	private static final int ITERATION_MILLIS = TIMER_TICK / (CHANGING_IN_TIME * 10);
	private static final int START_RED = 0;
	private static final int START_GREEN = 235;
	private static final int START_BLUE = 0;
	private static final int RADIUS = 80;
	private double i;
	private int red, green, blue;
	private int x, y;
	private double arg;
	private PokerGame gamePanel;
	private Timer t;
	private Player player;
	public static boolean suspended;
	
	/**
	 * Create new TimerCake object at the start of the game.
	 * @param x X coordinate of the Timer Cake.
	 * @param y Y coordinate of the Timer Cake.
	 * @param gamePanel The poker panel.
	 * @param player The current player.
	 */
	public TimerCake(int x, int y, PokerGame gamePanel, Player player) {
		this.x = x;
		this.y = y;
		this.gamePanel = gamePanel;
		reset(player);
	}
	
	/**
	 * Starts the timer for the current player.
	 * If the timer is over, means the player didn't act, and he rather do
	 * fold if current round bet bigger that his bet, otherwise check.
	 */
	public void startTimer() {
		t = new Timer(ITERATION_MILLIS, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (arg / CHANGING_IN_ARC > 1) {
					arg = arg - CHANGING_IN_ARC;
					i = i + CHANGING_IN_COLOR;
					if (red < green) {
						red = (int) i;
					}
					else {
						green = START_GREEN * 2 - (int) i;
					}
					gamePanel.repaint();
				}
				else {
					if (player.getBet() == gamePanel.texasHoldEm.getCurrentBet()) {
						try {
							gamePanel.getButtonsManager().callClicked();
						} catch (IOException e1) {
							System.out.println("Money / Check sound not found");
							e1.printStackTrace();
						}
					}
					else {
						try {
							gamePanel.getButtonsManager().foldClicked();
						} catch (IOException e1) {
							System.out.println("Fold sound not found");
							e1.printStackTrace();
						}
					}
				}
			}
		});
		t.start();
	}
	
	/**
	 * Checks if the timer is running for certain player.
	 * @return true if the timer is running.
	 * else @return false.
	 */
	public boolean isTimerRunning() {
		if (t != null && t.isRunning()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Stops the timer.
	 */
	public void stopTimer() {
		t.stop();
	}
	
	/**
	 * Draws the timer, while it's running.
	 * @param g Graphics g
	 */
	public void draw(Graphics g) {
		if (t.isRunning()) {
			g.setColor(new Color(red, green, blue));
		    g.fillArc(x, y, RADIUS, RADIUS, START_ANGLE, (int) (arg - arg / TIMER_TICK));
		    g.setColor(Color.BLACK);
		    g.drawOval(x, y, RADIUS, RADIUS);
		}
	}
	
	/**
	 * Sets the X coordinate of the timer.
	 * @param x new X coordinate.
	 */
	public void setX(int x) {
		this.x = x;		
	}
	
	/**
	 * Sets the Y coordinate of the timer.
	 * @param y new Y coordinate.
	 */
	public void setY(int y) {
		this.y = y;	
	}
	
	/**
	 * Resets the timer, and change the player it belongs to.
	 * @param player new player the timer will belong to.
	 */
	public void reset(Player player) {
		i = 0;
		arg = MAX_CHANGE_ANGLE;
		red = START_RED;
		blue = START_BLUE;
		green = START_GREEN;
		this.player = player;
	}
}
