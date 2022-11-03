package org.elder.engine.ecs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentManagerTest {

    @GameComponent
    static class TestComponent2 extends Component {
        long aLong = 1337;
    }

    @BeforeEach
    void setup() {
        ComponentRegistry.getInstance().registerComponent(TestComponent.class);
        ComponentRegistry.getInstance().registerComponent(TestComponent2.class);
    }

    @Test
    void shouldAddMultipleComponents() {
        var componentManager = new ComponentManager();

        for (int i = 0; i < 10; i++) {
            componentManager.addComponent(i, TestComponent.class);
        }

        int counter = 0;
        for (TestComponent ignored : componentManager.componentListIterable(TestComponent.class)) {
            counter++;
        }
        assertThat(counter).isEqualTo(10);
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

    @Test
    void shouldHandleInterleavedComponents() {
        var componentManager = new ComponentManager();
        for (int i = 0; i < 3; i++) {
            componentManager.addComponent(i, TestComponent.class);
        }

        componentManager.addComponent(2, TestComponent2.class);

        var componentList = StreamSupport
                .stream(componentManager.componentListIterable(TestComponent2.class).spliterator(), false)
                .toList();

        assertThat(componentList).size().isEqualTo(1);
    }

    @Test
    void shouldIterateThroughCompoundComponents() {
        var componentManager = new ComponentManager();

        componentManager.addComponent(0, TestComponent.class);
        componentManager.addComponent(0, TestComponent2.class);
        componentManager.addComponent(1, TestComponent.class);
        componentManager.addComponent(2, TestComponent2.class);
        componentManager.addComponent(3, TestComponent.class);
        componentManager.addComponent(3, TestComponent2.class);

        int counter = 0;
        for (ComponentManager.Pair<TestComponent, TestComponent2> pair : componentManager.compoundListIterable(TestComponent.class, TestComponent2.class)) {
            counter++;
        }

        assertThat(counter).isEqualTo(2);
    }

}