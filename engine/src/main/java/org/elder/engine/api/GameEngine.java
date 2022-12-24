package org.elder.engine.api;

import org.elder.engine.Scene;
import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.ecs.api.Resource;
import org.elder.engine.ecs.system.SystemManager;
import org.elder.engine.ecs.system.SystemManagerBuilder;

public abstract class GameEngine<S extends BasicScene> extends Thread implements GameEngineApi<S> {

    protected final GameExecutable gameExecutable;
    protected final SystemManager systemManager;

    protected Scene activeScene;

    protected GameEngine(GameExecutable gameExecutable, Resource[] resources) {
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

    protected abstract void initialize();

    protected abstract void preUpdate();

    protected abstract void update(float delta);

    protected abstract boolean interruptGameLoop();
}
