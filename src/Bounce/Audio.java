/*
 * Audio.java
 *
 * Created on November 8, 2004, 3:59 PM
 */

package Bounce;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {
	private static final String audioDirectory = "C:/Users/sreno/eclipse-workspace/Bounce/src/audio/";
	private static HashMap<String, Clip> clips = new HashMap<String, Clip>();
	private static HashMap<String, String> fileNames = new HashMap<String, String>() {
		{
			put("photon", "photon.wav");
			put("die", "die.wav");
			put("playerHit", "shock.wav");
			put("tileHit", "tileFiz.wav");
			put("shot", "shot.wav");
		}
	};

	private static Clip clip;

	private Audio() {
	}

	public static void loadClips() {
		for (String key : fileNames.keySet()) {
			clips.put(key, loadAndGetClip(fileNames.get(key)));
		}
	}

	private static Clip loadAndGetClip(String file) {
		try {

			// From file
			String fullPath = audioDirectory + file;
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(fullPath));

			// At present, ALAW and ULAW encodings must be converted
			// to PCM_SIGNED before it can be played
			AudioFormat format = stream.getFormat();
			if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
				format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(),
						format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2,
						format.getFrameRate(), true); // big endian
				stream = AudioSystem.getAudioInputStream(format, stream);
			}

			// Create the clip
			DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(),
					((int) stream.getFrameLength() * format.getFrameSize()));
			Clip clip = (Clip) AudioSystem.getLine(info);

			// This method does not return until the audio file is completely loaded
			clip.open(stream);
			return clip;

		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (LineUnavailableException e) {
			System.out.println(e.getMessage());
		} catch (UnsupportedAudioFileException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static void playSound() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				// if the clip is running then stop it
				if (!clip.isRunning()) {
					clip.stop();
				}
				// reset the clip and start it
				clip.setFramePosition(0);
				clip.start();
			}
		});
		t.start();
	}

	public static void playClip(String key) {
		if (clips.containsKey(key)) {
			// set the active clip
			clip = clips.get(key);
			if (clip != null) {
				Thread t = new Thread(new Runnable() {
					public void run() {
						// if the clip is running then stop it
						if (!clip.isRunning()) {
							clip.stop();
						}
						// reset the clip and start it
						clip.setFramePosition(0);
						clip.start();
					}
				});
				t.start();
			}
		}
	}
}
