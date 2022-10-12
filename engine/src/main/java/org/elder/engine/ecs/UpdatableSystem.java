package org.elder.engine.ecs;

public interface UpdatableSystem extends Lifecycle {
    void update(float delta);

    void onSceneChanged(BasicScene scene);
}
