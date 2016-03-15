/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Object.Rendering;

import Main.Object.Component;
import Main.Object.Rendering.GLQuad;
import Main.Object.Rendering.Animation;
import Main.Object.Transform;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Does open Gl Stuff.
 *
 * For programs: Uses matrices to translate local coordinates to world world
 * coordinates. As well, ensures that: attrib 0 = is the input position attrib 1
 * = input color attrib 2 = input ST/UV coordinate (texture coordinate)
 *
 * The program needs a model Matrix, view Matrix, and projection Matrix
 *
 * It draws quads using their triangle components
 *
 * Each renderer has access to it's own animator, that does animation
 *
 * @author Ray
 */
public class Renderer extends Component {

    /**
     * Animator that returns a texture
     */
    private final Animator animator;

    /**
     * The transform component of the gameObject, where dimensions and world
     * coordinates are stored
     */
    private final Transform transform;

    /**
     * the modelMatrix of the gameObject that will work on the quad to move it
     * into it's world coordinates, and scale it, and rotate it
     */
    private Matrix4f modelMatrix;

    /**
     * The GLQuad that is a collection of vertices representing a rectangle.
     */
    private final GLQuad quad;

    /**
     * the id of the opengl shader program that is being used
     */
    private int program = -1;

    /**
     * Creates a new renderer component with a transform and an animator
     *
     * @param transform
     * @param animator
     */
    public Renderer(Transform transform, Animator animator) {
        modelMatrix = new Matrix4f();
        this.transform = transform;
        this.animator = animator;
        this.quad = new GLQuad(this.transform.getBaseDimensions());
    }

    /**
     * Creates a new renderer with a transform, animator and program id
     *
     * @param transform
     * @param animator
     * @param programId
     */
    public Renderer(Transform transform, Animator animator, int programId) {
        modelMatrix = new Matrix4f();
        this.transform = transform;
        this.animator = animator;
        this.quad = new GLQuad(this.transform.getBaseDimensions());
    }

    /**
     * Sets the openGl shader program that this renderer will use
     *
     * @param programId
     */
    public void setProgram(int programId) {
        this.program = programId;
    }

    /**
     * Draws the Quad object to the screen. A program must be set.
     * 
     * @throws RenderException with an appropriate error message
     */
    private void draw() throws RenderException {
        // @TODO: more error handling + testing 
        
        //If no program return error
        if (this.program == -1) {
            throw new RenderException("No program");
        }

        // manipulate matrix
        
        modelMatrix.translate(transform.position);
        modelMatrix.scale(new Vector3f(transform.scale.x, transform.scale.y, 1));
        modelMatrix.rotate((float) Math.toDegrees((double) transform.rotation.x),
                new Vector3f(1, 0, 0));
        modelMatrix.rotate((float) Math.toDegrees((double) transform.rotation.y),
                new Vector3f(0, 1, 0));
        modelMatrix.rotate((float) Math.toDegrees((double) transform.rotation.z),
                new Vector3f(0, 0, 1));
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
        modelMatrix.store(matrixBuffer);

        GL20.glUseProgram(program);

        GL20.glUniformMatrix4(GL20.glGetUniformLocation(program, "modelMatrix"),
                false, matrixBuffer);

        GL30.glBindVertexArray(quad.getVertexArrayId());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, animator.getFrame());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, quad.getIndiceArrayId());

        GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);
        
        
    }

}
