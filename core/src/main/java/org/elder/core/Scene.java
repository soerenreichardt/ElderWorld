package org.elder.core;

import org.elder.core.ecs.ComponentManager;
import org.elder.core.ecs.GameObject;
import org.elder.core.ecs.IdManager;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final String name;
    private final IdManager idManager;
    private final ComponentManager componentManager;
    private final List<GameObject> gameObjects;

    public Scene(String name) {
        this.name = name;
        this.idManager = new IdManager();
        this.componentManager = new ComponentManager();
        this.gameObjects = new ArrayList<>();
    }

    public void addGameObject(GameObject gameObject) {
        gameObject.initialize(idManager, componentManager);
        this.gameObjects.add(gameObject);
    }

    public ComponentManager componentManager() {
        return this.componentManager;
    }

}
