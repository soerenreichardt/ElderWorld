package org.elder.engine.script;

import org.elder.engine.api.GameEngineApi;
import org.elder.engine.ecs.api.AbstractGameObject;
import org.elder.engine.ecs.api.BasicScene;

public abstract class ScriptableGameObject extends AbstractGameObject implements CustomScript {

    protected final GameEngineApi<? extends BasicScene> api;

    public ScriptableGameObject(String name, GameEngineApi<? extends BasicScene> api) {
        super(name);
        this.api = api;
    }

    @Override
    public void start() {
        addComponent(Script.class).customScript = this;
    }

    public GameObject createGameObject(String name) {
        var gameObject = new GameObject(name);
        this.api.addGameObject(gameObject);
        return gameObject;
    }
}
