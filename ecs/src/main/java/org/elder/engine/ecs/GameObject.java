package org.elder.engine.ecs;

public class GameObject extends AbstractGameObject {
    public GameObject(String name) {
        super(name);
        SceneRepository.getScene().addGameObject(this);
    }

    @Override
    protected void start() {

    }
}
