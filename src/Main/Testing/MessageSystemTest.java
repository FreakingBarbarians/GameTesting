/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Testing;

import Main.Object.Component;
import Main.Object.GameObject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * I hate reflection
 *
 * @author Ray
 */
public class MessageSystemTest {

    private static Class<?>[][] primitives = {
        {Boolean.class, Boolean.TYPE},
        {Character.class, Character.TYPE},
        {Byte.class, Byte.TYPE},
        {Integer.class, Integer.TYPE},
        {Long.class, Long.TYPE},
        {Float.class, Float.TYPE},
        {Double.class, Double.TYPE}
    };

    public static void main(String[] args) {

        Object[] o = new Object[2];
        o[0] = 1;
        o[1] = "FACK";

//        Class<?>[] arguments = new Class<?>[2];
//        //System.out.println(o[0].getClass().equals(Integer.class));
//        for (int y = 0; y < o.length; y++) {
//            Boolean bitch = false;
//            for (int x = 0; x < primitives.length; x++) {
//                if (o[y].getClass().equals(primitives[x][0])) {
//                    arguments[y] = primitives[x][1];
//                    bitch = true;
//                    break;
//                }
//            }
//            if (!bitch) {
//                arguments[y] = o[y].getClass();
//            }
//        }
//        for (int i = 0; i < arguments.length; i++) {
//            System.out.println(arguments[i]);
//        }
        Component c1 = new TestComponent();
        GameObject obj = new GameObject();
        obj.addComponent(c1);

        obj.sendMessageDown("test", null, true);
        obj.sendMessageDown("test", o, true);
    }
}
