package org.elder.engine;

import org.elder.engine.ecs.AbstractGameObject;

public class GameObject extends AbstractGameObject {
    public GameObject(String name) {
        super(name);
        SceneRepository.getScene().addGameObject(this);
    }

    @Override
    protected void start() {

    }
}
