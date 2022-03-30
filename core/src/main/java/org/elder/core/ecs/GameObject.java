package org.elder.core.ecs;

import org.elder.core.Scene;

public abstract class GameObject {

    private static final int UNINITIALIZED = -1;

    private final String name;

    private Scene scene;
    private ComponentManager componentManager;
    protected Transform transform;

    private int id;

    public GameObject(String name) {
        this.name = name;
        this.id = UNINITIALIZED;
    }

    public void initialize(int id, Scene scene) {
        if (this.id == UNINITIALIZED) {
            this.id = id;

            this.scene = scene;
            this.componentManager = scene.componentManager();
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
        scene.removeGameObject(id);
    }
}
