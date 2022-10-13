package org.elder.engine;

public interface CustomScript {
    void initialize();

    void update(float delta);

    default void stop() {}
}
