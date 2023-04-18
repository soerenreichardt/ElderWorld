package org.elder.engine.rendering;

import org.elder.engine.ecs.Component;
import org.elder.engine.ecs.Transform;
import org.elder.engine.ecs.api.GameComponent;
import org.joml.Vector2f;

@GameComponent
public class Mesh extends Component {
    public Vector2f[] vertices;
    public int[] indices;
    public Shader shader;
    public Transform transform;

    private boolean initialized = false;

    public void initialize() {
        this.initialized = true;
    }

    public boolean isInitialized() {
        return this.initialized;
    }
}
