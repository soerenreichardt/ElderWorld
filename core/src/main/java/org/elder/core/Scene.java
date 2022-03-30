package org.elder.core;

import org.elder.core.ecs.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final String name;
    private final List<GameObject> gameObjects;

    public Scene(String name) {
        this.name = name;
        this.gameObjects = new ArrayList<>();
    }

    public void addGameObject(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }
}
