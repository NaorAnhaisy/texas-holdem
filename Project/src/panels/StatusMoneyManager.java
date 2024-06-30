package panels;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import hashMapPack.NameLblHashMap;
import javaPoker.TexasHoldEm;
import other.StaticFunctions;

/**
 * Represents the manager of the status and money labels of each player.
 * @author Naor Anhaisy
 *
 */
public class StatusMoneyManager {
	
	private static final int MONEY_OFFSET_FROM_NAME_LOCATION = 40;
	private static final int STATUS_OFFSET_FROM_NAME_LOCATION = 50;
	private static final Color CALL_CHECK_COLOR = new Color(0, 187, 243);
	private static final Color ALL_IN_COLOR = new Color(0, 153, 0);
	private static final int STATUS_WIDTH = 160, STATUS_HEIGHT = 40;
	private static final int MONEY_WIDTH = 400, MONEY_HEIGHT = 100;
	
	private NameLblHashMap nameLblHashMap;
	private TexasHoldEm texasHoldEm;
	private JPanel mainPanel;
	
	public JLabel myStatuslbl;
	private JLabel statuslbl1;
	private JLabel statuslbl2;
	private JLabel statuslbl3;
	private JLabel statuslbl4;
	private JLabel statuslbl5;
	private List<JLabel> statusLabels;
	
	private JLabel myMoneylbl;
	private JLabel moneylbl1;
	private JLabel moneylbl2;
	private JLabel moneylbl3;
	private JLabel moneylbl4;
	private JLabel moneylbl5;
	private List<JLabel> moneyLabels;

	private JLabel sumTableMoney;
	
	/**
	 * Initialize all the money and status labels, and adds the "MONEY ON TABLE"
	 * and the name of each player.
	 */
	public StatusMoneyManager(JPanel mainPanel, TexasHoldEm texasHoldEm) {
		
		this.mainPanel = mainPanel;
		this.texasHoldEm = texasHoldEm;
		nameLblHashMap = new NameLblHashMap();
		addSumtableMoneylbl();
		
		myMoneylbl = new JLabel();
		moneylbl1 = new JLabel();
		moneylbl2 = new JLabel();
		moneylbl3 = new JLabel();
		moneylbl4 = new JLabel();
		moneylbl5 = new JLabel();
		moneyLabels = Arrays.asList(myMoneylbl, moneylbl1, moneylbl2, moneylbl3, moneylbl4, moneylbl5);
		addAllPlayersMoney();
		
		myStatuslbl = new JLabel();
		statuslbl1 = new JLabel();
		statuslbl2 = new JLabel();
		statuslbl3 = new JLabel();
		statuslbl4 = new JLabel();
		statuslbl5 = new JLabel();
		statusLabels = Arrays.asList(myStatuslbl, statuslbl1, statuslbl2, statuslbl3, statuslbl4, statuslbl5);
	}
	
	/**
	 * Adds the "MONEY ON TABLE" label, that represents the
	 * amount of money the players play on.
	 * The winner gets it all.
	 */
	private void addSumtableMoneylbl() {
		sumTableMoney = new JLabel();
		sumTableMoney.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		sumTableMoney.setForeground(new Color(0, 153, 76));
		sumTableMoney.setBounds(530, 0, 400, 60);
		mainPanel.add(sumTableMoney);
	}
	
	/**
	 * updates the "MONEY ON TABLE" label, each round.
	 */
	public void updateSumBetOnTableLabel() {
		sumTableMoney.setText("MONEY ON TABLE: " + texasHoldEm.getSumBetOnTable() + " $");
	}
	
	
	/**
	 * Play for a player, and update his status label
	 * @param choise String of the player choice : "Fold", "Call", "Raise", "Check", "Out" or "ALL IN".
	 * @param bet bet of the player. if fold, x is 0.
	 * @param playerIndex index of the player.
	 */
	public void play(String choise, int bet, int playerIndex) {
		addStatusOfPlayers(playerIndex, choise);
		addMoneyLabelToPlayer(playerIndex);
		updateStatusLabel(statusLabels.get(playerIndex), choise, bet);
	}
	
	/**
	 * puts bounds to status label to specific player, and give the label a
	 * specific color by the stringStat given.
	 * @param i index of player
	 * @param stringStat status of action (Raise, Fold, Call, ALL IN or Not in game).
	 */
	public void addStatusOfPlayers(int i, String stringStat) {
		int x = (int) nameLblHashMap.getNameLblLocation(i).getX();
		int y = (int) nameLblHashMap.getNameLblLocation(i).getY() + STATUS_OFFSET_FROM_NAME_LOCATION;
		addStatusOfPlayer(x, y, statusLabels.get(i), i, stringStat);	
	}
	
	/**
	 * adds status label (Raise, All In, Call or Fold) in specific place, to specific player.
	 * each action has different foreground color.
	 * @param x Start X of label
	 * @param y Start Y of label
	 * @param lbl specific label to insert into
	 * @param playerIndex index of the player.
	 * @param stringStat string to describe the action the player chose.
	 */
	private void addStatusOfPlayer(int x, int y, JLabel lbl, int playerIndex, String stringStat) {
		lbl.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		if (stringStat.equals("Raise"))
			lbl.setForeground(Color.RED);
		else if (stringStat.equals("ALL IN"))
			lbl.setForeground(ALL_IN_COLOR);
		else if (stringStat.equals("Call") || stringStat.equals("Check"))
			lbl.setForeground(CALL_CHECK_COLOR);
		else if (stringStat.equals("Fold"))
			lbl.setForeground(Color.WHITE);
		else
			lbl.setForeground(Color.GRAY);
		lbl.setBounds(x, y, STATUS_WIDTH, STATUS_HEIGHT);
		lbl.setBackground(Color.WHITE);
		lbl.setVisible(true);
		mainPanel.add(lbl);
	}
	
	/**
	 * Updates a status label of player.
	 * @param lbl status label to update.
	 * @param s String of the action of the player.
	 * @param x money to bet. Matters only when s is not "Fold", "Out" or "Check".
	 */
	private void updateStatusLabel(JLabel lbl, String s, int x) {
		if (!s.equals("Fold") && !s.equals("Out") && !s.equals("Check"))
			lbl.setText(s + ": " + StaticFunctions.returnBigNumbersInShortcat(x) + " $");
		else 
			lbl.setText(s);
	}
	
	/**
	 * Adds money labels to all the players.
	 */
	public void addAllPlayersMoney() {
		for (int i = 0; i < TexasHoldEm.NUM_PLAYERS; i++) {
			addMoneyLabelToPlayer(i);
		}
	}
	
	/**
	 * Changes the visibility of the status labels of all the players 
	 * who still can change whether they in the game or not,
	 * such as if their last action was : Raise, Check or Call.
	 * For example, player who fold in the last action or all in,
	 * can not change his actions in the current hand.
	 * @param visibility true if to change to show, false to disappear the
	 * status labels.
	 */
	public void setAllChangingPlayersStatusLabelsVisible(boolean visibility) {
		for (JLabel statusLabel : statusLabels) {
			if (statusLabel.getText().contains(TexasHoldEm.RAISE) || statusLabel.getText() == TexasHoldEm.CHECK
					|| statusLabel.getText().contains(TexasHoldEm.CALL)) {
				statusLabel.setVisible(visibility);
			}
		}
		mainPanel.repaint();
	}
	
	/**
	 * Adds the money label of specific player, with the current amount of money he has.
	 * @param playerIndex index of the player.
	 */
	public void addMoneyLabelToPlayer(int playerIndex) {
		JLabel lbl = moneyLabels.get(playerIndex);
		int x = (int) nameLblHashMap.getNameLblLocation(playerIndex).getX();
		int y = (int) nameLblHashMap.getNameLblLocation(playerIndex).getY() + MONEY_OFFSET_FROM_NAME_LOCATION;
		lbl.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		lbl.setForeground(Color.YELLOW);
		lbl.setBounds(x, y, MONEY_WIDTH, MONEY_HEIGHT);
		lbl.setText("Money: " + StaticFunctions.returnBigNumbersInShortcat(texasHoldEm.getPlayer()[playerIndex].getMoney()) + " $");
		mainPanel.add(lbl);
	}
}
