package org.elder.engine.ecs;

import org.elder.engine.Scene;

public interface GameSystem extends Lifecycle {
    void update(float delta);

    void onSceneChanged(Scene scene);
}
