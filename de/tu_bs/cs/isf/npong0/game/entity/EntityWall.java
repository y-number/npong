package de.tu_bs.cs.isf.npong0.game.entity;

import de.tu_bs.cs.isf.npong0.game.Game;

/**
 * <p>Klasse der Wall Entit�ten
 * <p>Diese Klasse stellt die W�lle welche die Ecken des n-gons verbinden und
 * zur Registrierung von Punkten dienen sollen zur Verf�gung.  Nicht final.
 * 
 * @author David Hellmers
 * @author Marc Filbert
 * @version Alpha 0.1.0.0001
 */
public class EntityWall extends Entity {
	
    /** Sagt irgendwas aus...�ber Kollisionen, wahrscheinlich der letzten. */
    public long lastCollision;
    /** Die laufende Runde. */
    private Game runningGame;
	
    /**
     * <p>Beinhaltet alle wichtigen Attribute des Walls, wie
     * etwa Geschwindigkeit, anf�ngliche Position und generelle Bewegung.
     * Die Position sollte dabei fix sein und die Geschwindigkeit 0 betragen.
     * 
     * @param game Das laufende Spiel
     * @param posX Die X Position des Walls
     * @param posY Die Y Position des Walls
     */
	public EntityWall(Game game, double posX, double posY) {
		super(game);
		runningGame = game;
		this.speed = 0.0;
		this.posX = posX;
		this.posY = posY;
		this.lastCollision = 0;
		
	}
	
    /**
     * <p>Bestimmt das Verhalten des Walls im Fall
     * einer Kollision mit einem anderen Objekt
     * (H�chstwahrscheinlich dem Ball wenn alles glatt l�uft)
     * und ist f�r das Rendern des Objektes verantwortlich.
     * 
     * @param game Das laufende Spiel
     */
	@Override
	public void onUpdate (Game game) {
		
		super.onUpdate(game);
		handleCollisions(game);
		
	}

    /**
     * <p>Behandelt die Kollisionserkennung.
     */
    private void handleCollisions(Game game) {
        // TODO Auto-generated method stub
	}
}
