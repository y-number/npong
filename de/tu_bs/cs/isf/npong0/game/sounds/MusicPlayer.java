package de.tu_bs.cs.isf.npong0.game.sounds;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

import de.tu_bs.cs.isf.npong0.game.Game;

/**
 * <p>Klasse zum abspielen von Sounds aller Art.
 * 
 * @author David Hellmers
 * @version Alpha 0.1.0.0001
 */

public class MusicPlayer implements Runnable {

    /** Der Name der abzuspielenden Musikdatei. */
	private String fileName;
	/** Der AudioClip, mit dem die Musikdatei abgespielt wird. */
	private AudioClip player;
	/** Die URL einer Resource. */
	private URL resource;
	/** Der aktive Modus des Musikplayers. (Einmal oder Loop) */
	private int mode;
	/** Variable die aussagt ob der Musicplayer aktiv ist oder nicht. */
	private boolean running = false;
	/** Die Länge eines Sounds in Millisekunden. */
	private long trackLength;
	
	/**
	 * Erstellt einen MusicPlayer, welcher bei Bedarf in einem Thread gestartet werden kann.
	 * 
	 * @param fileName Der Name der abzuspielenden Musikdatei.
	 * @param mode Der aktive Modus des Musikplayers.
	 * @param trackLength Die Länge eines Sounds.
	 */
	public MusicPlayer(String fileName, int mode, long trackLength) {
		this.fileName = fileName;
		resource = MusicPlayer.class.getResource(fileName);
		player = Applet.newAudioClip(resource);
		this.mode = mode;
		this.trackLength = trackLength;
	}

	/**
	 * Gitb wieder ob der MusicPlayer aktiv ist.
	 * @return running true, wenn der MusicPlayer aktiv ist.
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Startet den MusicPlayer.
	 */
	@Override
	public void run() {
		running = true;
		switch (mode) {
			case 0:
				player.play();
				break;
			case 1:
				loop: while (running) {
					player.play();
					try {
						Thread.sleep(trackLength);
					} catch (InterruptedException e) {
						running = false;
						break loop;
					}
				}
				break;
		}
		player.stop();
		running = false;
	}
}
