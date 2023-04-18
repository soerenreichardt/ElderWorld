package org.elder.engine.api;

import org.elder.engine.ecs.api.AbstractGameObject;
import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.ecs.api.Resource;
import org.elder.engine.ecs.system.SystemManager;
import org.elder.engine.ecs.system.SystemManagerBuilder;

public abstract class GameEngine<S extends BasicScene> extends Thread implements GameEngineApi<S> {

    protected final GameExecutable<S> gameExecutable;
    protected final SystemManager systemManager;

    protected S activeScene;

    protected GameEngine(GameExecutable<S> gameExecutable, Resource[] resources) {
        this.gameExecutable = gameExecutable;
        this.systemManager = initializeSystemManager(SystemManager.builder(), resources);
    }

    protected abstract SystemManager initializeSystemManager(
            SystemManagerBuilder systemManagerBuilder,
            Resource[] resources
    );

    @Override
    public void run() {
        initialize();
        systemManager.start();
        gameExecutable.execute(this);

        var lastTime = System.nanoTime();
        while (!interruptGameLoop()) {
            var currentTime = System.nanoTime();
            float dt = (currentTime - lastTime) * 1E-9f;
            lastTime = currentTime;

            preUpdate();

            systemManager.update(dt);
            update(dt);
        }

        systemManager.stop();
    }

    @Override
    public void setScene(S scene) {
        if (activeScene != scene) {
            activeScene = scene;
            systemManager.onSceneChanged(scene);
        }
    }

    @Override
    public void addGameObject(AbstractGameObject gameObject) {
        if (activeScene != null) {
            activeScene.addGameObject(gameObject);
            systemManager.onGameObjectAdded(gameObject);
        }
    }

    protected abstract void initialize();

    protected abstract void preUpdate();

    protected abstract void update(float delta);

    protected abstract boolean interruptGameLoop();
}
