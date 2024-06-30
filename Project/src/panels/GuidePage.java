package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import main.Main;

/**
 * Represents the guide panel.
 * There, it's explained how to play Texas hold'm, and all the hands types.
 * @author Naor Anhaisy
 *
 */
@SuppressWarnings("serial")
public class GuidePage extends JPanel {

	private final JFrame frame;
	private JPanel p;
	private JButton previous;
	JScrollPane scrollBar;
	
	/**
	 * Default constructor that build that panel, and adds it's text.
	 * @param frame frame to add the panel to.
	 * @throws IOException if hands types image not found.
	 */
	public GuidePage(JFrame frame) throws IOException {
		this.frame = frame;
		p = this;
		setLayout(null);
		addText();
	}
	
	/**
	 * Adds all guide text, and in the end shows all the hands types.
	 * @throws IOException
	 */
	private void addText() throws IOException {
		
		JPanel txtPanel = new JPanel();
		txtPanel.setLayout(new BoxLayout(txtPanel, BoxLayout.Y_AXIS));
		txtPanel.setBackground(Color.WHITE);
		
		JLabel title = new JLabel("How To Play Poker ?");
		title.setPreferredSize(new Dimension(0, 100));
		title.setFont(new Font("Garamond", Font.BOLD, 70));
		title.setForeground(Color.RED);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		txtPanel.add(title);
		
		JLabel title2 = new JLabel("<html><u>Texas Holde'm Rules<u></html>");
		title2.setPreferredSize(new Dimension(0, 100));
		title2.setFont(new Font("Garamond", Font.BOLD, 55));
		title2.setForeground(Color.RED);
		title2.setHorizontalAlignment(SwingConstants.CENTER);
		txtPanel.add(title2);

		JTextPane pane = new JTextPane();
		pane.setContentType("text/html");
		pane.setEditable(false);
		pane.setPreferredSize(new Dimension(0, 950));
		pane.setForeground(Color.YELLOW);
		pane.setText("<html><p><h1><u>Betting Structures</u></h1></p>"
				+ "<p><h2>One player acts as dealer. This position is called the button and it rotates clockwise after every hand.</h2>"
				+ "</br><h2>The two players to the left of the dealer are called the small blind and the big blind, respectively.</h2>"
				+ "</br><h2>These two positions require forced bets of a pre-determined amount and are the only players to put money in the pot before the cards are dealt (if no ante in place).</h2>"
				+ "</br><h2>Every player then receives two cards face down. These are called “hole” cards.</h2>"
				+ "</br><h2>Once all hole cards have been dealt, the first betting round begins with the player sitting immediately to the left of the big blind. This player can fold, call (match the amount of the big blind) or raise.</h2>"
				+ "</br><h2>Betting then continues clockwise, with each player having the option to fold, call the amount of the highest bet before them, bet or raise.</h2>"
				+ "</br><h2>When the first betting round is completed, three community cards are flipped face up on the table. This is called the flop.</h2>"
				+ "</br><h2>The betting resumes, clockwise, with each player having the option to check (if no bet is in front of them), bet (or raise if a bet is before them), call or fold.</h2>"
				+ "</br><h2>When the second round of betting is finished, a fourth community card is flipped face up on the table. This is called the turn.</h2>"
				+ "</br><h2>The third round of betting commences with the first remaining player sitting to the left of the button.</h2>"
				+ "</br><h2>When the third round of betting is over, a fifth community card is flipped face up on the table. This is called the river.</h2>"
				+ "</br><h2>The fourth round of betting starts with the first remaining player seated to the left of the button. The betting continues to move clockwise.</h2>"
				+ "</p></html>");
		pane.setForeground(Color.RED);
		txtPanel.add(pane);
		
		JTextPane pane2 = new JTextPane();
		pane2.setContentType("text/html");
		pane2.setPreferredSize(new Dimension(0, 550));
		pane2.setText("<html><p><h1><u>The Details</u></h1></p>"
				+ "<p><h2>Now here are some more detailed looks at aspects of Texas Hold’em.</h2>"
				+ "<h2> Dealer, Small Blind and Big Blind A standard hold ’em game showing the position of the blinds relative to the dealer button.Hold ’em is normally played using small and big blinds – forced bets by two players. Antes (forced contributions by all players) may be used in addition to blinds, particularly in later stages of tournaments. "
				+ "<h2> A dealer “button” is used to represent the player in the dealer position; the dealer button rotates clockwise after each hand, changing the position of the dealer and blinds. The small blind is posted by the player to the left of the dealer and is usually equal to half of the big blind. The big blind, posted by the player to the left of the small blind, is equal to the minimum bet. "
				+ "<h2> In tournament poker, the blind/ante structure periodically increases as the tournament progresses. After one round of betting is done, the next betting round will start by the person after the big blind and small blind.When only two players remain, special ‘head-to-head’ or ‘heads up’ rules are enforced and the blinds are posted differently. In this case, the person with the dealer button posts the small blind, "
				+ "<h2> while his/her opponent places the big blind. The dealer acts first before the flop. After the flop, the dealer acts last and continues to do so for the remainder of the hand.</h2>"
				+ "</p></html>");
		//pane2.setText("<html><h2>Hello world</h2><h3>It's a beautiful day</h3></html>");
		txtPanel.add(pane2);
		
		JTextPane pane4 = new JTextPane();
		pane4.setContentType("text/html");
		pane4.setPreferredSize(new Dimension(0, 700));
		pane4.setText("<html><p><h1><u>Play of the Hold’em hand</u></h1></p>"
				+ "<p><h2>Each player is dealt two private cards in hold ’em, which are dealt first. Play begins with each player being dealt two cards face down, with the player in the small blind receiving the first card and the player in the button seat receiving the last card dealt. (As in most poker games, the deck is a standard 52-card deck containing no jokers.) These cards are the players’ hole or pocket cards. These are the only cards each player will receive individually, and they will only (possibly) be revealed at the showdown.</h2>"
				+ "<h2>The poker hand begins with a “pre-flop” betting round, beginning with the player to the left of the big blind (or the player to the left of the dealer, if no blinds are used) and continuing clockwise. A round of betting continues until every player has folded, put in all of their chips, or matched the amount put in by all other active players. Note that the blinds in the pre-flop betting round are counted toward the amount that the blind player must contribute. If all players call around to the player in the big blind position, that player may either check or raise.</h2>"
				+ "<h2>After the pre-flop betting round, assuming there remain at least two players taking part in the hand, the dealer deals a flop, three face-up community cards. The flop is followed by a second betting round. All betting rounds begin with the player to the button’s left and continue clockwise. After the flop betting round ends, a single community card (called the turn or fourth street) is dealt, followed by a third betting round. A final single community card (called the river or fifth street) is then dealt, followed by a fourth betting round and the showdown, if necessary</h2>"
				+ "<h2>In all casinos, the dealer will “burn” a card before the flop, turn, and river. The burn occurs so players who are betting cannot see the back of the next community card to come. This is done for historical/traditional reasons, to avoid any possibility of a player knowing in advance the next card to be dealt.</h2></p></html>");
		txtPanel.add(pane4);
		
		JTextPane pane5 = new JTextPane();
		pane5.setContentType("text/html");
		pane5.setPreferredSize(new Dimension(0, 400));
		pane5.setText("<html><p><h1><u>The Showdown</u></h1></p>"
				+ "<p><h2>If a player bets and all other players fold, then the remaining player is awarded the pot and is not required to show his hole cards.</h2>"
				+ "<h2>If two or more players remain after the final betting round, a showdown occurs. On the showdown, each player plays the best poker hand they can make from the seven cards comprising his two hole cards and the five community cards. A player may use both of his own two hole cards, only one, or none at all, to form his final five-card hand. If the five community cards form the player’s best hand, then the player is said to be playing the board and can only hope to split the pot, because each other player can also use the same five cards to construct the same hand.</h2></p></html>");
		txtPanel.add(pane5);
		
		
		JTextPane pane3 = new JTextPane();
		pane3.setContentType("text/html");
		pane3.setPreferredSize(new Dimension(0, 100));
		pane3.setText("<html><p><h1><u>Hand's Types</u></h1></p></html>");
		txtPanel.add(pane3);
		
		BufferedImage myPicture = ImageIO.read(new File("src/img/hands.png"));
		//Image resized =  myPicture.getScaledInstance( 500, 500, Image.SCALE_SMOOTH);
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		txtPanel.add(picLabel);
		
		JTextPane margin = new JTextPane();
		margin.setContentType("text/html");
		margin.setEditable(false);
		margin.setPreferredSize(new Dimension(0, 100));
		margin.setText("<html><p></p></html>");
		txtPanel.add(margin);
		previous = new JButton("Back to start page");
		previous.setFont(new Font("Sherif", Font.ITALIC, 30));
		previous.setForeground(Color.RED);
		previous.setBorder(null);
		//previous.setContentAreaFilled(false);
		previous.setOpaque(false);
		txtPanel.add(previous);
		
		previous.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("go to start page...");
				frame.remove(p);
				frame.repaint();
				StartPage formPanel;
				try {
					frame.setSize(Main.START_PAGE_WIDTH, Main.START_PAGE_HEIGHT);
					formPanel = new StartPage(frame);
					frame.setTitle("Poker Start Page");
					frame.add(formPanel);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					System.out.println("------ Start Page Loaded ------");
				} catch (Exception e1) {
					System.out.println("Error while loading start page.");
					e1.printStackTrace();
				}
				
			}
		});
		
		add(txtPanel);
		
		scrollBar = new JScrollPane(txtPanel);
		scrollBar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollBar.getVerticalScrollBar().setUnitIncrement(16);
		scrollBar.setBounds(0, 0, 790, 1000);
		add(scrollBar);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
		   public void run() { 
		       scrollBar.getVerticalScrollBar().setValue(0);
		   }
		});
	}
}
