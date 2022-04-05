package org.elder.core.rendering;

import org.elder.core.ecs.GameObject;
import org.joml.Matrix4f;

public class Camera extends GameObject {

    private static final String MAIN_CAMERA_NAME = "MainCamera";
    private static final float FOV = (float) Math.toRadians(60.0f);

    private final boolean isMainCamera;
    private final Matrix4f projection;
    private final CameraMode cameraMode;
    private float distance;

    enum CameraMode {
        ORTHOGRAPHIC,
        PERSPECTIVE
    }

    public Camera() {
        this(MAIN_CAMERA_NAME, CameraMode.ORTHOGRAPHIC);
    }

    public Camera(String name, CameraMode cameraMode) {
        super(name);
        this.cameraMode = cameraMode;
        this.isMainCamera = false;
        this.projection = new Matrix4f();
        this.distance = 0.0f;
    }

    @Override
    protected void start() {
    }

    public void initializeProjectionMatrix(int width, int height) {
        var aspect = (float) width / height;
        switch (cameraMode) {
            case PERSPECTIVE -> this.projection.setPerspective(FOV, aspect, -1f, 1f);
            case ORTHOGRAPHIC -> {
                this.projection.setOrtho(-aspect, aspect, -1, 1, -1, 1);
            }
        }
    }

    public Matrix4f projectionMatrix() {
        return this.projection;
    }

    public Matrix4f viewMatrix() {
        return this.transform.getModelMatrix().translate(0, 0, distance);
    }

    public void setDistance(float newDistance) {
        this.distance = newDistance;
    }

    public float distance() {
        return this.distance;
    }
}
