package org.elder.engine;

import org.elder.engine.ecs.BasicScene;

public class SceneRepository {
    private static BasicScene scene;

    public static void setScene(BasicScene scene) {
        SceneRepository.scene = scene;
    }

    public static BasicScene getScene() {
        return SceneRepository.scene;
    }
}
