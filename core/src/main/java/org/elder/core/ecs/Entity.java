package org.elder.core.ecs;

import java.util.UUID;

public abstract class Entity {

    private final String name;
    private final UUID id;
    private final ComponentManager componentManager;

    public Entity(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.componentManager = ComponentManager.getInstance();
    }

    public <C extends Component> void addComponent(Class<C> componentClass) {
        this.componentManager.addComponent(id, componentClass);
    }

    public <C extends Component> C getComponent(Class<C> componentClass) {
        return this.componentManager.getComponent(id, componentClass);
    }
}
