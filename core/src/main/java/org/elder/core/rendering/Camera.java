package org.elder.core.rendering;

import org.elder.core.ecs.GameObject;
import org.joml.Matrix4f;

public class Camera extends GameObject {

    private static final String MAIN_CAMERA_NAME = "MainCamera";

    private final boolean isMainCamera;
    private Matrix4f projection;

    public Camera() {
        super(MAIN_CAMERA_NAME);
        this.isMainCamera = true;
    }

    public Camera(String name) {
        super(name);
        this.isMainCamera = false;
    }

    @Override
    protected void start() {
        this.projection = addComponent(Projection.class).projection;
    }

    public Matrix4f projectionMatrix() {
        return this.projection;
    }
}
