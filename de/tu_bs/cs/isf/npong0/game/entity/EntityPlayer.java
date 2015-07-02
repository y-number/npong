package de.tu_bs.cs.isf.npong0.game.entity;

import de.tu_bs.cs.isf.npong0.game.Game;
import de.tu_bs.cs.isf.npong0.game.keys.Key;
import de.tu_bs.cs.isf.npong0.game.keys.Keybind;
import de.tu_bs.cs.isf.npong0.game.server.Connection;

/**
 * <p>Klasse der Spieler Entitäten
 * <p>Diese Klasse erlaubt die Erstellung von Schlägern und Linien auf 
 * welchen sich diese bewegen können. Macht außerdem die 
 * Schlägerbewegung möglich.
 * 
 * @author David Hellmers
 * @author Marc Filbert
 * @version Alpha 0.1.1.0003
 */
public class EntityPlayer extends Entity {
	
    /** Der Keybind für die Bewegung des Schlägers nach links. **/
	public Keybind keyUp;
	/** Der Keybind für die Bewegung des Schlägers nach rechts. **/
	public Keybind keyDown;
	/** Die Verbindung zu einem verbundenen Controller. **/
	private Connection controller;
	/** Die aktuelle Position des Schlägers. **/
	private int position;
	/** Die maximale Schlägerposition. **/
	private static final int MAX_POSITION = 50;
	/** Beschreibt die Groesse des Schlägers im Verhältnis 1:X zur Spielerseite. **/
	private static final int LENGTH_DIVIDENT = 7;
	/** Die Linie auf welcher sich der Schläger bewegt. **/
	private double[] racketLine = new double[4];
	/** Die Koordinaten für Start- und Endkoordinaten der Linie auf der sich der Schläger bewegt. **/
	private double[] racketCoordinates = new double[4];
	/** Sagt aus ob ein Spieler das Spiel verlassen hat **/
	private boolean hasLeft = false;

	/**
	 * Erweitert die grundlegende Entity mit weiteren Attributen und der Möglichkeit
	 * zur Bewegung der Schläger Entität.
	 * 
	 * @param game Das laufende Spiel
	 * @param controller Der betreffende Controller
	 * @param startX Der Startpunkt auf der X Achse.
	 * @param startY Der Startpunkt auf der Y Achse.
	 * @param endX Der Endpunkt auf der X Achse.
	 * @param endY Der Endpunkt auf der Y Achse.
	 */
	public EntityPlayer(Game game, Connection controller, double startX, double startY, double endX, double endY) {
		
		super(game);
		runningGame = game;
		this.controller = controller;
		this.speed = 1.0 * game.speedModifierPlayers;
		
		racketLine[0] = startX;
		racketLine[1] = startY;
		racketLine[2] = endX;
		racketLine[3] = endY;
		position = MAX_POSITION / 2;

		double deltaX = this.racketLine[2] - this.racketLine[0];
		double deltaY = this.racketLine[3] - this.racketLine[1];
		this.height = Math.sqrt(deltaX * deltaX + deltaY * deltaY) / LENGTH_DIVIDENT;

		this.keyUp = new Keybind(Key.LEFT, "LEFT", controller);
		this.keyDown = new Keybind(Key.RIGHT, "RIGHT", controller);
	}
	
	/**
	 * Gibt die Linie auf welcher sich der Schläger bewegt zurück.
	 * @return racketLine
	 */
	public double[] getRacketLine() {
		return racketLine;
	}

	/**
	 * Gibt die Schlägerkoordinaten wieder.
	 * @return racketCoordinates
	 */
	public double[] getRacketCoordinates() {
		return racketCoordinates;
	}

	/**
	 * Gibt den Controller zurŸck.
     * @return controller
	 */
	public Connection getController() {
		return controller;
	}

	/**
	 * Die von Entity geerbte onUpdate Methode. Hier erweitert durch 
	 * HandleInput, für den Fall dass ein Spieler die Runde verlässt.
     * @param game Das laufende Spiel
	 */
	@Override
	public void onUpdate(Game game) {
		this.setBounds();
		this.moveEntitiy(game);
		this.doRender(game);
		
		if (!hasLeft) {
			handleInput(game);
		}
	}

	/**
	 * Registriert das Input der Controller und bewegt den Schläger entsprechend.
	 * 
	 * @param game Das laufende Spiel
	 */
	private void handleInput(Game game) {
		
		if (this.keyUp.isKeyDown()) {
			if (this.position >= 0.0 + this.speed) {
				this.position -= this.speed;
			} else {
				this.position = 0;
			}
		} else if (this.keyDown.isKeyDown()) {
			if (this.position <= MAX_POSITION - this.speed) {
				this.position += this.speed;
			} else {
				this.position = MAX_POSITION;
			}
		}
		
	}
	
	/**
	 * Behandelt die Bewegung des Schlägers auf seiner Geraden.
	 * 
	 * @param game Das laufende Spiel
	 */
	public void  moveEntitiy(Game game) {
		double deltaX = this.racketLine[2] - this.racketLine[0];
		double deltaY = this.racketLine[3] - this.racketLine[1];
		double deltaPosition = (double) this.position / MAX_POSITION;
		double newX = this.racketLine[0] + (deltaPosition * deltaX);
		double newY = this.racketLine[1] + (deltaPosition * deltaY);

		double alpha = Math.atan(deltaY/deltaX) / Math.PI * 180.0;
		double deltaXRacket = Math.cos(alpha * Math.PI / 180.0) * this.height;
		double deltaYRacket = Math.sin(alpha * Math.PI / 180.0) * this.height;
		racketCoordinates[0] = newX;
		racketCoordinates[1] = newY;
		racketCoordinates[2] = racketCoordinates[0] + deltaXRacket;
		racketCoordinates[3] = racketCoordinates[1] + deltaYRacket;
		
		super.moveEntitiy(game, newX, newY);
	}
	
	/**
	 * Trennt die Verbindung zum Controller.
	 */
	public void disconnect() {
		this.controller.disconnect();
	}
	
	/**
	 * Setzt einen Schläger auf die zentrale Position zurück.
	 */
	public void leave() {
		this.position = MAX_POSITION / 2;
		this.hasLeft = true;
		
	}

}
