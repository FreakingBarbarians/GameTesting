/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Main.Asset.AnimationBuilder;
import Main.Asset.AssetLoader;
import Main.Asset.BuildException;
import Main.Object.GameObject;
import Main.Object.Rendering.Animation;
import Main.Object.Rendering.Animator;
import Main.Object.Rendering.AnimatorException;
import Main.Object.Rendering.Renderer;
import Main.Object.Transform;
import Main.View.Camera;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author 3101209
 */
public class Main {

    // Game Variables
    private boolean gameOn = true;
    private List<GameObject> gameObjects = new ArrayList();

    // Window Variables
    private int HEIGHT = 1000;
    private int WIDTH = 1000;
    private String WINDOW_TITLE = "Test_View";

    // Rendering
    /**
     * Camera of this view
     */
    private Camera camera;
    /**
     * Program ID
     */
    public static int program;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        gameOn = true;
        camera = new Camera(WIDTH, HEIGHT);
        setupOpenGL();
        setupProgram();
        load();

        while (!Display.isCloseRequested()) {
            logic();
            draw();
            Display.sync(60);
            Display.update();
        }

    }

    public void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        camera.update();

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

        for (GameObject g : this.gameObjects) {
            g.sendMessageDown("draw", null, false);
        }
        GL20.glUseProgram(0);
    }

    public void logic() {
        for (GameObject g : gameObjects) {
            Object[] args = {0.001f};
            g.sendMessageDown("rotate", args, false);
        }
    }

    private void setupProgram() {
        int vertexShader = AssetLoader.loadShader("src/vertex.glsl", GL20.GL_VERTEX_SHADER);
        int fragmentShader = AssetLoader.loadShader("src/fragment.glsl", GL20.GL_FRAGMENT_SHADER);

        this.program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmentShader);

        GL20.glBindAttribLocation(program, 0, "in_Position");
        GL20.glBindAttribLocation(program, 1, "in_Color");
        GL20.glBindAttribLocation(program, 2, "in_TextureCoord");

        GL20.glLinkProgram(program);
        GL20.glValidateProgram(program);
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
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public void addGameObject(GameObject g) {
        this.gameObjects.add(g);
    }

    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public synchronized void closeGame() {
        if (gameOn == false) {
            return;
        }
        gameOn = false;
    }

    private void load() {
        GameObject o = new GameObject();
        Animation a = null;
        try {
            a = new AnimationBuilder().addAnimation("img.png", 100)
                    .addAnimation("RedBall.png", 100)
                    .addAnimation("ShittyRocket.png", 100).setLooping()
                    .setName("idle")
                    .build();
            a.play();
        } catch (BuildException ex) {
            System.out.println("Build Failed!");
            ex.printStackTrace();
            System.exit(-1);
        }

        Animator animator = new Animator();
        try {
            animator.addAnimation(a);
        } catch (AnimatorException ex) {
            System.out.println("Already Exists");
        }

        Transform transform = new Transform(new Vector3f(0, 0, 0), new Vector2f(1, 1));
        Renderer renderer = new Renderer(transform, animator);

        o.addComponent(renderer);
        o.addComponent(transform);
        gameObjects.add(o);
    }
}
