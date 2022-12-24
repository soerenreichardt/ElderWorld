package org.elder.engine.api;

public interface GameEngineRunner {
    void start(GameExecutable<?> gameExecutable) throws InterruptedException;
}
