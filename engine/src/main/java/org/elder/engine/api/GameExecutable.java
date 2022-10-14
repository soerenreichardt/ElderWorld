package org.elder.engine.api;

import org.elder.engine.Scene;

@FunctionalInterface
public interface GameExecutable {
    void execute(GameEngineApi<Scene> api);
}
