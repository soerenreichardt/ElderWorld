package org.elder.core.ecs;

import org.elder.engine.ecs.Component;
import org.elder.engine.ecs.GameComponent;

@GameComponent
class TestComponent extends Component {
    int anInt = 42;
}
