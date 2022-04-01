package org.elder.core.rendering;

import org.elder.core.ecs.Component;
import org.elder.core.ecs.GameComponent;
import org.joml.Matrix4f;

@GameComponent
public class Projection extends Component {
    Matrix4f projection = new Matrix4f();
}
