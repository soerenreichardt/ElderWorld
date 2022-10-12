package org.elder.engine;

public class SceneRepository {
    private static Scene scene;

    public static void setScene(Scene scene) {
        SceneRepository.scene = scene;
    }

    public static Scene getScene() {
        return SceneRepository.scene;
    }
}
