package org.elder.engine;

@FunctionalInterface
public interface GameExecutable {
    void execute(GameEngineApi<Scene> api);
}
