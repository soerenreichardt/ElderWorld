package org.elder.core.ecs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityTest {

    static class TestEntity extends GameObject {

        public TestEntity() {
            super("Test");
        }

        protected void start() {
            addComponent(TestComponent.class);
        }
    }

    IdManager idManager = new IdManager();
    ComponentManager componentManager = new ComponentManager();

    @BeforeEach
    void setup() {
        ComponentRegistry.getInstance().registerComponent(Transform.class);
        ComponentRegistry.getInstance().registerComponent(TestComponent.class);
    }

    @Test
    void shouldAddAndGetComponent() {
        var entity = new TestEntity();
        entity.initialize(idManager, componentManager);

        var component = entity.getComponent(TestComponent.class);
        assertThat(component).isInstanceOf(TestComponent.class);
        assertThat(component.anInt).isEqualTo(42);
    }
}