package de.tu_bs.cs.isf.npong0.game.view;

import org.lwjgl.opengl.GL11;

/**
 * <p>Erschafft Rechtecke mit bestimmter Position, Größe und Farbe.
 * 
 * @author David Hellmers
 * @version Alpha 0.1.0.0001
 */

public class Gui {

	/**
	 * Gibt den Rotwert eines Hexcodes wieder.
	 * @param color Der Farbwert
	 * @return Der Dezimalwert einer Hexadezimalzahl
	 */
	public double getRedFromHex (int color) {
		
		return (color >> 16 & 0xFF) / 255.0; // konvertiert den roten Teil aus dem Hex code
	}
	
	/**
	 * Gibt den Grünwert eines Hexcodes wieder.
	 * @param color Der Farbwert
	 * @return Der Dezimalwert einer Hexadezimalzahl
	 */
	public double getGreenFromHex (int color) {
		
		return (color >> 8 & 0xFF) / 255.0;
	}
	
	/**
	 * Gibt den Blauwert eines Hexcodes wieder.
	 * @param color Der Farbwert
	 * @return Der Dezimalwert einer Hexadezimalzahl
	 */
	public double getBlueFromhHex (int color) {
		
		return (color & 0xFF) / 255.0;
	}
	
	/**
	 * Gibt den Alphawert eines Hexcodes wieder.
	 * @param color Der Farbwert
	 * @return Der Dezimalwert einer Hexadezimalzahl
	 */
	public double getAlphaFromHex (int color) {
		
		return (color >> 24 & 0xFF) / 255.0;
	}
	
	/**
	 * Erschafft ein Rechteck mit bestimmter Position, Größe und Farbe.
	 * 
	 * @param x Die X Position des Rechtecks.
	 * @param y Die Y Position des Rechtecks.
	 * @param width Die Breite des Rechtecks.
	 * @param height Die Höhe des Rechtecks.
	 * @param color Die Farbe des Rechtecks.
	 */
	public void drawRect (double x, double y, double width, double height, int color) {
		
		double r = this.getRedFromHex(color);
		double g = this.getGreenFromHex(color);
		double b = this.getBlueFromhHex(color);
		double a = this.getAlphaFromHex(color);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glColor4d(r, g, b, a);
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x + width, y);
		GL11.glVertex2d(x + width, y + height);
		GL11.glVertex2d(x, y + height);
		
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
	}
}
