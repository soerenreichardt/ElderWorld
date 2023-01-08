package org.elder.engine.ecs.api;

import org.elder.engine.ecs.ComponentManager;
import org.elder.engine.ecs.IdManager;

import java.util.ArrayList;
import java.util.List;

public class BasicScene {

    private final String name;
    private final IdManager idManager;
    private final ComponentManager componentManager;
    private final List<AbstractGameObject> gameObjects;

    public BasicScene(String name) {
        this.name = name;
        this.idManager = new IdManager();
        this.componentManager = new ComponentManager();
        this.gameObjects = new ArrayList<>();
    }

    public <T extends AbstractGameObject> T addGameObject(T gameObject) {
        var id = idManager.getNewId();
        gameObject.initialize(id, this);
        if (id == gameObjects.size()) {
            this.gameObjects.add(gameObject);
        } else {
            this.gameObjects.set(id, gameObject);
        }
        return gameObject;
    }

    public void removeGameObject(int id) {
        idManager.removeId(id);
        componentManager.removeEntity(id);
        gameObjects.remove(id);
    }

    public ComponentManager componentManager() {
        return this.componentManager;
    }
}
