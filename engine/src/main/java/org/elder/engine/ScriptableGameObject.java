package org.elder.engine;

import org.elder.engine.ecs.GameObject;

public abstract class ScriptableGameObject extends GameObject implements CustomScript {

    public ScriptableGameObject(String name) {
        super(name);
    }

    @Override
    public void start() {
        addComponent(Script.class).customScript = this;
    }
}
