package org.elder.core.ecs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentManagerTest {

    @BeforeEach
    void setup() {
        ComponentManager.getInstance().registerComponent(TestComponent.class);
    }

    @Test
    void shouldAddMultipleComponents() {
        var componentManager = ComponentManager.getInstance();

        for (int i = 0; i < 10; i++) {
            componentManager.addComponent(i, TestComponent.class);
        }

        assertThat(componentManager.getComponents(TestComponent.class)).size().isEqualTo(10);
    }

}