package org.elder.core.ecs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityTest {

    static class TestEntity extends Entity {

        public TestEntity() {
            super("Test");
        }
    }

    @BeforeEach
    void setup() {
        ComponentManager.getInstance().registerComponent(TestComponent.class);
    }

    @Test
    void shouldAddAndGetComponent() {
        var entity = new TestEntity();
        entity.addComponent(TestComponent.class);

        var component = entity.getComponent(TestComponent.class);
        assertThat(component).isInstanceOf(TestComponent.class);
        assertThat(component.anInt).isEqualTo(42);
    }
}