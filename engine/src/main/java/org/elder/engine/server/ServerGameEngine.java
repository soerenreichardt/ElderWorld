package org.elder.engine.server;

import org.elder.engine.Scene;
import org.elder.engine.api.GameEngine;
import org.elder.engine.api.GameExecutable;
import org.elder.engine.ecs.api.Resource;
import org.elder.engine.ecs.system.SystemManager;
import org.elder.engine.ecs.system.SystemManagerBuilder;
import org.elder.engine.physics.PositioningSystem;
import org.elder.engine.script.ScriptSystem;

import java.util.concurrent.atomic.AtomicBoolean;

public class ServerGameEngine extends GameEngine<Scene> {

    private final AtomicBoolean exitFlag;

    protected ServerGameEngine(GameExecutable<Scene> gameExecutable, AtomicBoolean exitFlag, Resource[] resources) {
        super(gameExecutable, resources);
        this.exitFlag = exitFlag;
    }

    @Override
    protected SystemManager initializeSystemManager(SystemManagerBuilder systemManagerBuilder, Resource[] resources) {
        return systemManagerBuilder.fromList()
                .withResources(resources)
                .withSystemClass(PositioningSystem.class)
                .withSystemClass(ScriptSystem.class)
                .build();
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void preUpdate() {

    }

    @Override
    protected void update(float delta) {

    }

    @Override
    protected boolean interruptGameLoop() {
        return exitFlag.get();
    }
}
