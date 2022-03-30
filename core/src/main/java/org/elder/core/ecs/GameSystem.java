package org.elder.core.ecs;

import org.elder.core.Scene;

public interface GameSystem extends Lifecycle {
    void update(float delta);

    void onSceneChanged(Scene scene);
}
