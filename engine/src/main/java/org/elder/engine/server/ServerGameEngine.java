package org.elder.engine.server;

import org.elder.engine.api.GameEngine;
import org.elder.engine.api.GameExecutable;
import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.ecs.api.Resource;
import org.elder.engine.ecs.api.UpdatableSystem;
import org.elder.engine.ecs.system.SystemManager;
import org.elder.engine.ecs.system.SystemManagerBuilder;
import org.elder.engine.physics.PositioningSystem;
import org.elder.engine.script.ScriptSystem;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerGameEngine extends GameEngine<BasicScene> {

    private final AtomicBoolean exitFlag;
    private final List<Class<? extends UpdatableSystem>> additionalSystems;

    protected ServerGameEngine(
            GameExecutable<BasicScene> gameExecutable,
            AtomicBoolean exitFlag,
            List<Class<? extends UpdatableSystem>> additionalSystems,
            Resource[] resources
    ) {
        super(gameExecutable, resources);
        this.exitFlag = exitFlag;
        this.additionalSystems = additionalSystems;
    }

    @Override
    protected SystemManager initializeSystemManager(SystemManagerBuilder systemManagerBuilder, Resource[] resources) {
        var systemManagerBuilderWithDefaultSystems = systemManagerBuilder.fromList()
                .withResources(resources)
                .withSystemClass(PositioningSystem.class)
                .withSystemClass(ScriptSystem.class)
                .skipMissingDependencies();

        additionalSystems.forEach(systemManagerBuilderWithDefaultSystems::withSystemClass);

        return systemManagerBuilderWithDefaultSystems
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
