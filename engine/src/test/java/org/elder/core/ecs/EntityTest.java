package org.elder.core.ecs;

import org.elder.engine.Scene;
import org.elder.engine.ecs.AbstractGameObject;
import org.elder.engine.ecs.ComponentRegistry;
import org.elder.engine.ecs.IdManager;
import org.elder.engine.ecs.Transform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityTest {

    static class TestEntity extends AbstractGameObject {

        public TestEntity() {
            super("Test");
        }

        protected void start() {
            addComponent(TestComponent.class);
        }
    }

    IdManager idManager = new IdManager();
    Scene scene;

    @BeforeEach
    void setup() {
        ComponentRegistry.getInstance().registerComponent(Transform.class);
        ComponentRegistry.getInstance().registerComponent(TestComponent.class);
        scene = new Scene("Test");
    }

    @Test
    void shouldAddAndGetComponent() {
        var entity = new TestEntity();
        entity.initialize(idManager.getNewId(), scene);

        var component = entity.getComponent(TestComponent.class);
        assertThat(component).isInstanceOf(TestComponent.class);
        assertThat(component.anInt).isEqualTo(42);
    }
}