package org.elder.engine;

import org.elder.engine.ecs.GameSystem;
import org.elder.engine.ecs.UpdatableSystem;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SystemManager implements UpdatableSystem {

    private final Map<Class<?>, Resource> resourceMap;
    private final List<Set<UpdatableSystem>> tieredSystems;
    private boolean systemsLoaded;

    public SystemManager(Resource[] resources) {
        this.resourceMap = toResourceMap(resources);
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
                    var constructor = selectConstructor(systemClass);
                    var arguments = getInjectableArguments(constructor);
                    
                    systems.add(constructor.newInstance(arguments));
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

    private Constructor<? extends UpdatableSystem> selectConstructor(Class<? extends UpdatableSystem> systemClass) throws NoSuchMethodException {
        var declaredConstructors = (Constructor<? extends UpdatableSystem>[]) systemClass.getDeclaredConstructors();
        if (declaredConstructors.length == 0) {
            return systemClass.getConstructor();
        }

        return Arrays.stream(declaredConstructors)
                .filter(constructor -> Arrays.stream(constructor.getParameterTypes()).allMatch(Resource.class::isAssignableFrom))
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new IllegalStateException("Could not find valid constructor with only `Resource` arguments"));
    }

    private Object[] getInjectableArguments(Constructor<? extends UpdatableSystem> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(resourceMap::get)
                .toArray();
    }

    private static Map<Class<?>, Resource> toResourceMap(Resource[] resources) {
        var resourceMap = new HashMap<Class<?>, Resource>();
        for (Resource resource : resources) {
            resourceMap.put(resource.getClass(), resource);
        }
        return resourceMap;
    }
}
