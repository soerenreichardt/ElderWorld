package org.elder.core.ecs;

import org.joml.Matrix4f;
import org.joml.Vector2f;

@GameComponent
public class Transform extends Component {
    public Vector2f position = new Vector2f();
    public Vector2f scale = new Vector2f(1);
    public Vector2f rotation = new Vector2f();

    public Matrix4f getModelMatrix() {
        return new Matrix4f()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .scale(scale.x, scale.y, 1.0f)
                .translate(position.x, position.y, 0.0f);
    }

    public void scale(float newScale) {
        scale.set(newScale, newScale);
    }
}
