package de.tu_bs.cs.isf.npong0.game.entity;

import de.tu_bs.cs.isf.npong0.game.Game;
import de.tu_bs.cs.isf.npong0.game.server.Connection;

/**
 * <p>Provisorische Klasse zur Behandlung der Entitäten zur Anzeige des Punktestandes. 
 * <p>Wird sobald fertiggestellt die Punktestände darstellen
 * Nicht final.
 * @author Marc Filbert
 * @version Alpha 0.1.0.0001
 */

public class EntityScore extends Entity {

    /** Die laufende Runde. */
    private Game runningGame;	
	 
    /**
     * <p>Beinhaltet alle wichtigen Attribute des Scores, wie
     * etwa Position auf dem Bildschirm und die angezeight Punktezahl
     * Die Position sollte dabei fix sein und die Geschwindigkeit 0 betragen.
	 *   @param game Das laufende Spiel
	 *   @param posX Die X Position der Punktestandsanzeige
	 *   @param posY Die Y Position der Punktestzandsanzeige
	 *   @param points die Punkte aus der Connection Methode  
	 *   
     */

    public EntityScore(Game game, double posX, double posY, int points) {
        super(game);
        runningGame = game;
		this.speed = 0.0;
        this.posX = posX;
        this.posY = posY;
        this.score_points = getScore();
        
    }
    
    /**
     * Erlaubt den Zugriff auf den aktuellen Punktestand des jeweiligen Spielers.
     * 
     * @return Punktestand der Spieler aus der Connection Methode
     */
    public int getScore() {
        
        return Connection.getPoints();
    }
    
    /**
     * <p>Bestimmt das Verhalten der Punktestandanzeige im Fall
     * einer Kollision mit einem anderen Objekt
     * (Bestenfalls überhaupt keinem)
     * und ist für das Rendern des Objektes verantwortlich.
     * 
     * @param game das laufende Spiel
     */
	@Override
	public void onUpdate (Game game) {
		
		super.onUpdate(game);
		
	}

}
