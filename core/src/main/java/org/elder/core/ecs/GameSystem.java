package org.elder.core.ecs;

import org.elder.core.Scene;

public interface GameSystem extends Lifecycle {
    void update(float delta);

    void reset();

    void onSceneChanged(Scene scene);
}
