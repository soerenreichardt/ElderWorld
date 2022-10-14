package org.elder.engine;

import org.elder.engine.ecs.BasicScene;

public interface GameEngineApi<S extends BasicScene> {
    void setScene(S scene);
}
