package org.elder.engine.physics;

import org.elder.engine.ecs.Component;
import org.elder.engine.ecs.Transform;
import org.elder.engine.ecs.api.GameComponent;
import org.joml.Vector2f;

@GameComponent
public class Velocity extends Component {
    public Vector2f velocity = new Vector2f();
    public Vector2f maxVelocity = new Vector2f();
    public Vector2f rotation = new Vector2f();
    public Transform transform;
}
