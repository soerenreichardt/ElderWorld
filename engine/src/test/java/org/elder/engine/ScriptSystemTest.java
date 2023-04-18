package org.elder.engine;

import org.elder.engine.api.GameEngineApi;
import org.elder.engine.ecs.ComponentRegistry;
import org.elder.engine.ecs.Transform;
import org.elder.engine.ecs.api.AbstractGameObject;
import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.physics.Velocity;
import org.elder.engine.script.Script;
import org.elder.engine.script.ScriptSystem;
import org.elder.engine.script.ScriptableGameObject;
import org.joml.Vector2f;
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
        var gameObject = new NewGameObjectScriptableObject("ScriptableObject", new SceneBasedApi(scene));
        scene.addGameObject(gameObject);

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
            super(name, null);
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
        public NewGameObjectScriptableObject(String name, SceneBasedApi sceneBasedApi) {
            super(name, sceneBasedApi);
        }

        @Override
        public void initialize() {
            var go = createGameObject("Foo");
            var velocity = go.addComponent(Velocity.class);
            velocity.velocity = new Vector2f(13.37f, 13.37f);
        }

        @Override
        public void update(float delta) {

        }
    }

    static class SceneBasedApi implements GameEngineApi<BasicScene> {

        private final BasicScene scene;

        SceneBasedApi(BasicScene scene) {
            this.scene = scene;
        }

        @Override
        public void setScene(BasicScene scene) {

        }

        @Override
        public void addGameObject(AbstractGameObject gameObject) {
            this.scene.addGameObject(gameObject);
        }
    }
}
