package de.tu_bs.cs.isf.npong0.game.entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import de.tu_bs.cs.isf.npong0.game.Game;
import de.tu_bs.cs.isf.npong0.game.view.Gui;

/**
 * <p>�berklasse aus der alle Entit�ten abgeleitet werden. 
 * <p>Wird sobald fertiggestellt alle wichtigen Attribute und 
 * Funktionen der Entit�ten festlegen. Nicht final.
 * 
 * @author David Hellmers
 * @author Marc Filbert
 * @version Alpha 0.1.0.0000
 */
public class Entity {

	/**	Die Position auf der X Achse. */
	public double posX;
	/**	Die Position auf der Y Achse. */
	public double posY;
	/**	Die Bewegungsgeschwindigkeit auf der X Achse. */
	public double motionX;
	/**	Die Bewegungsgeschwindigkeit auf der Y Achse. */
	public double motionY;
	/**	Die Breite. */
	public double width;
	/**	Die H�he. */
	public double height;
	/**	Die Geschwindigkeit. */
	public double speed;
	/**	Das Rechteck. */
	public Rectangle rect;
	/**	Das Benutzerinterface. */
	public Gui gui;
	/**	Das laufende Spiel. */
	protected Game runningGame;
	/** Die Punkte */
	public int score_points;
	
    /**
     * <p>Beinhaltet alle wichtigen Attribute der Entit�t, wie
     * etwa Geschwindigkeit, anf�ngliche Position, generelle Bewegung
     * und Abmessungen.
     * 
     * @param game Das laufende Spiel
     */
	public Entity(Game game) {
		
		this.posX = 0.0;
		this.posY = 0.0;
		this.motionX = 0.0;
		this.motionY = 0.0;
		this.width = 16.0;
		this.height = 16.0;
		this.speed = 5.0;
		
		this.rect = new Rectangle();
		this.setBounds();
		this.gui = new Gui();
		runningGame = game;
	}
	
    /**
     * <p>Setzt die Grenzen der Entit�t und ihre
     * Position auf X und Y Achse fest.
     */
	public void setBounds() {
		
		this.rect.x = (int) this.posX;
		this.rect.y = (int) this.posY;
		this.rect.width = (int) this.width;
		this.rect.height = (int) this.height;
	}
	
    /**
     * <p>Bestimmt das Verhalten der Entit�t im Fall
     * einer Kollision mit einem anderen Objekt, setzt
     * die Abgrenzung der Entit�ten um und ist f�r
     * das Rendern des Objektes verantwortlich.
     * 
     * @param game Das laufende Spiel
     */
	public void onUpdate(Game game) {
		this.setBounds();
		this.moveEntitiy(game, this.posX + this.motionX, this.posY + this.motionY);
		this.doRender(game);
	}

    /**
     * <p>Verantwortlich f�r die Bewegung der Entit�ten und sobald
     * gefixt verantwortlich f�r das Zur�cksetzen dieser im Fall dass
     * das Spielfeld verlassen wird.
     * 
     * @param newX Die neue X Position der Entit�t
     * @param newY Die neue Y Position der Entit�t
     * @param game Das laufende Spiel
     */
	public void moveEntitiy(Game game, double newX, double newY) {
		// TODO Auto-generated method stub
	/**	
		if (this.posX <= 0) {
			posX = Pong.DISPLAY_WIDTH / 2;
			posY = Pong.DISPLAY_HEIGHT / 2;
				
		} else if (posX >= Pong.DISPLAY_WIDTH) {
			posX = Pong.DISPLAY_WIDTH / 2;
			posY = Pong.DISPLAY_HEIGHT / 2;
		
		} else if (posY <= 0) {
			posX = Pong.DISPLAY_WIDTH / 2;
			posY = Pong.DISPLAY_HEIGHT / 2;
			
		} else if (posY >= Pong.DISPLAY_HEIGHT) {
			posX = Pong.DISPLAY_WIDTH / 2;
			posY = Pong.DISPLAY_HEIGHT / 2;
		} else {
		**/
			this.posX = newX;
			this.posY = newY;
//		}
	}

    /**
     * <p>Rendert die Entit�ten. Derzeitig noch als Rechtecke, sobald 
     * implementiert als Sprites.
     * 
     * @param game Das laufende Spiel
     */
	public void doRender(Game game) {
		
		gui.drawRect(this.posX, this.posY, this.width, this.height, 0xFF000000); //transparenz, hex erst die letzten 4 0en
		
	}
}
