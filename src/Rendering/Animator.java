/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rendering;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * Adds an animation with a specified name as the key. Throws an
     * AnimatorException. Will NOT overwrite the local name inside each
     * animation. Instead the name passed will overshadow the name. Useful for
     * setting default animations like "idle".
     *
     * @param name
     * @param animation
     * @throws AnimatorException
     */
    public void addAnimation(Animation animation, String name)
            throws AnimatorException {

        if (animations.containsKey(name)) {
            throw new AnimatorException("Animation Name Already Exists: "
                    + animation.getName());
        }
        animations.put(name, animation);
    }

    /**
     * Adds an animation using the animation's local name as the key. Throws an
     * AnimatorException.
     *
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
     * @param name Key of the animation
     * @param interrupt
     * @throws AnimatorException
     */
    public void playAnimation(String name, boolean interrupt)
            throws AnimatorException {
        if (!animations.containsKey(name)) {
            throw new AnimatorException("Cannot play: " + name
                    + ", animation does not exist");
        }

        // Interrupting play. Idle can always be interrupted
        if (interrupt || activeAnimation == animations.get("idle")) {
            // Check if activeAnimation is null
            if (activeAnimation == null) {
                activeAnimation = this.animations.get(name);
                activeAnimation.play();
            } else {
                // Otherwise
                // Stop and reset the current anim (interrupt it)
                activeAnimation.stop();
                activeAnimation.reset();
                // Play new anim
                activeAnimation = animations.get(name);
                activeAnimation.play();

            }
        } // Non-interrupting play
        else {
            if (activeAnimation == null) {
                activeAnimation = animations.get(name);
                activeAnimation.play();
                return;
            }

            if (activeAnimation.isPlaying()) {
                throw new AnimatorException("Cannot play: " + name
                        + ", animation already playing");
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
            if (activeAnimation != null && activeAnimation.isPlaying()) {
                activeAnimation.animate(dtime);
            } else {
                this.playAnimation("idle", true);
            }
            return activeAnimation.getFrame();
        } catch (TimingException e) {
            throw new AnimatorException("Could not resolve frame: animation: "
                    + activeAnimation.getName() + " at animation time: "
                    + activeAnimation.animatedTime + "ms");
        }
    }

    /**
     * Returns the id of the active texture (frame)
     *
     * @return the id of the active texture
     * @throws AnimatorException if no animation is playing (but idle should
     * always be playing so, no animations in the animator)
     */
    public int getFrame() throws AnimatorException {
        if (activeAnimation != null) {
            return activeAnimation.getFrame();
        } else {
            try {
                this.playAnimation("idle", false);
            } catch (AnimatorException ex) {
                ex.appendToErrorMessage("no default animation to play");
                throw ex;
            }
        }
        return activeAnimation.getFrame();
    }
}

// Think, should the animator interrupt, or should the animation be interruptable?
