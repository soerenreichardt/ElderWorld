package org.elder.core.ecs;

public interface GameSystem extends Lifecycle {
    void update(float delta);
}
