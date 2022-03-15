package org.elder.core.ecs;

import org.joml.Vector2f;

@GameComponent
public class Transform extends Component {
    public Vector2f position;
    public Vector2f scale;
    public Vector2f rotation;
}
