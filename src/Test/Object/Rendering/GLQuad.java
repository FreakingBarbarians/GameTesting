/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test.Object.Rendering;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;

public class GLQuad {

    // get transform dimensions, translate into vertices and put in buffer.
    // GLQuad is immutable
    // Vertices, in a 2d array, where each row is a vertex. 
    // generally for squares, it is 4 by 4
    // 0th row is top left vertex, rest are in order counter-clockwise.
    /**
     * The vertices of this shape, where 0 is the top left vertex, and each
     * vertex after is the next counterclockwise vertex
     */
    private final float[][] vertices;

    /**
     * The indices that define the triangles to draw to create the quad
     */
    private static final byte[] indices = {
        0, 1, 0,
        0, 2, 3
    };

    /**
     * the type of element, an int that matched the type in OpenGL
     */
    private static final int elmentType = GL11.GL_FLOAT;

    /**
     * The size of each element in bytes
     */
    private static final int elementSize = 4;

    /**
     * The number of vertices in this shape
     */
    private static final int numVertices = 4;

    /**
     * The number of elements that compose the position of each vertex
     */
    private static final int positionElements = 4;

    /**
     * The number of elements that compose the color of each vertex
     */
    private static final int colorElements = 4;

    /**
     * The number of elements that compose the texture coordinates of each
     * vertex
     */
    private static final int textureElements = 2;

    /**
     * The number of elements per vertex
     */
    private static final int elementsPerVertex = positionElements
            + colorElements
            + textureElements;

    /**
     * The number of elements per shape
     */
    private static final int elements = elementsPerVertex * numVertices;

    /**
     * The number of bytes per vertex
     */
    private static final int bytesPerVertex = numVertices * elementsPerVertex;

    /**
     * The offset of the position elements in the databuffer in byte
     */
    private static final int positionOffset = 0;

    /**
     * the offset of the color elements in the databuffer in byte
     */
    private static final int colorOffset = positionElements * elementSize;

    /**
     * the offset of the texture coordinate elements in the databuffer in bytes
     */
    private static final int textureOffset = colorOffset + colorElements
            * elementSize;

    /**
     * The total number of bytes per shape
     */
    private static final int size = bytesPerVertex * numVertices;

    /**
     * the id of the vertex array associated with this object
     */
    private final int vaoId;

    /**
     * the id of the buffer array associated with this object
     */
    private final int vboId;

    /**
     * the id of the index array associated with this object
     */
    private final int vboiId;

    /**
     * creates a new quad with dimensions
     *
     * @param dimensions
     */
    public GLQuad(Vector2f dimensions) {
        vertices = new float[][]{
            {-dimensions.x / 2, dimensions.y / 2, 0, 1f},
            {-dimensions.x / 2, -dimensions.y / 2, 0, 1f},
            {dimensions.x / 2, -dimensions.y / 2, 0, 1f},
            {dimensions.x / 2, dimensions.y / 2, 0, 1f}
        };

        float[][] rgba = new float[][]{
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1}
        };

        float[][] st = new float[][]{
            {0, 0},
            {0, 1},
            {1, 1},
            {1, 0}
        };

        FloatBuffer data = BufferUtils.createFloatBuffer(elements);

        for (int i = 0; i < numVertices; i++) {
            data.put(vertices[i]);
            data.put(rgba[i]);
            data.put(st[i]);
        }

        data.flip();

        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        vboId = GL15.glGenBuffers();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, GLQuad.positionElements,
                GLQuad.elmentType, false, GLQuad.bytesPerVertex,
                GLQuad.positionOffset);

        GL20.glVertexAttribPointer(0, GLQuad.positionElements,
                GLQuad.elmentType, false, GLQuad.bytesPerVertex,
                GLQuad.colorOffset);

        GL20.glVertexAttribPointer(0, GLQuad.positionElements,
                GLQuad.elmentType, false, GLQuad.bytesPerVertex,
                GLQuad.textureOffset);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        ByteBuffer indiceBuffer = BufferUtils.createByteBuffer(indices.length);
        indiceBuffer.put(indices);
        indiceBuffer.flip();

        vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indiceBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Returns the id of the object's associated vertex array
     *
     * @return int vertex array id
     */
    public int getVertexArrayId() {
        return vaoId;
    }

    /**
     * Returns the id of the object's associated indice array
     *
     * @return int indice array id
     */
    public int getIndiceArrayId() {
        return vboiId;
    }

    /**
     * Returns the id of the object's associated buffer array
     *
     * @return int buffer array id
     */
    public int getBufferArrayId() {
        return vboId;
    }

    /**
     * Releases the data contained in this GLQuad and destroys it, must be
     * called before deleting to prevent memory leaks
     */
    public void destroy() {
        GL30.glDeleteVertexArrays(vaoId);
        GL15.glDeleteBuffers(vboId);
        GL15.glDeleteBuffers(vboiId);
    }

}
