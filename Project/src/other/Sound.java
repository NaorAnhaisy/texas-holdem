package other;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public abstract class Sound {
	
	public static boolean isSoundOn = true;
	
	/**
	 * create an audio stream from the input stream
	 * play the audio clip with the audio player class
	 * @param string name in sounds folder of the specific sound.
	 * @throws IOException if sound wasn't found
	 */
	public static void playSound(String soundName) throws IOException {
		if (isSoundOn) {
			String filepath = "src/sound/" + soundName + ".wav";
		    InputStream in = new FileInputStream(filepath);
		    AudioStream audioStream = new AudioStream(in);
		    AudioPlayer.player.start(audioStream);
		}
	}
	
	/**
	 * Plays in sound what is the winning hand.
	 * @param winType the winning type, as it is in HandEval.java class.
	 */
	public static void playSoundOfWinType(int winType) {
		try {
			switch (winType) {
			case 1:
				playSound("WinHighCard");
				break;
			case 2:
				playSound("WinPair");
				break;
			case 3:
				playSound("WinTwoPair");
				break;
			case 4:
				playSound("WinThreeOfKind");
				break;
			case 5:
				playSound("WinStraight");
				break;
			case 6:
				playSound("WinFlush");
				break;
			case 7:
				playSound("WinFullHouse");
				break;
			case 8:
				playSound("WinFourOfKind");
				break;
			case 9:
				playSound("WinStraightFlush");
				break;
			case 10:
				System.out.println("Unforunatlly, no sound for royal flash is exist.");
				break;
			default:
				break;
			}
			
		} catch (IOException e) {
			System.out.println("Win sound of winning type number " + winType + " is not found.");
		}
	}

}
