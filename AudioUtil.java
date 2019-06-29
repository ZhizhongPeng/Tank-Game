package util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import util.AudioUtil;
/**
 * sound effect class
 * 
 */

@SuppressWarnings("deprecation")
public class AudioUtil{
	/**
	 * tank birth music
	 */
	public static final String ADD="audio/add.wav";
	/**
	 * boom music
	 */
	public static final String BLAST="audio/blast.wav";
	/**
	 * bullet music
	 */
	public static final String FIRE="audio/fire.wav";
	/**
	 * game over music
	 */
	public static final String GAMEOVER="audio/gameover.wav";
	/**
	 * bullet hit music
	 */
	public static final String HIT="audio/hit.wav";
	/**
	 * opening music
	 */
	public static final String START="audio/start.wav";
	/**
	 * Get all background music
	 */
	public static List<AudioClip> getAudios(){
		List<AudioClip> audios = new ArrayList<>();
		try {
			audios.add(Applet.newAudioClip(new File(AudioUtil.START).toURL()));
			audios.add(Applet.newAudioClip(new File(AudioUtil.ADD).toURL()));
			audios.add(Applet.newAudioClip(new File(AudioUtil.BLAST).toURL()));
			audios.add(Applet.newAudioClip(new File(AudioUtil.FIRE).toURL()));
			audios.add(Applet.newAudioClip(new File(AudioUtil.HIT).toURL()));
			audios.add(Applet.newAudioClip(new File(AudioUtil.GAMEOVER).toURL()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//audios.add((AudioUtil.class.getResource(AudioUtil.BGM)));
		return audios;
	}
}