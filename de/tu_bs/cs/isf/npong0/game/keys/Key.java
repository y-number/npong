package de.tu_bs.cs.isf.npong0.game.keys;

/**
 * Die Klasse Key, verarbeitet Inputs.
 * 
 * @author David Hellmers
 * @version Alpha 0.1.0.0001
 */
public enum Key {
	RIGHT, LEFT, NONE;
	
	/**
	 * Mappt einen eingegeben int auf den zugehoerige Key
	 * @param value Der Eingabewert
	 * @return RIGHT, LEFT oder NONE
	 */
	public static Key getKey(int value) {
		switch (value) {
			case 0:
				return RIGHT;
			case 1:
				return LEFT;
			default:
				return NONE; 
		}
	}
}
