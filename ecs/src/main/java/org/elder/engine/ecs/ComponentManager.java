package org.elder.engine.ecs;

import java.util.*;

public class ComponentManager {

    private final Map<Class<? extends Component>, ComponentArray> components;
    private final ComponentRegistry componentRegistry;

    public ComponentManager() {
        this.components = new HashMap<>();
        this.componentRegistry = ComponentRegistry.getInstance();
    }

    public <T extends Component> Iterable<T> componentListIterable(Class<T> componentClass) {
        return () -> (Iterator<T>) Optional.ofNullable(components.get(componentClass))
                .map(ComponentArray::iterator)
                .orElseGet(Collections::emptyIterator);
    }

    public <T1 extends Component, T2 extends Component> Iterable<Pair<T1, T2>> compoundListIterable(Class<T1> class1, Class<T2> class2) {
        var components1 = this.components.get(class1);
        var components2 = this.components.get(class2);

        if (components1 == null || components2 == null) {
            return Collections.emptyList();
        }
        return () -> new ComponentPairIterator<>(components1, components2);
    }

    public <C extends Component> C addComponent(int entityId, Class<C> componentClass) {
        var componentFactory = this.componentRegistry.getComponentFactory(componentClass);
        var component = componentFactory.create();
        var componentArray = this.components.computeIfAbsent(componentClass, __ -> new ComponentArray());

        componentArray.addComponent(entityId, component);

        return (C) component;
    }

    public <C extends Component> C getComponent(int entityId, Class<C> componentClass) {
        if (this.components.containsKey(componentClass)) {
            return (C) this.components.get(componentClass).get(entityId);
        }
        throw new IllegalArgumentException(String.format("No component of type %s was found", componentClass.getSimpleName()));
    }

    public void removeEntity(int entityId) {
        components.values().forEach(componentList -> componentList.remove(entityId));
    }

    @FunctionalInterface
    interface ComponentFactory<C extends Component> {
         C create();
    }

    public static class Pair<T1 extends Component, T2 extends Component> {
        public T1 component1;
        public T2 component2;
    }
}
