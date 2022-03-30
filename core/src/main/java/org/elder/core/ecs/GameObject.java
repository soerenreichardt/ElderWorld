package org.elder.core.ecs;

public abstract class GameObject {

    private static final int UNINITIALIZED = -1;

    private final String name;

    private IdManager idManager;
    private ComponentManager componentManager;
    protected Transform transform;

    private int id;

    public GameObject(String name) {
        this.name = name;
        this.id = UNINITIALIZED;
    }

    public void initialize(IdManager idManager, ComponentManager componentManager) {
        if (id == UNINITIALIZED) {
            this.id = idManager.getNewId();

            this.idManager = idManager;
            this.componentManager = componentManager;
            this.transform = addComponent(Transform.class);
            start();
        } else {
            throw new IllegalStateException(String.format("Entity %s is already initialized", name));
        }
    }

    protected abstract void start();

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
}
