package org.elder.engine;

import org.elder.engine.ecs.ComponentRegistry;
import org.elder.engine.ecs.GameObject;
import org.elder.engine.ecs.SceneRepository;
import org.elder.engine.ecs.Transform;
import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.physics.Velocity;
import org.elder.engine.script.Script;
import org.elder.engine.script.ScriptSystem;
import org.elder.engine.script.ScriptableGameObject;
import org.joml.Vector2f;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class ScriptSystemTest {

    ScriptSystem scriptSystem = new ScriptSystem();

    @BeforeEach
    void setup() {
        ComponentRegistry.getInstance().registerComponent(Transform.class);
        ComponentRegistry.getInstance().registerComponent(Velocity.class);
        ComponentRegistry.getInstance().registerComponent(Script.class);
    }

    @AfterEach
    void tearDown() {
        SceneRepository.setScene(null);
    }

    @Test
    void shouldExecuteCustomScript() {
        var scene = new BasicScene("Test");
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

    @Test
    void shouldSpawnNewComponents() {
        var scene = new BasicScene("Test");
        var gameObject = new NewGameObjectScriptableObject("ScriptableObject");
        scene.addGameObject(gameObject);

        SceneRepository.setScene(scene);
        scriptSystem.start();
        scriptSystem.onSceneChanged(scene);

        var velocities = scene.componentManager().componentListIterable(Velocity.class).iterator();
        assertThat(velocities.hasNext()).isTrue();
        var velocity = velocities.next();
        assertThat(velocity.velocity).isEqualTo(new Vector2f(13.37f, 13.37f));
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

    static class NewGameObjectScriptableObject extends ScriptableGameObject {
        public NewGameObjectScriptableObject(String name) {
            super(name);
        }

        @Override
        public void initialize() {
            var go = new GameObject("Foo");
            var velocity = go.addComponent(Velocity.class);
            velocity.velocity = new Vector2f(13.37f, 13.37f);
        }

        @Override
        public void update(float delta) {

        }
    }
}
