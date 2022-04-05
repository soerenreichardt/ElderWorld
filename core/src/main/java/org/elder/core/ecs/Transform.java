package org.elder.core.ecs;

import org.joml.Matrix4f;
import org.joml.Vector2f;

@GameComponent
public class Transform extends Component {
    public Vector2f position = new Vector2f();
    public Vector2f scale = new Vector2f(1);
    public Vector2f rotation = new Vector2f();

    private final Matrix4f worldMatrix = new Matrix4f();

    public Matrix4f getModelMatrix() {
        return getModelMatrix(0.0f);
    }

    public Matrix4f getModelMatrix(float distance) {
        return worldMatrix.identity()
                .translate(position.x, position.y, distance)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .scale(scale.x, scale.y, 1.0f);
    }

    public void scale(float newScale) {
        scale.set(newScale, newScale);
    }
}
