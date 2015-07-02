package de.tu_bs.cs.isf.npong0.game.keys;

/**
 * Klasse zum Verwalten der verschiedenen KeyActions.
 * 
 * @author David Hellmers
 * @version Alpha 0.1.0.0001
 */

public enum KeyAction {
	RIGHTPRESSED, RIGHTRELEASED, LEFTPRESSED, LEFTRELEASED, EXIT, NONE;
	
	/**
	 * Mappt einen gegebenen int auf die zugehoerige KeyAction
	 * @param value Der Eingabewert
	 * @return Die zugehoerige KeyAction
	 */
	public static KeyAction getKey(int value) {
		switch (value) {
			case 0:
				return RIGHTPRESSED;
			case 1:
				return RIGHTRELEASED;
			case 2:
				return LEFTPRESSED;
			case 3:
				return LEFTRELEASED;
			case 4:
				return EXIT;
			default:
				return NONE;
		}
	}
}
