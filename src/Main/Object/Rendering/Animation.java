/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Object.Rendering;

/**
 * Holds an animation, and can play through it. Does not draw, instead returns
 * an id corresponding to a texture.
 *
 * @author Raymond Gao
 */
public class Animation {

    /**
     * the name of the animation, idle is a special name. Refer to animator doc
     * for more information
     */
    private String name;

    /**
     * An animation, stores an array of loaded Textures' ID's in the GL Context
     * Has an array: animArray [id1][id2][id3][id4][id5] [t1][t2][t3][t4][t5] A
     * counter: state keeps track of what frame the anim is on in milliseconds
     */
    int[][] animArray;

    /**
     * An array that stores the time when each frame should play.
     */
    int cumulativeTime[];

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
     * The OpenGL id for the frame of animation
     */
    private int frameId;

    /**
     * Creates an animation based on an array, and whether or not the anim is
     * looping
     *
     * @param name
     * @param animArray
     * @param looping
     */
    public Animation(String name, int[][] animArray, boolean looping) {
        this.name = name;
        this.looping = looping;
        this.animArray = animArray;
        animatedTime = 0;
        animationTime = 0;
        for (int i = 0; i < animArray[0].length; i++) {
            animationTime += animArray[1][i];
        }

        cumulativeTime = new int[animArray[0].length];
        cumulativeTime[0] = 0;
        for (int i = 1; i < cumulativeTime.length; i++) {
            cumulativeTime[i] = cumulativeTime[i - 1] + animArray[1][i - 1];
        }
        this.frameId = animArray[0][0];
    }

    public Animation() {

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
        this.frameId = animArray[0][0];
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
     * @throws TimingException if cannot get animation
     */
    public void animate(float dtime) throws TimingException {
        if (!over) {
            animatedTime += dtime;
            if (animatedTime >= animationTime) {
                if (looping) {
                    animatedTime = animatedTime % animationTime;
                } else {
                    this.over = true;
                }
            }
            for (int i = cumulativeTime.length - 1; i >= 0; i--) {
                if (animatedTime >= cumulativeTime[i]) {
                    // System.out.println(animatedTime + "|" + cumulativeTime[i]);
                    this.frameId = animArray[0][i];
                    return;
                }
            }
            throw new TimingException();
        }
    }

    /**
     * Returns true, if the animation is playing
     *
     * @return
     */
    public boolean isPlaying() {
        return !this.over;
    }

    /**
     * returns the name of this animation
     *
     * @return
     */
    public String getName() {
        return name;
    }

    public int getFrame() {
        return this.frameId;
    }
}
