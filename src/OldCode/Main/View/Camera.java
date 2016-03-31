/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OldCode.Main.View;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Rayayayayay
 */
public class Camera {

    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;
    private Vector3f position;

    private float fov;
    private float aspectRatio;
    private float near_plane;
    private float far_plane;

    private float y_scale;
    private float x_scale;
    private float frustum_length;

    private int HEIGHT;
    private int WIDTH;

    private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public Camera(int WIDTH, int HEIGHT) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;

        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();

        position = new Vector3f(0, 0, -1);

        fov = 100;
        aspectRatio = WIDTH / HEIGHT;
        near_plane = 0.1f;
        far_plane = 100f;

        y_scale = (float) (1 / Math.tan(Math.toRadians(fov / 2f)));
        x_scale = y_scale / aspectRatio;
        frustum_length = far_plane - near_plane;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public void translate(Vector3f deltaPos) {
        position = position.translate(deltaPos.x, deltaPos.y, deltaPos.z);
    }

    public FloatBuffer getProjectionMatrixBuffer() {
        projectionMatrix.store(matrixBuffer);
        matrixBuffer.flip();
        return matrixBuffer;
    }

    public FloatBuffer getViewMatrixBuffer() {
        viewMatrix.store(matrixBuffer);
        matrixBuffer.flip();
        return matrixBuffer;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public void update() {
        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        viewMatrix.translate(position, viewMatrix);
    }

    // @TODO: Rotations n stuff
}
