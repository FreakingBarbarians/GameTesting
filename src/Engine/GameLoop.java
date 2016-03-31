/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

/**
 * The GameLoop is responsible for creating an instance of the game and
 * listening for input, and managing game logic and rendering. The GameLoop also
 * holds our OpenGL context, and preforms OpenGL related Loading. Because of
 * this design, loading will cause the game loop to freeze. <--- Problem?>
 *
 *
 * Timing: Timing will be based off ticks. We'll go for a modest 64 ticks per
 * second. 1 tick every 16.625 seconds.
 *
 * @author
 */
public class GameLoop {

    public static final float TICK_RATE = 16.625f;
    
    public static void main(String[] args) {
        
    }

}
