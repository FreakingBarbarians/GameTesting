/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import Engine.ResourceManager.ManagerState;
import MessageSystem.Message;
import OldCode.Main.Object.GameObject;
import de.matthiasmann.twl.utils.PNGDecoder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

/**
 * This class is responsible for keeping a reference to resources used in the
 * game. The goal is to use a flyweight design pattern for textures, sounds, and
 * other data things. (data things... how professional c:) Since the engine uses
 * openGL, the context must be in the same thread as the ResourceManager. Only
 * one ResourceManager will be used, so the class will be static.
 *
 * The RSM implements most of its behavior through a queue. Outside objects will
 * call RSM methods to generate messages into the evtqueue. Each game loop the
 * RSM will be given xyz time to process events in the queue.
 *
 * Each cycle the RSM updates, it checks for and deallocates resources not used
 * This has no time limit, so large amounts of deallocation may cause the game
 * to hang for small periods of time.
 *
 * The RSM has 2 states, PLAY_STATE, LOAD_STATE. PLAY_STATE gives the RSM less
 * cpu milliseconds per tick to complete load operations. LOAD_STATE gives the
 * RSM almost all the time per tick for load operations.
 *
 * 
 * Behaviors:
 * 1) Switch states between load and game, and change load time limit based on
 * that
 * 2) Load and store resources. Store static resources indefinitely, store
 * dynamic resources based on pointer count.
 * 3) Quickly give 
 * TODO!: really, find out a way to separate contexts in openGL
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

    public enum resourceTypes {

        /**
         * resource of audio type
         */
        AUDIO_RESOURCE,
        /**
         * resource of texture type
         */
        TEXTURE_RESOURCE,
        /**
         * resource of script type
         */
        SCRIPT_RESOURCE
    }

    public static final String[] AUDIO_EXTENSIONS = {"ogg", "wav"};
    public static final String[] TEXTURE_EXTENSIONS = {"png"};

    //Hashmaps are fast
    private static HashMap<String, Integer[]> textures = new HashMap();
    private static HashMap<String, Integer> staticTextures = new HashMap();

    private static HashMap<String, Object[]> audio = new HashMap();
    private static HashMap<String, Object> staticAudio = new HashMap();

    private static HashMap<String, Object[]> scripts = new HashMap();
    private static HashMap<String, Object[]> staticScripts = new HashMap();

    private static HashMap<String, Integer> programs = new HashMap();
    private static String resourcePath = ""; //TODO point this to whever src is

    /**
     * The state of the Resource Manager as stored and represented by an enum.
     */
    private static ManagerState state = ManagerState.LOAD_STATE;

    /**
     * The maximum amount of milliseconds the Resource Manager can be loading
     * things. Changes based on the Resource Manager's State
     */
    private static int maxLoadTime = 16;

    /**
     * An eventQueue for load messages
     */
    private static LinkedList<Message> eventQueue = new LinkedList();

    /**
     * Hidden constructor
     *
     * @throws InstantiationException if instantiated.
     */
    private ResourceManager() throws InstantiationException {
        System.out.println("This class is static, stop breaking things");
        throw new InstantiationException();
    }

    /**
     * Updates the Resource Manager by 1 Tick. In this tick the evt queue will
     * be processed. The RSM will check for unneeded textures.
     */
    public static void update() {
        //TODO!! The update method.
        // Prune textures for unused textures
        for (String key : textures.keySet()) {
            if (textures.get(key)[1] < 1) {
                textures.remove(key);
            }
        }
        // TODO!! Same for audio.
        // TODO!! Same for Scripts!
        // TODO!! Same for Other things?

        ResourceManager.executeEvents();
    }

    public static void setLoadState() {

    }

    /**
     * Returns the requested resource, or null if the resource does not exist.
     *
     * @param name
     * @return
     */
    public static Object getResource(String name) {
        if (staticTextures.containsKey(name)) {
            return staticTextures.get(name);
        } else if (textures.containsKey(name)) {
            return textures.get(name);
        } else if (audio.containsKey(name)) {
            return audio.get(name);
        } else if (staticAudio.containsKey(name)) {
            return audio.get(name);
        } else if (staticScripts.containsKey(name)) {
            return staticScripts.get(name);
        } else if (programs.containsKey(name)) {
            return programs.get(name);
        } else {
            System.out.println("Missed Resource: " + name);
            return null;
        }
    }

    /**
     * Notifies the RSM that an object has been destroyed with manifest
     * resources.
     *
     * @param manifest HashMap containing the name and paths of resources
     */
    public static void notifyUnload(HashMap<String, String> manifest) {
        ResourceManager.eventQueue.addLast(new unloadMessage(manifest));
    }

    /**
     * Notifies the RSM that an object needs manifest resources.
     *
     * @param manifest HashMap containing the name and paths of resources
     */
    public static void notifyLoad(HashMap<String, String> manifest) {
        ResourceManager.eventQueue.addLast(new loadMessage(manifest));
    }

    /**
     * Decrements the user count of resourceID, and deallocates if resourceID
     * has no users.
     *
     * @param resoruceID
     */
    private static void deallocate(String resoruceID) {
        if (textures.containsKey(resoruceID)) {
            Integer[] texture = textures.get(resoruceID);
            texture[1]--;
            if (texture[1] <= 0) {
                textures.remove(resoruceID);
                return;
            } else {
                textures.replace(resoruceID, texture);
            }
        } else if (audio.containsKey(resoruceID)) {
            // Same shebang with audio.
            // TODO Audio, deallocation
        }
    }

    /**
     * Processes allocate command. If the data is already allocated, it will
     * increase the user count. Otherwise it will allocate the data by loading
     * it.
     *
     * @param resourceID ID of the resource
     * @param resourcePath Path of the resource
     */
    private static void allocate(String resourceID, String resourcePath) {
        if (textures.get(resourceID) != null) {
            textures.get(resourceID)[1]++;
        } else if (staticTextures.get(resourceID) != null) {
            return;
        } else if (audio.get(resourceID) != null) {
            audio.get(resourceID)[1] = ((int) audio.get(resourceID)[1]) + 1;
        } else if (staticAudio.get(audio) != null) {
            return;
        } else if (false) {
            // @TODO For scripts.
            return;
        } else {
            // Get last characters until the "." then call the appropriate meth.
            // Probably going to refactor loadTexturePNG into loadTexture
            // And then make loadTexture handle a multitude of different texture
            // Styles.
            // TODO Load
        }
    }

    /**
     * Process load events under x total time. TODO: Stream Support. Loading
     * should be asynchronous with rendering. OpenGLContexts are being a pain
     * right in this regard. Will always load at least 1 thing per tick.
     */
    private static void executeEvents() {
        double start_time = System.nanoTime() / 1000000; // FIXME: Maybe use Sys?
        while (eventQueue.size() > 0) {
            eventQueue.removeFirst().execute();
            if (((System.nanoTime() / 1000000) - start_time) >= maxLoadTime) {
                return;
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

    //TODO: Currently loadTexture only loads into GL13.GL_TEXTURE0 and only
    //loads PNGs
    //TODO: Load Methods for Audio, Programs, and 
    /**
     * Pushes the openGL texture to the GPU and stores the ID in the hashmap. If
     * the texture already exists, does nothing.
     *
     * @param path
     */
    private static void loadTextureResource(String name, String path) {
        if (textures.containsKey(path)) {
            return;
        } else {
            Integer data[] = new Integer[2];
            data[0] = loadTexturePNG(path, GL13.GL_TEXTURE0);
            data[1] = 1;
            textures.put(name, data);
        }
    }

    /**
     * Pushes the openGL texture to the GPU and stores the ID in the hashmap. If
     * the texture already exists, does nothing.
     *
     * @param path
     */
    private static void loadStaticTextureResource(String name, String path) {
        if (staticTextures.containsKey(path)) {
            return;
        } else {
            staticTextures.put(path, loadTexturePNG(path, GL13.GL_TEXTURE0));
        }
    }

    /**
     * Loads a png texture, pushes to openGL and returns the id of the texture.
     *
     * @param filename The name of the file to be loaded
     * @param textureUnit The GL texture unit used
     * @return the id of the texture
     */
    private static int loadTexturePNG(String filename, int textureUnit) {
        ByteBuffer buf = null;
        int tWidth = 0;
        int tHeight = 0;

        try {
            // Open the PNG file as an InputStream
            InputStream in = new FileInputStream(filename);
            // Link the PNG decoder to this stream
            PNGDecoder decoder = new PNGDecoder(in);

            // Get the width and height of the texture
            tWidth = decoder.getWidth();
            tHeight = decoder.getHeight();

            // Decode the PNG file in a ByteBuffer
            buf = ByteBuffer.allocateDirect(
                    4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Create a new texture object in memory and bind it
        int texId = GL11.glGenTextures();
        GL13.glActiveTexture(textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

        // All RGB bytes are aligned to each other and each component is 1 byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        // Upload the texture data and generate mip maps (for scaling)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, tWidth, tHeight, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR_MIPMAP_LINEAR);

        return texId;
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

    /**
     * This message is appended to the evt queue when a load request is called.
     */
    private static class loadMessage extends Message {

        HashMap<String, String> resourceManifest;

        public loadMessage(HashMap<String, String> manifest) {
            resourceManifest = manifest;
        }

        public void execute() {
            for (String key : resourceManifest.keySet()) {
                ResourceManager.allocate(key, resourceManifest.get(key));
            }
        }
    }

    /**
     * This message is appended to the evt queue when an unload request is
     * called.
     */
    private static class unloadMessage extends Message {

        HashMap<String, String> resourceManifest;

        public unloadMessage(HashMap<String, String> manifest) {
            resourceManifest = manifest;
        }

        @Override
        public void execute() {
            for (String key : resourceManifest.keySet()) {
                ResourceManager.deallocate(key);
            }
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
//<editor-fold desc="deleted ideas">
//    /**
//     * The time resources stay in the game unused.
//     */
//    private static final int DECAY_TIME = 60000;
//</editor-fold>
