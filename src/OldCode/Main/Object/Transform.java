/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OldCode.Main.Object;

import OldCode.Main.Object.Rendering.GLQuad;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * The Transform Component gives a gameObject a physical presence in the game
 * world and is responsible for keeping track of and changing those attributes:
 * Dimensions, Rotation, Position, Scale. If the transform is contained in a a
 * GameObject contained by another GameObject, the position and rotation
 * coordinates will be relative to the parent object. And the same applies for
 * parents of this object. and so on and so forth.
 *
 * @author Raymond Gao
 */
public class Transform extends Component {

    // for now we will assume everything is a square
    /**
     * The position of the gameObject in game space, (x,y,z)
     */
    public Vector3f position;

    /**
     * The Scale of the gameObject, (x,y)
     */
    public Vector2f scale = new Vector2f(1f, 1f);

    /**
     * The rotation of the gameObject, (x,y,z) (0,0,0) points north
     */
    public Vector3f rotation = new Vector3f(0, 0, 0);

    /**
     * The dimensions of the gameObject, (x,y)
     */
    private Vector2f dimensions;

    /**
     * The associated GLObject, used for rendering.
     */
    // private GLQuad glObject = null;
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
        // glObject = new GLQuad(dimensions);
    }

    /**
     * Creates a new TransformComponent with starting world position at the
     * origin (0,0,0), and dimensions of (1,1,)
     */
    public Transform() {
        this.position = new Vector3f(0, 0, 0);
        this.dimensions = new Vector2f(1, 1);
        // glObject = new GLQuad(dimensions);
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
        // glObject = new GLQuad(dimensions);
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

    public void rotate(float degrees) {
        this.rotation.z += degrees;
    }

    /**
     * Returns the glQuad of this transform
     *
     * @return
     */
    // public GLQuad getGLQuad() {
    // return glObject;
    // }
    @Override
    public void update(float dtime) {
        // do nothing
    }

}
