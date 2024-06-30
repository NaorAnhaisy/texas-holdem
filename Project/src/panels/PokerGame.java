package panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import graphicsDrawImg.BackgroundImg;
import graphicsDrawImg.BigSmallBlinds;
import graphicsDrawImg.DealerGraphic;
import graphicsDrawImg.DrawBoardCards;
import graphicsDrawImg.DrawBurnCards;
import graphicsDrawImg.DrawChancesBar;
import graphicsDrawImg.DrawGrayPlayers;
import graphicsDrawImg.DrawPlayersCards;
import graphicsDrawImg.TimerCake;
import hashMapPack.CardsLocationsHashMap;
import hashMapPack.NameLblHashMap;
import hashMapPack.TimerCakeHashMap;
import javaPoker.Player;
import javaPoker.TexasHoldEm;
import other.Sound;

/**
 * Represents the Poker Game's panel.
 * @author Naor Anhaisy
 * 
 */
@SuppressWarnings("serial")
public class PokerGame extends JPanel {
	
	public static final int BOARD_CARD_HEIGHT = 110;
	public static final int BOARD_CARD_WIDTH = 75;
	public static final int CARD_WIDTH = 70;
	public static final int CARD_HEIGHT = 100;
	public static final int SPACE_BETWEEN_DECK_CARDS = 85;
	private static final int NAME_WIDTH = 200, NAME_HEIGHT = 40;
	
	private JFrame frame;
	private JPanel pokerPanel;
	public TexasHoldEm texasHoldEm;
	public TimerCake t;
	private DealerGraphic dealerIcons;
	private DrawPlayersCards allPlayersCards;
	private BigSmallBlinds bigSmallBlinds;
	private BackgroundImg backgroundImg;
	private DrawGrayPlayers drawGrayPlayers;
	private DrawBurnCards drawBurnCards;
	private DrawBoardCards drawBoardCards;
	public DrawChancesBar drawChancesBar;
	
	private StatusMoneyManager statusMoneyManager;
	private ButtonsManager buttonsManager;

	private TimerCakeHashMap timerCakeHashMap;
	private NameLblHashMap nameLblHashMap;
	private CardsLocationsHashMap greenRectHashMap;
	
	private JLabel myNamelbl;
	private JLabel namelbl1;
	private JLabel namelbl2;
	private JLabel namelbl3;
	private JLabel namelbl4;
	private JLabel namelbl5;
	
	private JLabel winner;
	private JLabel winnerHand;
	
	/**
	 * Main constructor that initialize all necessary items in the panel.
	 * @param holdEm TexasHoldEm parameter, to start play the game by it.
	 * @param frame frame that holds the panel.
	 * @throws IOException if BackgroundImg, DealerGraphic, DrawPlayersCards, DrawBurnCards images not found.
	 */
	public PokerGame(TexasHoldEm holdEm, JFrame frame) throws IOException {
		setLayout(null); 
		this.frame = frame;
		pokerPanel = this;
		texasHoldEm = holdEm;
		
		backgroundImg = new BackgroundImg();
		
		timerCakeHashMap = new TimerCakeHashMap();
		nameLblHashMap = new NameLblHashMap();
		greenRectHashMap = new CardsLocationsHashMap();
		
		drawGrayPlayers = new DrawGrayPlayers(texasHoldEm.getPlayer());
		dealerIcons = new DealerGraphic();
		allPlayersCards = new DrawPlayersCards(texasHoldEm);
		bigSmallBlinds = new BigSmallBlinds();
		drawBurnCards = new DrawBurnCards(texasHoldEm);
		drawBoardCards = new DrawBoardCards(texasHoldEm);
		drawChancesBar = new DrawChancesBar(texasHoldEm, this);
		
		myNamelbl = new JLabel();
		namelbl1 = new JLabel();
		namelbl2 = new JLabel();
		namelbl3 = new JLabel();
		namelbl4 = new JLabel();
		namelbl5 = new JLabel();
		addPlayersNames();
		
		statusMoneyManager = new StatusMoneyManager(this, texasHoldEm);
		
		buttonsManager = new ButtonsManager(this, texasHoldEm);
		buttonsManager.addChoiseButtons();
		
		winner = new JLabel("", JLabel.CENTER);
		winnerHand = new JLabel("", JLabel.CENTER);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color backgroundColor = Color.WHITE;
		setBackground(backgroundColor);
		backgroundImg.draw(g);
		drawGrayPlayers.draw(g);
		
		dealerIcons.draw(g);
		bigSmallBlinds.draw(g);
		
		allPlayersCards.draw(g);
		drawBurnCards.draw(g);
		
		// FORDEBUG:
//			allPlayersCards.showPlayerCards(g);
		
		if (t != null && t.isTimerRunning()) {
			addCurrentPlayerGreenMark(g, texasHoldEm.getCurrentPlayerIndex());
			t.draw(g);
		}
		drawChancesBar.draw(g);
		
		drawBoardCards.draw(g);
		addPlayersCardsAndWinnerLabel(g);
	}
	
	/**
	 * Adds the winning label.
	 * There, it's written who is the winner player, 
	 * and what is the hand he has.
	 */
	private void addWinnerLbl() {
		
		winner.setText(texasHoldEm.getWinner());
		winner.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
	    winner.setBounds(50, 650, frame.getWidth() - 100, 40);
	    changeWinnerAndWinnerHandLabels(winner);
	    
		winnerHand.setText("With: " + texasHoldEm.getWinnersHand());
		winnerHand.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		winnerHand.setBounds(50, 700, frame.getWidth() - 100, 40);
		changeWinnerAndWinnerHandLabels(winnerHand);
	}
	
	private void changeWinnerAndWinnerHandLabels(JLabel label) {
		Color foregroundColor = new Color(1, 147, 74);
		Color backgroundColor = new Color(214, 237, 254);
		label.setOpaque(true);
		label.setForeground(foregroundColor);
		label.setBackground(backgroundColor);
		add(label);
	}
	
	/**
	 * Adds timer cake for specific player, and resets the timer.
	 * @param playerIndex index of player.
	 */
	public void addTimer(int playerIndex) {
		int x = (int) timerCakeHashMap.getTimerCakeLocation(playerIndex).getX();
		int y = (int) timerCakeHashMap.getTimerCakeLocation(playerIndex).getY();
		if (t == null) {
			t = new TimerCake(x, y, this, texasHoldEm.getPlayer()[playerIndex]);
			t.startTimer();
		} else {
			t.setX(x);
			t.setY(y);
			t.reset(texasHoldEm.getPlayer()[playerIndex]);
		}
	}
	
	/**
	 * Adds all players names labels to the panel.
	 */
	private void addPlayersNames() {
		for (int i = 0; i < TexasHoldEm.NUM_PLAYERS; i++)
			addNameOfPlayers(i);
	}
	
	/**
	 * Adds the name label to specific place, specific label and data of specific player.
	 * @param x Start X of label.
	 * @param y Start Y of label.
	 * @param lbl specific label.
	 * @param i index of player.
	 */
	private void addNameLabel(int x, int y, JLabel lbl, int i) {
		lbl.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
		lbl.setForeground(Color.YELLOW);
		lbl.setBounds(x, y, NAME_WIDTH, NAME_HEIGHT);
		lbl.setText("<html><u>" + texasHoldEm.getPlayer()[i].getName() + "</html></u>");
		add(lbl);
	}
	
	/**
	 * adds name label to specific player.
	 * @param i index of player (0 - 5).
	 */
	public void addNameOfPlayers(int playerIndex) {
		int x = (int) nameLblHashMap.getNameLblLocation(playerIndex).getX();
		int y = (int) nameLblHashMap.getNameLblLocation(playerIndex).getY();
		
		switch (playerIndex) {
			case 0:
				addNameLabel(x, y, myNamelbl, playerIndex);
				myNamelbl.setForeground(new Color(100, 149, 237));
				break;
			case 1:
				addNameLabel(x, y, namelbl1, playerIndex);
				break;
			case 2:
				addNameLabel(x, y, namelbl2, playerIndex);
				break;
			case 3:
				addNameLabel(x, y, namelbl3, playerIndex);
				break;
			case 4:
				addNameLabel(x, y, namelbl4, playerIndex);
				break;
			case 5:
				addNameLabel(x, y, namelbl5, playerIndex);
				break;
				
			default:
				break;
		}
	}
	
	/**
	 * If game is ended, adds the winner labels, and shows all the unfolded players cards.
	 * @param g Graphics g
	 */
	private void addPlayersCardsAndWinnerLabel(Graphics g) {
		if (texasHoldEm.getRound() == 4) {
			addWinnerLbl();
			allPlayersCards.showPlayerCards(g);
		}
	}
	
	/**
	 * adds the green mark on the cards of the current player.
	 * @param g Graphics g
	 * @param i index of the current player.
	 */
	private void addCurrentPlayerGreenMark(Graphics g, int i) {
	    Graphics2D g2 = (Graphics2D) g;
	    int thickness = 3;
	    g2.setStroke(new BasicStroke(thickness));
	    g2.setColor(Color.GREEN);
	    Point p1 = greenRectHashMap.getCardsLocation(i, 0);
	    Point p2 = greenRectHashMap.getCardsLocation(i, 1);
	    if (i == 2 || i == 5) {
		    g2.drawRect((int) p1.getX(), (int) p1.getY(), CARD_HEIGHT, CARD_WIDTH);
		    g2.drawRect((int) p2.getX(), (int) p2.getY(), CARD_HEIGHT, CARD_WIDTH);
	    } else {
		    g2.drawRect((int) p1.getX(), (int) p1.getY(), CARD_WIDTH, CARD_HEIGHT);
		    g2.drawRect((int) p2.getX(), (int) p2.getY(), CARD_WIDTH, CARD_HEIGHT);
	    }
	}
	
	/**
	 * After finishing the current hand,
	 * if the player remains money, create new hand starts in 5 seconds.
	 * Else, create new game for the player, by pressing "New game" button.
	 */
	public void newHand() {
		JLabel newGame = new JLabel();
		newGame.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
		newGame.setForeground(Color.WHITE);
		newGame.setBounds(930, 705, 400, 100);
		newGame.setVisible(true);
		newGame.setBackground(Color.RED);
		add(newGame);
		if (texasHoldEm.getPlayer()[0].getMoney() > 0 && !texasHoldEm.isWholeGameOver()) {
			newGame.setText("Starting new hand...");
			System.out.println("Starting new hand...");
			Timer t = new Timer(5000, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					System.out.println("New hand started.");
					createNewPanel(texasHoldEm.getPlayer());
				}
			});
			
			t.setRepeats(false);
			t.start();
			
		} else {
		    JButton newGameButton = null;
			if (texasHoldEm.getWinnerPlayers().get(0).getId() == 0) {
				newGame.setBounds(680, 705, 580, 100);
				newGame.setText("Congratulations! You won the game!!");
				try {
					newGameButton = new JButton(new ImageIcon(buttonsManager.makePNGButtonsPath("button_start-new-game")));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				newGameButton.setBounds(1050, 620, 200, 50);
			} else {
				try {
					Thread.sleep(1200);
					Sound.playSound("PlayerLoses");
				} catch (IOException | InterruptedException e2) {
					System.out.println("PlayerLoses sound not found.");
				}
				newGame.setText("You Lost !");
				try {
					newGameButton = new JButton(new ImageIcon(buttonsManager.makePNGButtonsPath("button_new-game")));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				newGameButton.setBounds(1090, 725, 180, 50);
			}
			newGameButton.setVisible(true);
			add(newGameButton);
			newGameButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("New game started.");
					createNewPanel(null);
				}
			});
		}
	}
	
	/**
	 * Remove the current panel, creates new Texas holde'm game,
	 * and adds to the frame the new panel of the new game.
	 * @param players array of all the players in the game.
	 */
	private void createNewPanel(Player[] players) {
		frame.remove(pokerPanel);
		frame.repaint();
		final TexasHoldEm texasHoldem;
		try {
			if (players != null)
				texasHoldem = new TexasHoldEm(frame, false, players);
			else
				texasHoldem = new TexasHoldEm(frame, true, null);
			frame.add(texasHoldem.getPokerGame());
			frame.setVisible(true);
			System.out.println("------ Game Loaded Again ------");
			texasHoldem.getPokerGame().buttonsManager.setButtonsEnabled(false);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					texasHoldem.startPlay();
				}
			}).start(); 
		} catch (Exception e1) {
			System.out.println("Error while loading game page.");
			e1.printStackTrace();
		}
	}
	
	/**
	 * @return the status money manager of the panel.
	 */
	public StatusMoneyManager getStatusMoneyManager() {
		return statusMoneyManager;
	}

	/**
	 * @return the buttons manager of the panel.
	 */
	public ButtonsManager getButtonsManager() {
		return buttonsManager;
	}
}
