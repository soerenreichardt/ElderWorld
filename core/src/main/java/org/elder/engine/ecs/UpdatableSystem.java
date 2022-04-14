package org.elder.engine.ecs;

import org.elder.engine.Scene;

public interface UpdatableSystem extends Lifecycle {
    void update(float delta);

    void onSceneChanged(Scene scene);
}
