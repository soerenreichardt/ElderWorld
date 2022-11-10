package org.elder.engine.ecs.system;

import org.elder.engine.ecs.SceneRepository;
import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.ecs.system.test.TestSystem;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SystemManagerTest {

    @Test
    void shouldLoadSystemViaList() {
        var systemManager = SystemManager.builder().fromList()
                .withSystemClass(TestSystem.class)
                .build();
        systemManager.start();

        assertThat(systemManager.getLoadedSystems()).hasSize(1);
        assertThat(systemManager.getLoadedSystems()).hasExactlyElementsOfTypes(TestSystem.class);
    }

    @Test
    void shouldLoadSystemViaReflection() {
        var systemManager = SystemManager.builder().fromReflection()
                .withPackagePrefix("org.elder.engine.ecs.system.test")
                .build();
        systemManager.start();

        assertThat(systemManager.getLoadedSystems()).hasSize(1);
        assertThat(systemManager.getLoadedSystems()).hasExactlyElementsOfTypes(TestSystem.class);
    }

    @Test
    void shouldUpdateSceneRepositoryOnSceneChanged() {
        var systemManager = SystemManager.builder().fromList().build();
        systemManager.start();
        assertThat(SceneRepository.getScene()).isNull();

        var scene = new BasicScene("Test");
        systemManager.onSceneChanged(scene);

        assertThat(SceneRepository.getScene()).isEqualTo(scene);
    }

}