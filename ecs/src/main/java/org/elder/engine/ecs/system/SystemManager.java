package org.elder.engine.ecs.system;

import org.elder.engine.ecs.SceneRepository;
import org.elder.engine.ecs.api.BasicScene;
import org.elder.engine.ecs.api.UpdatableSystem;
import org.jetbrains.annotations.TestOnly;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SystemManager implements UpdatableSystem {

    private final SystemLoader systemLoader;
    private List<Set<UpdatableSystem>> tieredSystems;
    private boolean systemsLoaded;

    public static SystemManagerBuilder builder() {
        return new SystemManagerBuilder();
    }

    SystemManager(SystemLoader systemLoader) {
        this.systemLoader = systemLoader;
        this.systemsLoaded = false;
    }

    @Override
    public void start() {
        loadSystems();
        forEachSystem(UpdatableSystem::start);
    }

    @Override
    public void stop() {
        forEachSystem(UpdatableSystem::stop);
    }

    @Override
    public void update(float delta) {
        forEachSystem(system -> system.update(delta));
    }

    @Override
    public void onSceneChanged(BasicScene scene) {
        SceneRepository.setScene(scene);
        forEachSystem(system -> system.onSceneChanged(scene));
    }

    @TestOnly
    Set<UpdatableSystem> getLoadedSystems() {
        return this.tieredSystems.stream().flatMap(Set::stream).collect(Collectors.toSet());
    }

    private void loadSystems() {
        this.tieredSystems = systemLoader.loadTieredSystems();
        this.systemsLoaded = true;
    }

    private void forEachSystem(Consumer<UpdatableSystem> systemConsumer) {
        if (!systemsLoaded) {
            throw new IllegalStateException("SystemManager was not initialized");
        }
        tieredSystems.forEach(systemTier -> systemTier.forEach(systemConsumer));
    }
}
