package org.elder.engine;

import org.elder.engine.ecs.ComponentManager;
import org.elder.engine.ecs.GameObject;
import org.elder.engine.ecs.IdManager;
import org.elder.engine.rendering.Camera;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final String name;
    private final IdManager idManager;
    private final ComponentManager componentManager;
    private final List<GameObject> gameObjects;
    private final Camera camera;

    public Scene(String name) {
        this.name = name;
        this.idManager = new IdManager();
        this.componentManager = new ComponentManager();
        this.gameObjects = new ArrayList<>();
        this.camera = new Camera();
        addGameObject(camera);
    }

    public void addGameObject(GameObject gameObject) {
        var id = idManager.getNewId();
        gameObject.initialize(id, this);
        if (id == gameObjects.size()) {
            this.gameObjects.add(gameObject);
        } else {
            this.gameObjects.set(id, gameObject);
        }
    }

    public void removeGameObject(int id) {
        idManager.removeId(id);
        componentManager.removeEntity(id);
        gameObjects.remove(id);
    }

    public ComponentManager componentManager() {
        return this.componentManager;
    }

    public Camera camera() {
        return this.camera;
    }
}
