package org.elder.engine.api;

import org.elder.engine.ecs.api.AbstractGameObject;
import org.elder.engine.ecs.api.BasicScene;

public interface GameEngineApi<S extends BasicScene> {
    void setScene(S scene);

    void addGameObject(AbstractGameObject gameObject);
}
