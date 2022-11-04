package org.elder.engine.physics;

import org.elder.engine.ecs.ComponentManager;
import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.ecs.api.GameSystem;
import org.elder.engine.ecs.api.UpdatableSystem;
import org.elder.engine.input.Controllable;
import org.elder.engine.input.KeyInputResource;

import java.util.Collections;

@GameSystem
public class ControlsSystem implements UpdatableSystem {

    private final KeyInputResource keyInputResource;
    private Iterable<ComponentManager.Pair<Velocity, Controllable>> velocityControllableIterator;

    public ControlsSystem(KeyInputResource keyInputResource) {
        this.keyInputResource = keyInputResource;
        this.velocityControllableIterator = Collections.emptyList();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void update(float delta) {
        for (ComponentManager.Pair<Velocity, Controllable> velocityControllablePair : velocityControllableIterator) {
            var controlsInputMapping = velocityControllablePair.component2.controlsInputMapping;
            var velocity = velocityControllablePair.component1;

            velocity.velocity.x = 0.0f;
            velocity.velocity.y = 0.0f;

            if (keyInputResource.keyDown(controlsInputMapping.moveLeft())) {
                velocity.velocity.x = -velocity.maxVelocity.x;
            }

            if (keyInputResource.keyDown(controlsInputMapping.moveRight())) {
                velocity.velocity.x = velocity.maxVelocity.x;
            }

            if (keyInputResource.keyDown(controlsInputMapping.moveUp())) {
                velocity.velocity.y = velocity.maxVelocity.y;
            }

            if (keyInputResource.keyDown(controlsInputMapping.moveDown())) {
                velocity.velocity.y = -velocity.maxVelocity.y;
            }
        }
    }

    @Override
    public void onSceneChanged(BasicScene scene) {
        velocityControllableIterator = scene.componentManager().compoundListIterable(Velocity.class, Controllable.class);
    }
}
