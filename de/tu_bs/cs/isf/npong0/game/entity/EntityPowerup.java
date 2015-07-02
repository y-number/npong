package de.tu_bs.cs.isf.npong0.game.entity;

import de.tu_bs.cs.isf.npong0.game.Game;

/**
 * <p>Provisorische Klasse zur Behandlung der Powerup Entitäten. 
 * <p>Wird sobald fertiggestellt alle wichtigen Attribute der 
 * Powerups festlegen. Nicht final.
 * @author Marc Filbert
 * @version Alpha 0.1.1.0003
 */
public class EntityPowerup extends Entity {
    
    /** Sagt irgendwas aus...über Kollisionen, wahrscheinlich der letzten. */
    public long lastCollision;
    /** Die laufende Runde. */
    private Game runningGame;
	/** Der Geschwindigkeitsmodifikator */
	double powerupSpeedModifier = 5; // TODO Ein Powerup schreiben dass die Geschwindigkeit eines Balles oder Schlägers modifiziert.

    /**
     * <p>Beinhaltet alle wichtigen Attribute des Powerups, wie
     * etwa Geschwindigkeit, anfängliche Position und generelle Bewegung.
     * Die Position sollte dabei fix sein und die Geschwindigkeit 0 betragen.
     * 
     * @param game Das laufende Spiel
     * @param posX Die X Position des Powerups
     * @param posY Die Y Position dse Powerups
     */
    public EntityPowerup(Game game, double posX, double posY) {
        super(game);
        runningGame = game;
        this.speed = 0.0;
        this.posX = posX;
        this.posY = posY;
        this.lastCollision = 0;
        
    }
    
    /**
     * <p>Bestimmt das Verhalten des Powerups im Fall
     * einer Kollision mit einem anderen Objekt
     * (Höchstwahrscheinlich dem Ball wenn alles glatt läuft)
     * und ist für das Rendern des Objektes verantwortlich.
     * 
     * @param game Das laufende Spiel
     */
    @Override
    public void onUpdate (Game game) {
        
        super.onUpdate(game);
        handleCollisions(game);
        
    }

    /**
     * Modifiziert die Geschwindigkeit einer Entität.
     * 
     * @param game Das laufende Spiel
     * @param newSpeed Die neue Geschwindigkeit der  Entität.
     * @param powerupSpeedModifier Der Modifikator aus dm die neue Geschwindigkeit berechnet wird.
     */
	public void updateSpeed(Game game, double newSpeed, double powerupSpeedModifier) {
		newSpeed = speed * powerupSpeedModifier;
		this.speed = newSpeed;
	}

    /**
     * <p>Behandelt die Kollisionserkennung.
     */
    private void handleCollisions(Game game) {
        // TODO Auto-generated method stub
    }
}
