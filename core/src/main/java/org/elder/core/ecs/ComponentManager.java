package org.elder.core.ecs;

import java.util.*;

public class ComponentManager {

    private final Map<Class<? extends Component>, ArrayList<Component>> components;
    private final ComponentRegistry componentRegistry;

    public ComponentManager() {
        this.components = new HashMap<>();
        this.componentRegistry = ComponentRegistry.getInstance();
    }

    public <T extends Component> Optional<List<T>> getComponentListReference(Class<T> componentClass) {
        return Optional.ofNullable((List<T>) this.components.get(componentClass));
    }

    public <T extends Component> Iterable<T> getFilteringComponentListIterable(Class<T> componentClass) {
        return () -> new FilteringComponentListIterator<>(
                getComponentListReference(componentClass)
                        .orElseGet(List::of)
                        .iterator()
        );
    }

    public <C extends Component> C addComponent(int entityId, Class<C> componentClass) {
        var componentFactory = this.componentRegistry.getComponentFactory(componentClass);
        var component = componentFactory.create();
        var componentList = this.components.computeIfAbsent(componentClass, __ -> new ArrayList<>());
        if (entityId == componentList.size()) {
            componentList.add(component);
        } else if (entityId > componentList.size()) {
            for (int i = componentList.size(); i < entityId; i++) {
                componentList.add(null);
            }
            componentList.add(component);
        } else {
            componentList.set(entityId, component);
        }
        return (C) component;
    }

    public <C extends Component> C getComponent(int entityId, Class<C> componentClass) {
        if (this.components.containsKey(componentClass)) {
            return (C) this.components.get(componentClass).get(entityId);
        }
        throw new IllegalArgumentException(String.format("No component of type %s was found", componentClass.getSimpleName()));
    }

    public void removeEntity(int entityId) {
        components.values().forEach(componentList -> componentList.set(entityId, null));
    }

    public <C extends Component> void removeComponent(int entityId, Class<C> componentClass) {
        components.get(componentClass).set(entityId, null);
    }

    @FunctionalInterface
    interface ComponentFactory<C extends Component> {
         C create();
    }
}
