package org.elder.engine;

import org.elder.engine.ecs.GameSystem;
import org.elder.engine.ecs.UpdatableSystem;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SystemManager implements UpdatableSystem {

    private final List<Set<UpdatableSystem>> tieredSystems;
    private boolean systemsLoaded;

    public SystemManager() {
        this.tieredSystems = new ArrayList<>();
        this.systemsLoaded = false;
    }

    public void loadSystems() {
        var systemClassesWithDependencies = new Reflections("org.elder", Scanners.SubTypes.filterResultsBy(__ -> true))
                .getSubTypesOf(UpdatableSystem.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(GameSystem.class))
                .collect(Collectors.toMap(
                        Function.identity(),
                        clazz -> clazz.getAnnotation(GameSystem.class).value()
                ));

        var dependencyGraph = DependencyGraph.fromClassesWithDependencies(systemClassesWithDependencies);
        var tieredSystemClasses = dependencyGraph.topologicalSort();
        initializeTieredSystemClasses(tieredSystemClasses);
        systemsLoaded = true;
    }

    @Override
    public void start() {
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
    public void onSceneChanged(Scene scene) {
        forEachSystem(system -> system.onSceneChanged(scene));
    }

    private void initializeTieredSystemClasses(List<Set<Class<? extends UpdatableSystem>>> tieredSystemClasses) {
        try {
            for (Set<Class<? extends UpdatableSystem>> systemClasses : tieredSystemClasses) {
                var systems = new HashSet<UpdatableSystem>();
                tieredSystems.add(systems);
                for (Class<? extends UpdatableSystem> systemClass : systemClasses) {
                    var ctor = systemClass.getConstructor();
                    systems.add(ctor.newInstance());
                }
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void forEachSystem(Consumer<UpdatableSystem> systemConsumer) {
        if (!systemsLoaded) {
            throw new IllegalStateException("SystemManager was not initialized");
        }
        tieredSystems.forEach(systemTier -> systemTier.forEach(systemConsumer));
    }
}
