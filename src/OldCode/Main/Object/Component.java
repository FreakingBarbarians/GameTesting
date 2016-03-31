/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OldCode.Main.Object;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract Class, The most abstract of classes. Can be updated as time goes by.
 *
 * @author Raymond Gao
 */
public class Component {

    /**
     *
     */
    protected GameObject gameObject;

    private static final Class<?>[][] primitives = {
        {Boolean.class, Boolean.TYPE},
        {Character.class, Character.TYPE},
        {Byte.class, Byte.TYPE},
        {Integer.class, Integer.TYPE},
        {Long.class, Long.TYPE},
        {Float.class, Float.TYPE},
        {Double.class, Double.TYPE}
    };

    public void update(float dtime) {
        throw new UnsupportedOperationException("Unimplemented Abstract");
    }

    public boolean recieveMessage(String method, Object[] args, boolean required) {
        boolean returnValue = false;
        Class<?>[] arguments;
        if (args == null) {
            arguments = null;
        } else {
            arguments = new Class<?>[args.length];
            for (int y = 0; y < args.length; y++) {
                Boolean primitive = false;
                for (int x = 0; x < primitives.length; x++) {
                    if (args[y].getClass().equals(primitives[x][0])) {
                        arguments[y] = primitives[x][1];
                        primitive = true;
                        break;
                    }
                }
                if (!primitive) {
                    arguments[y] = args[y].getClass();
                }
            }
        }
        try {
            Method m = this.getClass().getDeclaredMethod(method, arguments);
            m.invoke(this, args);
        } catch (NoSuchMethodException ex) {
            return false;
        } catch (SecurityException ex) {
            ex.printStackTrace();
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public void destroy() {
        throw new UnsupportedOperationException("Unimplemented Abstract");
    }

    public void setObject(GameObject object) {
        this.gameObject = object;
    }
}
