package main;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import panels.StartPage;

public class Main {
	public static final int START_PAGE_HEIGHT = 650;
	public static final int START_PAGE_WIDTH = 474;
	
	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame("Texas hold'em Start Page");
		JPanel formPanel = new StartPage(frame);
		frame.add(formPanel); 
	    frame.setIconImage(ImageIO.read(new File("src/img/texas-holdem-logo.png")));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(START_PAGE_WIDTH, START_PAGE_HEIGHT);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		System.out.println("Followed by the game's documentation:");
	}
}
