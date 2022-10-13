package org.elder.engine;

import org.elder.engine.ecs.AbstractGameObject;

public abstract class ScriptableGameObject extends AbstractGameObject implements CustomScript {

    public ScriptableGameObject(String name) {
        super(name);
    }

    @Override
    public void start() {
        addComponent(Script.class).customScript = this;
    }
}
