/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test.Object.Rendering;

import Test.Object.TimingException;

/**
 * Holds an animation, and can play through it. Does not draw, instead returns
 * an id corresponding to a texture.
 *
 * @author Raymond Gao
 */
public class Animation {

    /**
     * An animation, stores an array of loaded Textures' ID's in the GL Context
     * Has an array: animArray [id1][id2][id3][id4][id5] [t1][t2][t3][t4][t5] A
     * counter: state keeps track of what frame the anim is on
     */
    int[][] animArray;

    /**
     * The time that the animation has animated
     */
    float animatedTime;

    /**
     * The total time of the animation
     */
    float animationTime;

    /**
     * Whether or not the animation is looping
     */
    private boolean looping;

    /**
     * Whether or not the animation is over
     */
    private boolean over = true;

    /**
     * Creates an animation based on an array, and whether or not the anim is
     * looping
     *
     * @param animArray
     * @param looping
     */
    public Animation(int[][] animArray, boolean looping) {
        this.looping = looping;
        this.animArray = animArray;
        animatedTime = 0;
        animationTime = 0;
        for (int i = 0; i < animArray.length; i++) {
            animationTime += animArray[i][1];
        }
    }

    /**
     * Sets the animation to playing
     */
    public void play() {
        this.over = false;
    }

    /**
     * Sets the animation to not playing
     */
    public void stop() {
        this.over = true;
    }

    /**
     * Resets the animation
     */
    public void reset() {
        animatedTime = 0;
    }

    /**
     * If the animation is playing, progresses the animation time by dtime.
     * Returns the id of the texture (frame) that the animation is on. If the
     * animations looping, once animatedTime >= animationTime animatedTime is
     * reset and the animation keeps playing. Otherwise the animation is set to
     * not playing.
     *
     * Note you can still get the frame for a non-playing animation. It is just
     * frozen (unchanging)
     *
     * @param dtime the change in time, in milliseconds
     * @return the id of the animation
     * @throws TimingException if cannot get animation
     */
    public int animate(float dtime) throws TimingException {
        if (!over) {
            animatedTime += dtime;
            if (animatedTime >= animationTime) {
                if (!looping) {
                    animatedTime -= animationTime;
                } else {
                    this.over = true;
                }
            }
        }

        for (int i = animArray.length - 1; i >= 0; i--) {
            if (animatedTime > animArray[i][1]) {
                return animArray[i][0];
            }
        }
        throw new TimingException(); // change this exception to animation exception
    }

}
