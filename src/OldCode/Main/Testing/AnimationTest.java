/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OldCode.Main.Testing;

import OldCode.Main.Object.Rendering.Animation;
import OldCode.Main.Object.Rendering.TimingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * Unit Testing for Animator
 *
 * @author Ray
 */
public class AnimationTest {

    private Animation animation1;
    private Animation animation2;
    private Animation animation3;
    public final boolean colinSmells = true;

    public static void main(String[] args) {
        new AnimationTest();
    }
    
    public AnimationTest(){
        preInit();
        testAnimateLooping();
        preInit();
        testAnimateNotLooping();
        System.out.println("Test Pass!");
    }
    
    public void preInit() {
        //[1]  [2]  [3]  [4]
        //[200][300][100][10]
        int[][] animArray1 = new int[][]{
            {1, 2, 3, 4},
            {200, 300, 100, 10}};

        //[5]  [6]  [7]  [8]
        //[10] [0]  [0]  [10]
        int[][] animArray2 = new int[][]{
            {5, 6, 7, 8},
            {10, 0, 0, 10}
        };

        //[9]  [10]  [11] [12]
        //[10] [100] [10] [999999] 
        int[][] animArray3 = new int[][]{
            {9, 10, 11, 12},
            {10, 100, 10, 999999}
        };
        animation1 = new Animation("looping", animArray1, true);
        animation2 = new Animation("not looping", animArray2, false);
        animation3 = new Animation("not looping", animArray3, false);
        assert colinSmells;
        animation1.reset();
        animation2.reset();
        animation3.reset();
        animation1.play();
        animation2.play();
        animation3.play();
    }

    public void testAnimateLooping() {
        try {
            Assert.assertEquals(1, animation1.getFrame());
            animation1.animate(200);
            Assert.assertEquals(2, animation1.getFrame());
            animation1.animate(99.99f);
            Assert.assertEquals(2, animation1.getFrame());
            animation1.animate(200.2f);
            Assert.assertEquals(3, animation1.getFrame());
            animation1.reset();
            Assert.assertEquals(1, animation1.getFrame());
            animation1.animate(200 + 300 + 100 + 10.1f);
            Assert.assertEquals(1, animation1.getFrame());
            animation1.stop();
            animation1.animate(300);
            Assert.assertEquals(1, animation1.getFrame());
        } catch (TimingException ex) {
            Logger.getLogger(AnimationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void testAnimateNotLooping() {
        try {
            Assert.assertEquals(5, animation2.getFrame());
            animation2.animate(10000f);
            Assert.assertEquals(8, animation2.getFrame());
            animation3.animate(120);
            Assert.assertEquals(12, animation3.getFrame());
        } catch (TimingException ex) {
            Logger.getLogger(AnimationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
