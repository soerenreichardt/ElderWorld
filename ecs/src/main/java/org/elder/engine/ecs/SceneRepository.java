package org.elder.engine.ecs;

import org.elder.engine.ecs.api.BasicScene;

public class SceneRepository {
    private static BasicScene scene;

    public static void setScene(BasicScene scene) {
        SceneRepository.scene = scene;
    }

    public static BasicScene getScene() {
        return SceneRepository.scene;
    }
}
