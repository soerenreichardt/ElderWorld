package org.elder.engine.ecs;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ComponentRegistry {

    private static final ComponentRegistry COMPONENT_REGISTRY = new ComponentRegistry();

    private final Map<Class<? extends Component>, ComponentManager.ComponentFactory<? extends Component>> componentFactories;

    public static ComponentRegistry getInstance() {
        return COMPONENT_REGISTRY;
    }

    private ComponentRegistry() {
        this.componentFactories = new HashMap<>();
    }

    public <C extends Component> void registerComponent(Class<? extends Component> componentClass) {
        ComponentManager.ComponentFactory<C> componentFactory = () -> {
            try {
                var constructor = componentClass.getDeclaredConstructor();
                constructor.setAccessible(true);
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
        componentFactories.put(componentClass, componentFactory);
    }

    public ComponentManager.ComponentFactory<? extends Component> getComponentFactory(Class<? extends Component> componentClass) {
        var componentFactory = this.componentFactories.get(componentClass);
        if (componentFactory == null) {
            throw new IllegalArgumentException(String.format("No component factory for class %s was found", componentClass.getSimpleName()));
        }
        return componentFactory;
    }
}
