package de.tu_bs.cs.isf.npong0.game.entity;

import de.tu_bs.cs.isf.npong0.game.Game;

/**
 * <p> Die Klasse erbt von Entity und erhaelt somit notwendige Methoden und Variablen
 * zur Bewegung und Kollisionserkennung des Enemys.
 * 
 * @author David Hellmers
 * @author Marc Filbert
 * @author Jonas Ahrens
 * @version Alpha 0.1.0.0001
 * */
public class EntityEnemy extends Entity{

	/**
	 * <p>Super Konstruktor , welcher die Variablen von Entity uebergibt und
	 * den Enemy mit spezifischen Werten versieht
     * 
     * @param game Das laufende Spiel
     */
	public EntityEnemy(Game game) {
		
		super(game);
		// TODO Auto-generated constructor stub
		this.posX = Game.DISPLAY_WIDTH - this.width - 16.0;
		this.posY = (Game.DISPLAY_HEIGHT / 2) - (this.height / 2.0);
		this.height *= 5.0;
	}
	
	/**
	 * <p>Erbt die onUpdate Methode von Entity
	 * */
	@Override
	public void onUpdate(Game game) {
		// TODO Auto-generated method stub
		super.onUpdate(game);
		
		double mistake = game.getRandRange(-5.1, 5.1);
		
		this.motionY = (this.posY - game.theBall.posY > 0.0) ? -(this.speed + mistake) : this.speed + mistake;
	}
	
	/**<p>Bewegung des Enemys
	 * */
	@Override
	public void moveEntitiy(Game game, double newX, double newY) {
		// TODO Auto-generated method stub
		if (this.posY < 0.0 && this.motionY < 0.0) return;
		if (this.posY + this.height > Game.DISPLAY_HEIGHT && this.motionY > 0.0) return;
		super.moveEntitiy(game, newX, newY);
	}
	


}
