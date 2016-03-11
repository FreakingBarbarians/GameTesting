/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test.Object.Rendering;

import java.util.Hashtable;

/**
 * Controls animation. State can be changed. Keeps track if the animation is
 * done or not. Only one animation can play at a time
 *
 * - The animator will default to an idle anim, if no other animation is playing
 * - The idle animation should be called "idle" and be looping. - The idle
 * animation is always interrupted, - Things that are not animated should just
 * have an idle animation, and that will play forever.
 *
 * @author Raymond Gao
 */
public class Animator {

    /**
     * Dictionary of animations
     */
    private Hashtable<String, Animation> animations;

    /**
     * The active animation, only one can play at a time
     */
    private Animation activeAnimation;

    /**
     * instantiates an animator with no animations
     */
    public Animator() {
        animations = new Hashtable<String, Animation>();
        activeAnimation = null;
    }

    /**
     * instantiates an animator with animations
     *
     * @param animations the animations
     */
    public Animator(Hashtable<String, Animation> animations) {
        this.animations = animations;
        activeAnimation = animations.get("idle");
    }

    /**
     * Adds an animation with name. Throws an AnimatorException
     *
     * @param name
     * @param animation
     * @throws AnimatorException
     */
    public void addAnimation(Animation animation) throws AnimatorException {
        if (animations.containsKey(animation.getName())) {
            throw new AnimatorException("Animation Name Already Exists: "
                    + animation.getName());
        }
        animations.put(animation.getName(), animation);
    }

    /**
     * Plays the animation with name. If interrupt is true it will stop and
     * reset the animation that is playing and play the specified animation. If
     * interrupt is false, it will play the specified animation if the previous
     * animation finished playing.
     *
     * @param name
     * @param interrupt
     * @throws AnimatorException
     */
    public void playAnimation(String name, boolean interrupt)
            throws AnimatorException {
        if (!animations.containsKey(name)) {
            throw new AnimatorException("Cannot play: " + name + ", animation does not exist");
        }

        // Interrupting play
        if (interrupt || activeAnimation.getName().equals("idle")) {
            // Stop and reset the current anim (interrupt it)
            activeAnimation.stop();
            activeAnimation.reset();
            // Play new anim
            activeAnimation = animations.get(name);
            activeAnimation.play();
        } // None interrupting play
        else {
            if (activeAnimation.isPlaying()) {
                throw new AnimatorException("Cannot play: " + name + ", animation already playing");
            }
            // reset
            activeAnimation.reset();
            // play
            activeAnimation = animations.get(name);
            activeAnimation.play();
        }
    }

    /**
     * progresses the active animation by dtime in milliseconds and returns the
     * id of the texture of the correct frame.
     *
     * @param dtime
     * @return
     * @throws AnimatorException
     */
    public int animate(float dtime) throws AnimatorException {
        try {
            return activeAnimation.animate(dtime);
        } catch (TimingException e) {
            throw new AnimatorException("Could not resolve frame: animation: "
                    + activeAnimation.getName() + " at animation time: "
                    + activeAnimation.animatedTime + "ms");
        }

    }

    /**
     * Returns the id of the active texture (frame)
     * @return 
     */
    public int getFrame() {
        return activeAnimation.getFrame();
    }
}


// Think, should the animator interrupt, or should the animation be interruptable?
