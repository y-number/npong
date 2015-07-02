package de.tu_bs.cs.isf.npong0.game;

import java.util.ArrayList;

import de.tu_bs.cs.isf.npong0.game.entity.*;

/**
 * <p>Provisorische Klasse zur Behandlung der Entit�ten zur Anzeige des Punktestandes. 
 * <p>Wird sobald fertiggestellt die Punktest�nde darstellen
 * Nicht final.
 * @author David Hellmers
 * @author Marc Filbert
 * @version Alpha 0.1.1.0002
 */
public class World {

	/**	Die Arraylist, welche die Schl�ger beinhaltet. */
	private ArrayList<EntityPlayer> activePlayers;
	/**	Die Arraylist, welche die B�lle beinhaltet. */
	private ArrayList<EntityBall> activeBalls;
	/**	Die Arraylist, welche die W�lle beinhaltet. */
	private ArrayList<EntityWall> activeWalls;
	/**	Die Arraylist, welche die Powerups beinhaltet. */
    private ArrayList<EntityPowerup> activePowerups;
	/**	Die Arraylist, welche die Punktestandsanzeigen beinhaltet. */
    private ArrayList<EntityScore> activeScores;
	/**	Das laufende Spiel. */
	private Game runningGame;
	
	/**
	 * <p>Der Konstruktor startet die Spielwelt und erschafft alle 
	 * notwendigen Arraylists um einen korrekten Spielablauf zu 
	 * gew�hrleisten. Dazu z�hlen Arraylists f�r Schl�ger, B�lle, 
	 * Powerups, Punktestandsanzeigen und W�lle.
	 *  
	 *   @param game Das laufende Spiel
	 */
	public World(Game game) {
		runningGame = game;
		activePlayers = new ArrayList<>();
		activeBalls = new ArrayList<>();
        activePowerups = new ArrayList<>();
		activeWalls = new ArrayList<>();
		activeScores = new ArrayList<>();
	}
	
	/** Gibt die Liste an aktiven Schl�gern wieder. */
	public ArrayList<EntityPlayer> getActivePlayers() {
		return activePlayers;
	}

	/** Gibt die Liste an aktiven B�llen wieder. */
	public ArrayList<EntityBall> getActiveBalls() {
		return activeBalls;
	}

	/** Gibt die Liste an aktiven W�llen wieder. */
	public ArrayList<EntityWall> getWalls() {
		return activeWalls;
	}
	
	/** Gibt die Liste an aktiven Powerups wieder. */
    public ArrayList<EntityPowerup> getPowerups() {
        return activePowerups;
    }
    
	/** Gibt die Liste an aktiven Punktestandsanzeigen wieder. */
    public ArrayList<EntityScore> getScore() {
        return activeScores;
    }
	
	/** Setzt alle Schl�ger, B�lle, Powerups, Punktestandsanzeigen und W�lle zur�ck. */
	public void reset() {
		activePlayers = new ArrayList<>();
		activeBalls = new ArrayList<>();
		activePowerups = new ArrayList<>();
		activeScores = new ArrayList<>();
		activeWalls = new ArrayList<>();
	}
	
	/** Setzt alle B�lle */
	public void resetBalls() {
		activeBalls = new ArrayList<>();
	}

	/** 
	 * Erstellt im Fall das ausreu�chend Spieler vorhanden sind alle Spielfeldobjekte.
	 * Diese beinhalten Schl�ger, B�lle, Powerups, Punktestandsanzeigen und W�lle.  
	 * */
	public void run() {
		
		if (runningGame.getConnectionCount() > 2) {
			for(EntityWall wall : this.activeWalls) {
				wall.onUpdate(runningGame);
			}
			for(EntityPlayer player : this.activePlayers) {
				player.onUpdate(runningGame);
			}
			for(EntityBall ball : this.activeBalls) {
				ball.onUpdate(runningGame);
			}
            for(EntityPowerup powerup : this.activePowerups) {
                powerup.onUpdate(runningGame);
            }
            for(EntityScore score : this.activeScores) {
                score.onUpdate(runningGame);
            }
		}
	}
}
