/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import Engine.ResourceManager.ManagerState;
import MessageSystem.Message;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class is responsible for keeping a reference to resources used in the
 * game. The goal is to use a flyweight design pattern for textures, sounds, and
 * other data things. (data things... how professional c:) Since the engine uses
 * openGL, the context must be in the same thread as the ResourceManager.
 * Generally only one ResourceManager will be used, so the class will be static.
 *
 * The RSM has 2 states, PLAY_STATE, LOAD_STATE. PLAY_STATE gives the RSM less
 * cpu milliseconds per tick to complete load operations. LOAD_STATE gives the
 * RSM almost all the time per tick for load operations.
 *
 * FIXTHIS: really, find out a way to separate contexts in openGL
 *
 * @author Raymond Gao
 */
public final class ResourceManager {

    /**
     * A Collection of States this manager can take
     */
    public enum ManagerState {

        /**
         * The Play State
         */
        PLAY_STATE,
        /**
         * The Load State
         */
        LOAD_STATE;
    }

    //Hashmaps are fast
    private static HashMap<String, Integer[]> textures = new HashMap();
    private static HashMap<String, Integer> staticTextures = new HashMap();

    private static HashMap<String, Object[]> audio = new HashMap();
    private static HashMap<String, Object> staticAudio = new HashMap();

    private static HashMap<String, Integer> programs = new HashMap();
    private static String resourcePath = ""; //TODO point this to whever src is

    /**
     * The state of the Resource Manager as stored and represented by an enum.
     */
    private static ManagerState state = ManagerState.PLAY_STATE;

    /**
     * The maximum amount of milliseconds the Resource Manager can be loading
     * things. Changes based on the Resource Manager's State
     */
    private static int maxLoadTime = 2;

    // Resource Manager will be given x time per update call to load.
    /**
     * An eventQueue for load messages
     */
    private static LinkedList<Message> eventQueue;
    /**
     * The time resources stay in the game unused.
     */
    private static final int DECAY_TIME = 60000;

    /**
     * Hidden constructor;
     *
     * @throws InstantiationException if instantiated.
     */
    private ResourceManager() throws InstantiationException {
        System.out.println("This class is static, stop breaking things");
        throw new InstantiationException();
    }

    /**
     * Updates the Resource Manager by 1 Tick.
     */
    public void update() {

//        // Prune textures for unused textures
//        for (String key : textures.keySet()) {
//            if (textures.get(key)[1] > DECAY_TIME) {
//                textures.remove(key);
//            }
//        }
//
//        // increment time unused for all textures
//        for (String key : textures.keySet()) {
//            Integer[] temp = textures.get(key);
//            temp[1] += (int) GameLoop.TICK_RATE;
//            textures.replace(key, temp);
//        }
    }

    public void deallocate(String textureID) {
        if (textures.containsKey(textureID)) {
            Integer[] texture = textures.get(textureID);
            texture[1]--;
            if (texture[1] <= 0) {
                textures.remove(textureID);
                return;
            } else {
                textures.replace(textureID, texture);
            }
        } else if (audio.containsKey(textureID)) {
            // Same shebang with audio.
        }
    }

    /**
     * Process load events under 1ms total time. TODO: Stream Support. Loading
     * should be asynchronous with rendering. OpenGLContexts are being a pain
     * right in this regard. Will always load at least 1 thing per tick.
     */
    public static void executeEvent() {
        long start_time = System.nanoTime() / 1000000; // FIXME: Maybe use Sys?
        while (eventQueue.size() > 0) {
            eventQueue.removeFirst().execute();
            if (((System.nanoTime() / 1000000) - start_time) >= maxLoadTime) {
                break;
            }
        }
    }

    /**
     * Returns the texture id of the texture with the string id passed. If the
     * texture is dynamic, it resets the decay value for that texture.
     *
     * @param id String identifier of the texture resource
     * @return the texture id or 0 if it is not found
     */
    public static int getTextureResource(String id) {
        int texture = staticTextures.getOrDefault(id, 0); // 0 is null
        if (texture == 0 && textures.containsKey(id)) {
            texture = textures.get(id)[0];
            textures.get(id)[1] = 0;
            return texture;
        }
        return texture;
    }

    /**
     * Pushes the openGL texture to the GPU and stores the ID in the hashmap. If
     * the texture already exists, does nothing.
     *
     * @param path
     */
    public static void loadTextureResource(String path) {
        if (textures.containsKey(path)) {
            return;
        } else {
            // do stuff TODO IMPLEMENT THIS
            System.out.println("load stuff, not implemented");
        }
    }

    /**
     * Pushes the openGL texture to the GPU and stores the ID in the hashmap. If
     * the texture already exists, does nothing.
     *
     * @param path
     */
    public static void loadStaticTextureResource(String path) {
        if (staticTextures.containsKey(path)) {
            return;
        } else {
            // do stuff TODO IMPLEMENT THIS
        }
    }

    /**
     * Returns the openGL program id of the program with the program id passed.
     *
     * @param id String identifier of the program
     * @return the program id or 0 if not found
     */
    public static int getProgram(String id) {
        return programs.getOrDefault(id, 0);
    }

    /**
     * Changes the state of the Resource Manager, and tweaks the max load time
     * accordingly.
     *
     * @param state Pass in states from the ManagerState enum within this class
     */
    public static void changeState(ManagerState state) {
        ResourceManager.state = state;
        if (state == ManagerState.LOAD_STATE) {
            maxLoadTime = 16;
        }
        if (state == ManagerState.PLAY_STATE) {
            maxLoadTime = 2;
        }
    }

    private class loadMessage extends Message {
        public void execute(Object[] args) {
            // Switch case to load different texture types based off enum
            // TODO This method.
        }
    }
}

// TODO: Test EVERY THING
// TODO: Consider using a private message class for allocation.
// Would be like:
/*
 allocateResource(String resourceName, String path, enum type?);
 Will make a private load message in the evtqueue. And this would allow
 the load commands to be private.
 */
