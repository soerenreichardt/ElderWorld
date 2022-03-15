package org.elder.core.rendering;

import org.elder.core.ecs.ComponentManager;
import org.elder.core.ecs.System;

import java.util.List;

public class RenderSystem implements System {

    private final List<Mesh> meshComponents;

    public RenderSystem() {
        this.meshComponents = ComponentManager.getInstance().getComponentListReference(Mesh.class);
    }

    @Override
    public void update(float delta) {

    }
}
