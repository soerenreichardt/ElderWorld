package org.elder.engine.ecs;

import org.elder.engine.Scene;

public abstract class AbstractGameObject {

    public static final int UNINITIALIZED = -1;

    private final String name;

    private Scene scene;
    private ComponentManager componentManager;
    protected Transform transform;

    private int id;

    public AbstractGameObject(String name) {
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

    public Transform transform() {
        return this.transform;
    }

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
