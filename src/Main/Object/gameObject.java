/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Object;

import Main.Object.Rendering.Animator;
import Main.Object.GameObject;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

/**
 * Any Object in the game world is a game Object. It is an empty container
 * defined by it's components.
 *
 * @author Raymond Gao
 */
public class GameObject {

    /**
     * Name of the Object, non-Unique in world space. Unique in local space i.e.
     * two "plane" objects can be in the world, but turbine objects must be
     * called "turbine 1" "turbine 2" etc.
     */
    private String name;

    /**
     * Unique id of the Object. Sub Objects share parent id but have differen
     * name
     */
    private int id;

    /**
     * Collection of this object's components
     */
    private ArrayList<Component> components;

    /**
     * Collection of this object's subObjects
     */
    private ArrayList<GameObject> gameObjects;

    /**
     * The Parent of this Object
     */
    private GameObject parent;

    public GameObject() {
        this.components = new ArrayList();
        this.gameObjects = new ArrayList();
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the first component of specified type.
     *
     * @param type Class of component
     * @return the component if exists, null if does not exist
     */
    public Component getComponent(Class type) {
        for (Component component : components) {
            if (component.getClass().equals(type)) {
                return component;
            }
        }
        return null;
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }

    /**
     * Adds a component to this gameobject if a component of that type does not
     * already exist. If it does exist, nothing will happen
     *
     * @param c The component
     */
    public void addComponent(Component c) {
        for (Component C : components) {
            if (c.getClass().equals(C.getClass())) {
                return;
            }
        }
        components.add(c);
    }

    public GameObject getGameObject(String name) {
        for (GameObject obj : gameObjects) {
            if (obj.getName().equals(name)) {
                return obj;
            }
        }
        return null;
    }

    public ArrayList<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    /**
     *
     * @param method
     * @param args
     * @param required
     * @throws MessageError
     */
    public void sendMessageDown(String method, Object[] args, boolean required) {
        for (Component e : components) {
            e.recieveMessage(method, args, required);
        }
        for (GameObject g : gameObjects) {
            g.sendMessageDown(method, args, required);
        }
    }

    public void sendMessageUp(String method, Object[] args, boolean required) {
        for (Component e : components) {
            e.recieveMessage(method, args, required);
        }
        if (parent != null) {
            parent.sendMessageUp(method, args, required);
        }
    }

    public void sendMessage(String method, Object[] args, boolean required) {

    }
}

// Should the world be a game object?
// That would be cool i guess...
