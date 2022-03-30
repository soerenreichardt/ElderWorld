package org.elder.core.ecs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentManagerTest {

    @BeforeEach
    void setup() {
        ComponentRegistry.getInstance().registerComponent(TestComponent.class);
    }

    @Test
    void shouldAddMultipleComponents() {
        var componentManager = new ComponentManager();

        for (int i = 0; i < 10; i++) {
            componentManager.addComponent(i, TestComponent.class);
        }

        assertThat(componentManager.getComponentListReference(TestComponent.class).get()).size().isEqualTo(10);
    }

    @Test
    void shouldRemoveComponentsOfDeletedEntities() {
        var componentManager = new ComponentManager();

        for (int i = 0; i < 3; i++) {
            componentManager.addComponent(i, TestComponent.class);
        }

        componentManager.removeEntity(1);
        assertThat(componentManager.getComponent(0, TestComponent.class)).isNotNull();
        assertThat(componentManager.getComponent(1, TestComponent.class)).isNull();
        assertThat(componentManager.getComponent(2, TestComponent.class)).isNotNull();
    }

}