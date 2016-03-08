/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test.Object.Rendering;

import Test.Object.Component;
import Test.Object.Rendering.GLQuad;
import Test.Object.Rendering.Animation;
import Test.Object.Transform;
import java.util.ArrayList;
import org.lwjgl.util.vector.Matrix4f;

/**
 *
 * @author 3101209
 */
public class Renderer extends Component {

    private Animator animator = null;
    private Transform transform;
    private Matrix4f modelMatrix;

    public Renderer(Transform transform, Animator animator) {
        this.transform = transform;
        this.animator = animator;
    }

    private void draw() {
        
    }

}
