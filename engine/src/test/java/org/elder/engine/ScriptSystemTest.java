package org.elder.engine;

import org.elder.engine.ecs.ComponentRegistry;
import org.elder.engine.ecs.Transform;
import org.elder.engine.input.Controllable;
import org.elder.engine.physics.Velocity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class ScriptSystemTest {

    ScriptSystem scriptSystem = new ScriptSystem();

    @BeforeEach
    void setup() {
        ComponentRegistry.getInstance().registerComponent(Transform.class);
        ComponentRegistry.getInstance().registerComponent(Controllable.class);
        ComponentRegistry.getInstance().registerComponent(Velocity.class);
        ComponentRegistry.getInstance().registerComponent(Script.class);
    }

    @Test
    void shouldExecuteCustomScript() {
        var scene = new Scene("Test");
        AtomicBoolean started = new AtomicBoolean(false);
        AtomicBoolean updated = new AtomicBoolean(false);
        var gameObject = new TestScriptableObject("ScriptableObject", started, updated);
        scene.addGameObject(gameObject);

        scriptSystem.start();
        scriptSystem.onSceneChanged(scene);
        scriptSystem.update(0.0f);

        assertThat(started.get()).isTrue();
        assertThat(updated.get()).isTrue();
    }

    static class TestScriptableObject extends ScriptableGameObject {

        private final AtomicBoolean started;
        private final AtomicBoolean updated;

        public TestScriptableObject(String name, AtomicBoolean started, AtomicBoolean updated) {
            super(name);
            this.started = started;
            this.updated = updated;
        }

        @Override
        public void initialize() {
            started.set(true);
        }

        @Override
        public void update(float delta) {
            updated.set(true);
        }
    }
}
