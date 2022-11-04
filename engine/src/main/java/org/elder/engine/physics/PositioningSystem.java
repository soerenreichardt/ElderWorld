package org.elder.engine.physics;

import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.ecs.api.GameSystem;
import org.elder.engine.ecs.api.UpdatableSystem;

import java.util.Collections;

@GameSystem({ ControlsSystem.class })
public class PositioningSystem implements UpdatableSystem {

    private Iterable<Velocity> velocityComponents;

    public PositioningSystem() {
        this.velocityComponents = Collections.emptyList();
    }

    @Override
    public void update(float delta) {
        for (Velocity velocityComponent : velocityComponents) {
            velocityComponent.transform.position.add(velocityComponent.velocity);
            velocityComponent.transform.rotation.add(velocityComponent.rotation);
        }
    }

    @Override
    public void onSceneChanged(BasicScene scene) {
        velocityComponents = scene.componentManager().componentListIterable(Velocity.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
