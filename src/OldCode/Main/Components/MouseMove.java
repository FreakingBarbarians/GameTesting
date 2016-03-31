/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OldCode.Main.Components;

import OldCode.Main.Object.Component;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author 3101209
 */
public class MouseMove extends Component {

    public MouseMove() {

    }

    @Override
    public void update(float dtime) {
        Object[] arguments = new Object[2];
        arguments[0] = (float) 10 * Mouse.getDX() / Deprecated.Main.Main.WIDTH;
        arguments[1] = (float) 10 * Mouse.getDY() / Deprecated.Main.Main.HEIGHT;
        System.out.println(arguments[0] + "|" + arguments[1]);
        this.gameObject.sendMessage("translate", arguments, true);
    }
}
