package org.elder.engine;

import org.elder.engine.ecs.BasicScene;
import org.elder.engine.rendering.Camera;

public class Scene extends BasicScene {

    private final Camera camera;

    public Scene(String name) {
        super(name);
        this.camera = new Camera();
        addGameObject(camera);
    }

    public Camera camera() {
        return this.camera;
    }
}
