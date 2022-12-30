package org.elder.engine.api;

import org.elder.engine.ecs.api.BasicScene;

@FunctionalInterface
public interface GameExecutable<S extends BasicScene> {
    void execute(GameEngineApi<S> api);
}
