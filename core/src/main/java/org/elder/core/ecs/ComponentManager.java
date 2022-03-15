package org.elder.core.ecs;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ComponentManager {

    private static final ComponentManager INSTANCE = new ComponentManager();

    public static ComponentManager getInstance() {
        return INSTANCE;
    }

    private final Map<Class<? extends Component>, Map<UUID, Component>> components;
    private final Map<Class<? extends Component>, ComponentFactory<? extends Component>> componentFactories;

    private ComponentManager() {
        this.components = new HashMap<>();
        this.componentFactories = new HashMap<>();
    }

    public <T extends Component> Collection<T> getComponents(Class<T> componentClass) {
        return (Collection<T>) this.components.get(componentClass).values();
    }

    public <C extends Component> C addComponent(UUID entityId, Class<C> componentClass) {
        var componentFactory = componentFactories.get(componentClass);
        var component = componentFactory.create();
        this.components
                .computeIfAbsent(componentClass, __ -> new HashMap<>())
                .put(entityId, component);
        return (C) component;
    }

    public <C extends Component> C getComponent(UUID entityId, Class<C> componentClass) {
        if (this.components.containsKey(componentClass)) {
            return (C) this.components.get(componentClass).get(entityId);
        }
        throw new IllegalArgumentException(String.format("No component of type %s was found", componentClass.getSimpleName()));
    }

    public <C extends Component> void registerComponent(Class<? extends Component> componentClass) {
        ComponentFactory<C> componentFactory = () -> {
            try {
                var constructor = componentClass.getDeclaredConstructor();
                return (C) constructor.newInstance();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(String.format(
                        "Component %s is missing mandatory empty constructor", componentClass.getSimpleName()),
                        e
                );
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
        getInstance().componentFactories.put(componentClass, componentFactory);
    }

    @FunctionalInterface
    interface ComponentFactory<C extends Component> {
         C create();
    }
}
