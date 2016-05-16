/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import Engine.ObjectManager;
import java.util.ArrayList;
import java.util.HashMap;
import Engine.RSM;
import java.util.List;
import java.util.UUID;

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
     * called "turbine 1" "turbine 2" etc. Local space meaning... within the
     * same GO
     */
    private final String name;

    /**
     * Unique id of the Object. Sub Objects share parent id but have different
     * name. Okay, so there is an astronomically low chance that the id's will
     * be duplicated. hmm switch?
     */
    private final UUID id;

    /**
     * Collection of this object's components
     */
    private List<Component> components;

    /**
     * Collection of this object's subObjects
     */
    private List<GameObject> gameObjects;

    /**
     * The Parent of this Object
     */
    private GameObject parent;

    /**
     * List of resources this G-O requires.
     */
    private HashMap<String, String> manifest;

    /**
     * Whether it is destroyed.
     */
    private boolean destroyed;

    /**
     * This object's manager.
     */
    ObjectManager manager;

    /**
     * Creates a gameObject with Manifest Resources
     *
     * @param manifest
     */
    private GameObject(UUID id, String name, HashMap<String, String> manifest,
            List<GameObject> gameObjects, List<Component> components,
            GameObject parent, ObjectManager manager) { // add ObjectManager
        this.destroyed = false;
        this.id = id;
        this.name = name;
        this.manifest = manifest;
        this.components = components;
        this.gameObjects = gameObjects;
        this.parent = parent;
        this.manager = manager;
        RSM.notifyLoad(manifest);
    }

    /**
     * Clone Constructor
     *
     * @param original
     */
    private GameObject(GameObject original) {
        id = UUID.randomUUID();
        name = new String(original.getName());
        manifest = new HashMap<String, String>(original.getManifest());
        gameObjects = new ArrayList(original.getGameObjects());
        components = new ArrayList(original.getComponents());
        parent = new GameObject(original.getParent());
    }

    /**
     * Returns the manifest of this GameObject.
     *
     * @return
     */
    public HashMap<String, String> getManifest() {
        return this.manifest;
    }

    /**
     * returns the name of the gameObject. The Name is non unique.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets this object's parent
     *
     * @param parent
     */
    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    /**
     * Returns this object's parent
     *
     * @return
     */
    public GameObject getParent() {
        return parent;
    }

    /**
     * Returns a unique ID for the gameObject. The id is unique. yay!
     *
     * @return
     */
    public UUID getID() {
        return this.id;
    }

    public void update() {
        for (GameObject subObject : gameObjects) {
            subObject.update();
        }
        for (Component component : components) {
            component.update();
        }
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

    /**
     * Returns a list of this object's components
     *
     * @return
     */
    public List<Component> getComponents() {
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
        c.setObject(this);
    }

    /**
     * Returns a sub-GameObject with specified name
     *
     * @param name
     * @return
     */
    public GameObject getGameObject(String name) {
        for (GameObject obj : gameObjects) {
            if (obj.getName().equals(name)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Returns all subGameObjects.
     *
     * @return
     */
    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    /**
     * Sends a message to components in this GO and all Sub-GO's to invoke
     * methods with name method and arguments args.
     *
     * @param method Method name
     * @param args Arguments
     */
    public void sendMessageDown(String method, Object[] args) {
        for (Component e : components) {
            e.recieveMessage(method, args);
        }
        for (GameObject g : gameObjects) {
            g.sendMessageDown(method, args);
        }
    }

    /**
     * Sends a message to components in this GO and all parent GO's to invoke
     * methods with name method and arguments args
     *
     * @param method Method name
     * @param args Arguments
     */
    public void sendMessageUp(String method, Object[] args) {
        for (Component e : components) {
            e.recieveMessage(method, args);
        }
        if (parent != null) {
            parent.sendMessageUp(method, args);
        }
    }

    /**
     * Sends a message to components in this GO to invoke methods with name
     * method and arguments args
     *
     * @param method Method name
     * @param args Argument
     */
    public void sendMessage(String method, Object[] args, boolean required) {
        for (Component e : components) {
            e.recieveMessage(method, args);
        }
    }

    public void Destroy() {
        // Append thos to a "TOBEDESTROYED QUEUE"
        // @TODO Dependant Method
        // GameLoop.notifyDestroy or sth like that. 
        // release id
        // run a "destroyed" script
        this.destroyed = true;
    }

    public void Destroy(float dt) {
        // Probably append this to a "TOBEDESTROYED" queue.
        // @TODO Dependant Method
        // wait dt milliseconds and get manifest to RSM
        // release id
        // run a "destroyed script"
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public static class GameObjectBuilder {

        private HashMap<String, String> manifest = new HashMap();
        private UUID id = null;
        private String name;
        private GameObject parent;
        private List<GameObject> gameObjects;
        private List<Component> components;
        private ObjectManager manager;

        public GameObjectBuilder() {
            id = UUID.randomUUID();
            gameObjects = new ArrayList();
            components = new ArrayList();
        }

        public GameObjectBuilder addResource(String resourceID, String path) {
            manifest.putIfAbsent(resourceID, path);
            return this;
        }

        public GameObjectBuilder removeResource(String resourceID) {
            manifest.remove(resourceID);
            return this;
        }

        public GameObjectBuilder setParent(GameObject parent) {
            this.parent = parent;
            return this;
        }

        public GameObjectBuilder addGameObject(GameObject subObject) {
            gameObjects.add(subObject);
            return this;
        }

        public GameObjectBuilder addComponent(Component component) {
            components.add(component);
            return this;
        }

        public GameObjectBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public GameObjectBuilder setManager(ObjectManager manager) {
            this.manager = manager;
            return this;
        }

        public GameObject build() {
            return new GameObject(id, name, manifest, gameObjects, components, parent, manager);
        }
    }

    public GameObject clone() {
        return new GameObject(UUID.randomUUID(), new String(this.name),
                new HashMap<String, String>(this.manifest),
                new ArrayList<GameObject>(this.gameObjects),
                new ArrayList<Component>(this.components),
                new GameObject(this.parent), this.manager);
    }
}

// Should the world be a game object?
// That would be cool i guess...
