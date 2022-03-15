package org.elder.core.ecs;

public abstract class Entity {

    private final String name;
    private final ComponentManager componentManager;
    private final IdManager idManager;
    private final int id;

    public Entity(String name) {
        this.name = name;
        this.idManager = IdManager.getInstance();
        this.componentManager = ComponentManager.getInstance();
        this.id = idManager.getNewId();
    }

    public <C extends Component> C addComponent(Class<C> componentClass) {
        return this.componentManager.addComponent(id, componentClass);
    }

    public <C extends Component> C getComponent(Class<C> componentClass) {
        return this.componentManager.getComponent(id, componentClass);
    }

    public final void destroy() {
        componentManager.removeEntity(id);
        idManager.removeId(id);
    }

    protected void onDestroy() {}
}
