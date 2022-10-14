package org.elder.engine.script;

public interface CustomScript {
    void initialize();

    void update(float delta);

    default void stop() {}
}
