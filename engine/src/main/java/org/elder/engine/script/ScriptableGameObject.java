package org.elder.engine.script;

import org.elder.engine.ecs.api.AbstractGameObject;
import org.elder.engine.ecs.api.BasicScene;

public abstract class ScriptableGameObject extends AbstractGameObject implements CustomScript {

    private BasicScene scene;

    public ScriptableGameObject(String name) {
        super(name);
    }

    @Override
    public void initialize(int id, BasicScene scene) {
        super.initialize(id, scene);
        this.scene = scene;
    }

    @Override
    public void start() {
        addComponent(Script.class).customScript = this;
    }

    public GameObject createGameObject(String name) {
        if (this.scene != null) {
            return this.scene.addGameObject(new GameObject(name));
        } else {
            throw new IllegalStateException(String.format("Scriptable game object %s is not initialized", name()));
        }
    }
}
