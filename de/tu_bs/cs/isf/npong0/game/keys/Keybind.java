package de.tu_bs.cs.isf.npong0.game.keys;

import org.lwjgl.input.Keyboard;
import de.tu_bs.cs.isf.npong0.game.server.Connection;

/**
 * Klasse zur Verarbeitung von Keyinputs
 * 
 * @author David Hellmers
 * @version Alpha 0.1.0.0001
 */

public class Keybind {
    
    /** Der Code eines Keys */
	public Key keyCode;
    /** Die Beschreibung eines Keys */
	public String description;
    /** Der Status eines Keys */
	public boolean[] keyStates;
    /** Die Connection eines spezifischen Controllers */
	private Connection controller;
	
	/**
	 * Erstellt einen Keybind, welcher dann abgehorcht werden kann.
	 * @param keyCode Der Code des Keys
	 * @param description Die Beschreibung des Keys
	 * @param controller Der betreffende Controller
	 */
	public Keybind (Key keyCode, String description, Connection controller) {
		
		this.keyCode = keyCode;
		this.description = description;
		this.keyStates = new boolean[256];
		this.controller = controller;
	}

	/**
	 * Checkt ob ein Key gedrückt wurde
	 * @param i Ein Key
	 * @return den Status des Keys oder false
	 */
	private boolean checkKey(int i) {
		
		if (Keyboard.isKeyDown(i) != this.keyStates[i]) {
			return this.keyStates[i] != this.keyStates[i];
		} else {
			return false;
		}
	}
	
	/**
	 * Checkt ob ein Key gedrückt wird
	 * @return Der KEycode des betätigten keys
	 */
	public boolean isKeyDown() {
		
		return controller.isKeyDown(keyCode);
	}
	
//	public boolean isKeyPressed() {
//		return this.checkKey(this.keyCode);
//	}
	

}
