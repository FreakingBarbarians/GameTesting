/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import de.matthiasmann.twl.utils.PNGDecoder;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

/**
 *
 * @author 3101209
 */
public class PNGTexture {

    private int WIDTH;
    private int HEIGHT;
    private int size;
    private ByteBuffer data;
    
    
    public PNGTexture(String filename) {
        PNGDecoder decode;
        try {
            FileInputStream input = new FileInputStream(filename);
            decode = new PNGDecoder(input);
        } catch (IOException e) {
            System.out.println("Failed to Load Asset: " + filename);
            e.printStackTrace();
            System.exit(-1);
            return; // later on return a placeholder png
        }

        WIDTH = decode.getWidth();
        HEIGHT = decode.getHeight();
        size = WIDTH * HEIGHT * 4;
        data = BufferUtils.createByteBuffer(size);
        try {
            decode.decode(data, size, PNGDecoder.Format.RGBA);
        } catch (IOException e) {
            System.out.println("Failed to buffer Asset: " + filename);
            e.printStackTrace();
            System.exit(-1);
            return; // later on return a placeholder png
        }
        data.flip();
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getSize() {
        return size;
    }

    public ByteBuffer getdata() {
        return data;
    }
}
