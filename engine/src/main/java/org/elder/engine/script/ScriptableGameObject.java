package org.elder.engine.script;

import org.elder.engine.ecs.api.AbstractGameObject;

public abstract class ScriptableGameObject extends AbstractGameObject implements CustomScript {

    public ScriptableGameObject(String name) {
        super(name);
    }

    @Override
    public void start() {
        addComponent(Script.class).customScript = this;
    }
}
