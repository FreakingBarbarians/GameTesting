/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test.Object;

import Test.Object.Rendering.GLQuad;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * The Transform Component gives a gameObject a physical presence in the game
 * world and is responsible for keeping track of and changing those attributes:
 * Dimensions, Rotation, Position, Scale
 *
 * @author Raymond Gao
 */
public class Transform extends Component {

    // for now we will assume everything is a square
    /**
     * The position of the gameObject in game space, (x,y,z)
     */
    private Vector3f position;

    /**
     * The dimensions of the gameObject, (x,y)
     */
    private Vector2f dimensions;

    /**
     * The Scale of the gameObject, (x,y)
     */
    private Vector2f scale = new Vector2f(1f, 1f);

    /**
     * The rotation of the gameObject, (x,y,z) (0,0,0) points east
     */
    private Vector3f rotation = new Vector3f(0, 0, 0);

    /**
     * The associated GLObject, used for rendering.
     */
    private GLQuad glObject = null;

    /**
     * Creates a new TransformComponent with a starting world position and
     * dimensions
     *
     * @param xyz world coordinates
     * @param dimensions dimensions
     */
    public Transform(Vector3f xyz, Vector2f dimensions) {
        this.position = xyz;
        this.dimensions = dimensions;
        glObject = new GLQuad(dimensions);
    }

    /**
     * Creates a new TransformComponent with starting world position at the
     * origin (0,0,0), and dimensions of (1,1,)
     */
    public Transform() {
        this.position = new Vector3f(0, 0, 0);
        this.dimensions = new Vector2f(1, 1);
        glObject = new GLQuad(dimensions);
    }

    /**
     * Creates a new TransformComponent with starting world position (0,0,0) and
     * dimensions
     *
     * @param dimensions
     */
    public Transform(Vector2f dimensions) {
        this.position = new Vector3f(0, 0, 0);
        this.dimensions = new Vector2f(1, 1);
        glObject = new GLQuad(dimensions);
    }

    /**
     * Returns the scaled dimensions
     *
     * @return
     */
    public Vector2f getDimensions() {
        return new Vector2f(dimensions.x * scale.x, dimensions.y * scale.y);
    }

    /**
     * Returns the base dimensions
     *
     * @return
     */
    public Vector2f getBaseDimensions() {
        return dimensions;
    }

    /**
     * Translates the position by a delta x and a delta y
     *
     * @param dx
     * @param dy
     */
    public void translate(float dx, float dy) {
        position.x += dx;
        position.y += dy;
    }

    /**
     * Sets the position to a coordinate
     *
     * @param x
     * @param y
     */
    public void place(float x, float y) {
        position.x = x;
        position.y = y;
    }

    /**
     * Changes the scale of the object by delta x and delta y
     *
     * @param dxScale
     * @param dyScale
     */
    public void scale(float dxScale, float dyScale) {
        scale.x += dxScale;
        scale.y += dyScale;
    }

    /**
     * Sets the scale of the object to xScale and yScale
     *
     * @param xScale
     * @param yScale
     */
    public void setScale(float xScale, float yScale) {
        scale.x = xScale;
        scale.y = yScale;
    }

    /**
     * Returns the glQuad of this transform
     *
     * @return
     */
    public GLQuad getGLQuad() {
        return glObject;
    }
}
