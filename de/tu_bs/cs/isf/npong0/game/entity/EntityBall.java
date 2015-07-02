package de.tu_bs.cs.isf.npong0.game.entity;

import de.tu_bs.cs.isf.npong0.game.Game;
import de.tu_bs.cs.isf.npong0.game.sounds.MusicPlayer;

/**
 * <p>Klasse der Ball Entitäten
 * <p>Diese Klasse stellt die Bälle und sämtliche zugehörige
 * Funktionalität dieser zur Verfügung. Nicht final.
 * 
 * @author David Hellmers
 * @author Marc Filbert
 * @author Jonas Ahrens
 * @version Alpha 0.1.1.0003
 */
public class EntityBall extends Entity {

    /** Gibt an welcher Spieler im Ballbesitz ist. */
	public EntityPlayer lastCollision;
	/** Der Abprallsound. */
	private static MusicPlayer bounceSound = new MusicPlayer("sounds/pong.wav", 0, 1000);
	/** Der Thread für den Abprallsound. */
	private static Thread bounceThread = new Thread(bounceSound);
	
    /**
     * <p>Beinhaltet alle wichtigen Attribute des Balles, wie
     * etwa Geschwindigkeit, anfängliche Position und generelle Bewegung..
	 * 
	 * @param game
	 */
	public EntityBall(Game game) {
		super(game);
		this.speed /= 2.0 * game.speedModifierBalls;
		this.posX = (Game.DISPLAY_WIDTH/2) - (this.width/2);
		this.posY = (Game.DISPLAY_HEIGHT/2) - (this.height/2);
		this.motionX = (Math.random() < 0.5 ) ? -1 * this.speed : this.speed;
		this.motionY = (Math.random() < 0.5 ) ? -1 * this.speed : this.speed;
		this.lastCollision = null;
		
	}
	
    /**
     * <p>Verantwortlich für die Wiedergabe der Ballbewegung auf dem Display,die Bewegung 
     * selbst und die Kollisionserkennung.
	 * 
	 *  @param game
	 */
	@Override
	public void onUpdate (Game game) {
		
		super.onUpdate(game);
		handleCollisions(game);
		
	}
	
	/**
	 * <p> Vorläufige Methode zur Berechnung der Kollision mit Wand und Schläger.
	 * <p> Wird die folgenden Tage fertig gestellt.
	 * funktioniert für drei Spieler noch nicht richtig.
	 * 
	 * @param p Ein Spieler
	 * @return true oder false
	 */
	public boolean calcCollision(EntityPlayer p) {
		
		// Die Werte sind für Eclipse damit es nicht meckert...
		double[] racketCoords = p.getRacketCoordinates();
		
		/* Gerade, auf der der Racket laeuft. y = m*x+b
		 * m = (y2 - y1) / (x2 - x1)
		 */
		double m = (racketCoords[3] - racketCoords[1]) /
				(racketCoords[2] - racketCoords[0]);
		// Denn b = y_racketend - m * x_racketend 
		double b = racketCoords[3] - m * racketCoords[2];
		
		// Bildet die Orthogonale y = mOrtho * x + c
		double mOrtho = -1/m;
		double c = this.posY - mOrtho * this.posX;
	    // Umgestellt : c = y_ball - m_ortho * x_ball
		
		double xIntersect = (c - b) / (m - mOrtho);
		// Gleichsetzen der beiden Geraden um Schnittpunkt zu ermitteln
		
		double yIntersect = m * xIntersect + b;
		// Pythoagoras
		double ballIntersectDistance = Math.sqrt(
				(this.posX - xIntersect) * (this.posX - xIntersect) 
				+ (this.posY - yIntersect) * (this.posY - yIntersect)
				);
		double ballRacketDistance = Math.sqrt(
				(this.posX - racketCoords[0]) * (this.posX - racketCoords[0]) 
				+ (this.posY + racketCoords[1]) * (this.posY - racketCoords[1])
				);
		double ballRacketDistance2 = Math.sqrt(
				(this.posX - racketCoords[2]) * (this.posX - racketCoords[2]) 
				+ (this.posY + racketCoords[3]) * (this.posY - racketCoords[3])
				);

		boolean collisionWall = ballIntersectDistance < this.height;
		boolean isInXIntervall = (xIntersect > racketCoords[0] && xIntersect < racketCoords[2])
				|| (xIntersect > racketCoords[2] && xIntersect < racketCoords[0]);
		boolean touchRacket = ballRacketDistance < this.height || ballRacketDistance2 < this.height; 
		boolean collisionPlayer = ballIntersectDistance < this.height + p.width
				&& (isInXIntervall || touchRacket);
		if (collisionPlayer && (lastCollision == null || lastCollision != p)) {
			lastCollision = p;
			this.bounce(m);
			
			bounceThread.start();
			return true;
		} else if (collisionWall) {
			if (lastCollision != null) {
				 if (lastCollision.getController().score() == this.runningGame.maxScore) {
					 // TODO Tatsächlich eine neue Runde starten wenn die maximale Punktzahl erreicht wurde.
				 }
				
			}
			this.runningGame.startNewRound();
			return true;
		}
		return false;	
	}
	
    /**
     * <p>Behandelt das Verhalten des Balls bei der Kollision mit einem anderen Objekt. 
	 * 
	 * @param game Das laufende Spiel
	 */
	private void handleCollisions(Game game) {
		
		// TODO Diese Zeilen sind unnötig sobald die Wände richtig erkannt werden.
		if (this.posX < 0.0) this.bounceX();
		if (this.posX + this.width > Game.DISPLAY_WIDTH) this.bounceX();
		if (this.posY < 0.0) this.bounceY();
		if (this.posY + this.height > Game.DISPLAY_HEIGHT) this.bounceY();
		
		boolean collision = false;
		for (EntityPlayer p : game.theWorld.getActivePlayers()) {
			if (calcCollision(p)) {
				break;
			}
		}
	}
	
    /**
     * <p>Bestimmt die neue Bewegungsrichtung auf der x Achse nach einer Kollision.
     */
	private void bounceX() {
		// TODO Soll später von der Aufschlagsstelle abhängen.
		this.motionX = -this.motionX;
	}
	
    /**
     * <p>Bestimmt die neue Bewegungsrichtung auf der y Achse nach einer Kollision.
     */
	private void bounceY() {
        // TODO Soll später von der Aufschlagsstelle abhängen.
		this.motionY = -this.motionY;
		
	}
	
	/**
	 * Die Methode welche entscheidet ob die bounceX oder die bounceY Methode ausgeführt werden muss.
	 * @param m Die Steigung der Ballbewegungsgerade
	 */
	public void bounce(double m) {
		if (Math.abs(m) < 1) {
			bounceY();
		} else {
			bounceX();
		}
	}
	
}
