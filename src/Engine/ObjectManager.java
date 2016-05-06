/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import Components.GameObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A collection of game objects. Objects deleted and added are put into a list
 * before being added, the additions/deletions will be made next tick.
 *
 */
public class ObjectManager {

    /**
     * List of objects to be added this tick, will be added next tick.
     */
    private List<GameObject> addQueue;
    /**
     * List of objects that are active.
     */
    private Map<Integer, GameObject> Objects;
    /**
     * List of objects deleted this tick, will be removed next tick.
     */
    private List<GameObject> removeQueue;

    /**
     * Creates an ObjectManager with no objects.
     */
    public ObjectManager() {
        addQueue = new LinkedList();
        Objects = new HashMap();
        removeQueue = new LinkedList();
    }

    /**
     * Queues an object that will be added next tick.
     *
     * @param object
     */
    public void addObject(GameObject object) {
        addQueue.add(object);
    }

    /**
     *
     * @param object
     */
    public void removeObject(GameObject object) {
        removeQueue.add(object);
    }

    /**
     * Process both queues, then updates each object.
     */
    public void update() {
        for (GameObject object : removeQueue) {
            Objects.remove(object.getID().hashCode());
        }
        removeQueue.clear();
        for (GameObject object : addQueue) {
            Objects.put(object.getID().hashCode(), object);
        }
        Objects.clear();
        for (GameObject object : Objects.values()) {
            object.update();
        }
    }
}
