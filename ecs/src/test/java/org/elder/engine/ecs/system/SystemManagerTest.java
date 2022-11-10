package org.elder.engine.ecs.system;

import org.elder.engine.ecs.system.test.TestSystem;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SystemManagerTest {

    @Test
    void shouldLoadSystemViaList() {
        var systemManager = SystemManager.builder().fromList()
                .withSystemClass(TestSystem.class)
                .build();
        systemManager.loadSystems();

        assertThat(systemManager.loadedSystems()).hasSize(1);
        assertThat(systemManager.loadedSystems()).hasExactlyElementsOfTypes(TestSystem.class);
    }

    @Test
    void shouldLoadSystemViaReflection() {
        var systemManager = SystemManager.builder().fromReflection()
                .withPackagePrefix("org.elder.engine.ecs.system.test")
                .build();
        systemManager.loadSystems();

        assertThat(systemManager.loadedSystems()).hasSize(1);
        assertThat(systemManager.loadedSystems()).hasExactlyElementsOfTypes(TestSystem.class);
    }

}