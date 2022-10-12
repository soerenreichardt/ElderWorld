package org.elder.engine.ecs;

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

    public void addGameObject(AbstractGameObject gameObject) {
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
}
