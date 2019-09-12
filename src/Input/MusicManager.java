package Input;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicManager {
	private InputStream audioFile = getClass().getResourceAsStream("/music/pacman-intro.wav");;
	private AudioInputStream audioStream;
	private AudioFormat format;
	private DataLine.Info info;
	private Clip audioClip;
	private InputStream audioFile2 = getClass().getResourceAsStream("/music/pacman_chomp.wav");;
	private AudioInputStream audioStream2;
	private Clip audioClip2;
	private InputStream audioFile3 = getClass().getResourceAsStream("/music/pacman-intro.wav");
	private AudioInputStream audioStream3;
	private Clip audioClip3;
	private InputStream audioFile4 = getClass().getResourceAsStream("/music/pacman-intro.wav");
	private AudioInputStream audioStream4;
	private Clip audioClip4;
	private boolean NotInitialized=true;
	private boolean Restart=false;
	public MusicManager() {
	}

	public void MusicManager() {

		try {
			if(Restart==true) {
				Game.GameStates.PauseState.count=0;
			NotInitialized=true;
			audioClip.close();	
			audioStream.close();
			audioFile = getClass().getResourceAsStream("/music/pacman-intro.wav");
			audioStream = AudioSystem.getAudioInputStream(audioFile);
			format = audioStream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.open(audioStream);
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);
			
			audioClip2.close();
			audioStream2.close();
			audioFile2 = getClass().getResourceAsStream("/music/pacman_chomp.wav");
			audioStream2 = AudioSystem.getAudioInputStream(audioFile2);
			format = audioStream2.getFormat();
			info = new DataLine.Info(Clip.class, format);
			audioClip2 = (Clip) AudioSystem.getLine(info);
			
			audioClip3.close();
			audioStream3.close();
			audioFile3 = getClass().getResourceAsStream("/music/pacman-intro.wav");
			audioStream3 = AudioSystem.getAudioInputStream(audioFile3);
			format = audioStream3.getFormat();
			info = new DataLine.Info(Clip.class, format);
			audioClip3 = (Clip) AudioSystem.getLine(info);
			
			audioClip4.close();
			audioStream4.close();
			audioFile4 = getClass().getResourceAsStream("/music/pacman-intro.wav");
			audioStream4 = AudioSystem.getAudioInputStream(audioFile4);
			format = audioStream4.getFormat();
			info = new DataLine.Info(Clip.class, format);
			audioClip4 = (Clip) AudioSystem.getLine(info);


		}else {
			audioStream = AudioSystem.getAudioInputStream(audioFile);
			format = audioStream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.open(audioStream);
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);

			audioStream2 = AudioSystem.getAudioInputStream(audioFile2);
			format = audioStream2.getFormat();
			info = new DataLine.Info(Clip.class, format);
			audioClip2 = (Clip) AudioSystem.getLine(info);

			audioStream3 = AudioSystem.getAudioInputStream(audioFile3);
			format = audioStream3.getFormat();
			info = new DataLine.Info(Clip.class, format);
			audioClip3 = (Clip) AudioSystem.getLine(info);
			
			audioStream4 = AudioSystem.getAudioInputStream(audioFile4);
			format = audioStream4.getFormat();
			info = new DataLine.Info(Clip.class, format);
			audioClip4 = (Clip) AudioSystem.getLine(info);


		}} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	public void MusicManagerChangeMenuToGame() {

		try {

			audioClip.stop();
			audioClip2.open(audioStream2);
			audioClip2.loop(Clip.LOOP_CONTINUOUSLY);



		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}





	}
	public void MusicManagerChangeGameToPause() {

		try {

			audioClip2.stop();
			if(NotInitialized==true) {
				audioClip3.open(audioStream3);
				audioClip3.loop(Clip.LOOP_CONTINUOUSLY);
				NotInitialized=false;


			}
			else {
				audioClip3.start();
			}}
		catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}





	}
	public void MusicManagerPauseToMenu() {
		Restart=true;
		MusicManager();
		
	
		






	}
	public void MusicManagerPauseToGame() {

		audioClip3.stop();
		audioClip2.start();





	}






	public void MusicManagerGameToGameOver() {

		try {
			audioClip2.stop();
			audioClip4.open(audioStream4);
			audioClip4.loop(Clip.LOOP_CONTINUOUSLY);


		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}





	}

}
