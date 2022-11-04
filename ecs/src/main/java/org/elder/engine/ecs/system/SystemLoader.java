package org.elder.engine.ecs.system;

import org.elder.engine.ecs.DependencyGraph;
import org.elder.engine.ecs.api.GameSystem;
import org.elder.engine.ecs.api.Resource;
import org.elder.engine.ecs.api.UpdatableSystem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SystemLoader {

    private final Stream<Class<? extends UpdatableSystem>> systemClassesStream;
    private final Resource[] resources;

    public SystemLoader(Stream<Class<? extends UpdatableSystem>> systemClassesStream, Resource[] resources) {
        this.systemClassesStream = systemClassesStream;
        this.resources = resources;
    }

    public List<Set<UpdatableSystem>> loadTieredSystems() {
        var systemClassesWithDependencies = systemClassesStream
                .filter(clazz -> clazz.isAnnotationPresent(GameSystem.class))
                .collect(Collectors.toMap(
                        Function.identity(),
                        clazz -> clazz.getAnnotation(GameSystem.class).value()
                ));

        var dependencyGraph = DependencyGraph.fromClassesWithDependencies(systemClassesWithDependencies);
        var tieredSystemClasses = dependencyGraph.topologicalSort();

        var resourceMap = toResourceMap(resources);
        return initializeTieredSystemClasses(tieredSystemClasses, resourceMap);
    }

    private List<Set<UpdatableSystem>> initializeTieredSystemClasses(List<Set<Class<? extends UpdatableSystem>>> tieredSystemClasses, Map<Class<?>, Resource> resourceMap) {
        try {
            List<Set<UpdatableSystem>> tieredSystems = new ArrayList<>();
            for (Set<Class<? extends UpdatableSystem>> systemClasses : tieredSystemClasses) {
                var systems = new HashSet<UpdatableSystem>();
                tieredSystems.add(systems);
                for (Class<? extends UpdatableSystem> systemClass : systemClasses) {
                    var constructor = selectConstructor(systemClass);
                    var arguments = getInjectableArguments(constructor, resourceMap);

                    systems.add(constructor.newInstance(arguments));
                }
            }
            return tieredSystems;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
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

    private Object[] getInjectableArguments(Constructor<? extends UpdatableSystem> constructor, Map<Class<?>, Resource> resourceMap) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(resourceMap::get)
                .toArray();
    }

    private Map<Class<?>, Resource> toResourceMap(Resource[] resources) {
        var resourceMap = new HashMap<Class<?>, Resource>();
        for (Resource resource : resources) {
            resourceMap.put(resource.getClass(), resource);
        }
        return resourceMap;
    }
}
