/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OldCode.Main.Asset;

import OldCode.Main.Object.Rendering.Animation;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL13;

/**
 *
 * @author 3101209
 */
public class AnimationBuilder {

    private Animation animation;

    private boolean hasAnimation = false;
    private boolean hasLooping = false;
    private boolean hasName = false;

    private boolean looping;
    private String name;
    private List<int[]> animations;

    public AnimationBuilder() {
        name = "null";
        hasName = true;
        animations = new ArrayList();
        animation = new Animation();
    }

    public AnimationBuilder addAnimation(String fileName, int time) {
        hasName = true;
        int texId = AssetLoader.loadTexture(fileName, GL13.GL_TEXTURE0);
        int[] anim = {texId, time};
        animations.add(anim);
        hasAnimation = true;
        return this;
    }

    public AnimationBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public AnimationBuilder setLooping() {
        looping = true;
        hasLooping = true;
        return this;
    }

    public AnimationBuilder setUnLooping() {
        looping = false;
        hasLooping = true;
        return this;
    }

    public Animation build() throws BuildException {
        if (hasAnimation & hasLooping & hasName) {
            int[][] array = new int[animations.size()][2];
            array = animations.toArray(array); // does this work?
            animation = new Animation(name, array, looping);
            // debug/trace
//            for (int i = 0; i < array.length; i++) {
//                for (int y = 0; y < array[0].length; y++) {
//                    System.out.print(array[i][y]+"|");
//                }
//            }
            System.out.println(animation.getFrame());
            return animation;
        } else {
            throw new BuildException();
        }
    }
}
