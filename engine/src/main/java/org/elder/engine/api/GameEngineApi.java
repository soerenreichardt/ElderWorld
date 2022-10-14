package org.elder.engine.api;

import org.elder.engine.ecs.BasicScene;

public interface GameEngineApi<S extends BasicScene> {
    void setScene(S scene);
}
