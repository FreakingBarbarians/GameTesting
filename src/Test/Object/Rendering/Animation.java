/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test.Object.Rendering;

import Test.Object.TimingException;

/**
 *
 * @author 3101209
 */
public class Animation {

    /**
     * An animation, stores an array of loaded Textures' ID's in the GL Context
     * Has an array: animArray [id1][id2][id3][id4][id5] [t1][t2][t3][t4][t5] A
     * counter: state keeps track of what frame the anim is on
     */
    int[][] animArray;
    float time;
    float animTime;

    public Animation(int[][] animArray) {
        this.animArray = animArray;
        time = 0;
        animTime = 0;
        for (int i = 0; i < animArray.length; i++) {
            animTime += animArray[i][1];
        }
    }
    
    public int getTexture(float dtime) throws TimingException {
        time += dtime;
        if (time >= animTime) {
            time -= animTime;
        }

        for (int i = animArray.length - 1; i >= 0; i--) {
            if (time > animArray[i][1]) {
                return animArray[i][0];
            }
        }

        throw new TimingException();
    }
}
