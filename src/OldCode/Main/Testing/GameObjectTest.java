/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OldCode.Main.Testing;

import OldCode.Main.Object.GameObject;
import OldCode.Main.Object.Transform;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author 3101209
 */
public class GameObjectTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GameObjectTest.setup();
    }

    private static void setup() {
        GameObject g = new GameObject();
        g.addComponent(new Transform(new Vector3f(1f,1f,1f), new Vector2f(10f,10f)));
        System.out.println((Transform) g.getComponent(Transform.class));
    }

}
