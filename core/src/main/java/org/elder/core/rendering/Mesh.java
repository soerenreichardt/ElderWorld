package org.elder.core.rendering;

import org.elder.core.ecs.Component;
import org.elder.core.ecs.GameComponent;
import org.joml.Vector2f;

@GameComponent
public class Mesh extends Component {
    public Vector2f[] vertices;
    public int[] indices;
}
