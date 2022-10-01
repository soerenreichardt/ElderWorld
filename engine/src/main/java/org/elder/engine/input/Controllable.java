package org.elder.engine.input;

import org.elder.engine.ecs.Component;
import org.elder.engine.ecs.GameComponent;

@GameComponent
public class Controllable extends Component {
    public ControlsInputMapping controlsInputMapping = new DefaultControlsMapping();
}
