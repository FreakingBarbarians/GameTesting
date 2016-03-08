/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test.Object;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author 3101209
 */
public class Transform extends Component {

//    public static final int SQUARE;
//    public static final int TRIANGLE;
//    public static final int CIRCLE;
    // for now we will assume everything is a square
    private Vector3f pos;
    private Vector2f dimensions;
    private Vector2f scale = new Vector2f(1f, 1f);

    public Transform(Vector3f xyz, Vector2f dimensions) {
        this.pos = xyz;
        this.dimensions = dimensions;
    }

    public Vector2f getDimensions() {
        return new Vector2f(dimensions.x * scale.x, dimensions.y * scale.y);
    }

    public Vector2f getBaseDimensions() {
        return dimensions;
    }

    public void translate(float dx, float dy) {
        pos.x += dx;
        pos.y += dy;
    }

    public void place(float x, float y) {
        pos.x = x;
        pos.y = y;
    }

    public void scale(float dxScale, float dyScale) {
        scale.x += dxScale;
        scale.y += dyScale;
    }

    public void setScale(float xScale, float yScale) {
        scale.x = xScale;
        scale.y = yScale;
    }
}
