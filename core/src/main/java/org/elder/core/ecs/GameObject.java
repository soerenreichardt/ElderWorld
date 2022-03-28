package org.elder.core.ecs;

public abstract class GameObject extends Entity {

    protected final Transform transform;

    public GameObject(String name) {
        super(name);
        this.transform = addComponent(Transform.class);
    }

    public void start() {

    }
}
