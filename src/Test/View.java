/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * Not sure if View is the right thing to call it.
 * @author Ray
 */
public class View implements Runnable {

    private int HEIGHT = 1080;
    private int WIDTH = 1920;
    private String WINDOW_TITLE = "Test_View";
    private final Test game;

    public View(int w, int h, String title, Test game) {
        HEIGHT = h;
        WIDTH = w;
        WINDOW_TITLE = title;
        this.game = game;
    }

    private void setupOpenGL() {
        // Setup an OpenGL context with API version 3.2
        try {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAtrributes = new ContextAttribs(3, 2, ContextAttribs.CONTEXT_CORE_PROFILE_BIT_ARB).withProfileCore(true);

            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle(WINDOW_TITLE);
            Display.create(pixelFormat, contextAtrributes);

            System.out.println("OpenGL Version:" + GL11.glGetString(GL11.GL_VERSION));

            GL11.glViewport(0, 0, WIDTH, HEIGHT);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Setup an XNA like background color
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);

        // Map the internal OpenGL coordinate system to the entire screen
        GL11.glViewport(0, 0, 64, 64);
    }

    @Override
    public void run() {
        this.setupOpenGL();
        
        while (game.isGameOn()) {
            // Game closed by window
            if (Display.isCloseRequested()) {
                game.closeGame();
                break;
            }
        }
    }

}
