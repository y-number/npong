/*
 * Copyright (c) 2002-2010 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * <p>Diese Klasse ermoeglicht die Darstellung von Sprites für N-Pong
 * @author Jonas Ahrens
 * 
 */
package de.tu_bs.cs.isf.npong0.game.view;
 
import java.io.IOException;
 
import static org.lwjgl.opengl.GL11.*;

/**
 * <p>Klasse zum erstellen der Sprites.
 *
 * @author Jonas Ahrens
 * @version Alpha 0.1.0.0001
 */

public class Sprite {
	//TODO Natuerlich muessen die Methoden und Werte auf N-Pong angepasst werden. Diese Klasse soll
	//lediglich eine Schablone fuer die Erstellung von Sprites darstellen, damit wir endlich vorankommen.
    /**
     * <p> Die Textur , welche das Bild für diesen Sprite speichert
     */
    private Texture texture;
 
    /**
     * <p> Die Breite in Pixeln dieses Sprites
     */
    private int width;
 
    /**
     * <p> Die Hoehe in Pixeln dieses Sprites
     */
    private int height;
 
    /**
     * <p> Erstellt einen neuen Sprite eines spezifischen Bildes.
     *
     * @param loader der verwendete texture loader
     * @param ref Ein Bezug zum Bild auf welchem dieser Sprite basieren soll
     */
    public Sprite(Textureloader loader, String ref) {
    try {
            texture = loader.getTexture("images/" + ref);
      width = texture.getImageWidth();
      height = texture.getImageHeight();
    } catch (IOException ioe) {
        ioe.printStackTrace();
      System.exit(-1);
    }
    }
 
    /**
     * <p>Get fuer die Breite dieses Sprites in Pixeln
     *
     * @return die Breite dieses Sprites in Pixeln
     */
    public int getWidth() {
        return texture.getImageWidth();
    }
 
    /**
     *<p> Get fuer die Hoehe dieses Sprites in Pixeln
     *
     * @return die Hoehe dieses Sprites in Pixeln
     */
    public int getHeight() {
        return texture.getImageHeight();
    }
 
    /**
     * <p>Zeichnet den Sprite an dem spezifischen Ort
     * @param x Die x Koordinate des Sprites
     * @param y Die y Koordinate des Sprites
     */
    public void draw(int x, int y) {
        // speichert die aktuelle Modell Matrix
        glPushMatrix();
 
        // bindet in the angemessene Textur für diesen Sprite ein
        texture.bind();
 
        // uebersetzt zum richtigen Ort und bereitet das Zeichnen vor
        glTranslatef(x, y, 0);
 
        // zeichnet eine viereckige Textur um sich dem Sprite anzupassen
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(0, 0);
 
            glTexCoord2f(0, texture.getHeight());
            glVertex2f(0, height);
 
            glTexCoord2f(texture.getWidth(), texture.getHeight());
            glVertex2f(width, height);
 
            glTexCoord2f(texture.getWidth(), 0);
            glVertex2f(width, 0);
        }
        glEnd();
 
        // stellt die Modellsichtmatrix wieder her um Verunreinigung zu verhindern
        glPopMatrix();
    }
}
