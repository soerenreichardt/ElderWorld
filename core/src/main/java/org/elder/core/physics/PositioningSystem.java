package org.elder.core.physics;

import org.elder.core.Scene;
import org.elder.core.ecs.GameSystem;

import java.util.Collections;

public class PositioningSystem implements GameSystem {

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
    public void onSceneChanged(Scene scene) {
        velocityComponents = scene.componentManager().componentListIterable(Velocity.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
