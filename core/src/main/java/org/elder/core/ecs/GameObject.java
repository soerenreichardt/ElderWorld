package org.elder.core.ecs;

public abstract class GameObject {

    private static final int UNINITIALIZED = -1;

    private final String name;
    private final IdManager idManager;
    private final int id;

    private ComponentManager componentManager;
    protected Transform transform;

    public GameObject(String name) {
        this.name = name;
        this.idManager = IdManager.getInstance();
        this.id = GameObject.this.idManager.getNewId();
    }

    public void initialize(ComponentManager componentManager) {
        if (id != UNINITIALIZED) {
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
