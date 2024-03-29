package org.elder.engine.server;

import org.elder.engine.api.GameEngineRunner;
import org.elder.engine.api.GameExecutable;
import org.elder.engine.ecs.ComponentRegistry;
import org.elder.engine.ecs.Transform;
import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.ecs.api.Resource;
import org.elder.engine.ecs.api.UpdatableSystem;
import org.elder.engine.physics.Velocity;
import org.elder.engine.script.Script;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class ServerGameEngineRunner implements GameEngineRunner<BasicScene> {

    private final AtomicBoolean exitFlag;
    private final List<Class<? extends UpdatableSystem>> systems;

    public ServerGameEngineRunner(List<Class<? extends UpdatableSystem>> systems) {
        this.systems = systems;
        this.exitFlag = new AtomicBoolean(false);
        registerComponents();
    }

    @Override
    public void start(GameExecutable<BasicScene> gameExecutable) throws InterruptedException {
        var gameEngine = new ServerGameEngine(gameExecutable, exitFlag, systems, new Resource[0]);
        gameEngine.start();
        while (!exitFlag.get()) {
            LockSupport.park(100_000_000);
        }
        gameEngine.join();
    }

    private void registerComponents() {
        var componentRegistry = ComponentRegistry.getInstance();
        componentRegistry.registerComponent(Transform.class);
        componentRegistry.registerComponent(Velocity.class);
        componentRegistry.registerComponent(Script.class);
    }
}
