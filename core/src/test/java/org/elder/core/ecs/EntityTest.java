package org.elder.core.ecs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityTest {

    static class TestEntity {

        private static final int UNINITIALIZED = -1;
        private final String name;
        private final IdManager idManager;
        private final int id;
        private ComponentManager componentManager;

        public TestEntity() {
            this.name = "Test";
            this.idManager = IdManager.getInstance();
            this.id = TestEntity.this.idManager.getNewId();
        }

        protected void start() {
            addComponent(TestComponent.class);
        }

        public void initialize(ComponentManager componentManager) {
            if (id != UNINITIALIZED) {
                this.componentManager = componentManager;
                start();
            } else {
                throw new IllegalStateException(String.format("Entity %s is already initialized", name));
            }
        }

        public <C extends Component> C addComponent(Class<C> componentClass) {
            return this.componentManager.addComponent(id, componentClass);
        }

        public <C extends Component> C getComponent(Class<C> componentClass) {
            return this.componentManager.getComponent(id, componentClass);
        }

        public final void destroy() {
            componentManager.removeEntity(id);
            idManager.removeId(id);
        }

        protected void onDestroy() {}
    }

    ComponentManager componentManager = new ComponentManager();

    @BeforeEach
    void setup() {
        ComponentRegistry.getInstance().registerComponent(TestComponent.class);
        IdManager.getInstance().reset();
    }

    @Test
    void shouldAddAndGetComponent() {
        var entity = new TestEntity();
        entity.initialize(componentManager);

        var component = entity.getComponent(TestComponent.class);
        assertThat(component).isInstanceOf(TestComponent.class);
        assertThat(component.anInt).isEqualTo(42);
    }
}