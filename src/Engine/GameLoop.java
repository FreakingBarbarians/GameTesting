/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * The GameLoop is responsible for creating an instance of the game and
 * listening for input, and managing game logic and rendering. The GameLoop also
 * holds our OpenGL context, and preforms OpenGL related Loading.
 *
 *
 * Timing: Timing will be based off ticks. We'll go for a modest 64 ticks per
 * second. 1 tick every 16.625 seconds.
 *
 * @author Ray!
 */
public class GameLoop {

    /**
     * Game is on?.
     */
    public static boolean gameOn = false;
    /**
     * Desired Frame Rate.
     */
    public static int framerate = 60;
    /**
     * Window Height.
     */
    public static final int height = 300;
    /**
     * Window Width.
     */
    public static final int width = 900;
    /**
     * Ticks (game logic steps) per second.
     */
    public static final short TICK_COUNT = 64; // per second

    /**
     * Length of each tick.
     */
    public static final double TICK_RATE = (double) 1000 / 128; // in ms
    /**
     * Title of the window.
     */
    public static final String title = "";
    /**
     * Reference to the loop.
     */
    public static GameLoop game;
    /**
     * ObjectCollection and Manager.
     */
    public static ObjectManager manager = new ObjectManager();

    /**
     * Creates a new game loop.
     */
    public GameLoop() {

        try {
            this.startUp();
        } catch (LWJGLException startupFail) {
            System.out.println("Failed to Start Up");
            System.exit(-1);
        }
        double tickTime = 0;
        System.out.println("TickRate:" + TICK_RATE);

        // debug
        int debug_secondfpsCounter = 0;
        double secondTime = (double) System.nanoTime() / 1000000;
        while (gameOn & !Display.isCloseRequested()) {
            double startTime = (double) System.nanoTime() / 1000000;

            while (tickTime >= TICK_RATE) {
                // System.out.println(tickTime);
                manageResources();
                gameLogic();
                tickTime -= TICK_RATE;
            }

            render();

            // debug
            debug_secondfpsCounter++;
            Display.update();
            tickTime += (double) (System.nanoTime() / 1000000) - startTime;
            if (((System.nanoTime() / 1000000) - secondTime) >= 1000) {
                System.out.println(debug_secondfpsCounter);
                debug_secondfpsCounter = 0;
                secondTime = (double) System.nanoTime() / 1000000;
            }
        }
        this.shutDown();

    }

    public static void main(String[] args) {
        new GameLoop();
    }

    private void startUp() throws LWJGLException {
        GameLoop.game = this;
        gameOn = true;
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.setTitle(title);
        Display.create(new PixelFormat());
        // Some sort of loading.    
    }

    private void shutDown() {
        Display.destroy();
    }

    private void render() {
        // While draw etc etc.
        Display.sync(framerate);
        Display.update();
        // System.out.println("Rendering");
    }

    private void manageResources() {
        ResourceManager.update();
    }

    private void gameLogic() {
        manager.update();
        // @TODO: do shit
    }
}
