package org.elder.core.ecs;

public abstract class GameObject extends Entity {

    public GameObject(String name) {
        super(name);
        addComponent(Transform.class);
    }

    public void start() {

    }
}
