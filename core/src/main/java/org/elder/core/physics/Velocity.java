package org.elder.core.physics;

import org.elder.core.ecs.Component;
import org.elder.core.ecs.GameComponent;
import org.elder.core.ecs.Transform;
import org.joml.Vector2f;

@GameComponent
public class Velocity extends Component {
    public Vector2f velocity = new Vector2f();
    public Vector2f rotation = new Vector2f();
    public Transform transform;
}
