package panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import graphicsDrawImg.ConfettiManager;
import hashMapPack.ImageBank;
import javaPoker.TexasHoldEm;
import other.Sound;

/**
 * Represents the Start Page panel.
 * @author Naor Anhaisy
 *
 */
@SuppressWarnings("serial")
public class StartPage extends JPanel {
	
	private static final int TIME_BETWEEN_SWITCH_IMAGE = 7;
	private static final int POKER_GAME_HEIGHT = 810;
	private static final int POKER_GAME_WIDTH = 1300;
	private static final int GUIDE_PAGE_WIDTH = 800;
	private static final int NUMBER_OF_IMAGES = 3;
	private static final int BUTTONS_FONT = 18;
	public static String name = "";
	private final JFrame frame;
	private JPanel p;
	private JButton playButton, guide, soundOn, soundOff;
	private JLabel addNameLbl, msgLbl;
	private JTextField nickNameTxt;
	private BufferedImage image;
	private BufferedImage [] images = new BufferedImage[3];
	private int numOfImg;
	
	private ConfettiManager konfatiManager;
	
	/**
	 * Create new Panel, and initialize all it's components.
	 * @param frame frame to add the panel to.
	 * @throws IOException if any image not found.
	 */
	public StartPage(JFrame frame) throws IOException {
		this.frame = frame;
		p = this;
		setLayout(null);
		setBackground(Color.BLACK);
		
		initaliseImages();
	    switchImages();
	    
	    addAnimation();
	    createSoundButtons();
	    addNickName();
	    addButtons();
	}
	
	/**
	 * Adds the background animation of the konfatis
	 */
	private void addAnimation() {
		konfatiManager = new ConfettiManager(this);
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				while (true) {
					konfatiManager.timePassed();
				}
			}
		};
		Thread animationThread = new Thread(runnable);
		animationThread.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, frame.getWidth(), frame.getHeight() - 100, null);
		g.drawImage(images[numOfImg], 0, 0, frame.getWidth(), 350, null);
		konfatiManager.draw(g);
	}
	
	/**
	 * Initialize all the background images of the page.
	 * @throws IOException if any of the background images not found.
	 */
	private void initaliseImages() throws IOException {
		numOfImg = 0;
		String path = "src/img/pokerStartPage.png";
		image = ImageBank.getImage(path);
	    
	    for (int i = 1; i <= NUMBER_OF_IMAGES; i++) {
	    	path = "src/img/pic" + i + ".png";
	    	images[i - 1] = ImageBank.getImage(path);
	    }
	}
	
	/**
	 * Activates thread, that includes timer, that each SWITCH_IMAGE_TIMER time
	 * switch the background image, so it's looks flickering.
	 */
	private void switchImages() {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
					
					Timer timer = new Timer(TIME_BETWEEN_SWITCH_IMAGE * 100, new ActionListener() {
		                @Override
		                public void actionPerformed(ActionEvent e) {
		                	numOfImg = (numOfImg + 1) % NUMBER_OF_IMAGES;
		                    repaint();
		                }
		            });
		            timer.setRepeats(true);
		            timer.start();
				}
		});
		thread.start();
	}
	
	/**
	 * Create sound buttons - soundOn and soundOff.
	 * So, it will be able to cancel and active the sound option.
	 * @throws IOException if sound on or sound off images not found.
	 */
	private void createSoundButtons() throws IOException {
	    soundOn = new JButton(new ImageIcon(ImageIO.read(new File("src/img/1.png"))));
		soundOff = new JButton(new ImageIcon(ImageIO.read(new File("src/img/2.png"))));
		
	    changeSoundButtons(soundOn);
	    changeSoundButtons(soundOff);
	    
	    soundOff.setVisible(false);
	    add(soundOn);
		add(soundOff);
		
		soundOn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Sound.isSoundOn = false;
				System.out.println("isSoundOn: " + Sound.isSoundOn);
				soundOn.setVisible(false);
				soundOff.setVisible(true);
			}
		});
		
		soundOff.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Sound.isSoundOn = true;
				System.out.println("isSoundOn: " + Sound.isSoundOn);
				soundOn.setVisible(true);
				soundOff.setVisible(false);
			}
		});
	}
	
	
	/**
	 * Change sound buttons - cancel their border, cancel their content area filled
	 * cancel their opaque and initialize their location and size.
	 * @param sound the button to change.
	 */
	private void changeSoundButtons(JButton sound) {
		sound.setBorder(null);
		sound.setContentAreaFilled(false);
		sound.setOpaque(false);
		sound.setBounds(40, 560, 50, 50);
	}
	
	/**
	 * Adds the name and massage labels, and the name's text field.
	 */
	private void addNickName() {
		addNameLbl = new JLabel("<html><u>Enter your nickname:</html></u> ");
		addNameLbl.setFont(new Font("Garamond", Font.BOLD, 25));
		addNameLbl.setBounds(10, 500, 300, 30);
		addNameLbl.setForeground(new Color(0, 200, 0));
		add(addNameLbl);
		
		nickNameTxt = new JTextField();
		nickNameTxt.setFont(new Font("Garamond", Font.BOLD, 30));
		nickNameTxt.setBounds(250, 495, 200, 40);
		nickNameTxt.setForeground(new Color(230, 0, 0));
		nickNameTxt.setOpaque(false);
		nickNameTxt.setBorder(null);
		nickNameTxt.setDocument(new JTextFieldLimit(12));
		add(nickNameTxt);
		
		msgLbl = new JLabel("(Enter 1 ~ 12 letters)");
		msgLbl.setFont(new Font("Garamond", Font.BOLD, 15));
		msgLbl.setBounds(50, 520, 300, 30);
		msgLbl.setForeground(new Color(0, 200, 0));
		add(msgLbl);
	}
	
	/**
	 * Adds the "Play now" and "How to play" buttons, and adds their action listener.
	 */
	private void addButtons() {
		playButton = new JButton("Play Now");
		guide = new JButton("How to Play ?");
		
		changeButtons(playButton);
		changeButtons(guide);
		
		playButton.setFont(new Font("verdana", Font.ITALIC, BUTTONS_FONT));
		playButton.setBounds(130, 560, 130, 50);
		
		guide.setFont(new Font("verdana", Font.ITALIC, BUTTONS_FONT));
		guide.setBounds(270, 560, 180, 50);
		
		add(playButton);
		add(guide);
		
		guide.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("go to guide...");
				frame.remove(p);
				GuidePage guidePanel;
				try {
					guidePanel = new GuidePage(frame);
					frame.add(guidePanel);
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					frame.setTitle("Guide - How To Play Texas Holde'm ?");
					frame.setSize(GUIDE_PAGE_WIDTH, (int) screenSize.getHeight());
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					System.out.println("------ Guide Loaded ------");
				} catch (Exception e1) {
					System.out.println("Error while loading guide page.");
					e1.printStackTrace();
				}
			}
		});
		
		playButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// FORDEBUG
				name = nickNameTxt.getText();
//				name = "Naor";
				if (!name.equals("")) {
					frame.remove(p);
					frame.repaint();
					final TexasHoldEm texasHoldem;
					try {
						texasHoldem = new TexasHoldEm(frame, true, null);
						frame.setTitle("Texas Hold'em Poker");
						frame.add(texasHoldem.getPokerGame());
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						frame.setSize(POKER_GAME_WIDTH, POKER_GAME_HEIGHT);
						frame.setLocationRelativeTo(null);
						frame.setVisible(true);
						System.out.println("------ Game Loaded ------");
						texasHoldem.getPokerGame().getButtonsManager().setButtonsEnabled(false);
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
				} else  {
					Runnable r = new Runnable() {
						
						@Override
						public void run() {
							msgLbl.setForeground(Color.RED);
							msgLbl.setFont(new Font("Garamond", Font.BOLD, 18));
							Timer timer = new Timer(800, new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent arg0) {
									msgLbl.setForeground(new Color(0, 200, 0));
									msgLbl.setFont(new Font("Garamond", Font.BOLD, 15));
								}
							});
							timer.setRepeats(false);
							timer.start();
						}
					};
					r.run();
				}
			}
		});
	}
	
	/**
	 * Create Mouse Adapter for the "Play button" and "How to play" buttons.
	 * @return Mouse Adapter of the "Play button" and "How to play" buttons.
	 */
	private MouseAdapter createMouseOver() {
		return new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				((Component) e.getSource()).setFont(new Font("verdana", Font.BOLD, BUTTONS_FONT));
				super.mouseEntered(e);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				((Component)e.getSource()).setFont(new Font("verdana", Font.ITALIC, BUTTONS_FONT));
				super.mouseExited(e);
			}
			
		};
	}
	
	/**
	 * Change the appearance of the "Play now" and "How to play" buttons.
	 * @param button button to change.
	 */
	private void changeButtons(JButton button) {
		button.setForeground(Color.RED);
		button.setFocusPainted(false);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.addMouseListener(createMouseOver());
	}
}

/**
 * Limits the JTextField's number of characters.
 * @author Naor
 *
 */
@SuppressWarnings("serial")
class JTextFieldLimit extends PlainDocument {
	
	private int limit;
	
	/**
	 * Initialize the text field limit to be the limit parameter.
	 * @param limit number of characters allowed to enter the text field.
	 */
	JTextFieldLimit(int limit) {
		super();
	    this.limit = limit;
	}
	
	/**
	 * Responsible to make sure in each insert to the string that the number of characters
	 * is not above the limit. if it does, it won't insert the char to the string.
	 */
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;
		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}
