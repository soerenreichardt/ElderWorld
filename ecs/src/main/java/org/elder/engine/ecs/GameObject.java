package org.elder.engine.ecs;

import org.elder.engine.ecs.api.AbstractGameObject;

public class GameObject extends AbstractGameObject {
    public GameObject(String name) {
        super(name);
        SceneRepository.getScene().addGameObject(this);
    }

    @Override
    protected void start() {

    }
}
