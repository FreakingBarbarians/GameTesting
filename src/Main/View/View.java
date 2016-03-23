/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.View;

import Main.Main;
import Main.Object.GameObject;
import de.matthiasmann.twl.utils.PNGDecoder;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.SharedDrawable;

/**
 * Not sure if View is the right thing to call it.
 *
 * @author Ray
 */
public class View implements Runnable {

    // Window Variables
    private int HEIGHT = 1000;
    private int WIDTH = 1200;
    private String WINDOW_TITLE = "Test_View";

    // Game
    private final Main game;

    // Rendering
    /**
     * Camera of this view
     */
    private Camera camera;

    private SharedDrawable context;
    /**
     * ID of the OpenGL Program this view uses, may modularize later into a
     * program class and a program builder. Static program!
     */
    public static int program;

    public View(int w, int h, String title, Main game, SharedDrawable context) {
        this.context = context;
        HEIGHT = h;
        WIDTH = w;
        WINDOW_TITLE = title;
        this.game = game;
        camera = new Camera(WIDTH, HEIGHT);
    }

    @Override
    public void run() {
        this.setupOpenGL();
        // Array of GameObjects or perhaps an array of renderable objects?
        this.setupProgram();

        while (game.isGameOn()) {
            // Game closed by window
            if (Display.isCloseRequested()) {
                game.closeGame();
                break;
            }

            this.draw();
        }
    }

    public void draw() {
        // Set the program
        GL20.glUseProgram(program);

        // CameraManipulation
        // Set the matrix
        GL20.glUniformMatrix4(
                GL20.glGetUniformLocation(program, "projectionMatrix"),
                false,
                camera.getProjectionMatrixBuffer());
        // set the other matrix
        GL20.glUniformMatrix4(GL20.glGetUniformLocation(
                program, "viewMatrix"),
                false,
                camera.getViewMatrixBuffer());

        for (GameObject g : game.getGameObjects()) {
            g.sendMessageDown("draw", null, false);
        }
        GL20.glUseProgram(0);
    }

    private void setupOpenGL() {
        // Setup an OpenGL context with API version 3.2
        try {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAtrributes = new ContextAttribs(3, 2, ContextAttribs.CONTEXT_CORE_PROFILE_BIT_ARB).withProfileCore(true);

            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle(WINDOW_TITLE);
            Display.create(pixelFormat, context, contextAtrributes);
            
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

    private void setupProgram() {
        int vertexShader = this.loadShader("src/vertex.glsl", GL20.GL_VERTEX_SHADER);
        int fragmentShader = this.loadShader("src/fragment.glsl", GL20.GL_FRAGMENT_SHADER);

        this.program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmentShader);

        GL20.glBindAttribLocation(program, 0, "in_Position");
        GL20.glBindAttribLocation(program, 1, "in_Color");
        GL20.glBindAttribLocation(program, 2, "in_TextureCoord");

        GL20.glLinkProgram(program);
        GL20.glValidateProgram(program);
    }

    private int loadShader(String filename, int type) {
        StringBuilder shaderSource = new StringBuilder();
        int shaderID = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }

        shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Could not compile shader.");
            System.exit(-1);
        }

        return shaderID;
    }
}
