package org.elder.engine.api;

import org.elder.engine.ecs.api.BasicScene;

public interface GameEngineRunner<S extends BasicScene> {
    void start(GameExecutable<S> gameExecutable) throws InterruptedException;
}
