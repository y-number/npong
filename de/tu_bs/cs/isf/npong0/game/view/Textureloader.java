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
 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
 
import javax.swing.ImageIcon;
 
import org.lwjgl.BufferUtils;
 
import static org.lwjgl.opengl.GL11.*;
 
/**
 * A utility class to load textures for OpenGL. This source is based
 * on a texture that can be found in the Java Gaming (www.javagaming.org)
 * Wiki. It has been simplified slightly for explicit 2D graphics use.
 *
 * OpenGL uses a particular image format. Since the images that are
 * loaded from disk may not match this format this loader introduces
 * a intermediate image which the source image is copied into. In turn,
 * this image is used as source for the OpenGL texture.
 *
 * 
 * @author Jonas Ahrens
 * 
 * @version Alpha 0.1.0.0001
 */
public class Textureloader {
    /** 
     *  <p>Die Tabelle der Texturen , welche hinein geladen wurden
     */
    private HashMap<String, Texture> table = new HashMap<String, Texture>();
 
    /**
     * 
     *  <p>Das Farbmodell ,welches Alpha fürs GL Bild enthealt
     */
    private ColorModel glAlphaColorModel;
 
    /** 
     * <p>Das Farbmodell fürs GL Bild
     */
    private ColorModel glColorModel;
 
    /**
     * <p> Erstellt den Buffer für die Textur ID's
     */
    private IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);
 
    /**
     * <p> Erstellt einen neuen texture loader , welcher auf dem Bedienungsfeld des Spiels basiert.
     */
    public Textureloader() {
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,8},
                                            true,
                                            false,
                                            ComponentColorModel.TRANSLUCENT,
                                            DataBuffer.TYPE_BYTE);
 
        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,0},
                                            false,
                                            false,
                                            ComponentColorModel.OPAQUE,
                                            DataBuffer.TYPE_BYTE);
    }
 
    /**
     * <p> Erschafft eine neue Textur ID
     *
     * @return Eine neue Textur ID
     */
    private int createTextureID() {
      glGenTextures(textureIDBuffer);
      return textureIDBuffer.get(0);
    }
 
    /**
     * <p>Laed eine Textur
     *
     * @param resourceName Der Ort aus welchem die Datei geladen wird
     * @return Die geladene Textur
     * @throws IOException Zeigt einen Fehler an , wenn kein Zugriff auf die Datei moeglich ist
     */
    public Texture getTexture(String resourceName) throws IOException {
        Texture tex = table.get(resourceName);
 
        if (tex != null) {
            return tex;
        }
 
        tex = getTexture(resourceName,
                         GL_TEXTURE_2D, // target
                         GL_RGBA,     // dst pixel format
                         GL_LINEAR, // min filter (unused)
                         GL_LINEAR);
 
        table.put(resourceName,tex);
 
        return tex;
    }
 
    /**
     * Load a texture into OpenGL from a image reference on
     * disk.
     * <p> Laed eine Textur von einem Bild auf der Festplatte ins OpenGL.
     * @param resourceName Der Ort , aus welchem die Datei geladen werden soll.
     * @param target The GL target to load the texture against
     * @param dstPixelFormat The pixel format of the screen
     * @param minFilter The minimising filter
     * @param magFilter The magnification filter
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTexture(String resourceName,
                              int target,
                              int dstPixelFormat,
                              int minFilter,
                              int magFilter) throws IOException {
        int srcPixelFormat;
 
        // Erstellt die Textur ID für diese Textur
        int textureID = createTextureID();
        Texture texture = new Texture(target,textureID);
 
        // bindet diese Textur ein
        glBindTexture(target, textureID);
 
        BufferedImage bufferedImage = loadImage(resourceName);
        texture.setWidth(bufferedImage.getWidth());
        texture.setHeight(bufferedImage.getHeight());
 
        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL_RGBA;
        } else {
            srcPixelFormat = GL_RGB;
        }
 
        // wandelt dieses Bild in einen Byte Buffer aus Texturdaten um
        ByteBuffer textureBuffer = convertImageData(bufferedImage,texture);
 
        if (target == GL_TEXTURE_2D) {
            glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
            glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
        }
 
        // erstellt aus dem Byte Buffer eine Textur
        glTexImage2D(target,
                      0,
                      dstPixelFormat,
                      get2Fold(bufferedImage.getWidth()),
                      get2Fold(bufferedImage.getHeight()),
                      0,
                      srcPixelFormat,
                      GL_UNSIGNED_BYTE,
                      textureBuffer );
 
        return texture;
    }
 
    /**
     * <p>Get fuer die naechst groeßere Potenz von 2 zur Nummer
     *
     * @param fold Die nummer des Ziels
     * @return Die Potenz von 2
     */
    private static int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    }
 
    /**
     * <p>Wandelt das gepufferte Bild in eine Textur um
     *
     * @param bufferedImage Das Bild welches in eine Textur umgewandelt wird
     * @param texture Die Textur in welchem die Daten gespeichert werden sollen
     * @return Der Buffer mit den Daten
     */
    private ByteBuffer convertImageData(BufferedImage bufferedImage,Texture texture) {
        ByteBuffer imageBuffer;
        WritableRaster raster;
        BufferedImage texImage;
 
        int texWidth = 2;
        int texHeight = 2;
 
        // findet die naechste Potenz von 2 fuer die Breite und Hoehe der erstellten Textur
        while (texWidth < bufferedImage.getWidth()) {
            texWidth *= 2;
        }
        while (texHeight < bufferedImage.getHeight()) {
            texHeight *= 2;
        }
 
        texture.setTextureHeight(texHeight);
        texture.setTextureWidth(texWidth);
 
        // erstellt ein Raster, welches von OpenGL als Quelle genutzt werden kann 
        // um eine Textur zu erstellen
        if (bufferedImage.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable());
        }
 
        // kopiert die Quelle des Bildes in das erstellte Bild
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,texWidth,texHeight);
        g.drawImage(bufferedImage,0,0,null);
 
        // erstellt einen Byte Buffer aus dem vorläufigen Bild
        // welches von OpenGL genutzt wird um eine Textur zu erstellen
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();
 
        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();
 
        return imageBuffer;
    }
 
    /**
     *<p> laed eine gegebene Datei als ein gepuffertes Bild
     * @param ref Der Ort , aus welchem die Datei geladen wird
     * @return Das geladene gepufferte Bild
     * @throws IOException Zeigt einen Fehler an, wenn ein Bild nicht geladen werden konnte
     */
    private BufferedImage loadImage(String ref) throws IOException {
        URL url = Textureloader.class.getClassLoader().getResource(ref);
 
        if (url == null) {
            throw new IOException("Cannot find: " + ref);
        }
 
        // wegen eines Problems mit ImageIO und mixed signed code
        // zum laden von Bildern und dem aufsetzen auf ein neues gepuffertes Bild
        // wird ImageIcon verwendet
        Image img = new ImageIcon(url).getImage();
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
 
        return bufferedImage;
    }
}