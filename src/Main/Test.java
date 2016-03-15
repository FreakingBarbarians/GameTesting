/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author 3101209
 */
public class Test {

    private boolean gameOn = true;

    public Test() {
        new View(1280, 720, "Test Window", this).run();
    }

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        new Test();
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

}
