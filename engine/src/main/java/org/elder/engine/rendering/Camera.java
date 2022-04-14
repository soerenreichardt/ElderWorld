package org.elder.engine.rendering;

import org.elder.engine.ecs.GameObject;
import org.joml.Matrix4f;

public class Camera extends GameObject {

    private static final String MAIN_CAMERA_NAME = "MainCamera";
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1000.0f;

    private final boolean isMainCamera;
    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;
    private final CameraMode cameraMode;
    private float distance;

    enum CameraMode {
        ORTHOGRAPHIC,
        PERSPECTIVE
    }

    public Camera() {
        this(MAIN_CAMERA_NAME, CameraMode.PERSPECTIVE);
    }

    public Camera(String name, CameraMode cameraMode) {
        super(name);
        this.cameraMode = cameraMode;
        this.isMainCamera = false;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.distance = switch (cameraMode) {
            case PERSPECTIVE -> 2.0f;
            case ORTHOGRAPHIC -> 0.0f;
        };
    }

    @Override
    protected void start() {
    }

    public void initializeProjectionMatrix(int width, int height) {
        var aspect = (float) width / height;
        switch (cameraMode) {
            case PERSPECTIVE -> this.projectionMatrix.setPerspective(FOV, aspect, Z_NEAR, Z_FAR);
            case ORTHOGRAPHIC -> this.projectionMatrix.setOrtho(-aspect, aspect, -1, 1, -1, 1);
        }
    }

    public Matrix4f projectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f viewMatrix() {
        viewMatrix.identity();
        var position = transform.position;
        viewMatrix.lookAt(
                position.x, position.y, distance,
                0, 0, 0,
                0, 1, 0
        );
        return viewMatrix;
    }
}
