/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OldCode.Main.Testing;

import OldCode.Main.Asset.AnimationBuilder;
import OldCode.Main.Asset.AssetLoader;
import OldCode.Main.Asset.BuildException;
import OldCode.Main.Object.Rendering.Animation;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;

/**
 *
 * @author 3101209
 */
public class AnimationBuilderTest {

    /**
     * @param args Ehhhh
     */
    public static void main(String[] args) {
        new AnimationBuilderTest();
    }

    public AnimationBuilderTest() {
        setup();
    }

    public void setup() {
        try {
            Display.create();
        } catch (LWJGLException ex) {
            System.out.println("Failed to Create OpenGL Context");
            Logger.getLogger(AnimationBuilderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        AnimationBuilder b = new AnimationBuilder();
        b.setLooping();
        b.addAnimation("RedBall.png", 10);
        b.addAnimation("ShittyRocket.png", 20);
        try {
            Animation a = b.build();
        } catch (BuildException be) {
            System.out.println("Build Failed");
        }
    }

    public void setDown() {
        Display.destroy();
    }

}
