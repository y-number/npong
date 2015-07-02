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
package de.tu_bs.cs.isf.npong0.game.view;
 
import static org.lwjgl.opengl.GL11.*;
 
/**
 *<p> Klasse zur Berechnung und Darstellung der für die Sprites benötigten Texturen
 *
 * @author Jonas Ahrens
 * @version Alpha 0.1.0.0001
 */
public class Texture {
 
    /**
     * <p> Der GL Zieltyp 
     */
    private int target;
 
    /** 
     * <p> Die GL textur ID 
     */
    private int textureID;
 
    /**
     * <p> Hoehe des Bildes
     */
    private int height;
 
    /** 
     * <p>Breite des Bildes
     */
    private int width;
 
    /** 
     * <p>Breite der Textur 
     */
    private int texWidth;
 
    /** 
     * <p>Hoehe der Textur 
     */
    private int texHeight;
 
    /**
     * <p> Das Verhaeltnis der Breite vom Bild zur Textur
     */
    private float widthRatio;
 
    /** 
     * Das Verhaeltnis der Hoehe vom Bild zur Textur */
    private float heightRatio;
 
    /**
     *<p> Erstellt eine neue Textur
     *
     * @param target The GL target
     * @param textureID The GL texture ID
     */
    public Texture(int target, int textureID) {
        this.target = target;
        this.textureID = textureID;
    }
 
    /**
     * 
     * <p>Bindet den spezfischen GL Kontext in eine Textur ein
     */
    public void bind() {
        glBindTexture(target, textureID);
    }
 
    /**
     * 
     * <p>Set für die Höhe des Bildes
     *
     * @param height Die Höhe des Bildes
     */
    public void setHeight(int height) {
        this.height = height;
        setHeight();
    }
 
    /**
     * 
     * <p>Set für die Breite des Bildes
     *
     * @param width Die Breite des Bildes
     */
    public void setWidth(int width) {
        this.width = width;
        setWidth();
    }
 
    /**
     *
     * <p>Get der Höhe des originalen Bildes
     *
     * @return height Höhe des originalen Bildes
     */
    public int getImageHeight() {
        return height;
    }
 
    /**
     * 
     * <p>Get der Breite des originalen Bildes
     *
     * @return width Breite des originalen Bildes     */
    public int getImageWidth() {
        return width;
    }
 
    /**
     * <p>Get für die Höhe der physischen Textur
     *
     * @return Die Höhe der physischen Textur
     */
    public float getHeight() {
        return heightRatio;
    }
 
    /**
     * 
     * <p>Get für die Breite der physischen Textur
     *
     * @return Die Breite der physischen Textur
     */
    public float getWidth() {
        return widthRatio;
    }
 
    /**
     *<p> Set für die Hoehe der Textur
     *
     * @param texHeight Hoehe der Textur
     */
    public void setTextureHeight(int texHeight) {
        this.texHeight = texHeight;
        setHeight();
    }
 
    /**
     * <p>Set für die Breite der Textur
     *
     * @param texWidth Breite der Textur
     */
    public void setTextureWidth(int texWidth) {
        this.texWidth = texWidth;
        setWidth();
    }
 
    /**
     * Set the height of the texture. This will update the
     * ratio also.
     */
    private void setHeight() {
        if (texHeight != 0) {
            heightRatio = ((float) height) / texHeight;
        }
    }
 
    /**
     * Set the width of the texture. This will update the
     * ratio also.
     */
    private void setWidth() {
        if (texWidth != 0) {
            widthRatio = ((float) width) / texWidth;
        }
    }
}