package panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import graphicsDrawImg.DrawChancesBar;
import javaPoker.TexasHoldEm;
import other.Sound;

/**
 * Represents the side buttons controller (Raise, Call, Check, Fold, All in)
 * @author Naor Anhaisy
 *
 */
public class ButtonsManager {
	
	private static final int DISTANCE_BETWEEN_BUTTONS = 67;
	private static final int Y_BUTTONS = 500;
	private static final int X_BUTTONS = 1120;
	
	private StatusMoneyManager statusMoneyManager;
	private TexasHoldEm texasHoldEm;
	private PokerGame pokerGame;
	private JButton fold, call, check, raise, allin;
	private boolean isRaiseClicked;
	private JComboBox<Integer> raiseHigh;
	
	/**
	 * Default constructor that initialize the main fields of the class.
	 * @param mainPanel poker game panel.
	 * @param texasHoldEm Texas holde'm game.
	 */
	public ButtonsManager(PokerGame mainPanel, TexasHoldEm texasHoldEm) {
		isRaiseClicked = false;
		this.pokerGame = mainPanel;
		this.texasHoldEm = texasHoldEm;
		this.statusMoneyManager = mainPanel.getStatusMoneyManager();
	}
	
	/**
	 * Adds the buttons to panel, changes them as needed and adds their action listeners.
	 * @throws IOException if image of button not found.
	 */
	public void addChoiseButtons() throws IOException {
		
		fold = new JButton(new ImageIcon(makePNGButtonsPath("FoldButton1")));
	    changeButtons(fold, 0);
	    call = new JButton(new ImageIcon(makePNGButtonsPath("CallButton1")));
	    changeButtons(call, 1);
	    check = new JButton(new ImageIcon(makePNGButtonsPath("CheckButton1")));
	    changeButtons(check, 1);
	    raise = new JButton(new ImageIcon(makePNGButtonsPath("RaiseButton1")));
	    changeButtons(raise, 2);
	    allin = new JButton(new ImageIcon(makePNGButtonsPath("AllInButton1")));
	    changeButtons(allin, 2);
	    
	    updateButtonsText();
		
		fold.addActionListener(foldAction);
		call.addActionListener(callCheckAction);
		check.addActionListener(callCheckAction);
		raise.addActionListener(raiseAction);
		
		pokerGame.add(fold);
		pokerGame.add(call);
		pokerGame.add(check);
		pokerGame.add(raise);
	}
	
	/**
	 * Adds the action listener to fold button.
	 */
	ActionListener foldAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				foldClicked();
			} catch (IOException e1) {
				System.out.println("Fold Sound not found");
				e1.printStackTrace();
			}
		}
	};
	
	/**
	 * Adds the action listener to call and check buttons.
	 */
	ActionListener callCheckAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				callClicked();
			} catch (IOException e1) {
				System.out.println("Money / Check Sound not found");
				e1.printStackTrace();
			}
		}
	};
	
	/**
	 * Adds the action listener to raise button.
	 * Adds to the panel combo box with all the optional values to raise to.
	 */
	ActionListener raiseAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (!isRaiseClicked) {
				isRaiseClicked = true;
				
				// Adds values to raiseHigh ComboBox
				int minRaise = (texasHoldEm.getCurrentBet() + 21) / 20;
				int maxRaise = (texasHoldEm.getPlayer()[0].getMoney() + texasHoldEm.getPlayer()[0].getBet()) / 20;
				Integer[] raiseInts = new Integer[maxRaise - minRaise + 1];
				
				for (int i = 0; i < raiseInts.length; i++) {
					raiseInts[i] = (i + minRaise) * 20;
				}
				
				raiseHigh = new JComboBox<Integer>(raiseInts);
				raiseHigh.setSelectedItem(raiseInts[0]);
				raiseHigh.setVisible(true);
				
				// Value in raiseHigh clicked
				raiseHigh.addActionListener(raiseHighAction);
				raiseHigh.setVisible(true);
				
				pokerGame.add(raiseHigh);
				raiseHigh.setBounds(X_BUTTONS, 710, 100, 30);
			
			} else {
				isRaiseClicked = false;
				raiseHigh.setVisible(false);
			}
		}
	};
	
	/**
	 * Adds the action listener to raiseHighAction comboBox.
	 */
	ActionListener raiseHighAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			isRaiseClicked = false;
			try {
				raiseItemSelected();
			} catch (IOException e1) {
				System.out.println("Money Sound not found");
				e1.printStackTrace();
			}
		}
	};
	
	/**
	 * Make BufferedImage of name of button image.
	 * @param string name of the button (Fold, Raise, Call, Check, All In).
	 * @return BufferedImage of the image.
	 * @throws IOException if the image not found.
	 */
	public BufferedImage makePNGButtonsPath(String string) throws IOException {
		String path = "src/img/" + string + ".png";
	    File file = new File(path);
	    return ImageIO.read(file);
	}
	
	/**
	 * Change the look of play buttons (Fold, Raise, Call, Check, All In).
	 * @param button Play button to change it's look
	 * @param i vertical row to bound the button in.
	 */
	private void changeButtons(JButton button, int i) {
		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setBackground(new Color(117, 62, 199));
		button.setBounds(X_BUTTONS, Y_BUTTONS + DISTANCE_BETWEEN_BUTTONS * i, 172, 62);
		button.addMouseListener(createMouseOver());
	}
	
	/**
	 * Create Mouse Adapter for a button.
	 * When mouse over it, the button changes, and when the mouse exit,
	 * the button back to as the start.
	 * @return MouseAdapter for the button.
	 */
	private MouseAdapter createMouseOver() {
		return new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				((JButton) e.getSource()).setOpaque(true);
				((JButton) e.getSource()).setFocusPainted(true);
				((JButton) e.getSource()).setBorderPainted(true);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				((JButton) e.getSource()).setOpaque(false);
				((JButton) e.getSource()).setFocusPainted(false);
				((JButton) e.getSource()).setBorderPainted(false);
			}
		};
	}
	
	/**
	 * Play raise for the player.
	 * @throws IOException if raise sounds not found.
	 */
	public void raiseItemSelected() throws IOException {
		final int x = (int) raiseHigh.getSelectedItem();
		System.out.println("\n\nYou raised to " + x);
		Sound.playSound("moneySound");
		Sound.playSound("PlayerRaises");
		statusMoneyManager.addStatusOfPlayers(0, "Raise");
		statusMoneyManager.play("Raise", x, 0);
		
		raiseHigh.setVisible(false);
		setButtonsEnabled(false);
		
		makeBetRoundAndHandleGameContinue(x);
	}
	
	/**
	 * Play call or check for the player.
	 * @throws IOException if call/check sounds not found.
	 */
	public void callClicked() throws IOException {
		statusMoneyManager.addStatusOfPlayers(0, "Call");
		if (texasHoldEm.getPlayer()[0].getBet() == texasHoldEm.getCurrentBet()) {
			Sound.playSound("PlayerCheck");
			Sound.playSound("callSound");

			System.out.println("\n\nYou checked");
			statusMoneyManager.myStatuslbl.setText("Check");
			
		} else {
			Sound.playSound("moneySound");
			Sound.playSound("PlayerCalls");
			System.out.println("\n\nYou called");
			statusMoneyManager.myStatuslbl.setText("Call: " + texasHoldEm.getCurrentBet() + " $");
		}
		
		try {
			raiseHigh.setVisible(false);
		} catch (Exception e) { }
		setButtonsEnabled(false);
		
		makeBetRoundAndHandleGameContinue(texasHoldEm.getCurrentBet());
	}
	
	/**
	 * Starts bet round with the bet, and after the bet round either ends the 
	 * hand if the final round arrived. else, let my player play.
	 * @param bet bet of the player, for the round bet.
	 */
	private void makeBetRoundAndHandleGameContinue(final int bet) {
		Runnable runnable = new Runnable() {
			public void run() {
				
				statusMoneyManager.updateSumBetOnTableLabel();
				texasHoldEm.betRound(bet);
				
				// is the game over ?
				if (texasHoldEm.getRound() == 4) {
					pokerGame.newHand();
					setButtonsVisible(false);
					
				// the game still continue, and its my player's turn
				} else { 
					updateButtonsText();
					setButtonsEnabled(true);
					pokerGame.addTimer(texasHoldEm.getCurrentPlayerIndex());
				}
				pokerGame.repaint();
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	/**
	 * Play fold for the player. Make him unavailable to choose any action,
	 * and continue round bet until the last round.
	 * @throws IOException if fold sounds not found.
	 */
	public void foldClicked() throws IOException {
		System.out.println("\n\nYou folded");
		Sound.playSound("foldSound");
		Sound.playSound("PlayerFolds");
		statusMoneyManager.addStatusOfPlayers(0, "Fold");
		statusMoneyManager.myStatuslbl.setText("Fold");
		DrawChancesBar.removeChancesBar();

		try {
			raiseHigh.setVisible(false);
		} catch (Exception e) { }
		setButtonsVisible(false);
		texasHoldEm.getPlayer()[0].setFold(true);
		
		Runnable runnable = new Runnable() {
			public void run() {
				while (texasHoldEm.getRound() < 4)
				{
					texasHoldEm.betRound(texasHoldEm.getCurrentBet());
					if (texasHoldEm.getRound() == 4) {
						pokerGame.newHand();
					}
				}
				pokerGame.repaint();
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	/**
	 * set raise, fold, call, check and all in buttons enable by the parameter.
	 * @param enabled true to set enabled (true), else set them false.
	 */
	public void setButtonsEnabled(boolean enabled) {
		raise.setEnabled(enabled);
		fold.setEnabled(enabled);
		call.setEnabled(enabled);
		check.setEnabled(enabled);
		allin.setEnabled(enabled);
		pokerGame.repaint();
	}
	
	/**
	 * set raise, fold, call, check and all in buttons visibility by the parameter.
	 * @param visibility true to set visible (true), else set them false.
	 */
	private void setButtonsVisible(boolean visibility) {
		raise.setVisible(visibility);
		fold.setVisible(visibility);
		call.setVisible(visibility);
		check.setVisible(visibility);
		allin.setVisible(visibility);
		pokerGame.repaint();
	}
	
	/**
	 * Switch Call and Check buttons if necessary, 
	 * and handle case that player has only all in or fold options so
	 * remove unavailable options.
	 */
	public void updateButtonsText() {
		// Switch Call and Check buttons.
		if (texasHoldEm.getPlayer()[0].getBet() == texasHoldEm.getCurrentBet()) {
			call.setVisible(false);
			check.setVisible(true);
		} else {
			call.setVisible(true);
			check.setVisible(false);
		}
		
		// Make sure for only option of all in and fold.
		if (texasHoldEm.getPlayer()[0].getMoney() + texasHoldEm.getPlayer()[0].getBet() < texasHoldEm.getCurrentBet() + 20) {
			pokerGame.remove(call);
			pokerGame.remove(check);
			changeButtons(fold, 1);
			changeRaiseToAllIn();
		}
		pokerGame.repaint();
	}
	
	/**
	 * Change "raise" button to "all in", and adds action listener to "all in" button.
	 */
	private void changeRaiseToAllIn() {
		pokerGame.remove(raise);
		pokerGame.add(allin);
		pokerGame.repaint();
		if (allin.getActionListeners().length == 0) {
			allin.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					allInClicked();
				}
			});
		}
	}
	
	/**
	 * Makes the player bet of all his money, and lets game continue until
	 * the last round, and not let my player to choose until next hand.
	 */
	public void allInClicked() {
		try {
			Sound.playSound("moneySound");
		} catch (IOException e1) {
			System.out.println("Money Sound not found.");
		}
		texasHoldEm.makeMyPlayerBet(texasHoldEm.getPlayer()[0].getMoney());
		setButtonsVisible(false);
		
		Runnable runnable = new Runnable() {
			public void run() {
				while (texasHoldEm.getRound() < 4) {
					if (texasHoldEm.checkBetsEquals())
						texasHoldEm.nextRound();
					else
						texasHoldEm.betRound(0);
					if (texasHoldEm.getRound() == 4)
						pokerGame.newHand();
				}
				pokerGame.repaint();
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
}
